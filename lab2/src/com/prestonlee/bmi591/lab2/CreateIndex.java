package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

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
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class CreateIndex implements Runnable {

	public static final String FIELD_ID = "id";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_N = "n";
	public static final String FIELD_POS = "pos";
	public static final String FIELD_COUNT = "count";

	public static IndexWriter writer;
	public static String taggerConfig;

	public static final int NUM_WORKERS = 4;
	public static boolean keepGoing = true;
	public static Thread[] workers = new Thread[NUM_WORKERS];
	public static LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<String>(NUM_WORKERS);

	public static void main(final String[] args) throws IOException, InterruptedException {
		if (args.length != 3) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <input_sentences> <index_directory> <.tagger_file>\n");
		} else {
			final String inFile = args[0];
			final Scanner in = new Scanner(new File(inFile));
			System.out.println("Sentence source:\t" + inFile);

			taggerConfig = args[2];
			writer = createIndexWriter(args[1]);

			startWorkers();
			while (in.hasNextLine()) {
//				System.out.println("Queue size: " + queue.size());
				if (queue.size() >= NUM_WORKERS) {
//					System.out.println("Queue too big! (" + queue.size() + ")");
					Thread.sleep(100);
				} else {
					queue.putLast(in.nextLine());
				}
			}
			stopWorkers();
			// for (String s : nGrams) {
			// System.out.println(s);
			// }
			writer.close(true);

		}

	}

	protected static void stopWorkers() throws InterruptedException {
		keepGoing = false;
		for (int i = 0; i < NUM_WORKERS; i++) {
			Thread t = workers[i];
			t.join();
		}
	}

	protected static void startWorkers() {
		for (int i = 0; i < NUM_WORKERS; i++) {
			Thread t = new Thread(new CreateIndex());
			workers[i] = t;
			t.start();
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
			System.out.println(token + " count: " + count);
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

	public void run() {
		System.out.println("Working thread starting..");
		MaxentTagger tagger = new MaxentTagger(taggerConfig);
		long start;
		double rate = 0.0;
		// int count = 0;
		final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);

		try {

			while (keepGoing) {
				String sentence = queue.pollFirst(100, TimeUnit.MILLISECONDS);
				if (sentence == null) {
					Thread.sleep(100);
				} else {
					start = System.currentTimeMillis();
					// System.out.println(sentence);
					StringReader reader = new StringReader(sentence);
					TokenStream tokenizer = analyzer.tokenStream(null, reader);

					// Create N-grams using Lucene's voodoo.
					tokenizer = new ShingleFilter(tokenizer, 2, 3);
					CharTermAttribute cattr = tokenizer.addAttribute(CharTermAttribute.class);
					OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
					tokenizer.reset();
					while (tokenizer.incrementToken()) {
						updateDocument(writer, tagger, cattr.toString());
						writer.commit();
					}
					tokenizer.end();
					tokenizer.close();
					// writer.forceMergeDeletes(true);
					rate = ((System.currentTimeMillis() - start) / 1000.0);
					// count++;
					// System.out.println(count +
					// " sentences indexed. The last took " + rate +
					// " seconds.");
					System.out.println("Thread took " + rate + " seconds. (Thread: " + Thread.currentThread().getName() + ")");
				}
			}

		} catch (Exception e) {
			System.err.println("FATAL: Thread exiting due to exception. :(");
			e.printStackTrace();
			// TODO: handle exception
		}

		analyzer.close();

	}

}
