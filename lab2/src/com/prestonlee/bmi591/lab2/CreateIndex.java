package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
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
			final IndexWriter writer = createIndex(args[1]);
			final IndexSearcher searcher = createIndexReder(writer);
			final String taggerConfig = args[2];

			String sentence;
			final ArrayList<String> nGrams = new ArrayList<String>();
			final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);

			MaxentTagger tagger = new MaxentTagger(taggerConfig);

			while (in.hasNextLine()) {
				sentence = in.nextLine();
				System.out.println(sentence);
				StringReader reader = new StringReader(sentence);

				// Create N-grams using Lucene's voodoo.
				TokenStream tokenizer = analyzer.tokenStream(null, reader);
				tokenizer = new ShingleFilter(tokenizer, 2, 3);
				CharTermAttribute cattr = tokenizer.addAttribute(CharTermAttribute.class);
				OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
				tokenizer.reset();
				while (tokenizer.incrementToken()) {
					int startOffset = offsetAttribute.startOffset();
					int endOffset = offsetAttribute.endOffset();
					String token = cattr.toString();
					System.out.println(token);

					// Stanford tagger magic.
					int n = MaxentTagger.tokenizeText(new StringReader(token)).get(0).size();
					String tagged = tagger.tagTokenizedString(token);
					System.out.println(tagged);

					int id = token.hashCode();
					Term term = new Term(FIELD_ID);
					Document doc = null;
					try {
						doc = searcher.doc(id);
					} catch (IllegalArgumentException e) {
						System.out.println(e);
					}

					if (doc == null) {
						// Newly encountered n-gram.
						doc = new Document();
						doc.add(new IntField(FIELD_ID, id, Store.YES));
						doc.add(new StringField(FIELD_TEXT, token, Store.YES));
						doc.add(new StringField(FIELD_POS, tagged, Store.YES));
						doc.add(new IntField(FIELD_N, n, Store.YES));
						doc.add(new IntField(FIELD_COUNT, 1, Store.YES));
					} else {
						// Existing n-gram, so just increment the count.
						String cs = doc.get(FIELD_COUNT);
						int c = Integer.parseInt(cs);
						c++;
						doc.removeField(FIELD_COUNT);
						doc.add(new IntField(FIELD_COUNT, c, Store.YES));
					}
					writer.updateDocument(term, doc);

				}
				tokenizer.end();
				tokenizer.close();

			}
			for (String s : nGrams) {
				System.out.println(s);
			}
			writer.close();

		}

	}

	private static IndexSearcher createIndexReder(final IndexWriter pWriter) throws IOException {
		DirectoryReader reader = DirectoryReader.open(pWriter, true);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}

	protected static IndexWriter createIndex(final String indexPath) throws IOException {
		System.out.println("Index directory:\t" + indexPath);
		final File indexFile = new File(indexPath);
		final Directory indexDir = FSDirectory.open(indexFile);
		final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		final IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_44, analyzer);
		final IndexWriter index = new IndexWriter(indexDir, iwc);
		return index;
	}

}
