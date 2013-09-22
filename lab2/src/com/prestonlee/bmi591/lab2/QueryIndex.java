package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class QueryIndex {

	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <index_directory>\n");
		} else {
			final String indexPath = args[0];
			final Directory indexDir = FSDirectory.open(new File(indexPath));
			final DirectoryReader reader = DirectoryReader.open(indexDir);
			final IndexSearcher searcher = new IndexSearcher(reader);
			find100MostCommonBigrams(searcher);
			findAllGeneEntityNGrams(searcher);
			findAllOtherEntityNGrams(searcher);
			System.out.println("Done!");
		}
	}

	private static void findAllOtherEntityNGrams(IndexSearcher pSearcher) throws IOException {
		findAllEntityNGrams(pSearcher, Lab2SAXParser.PLACEHOLDER_OTHER);
	}

	private static void findAllGeneEntityNGrams(IndexSearcher pSearcher) throws IOException {
		findAllEntityNGrams(pSearcher, Lab2SAXParser.PLACEHOLDER_GENE);
	}

	private static void findAllEntityNGrams(IndexSearcher pSearcher, final String pEntity) throws IOException {
		RegexpQuery rQ = new RegexpQuery(new Term(CreateIndex.FIELD_TEXT, ".*" + pEntity.toLowerCase() + ".*"));
		NumericRangeQuery<Integer> nQ = NumericRangeQuery.newIntRange(CreateIndex.FIELD_COUNT, 5, Integer.MAX_VALUE, true, true);
		BooleanQuery q = new BooleanQuery();
		q.add(rQ, Occur.MUST);
		q.add(nQ, Occur.MUST);
		ScoreDoc[] docs = pSearcher.search(q, 100).scoreDocs;
		System.out.println("All " + pEntity + " n-grams:");
		for (int i = 0; i < docs.length; i++) {
			int id = docs[i].doc;
			Document doc = pSearcher.doc(id);
			System.out.println(doc.get(CreateIndex.FIELD_COUNT) + ": " + doc.get(CreateIndex.FIELD_TEXT));
		}
	}

	protected static void find100MostCommonBigrams(final IndexSearcher searcher) throws IOException {
		TermQuery nQ = new TermQuery(new Term(CreateIndex.FIELD_N), 2);
		NumericRangeQuery<Integer> countQ = NumericRangeQuery.newIntRange(CreateIndex.FIELD_COUNT, 50, 200, true, true);
		BooleanQuery q = new BooleanQuery();
		q.add(nQ, Occur.MUST);
		q.add(countQ, Occur.MUST);

		// TermQuery q = new TermQuery(new Term(CreateIndex.FIELD_TEXT,
		// "gene_entity"));
		ScoreDoc[] docs = searcher.search(q, 10).scoreDocs;
		System.out.println("Most common bigrams:");
		for (int i = 0; i < docs.length; i++) {
			int id = docs[i].doc;
			Document doc = searcher.doc(id);
			System.out.println(doc.get(CreateIndex.FIELD_COUNT) + ": " + doc.get(CreateIndex.FIELD_TEXT));
		}
	}

}
