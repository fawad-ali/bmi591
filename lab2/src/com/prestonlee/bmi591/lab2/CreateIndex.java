package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.Version;

public class CreateIndex {

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("\n\tUsage: java " + CreateIndex.class.getName() + " <input_sentences> " + " <index_directory>\n");
		} else {
			final String inFile = args[0];
			final Scanner in = new Scanner(new File(inFile));
			System.out.println("Sentence source:\t" + inFile);
			final IndexWriter index = createIndex(args[1]);

			StringReader reader;
			String sentence = in.nextLine();
			final ArrayList<String> nGrams = new ArrayList<String>();

			while (sentence != null) {
				System.out.println(sentence);
				Document doc = new Document();
//				doc.add(field)
//				reader = new StringReader(sentence);
//				TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_44, reader);
//				tokenizer = new StandardFilter(Version.LUCENE_44, tokenizer);
//				tokenizer = new StopFilter(Version.LUCENE_44, tokenizer, StandardAnalyzer.STOP_WORDS_SET);
//				 tokenizer= new ShingleFilter(tokenizer, 2, 3);
//				Attribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
//
//				while (tokenizer.incrementToken()) {
//					String curNGram = charTermAttribute.toString().toString();
//					nGrams.add(curNGram); // store each token into an ArrayList
//				}
//				sentence = in.nextLine();
			}
			for (String s : nGrams) {
				System.out.println(s);
			}
			
		}

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
