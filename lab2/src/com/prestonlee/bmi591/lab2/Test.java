package com.prestonlee.bmi591.lab2;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Test {

	public static void main(String[] args) throws IOException {
		String text = "This is a test string of really big words, punctuation, and other stuff.";

//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
//		TokenStream tokenizer = analyzer.tokenStream(null, new StringReader(text));
//		// tokenizer = new StandardFilter(Version.LUCENE_44, tokenizer);
//		tokenizer = new ShingleFilter(tokenizer, 2, 3);
//		CharTermAttribute cattr = tokenizer.addAttribute(CharTermAttribute.class);
//		OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
//
//		tokenizer.reset();
//		while (tokenizer.incrementToken()) {
//			int startOffset = offsetAttribute.startOffset();
//			int endOffset = offsetAttribute.endOffset();
//			// System.out.println(startOffset + ' ' + cattr.toString());
//			System.out.println(cattr.toString());
//		}
//		tokenizer.end();
//		tokenizer.close();
		
		MaxentTagger tagger = new MaxentTagger("file:///Users/preston/Downloads/stanford-postagger-2013-06-20/models/english-left3words-distsim.tagger");
		String tagged = tagger.tagTokenizedString(text);
		System.out.println(tagged);
	}

}
