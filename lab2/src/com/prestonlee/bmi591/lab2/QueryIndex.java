package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
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
			// NumericRangeQuery q =
			// NumericRangeQuery.newIntRange(CreateIndex.FIELD_COUNT, 2, 10,
			// true, true);
			TermQuery q = new TermQuery(new Term(CreateIndex.FIELD_TEXT, "gene_entity"));
			ScoreDoc[] docs = searcher.search(q, 10).scoreDocs;
			for (int i = 0; i < docs.length; i++) {
				int id = docs[i].doc;
				Document doc = searcher.doc(id);
				System.out.println(doc.get(CreateIndex.FIELD_COUNT) + ": " + doc.get(CreateIndex.FIELD_TEXT));
			}
			System.out.println("Done!");
		}
	}

}
