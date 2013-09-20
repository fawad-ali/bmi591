package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class QueryIndex {

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <input_sentences> <index_directory> <.tagger_file>\n");
		} else {
			final String indexPath = args[0];
			final Directory indexDir = FSDirectory.open(new File(indexPath));
			final DirectoryReader reader = DirectoryReader.open(indexDir);
			final IndexSearcher searcher = new IndexSearcher(reader);
			System.out.println("TODO!");
		}
	}

}
