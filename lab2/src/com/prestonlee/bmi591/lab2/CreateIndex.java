package com.prestonlee.bmi591.lab2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.Version;

public class CreateIndex {

	public static void main(String[] args) throws IOException {
		
		String inFile = args[0];
		Scanner in = new Scanner(new File(inFile));
		StringReader reader;
		ArrayList<String> nGrams = new ArrayList<String>();
		String sentence = in.nextLine();
		while (sentence != null) {
			reader = new StringReader(sentence);
			TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_44, reader);
			tokenizer = new StandardFilter(Version.LUCENE_44, tokenizer);
			tokenizer= new StopFilter(Version.LUCENE_44, tokenizer, StandardAnalyzer.STOP_WORDS_SET);
//			tokenizer= new ShingleFilter(tokenizer, 2, 3);
			Attribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);

			while (tokenizer.incrementToken()) {
				String curNGram = charTermAttribute.toString().toString();
				nGrams.add(curNGram); // store each token into an ArrayList
			}
			sentence = in.nextLine();
		}
		for(String s : nGrams) {
			System.out.println(s);
		}
	}

}
