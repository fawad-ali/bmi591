package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class CreateIndex {

	public static String FIELD_ID = "id";
	public static String FIELD_TEXT = "text";
	public static String FIELD_N = "n";
	public static String FIELD_POS = "pos";
	public static String FIELD_COUNT = "count";

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <input_sentences> <index_directory> <.tagger_file>\n");
		} else {
			final String inFile = args[0];
			final Scanner in = new Scanner(new File(inFile));
			System.out.println("Sentence source:\t" + inFile);

			String sentence;
			// final ArrayList<String> nGrams = new ArrayList<String>();
			final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);

			final String taggerConfig = args[2];
			MaxentTagger tagger = new MaxentTagger(taggerConfig);
			long start;
			double rate = 0.0;
			int count = 0;
			IndexWriter writer = null;
			while (in.hasNextLine()) {
				writer = createIndexWriter(args[1]);
				start = System.currentTimeMillis();
				sentence = in.nextLine();
				// System.out.println(sentence);
				StringReader reader = new StringReader(sentence);

				// Create N-grams using Lucene's voodoo.
				TokenStream tokenizer = analyzer.tokenStream(null, reader);
				tokenizer = new ShingleFilter(tokenizer, 2, 3);
				CharTermAttribute cattr = tokenizer.addAttribute(CharTermAttribute.class);
				OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
				tokenizer.reset();
				while (tokenizer.incrementToken()) {
					updateDocument(writer, tagger, cattr.toString());
				}
				tokenizer.end();
				tokenizer.close();
				writer.forceMergeDeletes(true);
				writer.commit();
				writer.close(true);
				rate = ((System.currentTimeMillis() - start) / 1000.0);
				count++;
				System.out.println(count + " sentences indexed. The last took " + rate + " seconds.");
			}
			// for (String s : nGrams) {
			// System.out.println(s);
			// }
			analyzer.close();

		}

	}

	protected static void updateDocument(final IndexWriter pWriter, MaxentTagger tagger, String token) throws IOException {
		// int startOffset = offsetAttribute.startOffset();
		// int endOffset = offsetAttribute.endOffset();
		// System.out.println(token);

		DirectoryReader reader = DirectoryReader.open(pWriter, true);
		IndexSearcher searcher = new IndexSearcher(reader);

		// DirectoryReader reader =
		// DirectoryReader.openIfChanged((DirectoryReader)
		// searcher.getIndexReader());
		// if (reader != null) {
		// searcher = new IndexSearcher(reader);
		// }

		// Stanford tagger magic.
		int n = MaxentTagger.tokenizeText(new StringReader(token)).get(0).size();
		String tagged = tagger.tagTokenizedString(token);

		int docId = 0;
		Term term = new Term(FIELD_TEXT, token);
		TermQuery q = new TermQuery(term);
		TopDocs results = searcher.search(q, 1);
		if (results.totalHits > 0) {
			docId = results.scoreDocs[0].doc;
		}
		Document doc = null;
		try {
			doc = reader.document(docId);
			// doc = searcher.doc(docId);
		} catch (IllegalArgumentException e) {
			// System.out.println(e);
		}

		if ("gene_entity".equals(token)) {
			System.out.println(token);
		}
		int count = 1;

		if (doc != null) {
			String cs = doc.get(FIELD_COUNT);
			count = Integer.parseInt(cs);
			count += 1;
			pWriter.deleteDocuments(term);
			pWriter.forceMergeDeletes(true);
		}
		doc = new Document();
		doc.add(new StringField(FIELD_TEXT, token, Store.YES));
		doc.add(new StringField(FIELD_POS, tagged, Store.YES));
		doc.add(new IntField(FIELD_N, n, Store.YES));
		doc.add(new IntField(FIELD_COUNT, count, Store.YES));
		if ("gene_entity".equals(token)) {
			System.out.println(count + ": " + token);
		}

		searcher.getIndexReader().close();
		// searcher.

		pWriter.addDocument(doc);
		// pWriter.updateDocument(term, doc);
		pWriter.commit();
		// writer.
	}

	// private static IndexReader createIndexReader(final IndexWriter pWriter)
	// throws IOException {
	// DirectoryReader reader = DirectoryReader.open(pWriter, true);
	// return reader;
	// }

	protected static IndexWriter createIndexWriter(final String indexPath) throws IOException {
		System.out.println("Index directory:\t" + indexPath);
		final File indexFile = new File(indexPath);
		final Directory indexDir = FSDirectory.open(indexFile);
		final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		final IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_44, analyzer);
		final IndexWriter writer = new IndexWriter(indexDir, iwc);
		// writer.
		return writer;
	}

}
