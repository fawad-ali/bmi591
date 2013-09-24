package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class QueryIndex {

	public static final String PRGE_ENTITY_FILE = "prge_entity.csv";
	public static final String GENE_ENTITY_FILE = "gene_entity.csv";
	public static final String BIGRAM_ENTITY_FILE = "bigrams_entity.csv";
	public static final String SENTENCE_SHORT = "Both these fragments reacted with protein A.";
	public static final String SENTENCE_MEDIUM = "A secondary site near the 5' end ( approximately 10 bases) was also observed.";
	public static final String SENTENCE_LONG = "Binding of FinP to the traJ GENE_ENTITY sequesters the traJ ribosome GENE_ENTITY, preventing its translation and repressing GENE_ENTITY transfer.";

	public static void main(final String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <index_directory> <output_director\n");
		} else {
			final File outputDir = new File(args[1]);
			// final FileOutputStream prgeFile = new FileOutputStream();
			// final PrintWriter prgeOut = new PrintWriter(new File(outputDir,
			// PRGE_ENTITY_FILE));
			// final PrintWriter geneOut = new PrintWriter(new File(outputDir,
			// GENE_ENTITY_FILE));
			// final PrintWriter bigramsOut = new PrintWriter(new
			// File(outputDir, BIGRAM_ENTITY_FILE));

			final String indexPath = args[0];
			final Directory indexDir = FSDirectory.open(new File(indexPath));
			final DirectoryReader reader = DirectoryReader.open(indexDir);
			final IndexSearcher searcher = new IndexSearcher(reader);

			ScoreDoc[] singles = searcher.search(NumericRangeQuery.newIntRange(CreateIndex.FIELD_N, 1, 2, true, false), Integer.MAX_VALUE).scoreDocs;
			Map<String, Entry<String, Integer>> singlesMap = countMap(searcher, singles);

			// findMostCommonBigrams(searcher, bigramsOut, singlesMap);
			// findAllGeneEntityNGrams(searcher, prgeOut, singlesMap);
			// findAllOtherEntityNGrams(searcher, geneOut, singlesMap);

			long n = 0;
			for (Map.Entry<String, Entry<String, Integer>> data : singlesMap.entrySet()) {
				n += data.getValue().getValue();
			}
			System.out.println("N == " + n);
			// prgeOut.close();
			// geneOut.close();
			// bigramsOut.close();

			String[] sents = { SENTENCE_SHORT, SENTENCE_MEDIUM, SENTENCE_LONG };
			for (int i = 0; i < sents.length; i++) {
				System.out.println("Sentence: " + sents[i]);
				StringReader pReader = new StringReader(sents[i]);
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
				TokenStream tokenizer = analyzer.tokenStream(null, pReader);

				// Create N-grams using Lucene's voodoo.
				tokenizer = new ShingleFilter(tokenizer, 2, 3);
				CharTermAttribute cattr = tokenizer.addAttribute(CharTermAttribute.class);
				OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
				tokenizer.reset();
				double prob = 1.0;
				while (tokenizer.incrementToken()) {
					String t = cattr.toString();
					if (t.matches(".*_.*") || t.matches(".*\\s+.*")) {
						continue;
					}
					Entry<String, Integer> entry = singlesMap.get(t);
					if (entry == null) {
						continue;
					}
					double tProb = singlesMap.get(t).getValue();
					prob *= (tProb / n);
					// System.out.println(t);
				}
				System.out.println("Probablity: " + prob);
				tokenizer.end();
				tokenizer.close();
			}
			System.out.println("Done!");
		}
	}

	private static void findAllOtherEntityNGrams(final IndexSearcher pSearcher, final PrintWriter pPrintWriter, Map<String, Entry<String, Integer>> pSinglesMap) throws IOException {
		findAllEntityNGrams(pSearcher, Lab2SAXParser.PLACEHOLDER_OTHER, pPrintWriter, pSinglesMap);
	}

	private static void findAllGeneEntityNGrams(final IndexSearcher pSearcher, final PrintWriter pPrintWriter, Map<String, Entry<String, Integer>> pSinglesMap) throws IOException {
		findAllEntityNGrams(pSearcher, Lab2SAXParser.PLACEHOLDER_GENE, pPrintWriter, pSinglesMap);
	}

	private static void findAllEntityNGrams(IndexSearcher pSearcher, final String pEntity, final PrintWriter pPrintWriter, Map<String, Entry<String, Integer>> pSinglesMap) throws IOException {
		RegexpQuery rQ = new RegexpQuery(new Term(CreateIndex.FIELD_TEXT, ".*" + pEntity.toLowerCase() + ".*"));
		NumericRangeQuery<Integer> nQ = NumericRangeQuery.newIntRange(CreateIndex.FIELD_N, 1, Integer.MAX_VALUE, true, false);
		BooleanQuery q = new BooleanQuery();
		q.add(rQ, Occur.MUST);
		q.add(nQ, Occur.MUST);
		ScoreDoc[] bigramDocs = pSearcher.search(q, Integer.MAX_VALUE).scoreDocs;
		writeBigramCountMap(pSearcher, pPrintWriter, bigramDocs, pSinglesMap);

	}

	protected static void writeBigramCountMap(IndexSearcher pSearcher, final PrintWriter pPrintWriter, ScoreDoc[] docs, Map<String, Entry<String, Integer>> pSinglesMap) throws IOException {
		Map<String, Map.Entry<String, Integer>> countMap = countMap(pSearcher, docs);
		String w1, w2;
		Entry<String, Integer> w1data, w2data;
		for (Map.Entry<String, Map.Entry<String, Integer>> e : countMap.entrySet()) {
			w1 = e.getKey().trim().replaceFirst(" .*", "");
			w2 = e.getKey().trim().replaceFirst(".* ", "");
			w1data = pSinglesMap.get(w1);
			w2data = pSinglesMap.get(w2);
			if (w1data != null && w2data != null) {
				pPrintWriter.println('"' + e.getKey().replace("\t", "") + "\",\"" + e.getValue().getValue() + "\",\"" + w1data.getValue() + "\",\"" + w2data.getValue() + "\",\""
						+ e.getValue().getKey().replace("\t", "") + '"');
			}
		}
	}

	// protected static void writeCountMap(IndexSearcher pSearcher, final
	// PrintWriter pPrintWriter, ScoreDoc[] docs) throws IOException {
	// Map<String, Map.Entry<String, Integer>> countMap = countMap(pSearcher,
	// docs);
	// for (Map.Entry<String, Map.Entry<String, Integer>> e :
	// countMap.entrySet()) {
	// pPrintWriter.println('"' + e.getKey().replace("\t", "") + '"' + ",\"" +
	// e.getValue().getValue() + "\"," + '"' +
	// e.getValue().getKey().replace("\t", "") + '"');
	// }
	// }

	protected static void findMostCommonBigrams(final IndexSearcher pSearcher, final PrintWriter pPrintWriter, Map<String, Entry<String, Integer>> singlesMap) throws IOException {
		// TermQuery nQ = new TermQuery(new Term(CreateIndex.FIELD_N), 2);
		NumericRangeQuery<Integer> nQ = NumericRangeQuery.newIntRange(CreateIndex.FIELD_N, 2, 3, true, false);
		// NumericRangeQuery<Integer> countQ =
		// NumericRangeQuery.newIntRange(CreateIndex.FIELD_COUNT, 1, null, true,
		// true);
		BooleanQuery q = new BooleanQuery();
		q.add(nQ, Occur.MUST);
		// q.add(countQ, Occur.MUST);

		// TermQuery q = new TermQuery(new Term(CreateIndex.FIELD_TEXT,
		// "gene_entity"));
		ScoreDoc[] docs = pSearcher.search(nQ, Integer.MAX_VALUE).scoreDocs;
		writeBigramCountMap(pSearcher, pPrintWriter, docs, singlesMap);
		System.out.println("Most common bigrams:");
		for (int i = 0; i < docs.length; i++) {
			int id = docs[i].doc;
			Document doc = pSearcher.doc(id);
			System.out.println(doc.get(CreateIndex.FIELD_COUNT) + ": " + doc.get(CreateIndex.FIELD_TEXT));
		}
	}

	protected static Map<String, Map.Entry<String, Integer>> countMap(final IndexSearcher pSearcher, final ScoreDoc[] docs) throws IOException {
		final HashMap<String, Map.Entry<String, Integer>> m = new HashMap<String, Map.Entry<String, Integer>>();
		String t;
		Integer runningTotal;
		int docCount;
		for (int i = 0; i < docs.length; i++) {
			int id = docs[i].doc;
			Document doc = pSearcher.doc(id);
			t = doc.get(CreateIndex.FIELD_TEXT);
			docCount = Integer.parseInt(doc.get(CreateIndex.FIELD_COUNT));
			if (t.contains("_ ") || t.contains(" _")) {
				continue;
			}
			// t = t.replace("_ ", "").replace(" _", "");
			Map.Entry<String, Integer> data = m.get(t);
			if (data == null) {
				m.put(t, new AbstractMap.SimpleEntry<String, Integer>(doc.get(CreateIndex.FIELD_POS), docCount));
			} else {
				runningTotal = data.getValue();
				data.setValue(runningTotal + docCount);
			}
		}
		return m;
	}
}
