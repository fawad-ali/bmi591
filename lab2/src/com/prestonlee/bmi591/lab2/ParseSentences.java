package com.prestonlee.bmi591.lab2;

import static java.lang.System.out;

/**
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class ParseSentences {

	public static void main(final String[] args) throws Exception {
		System.out.println("Copyright (c) 2013 Preston Lee. All rights reserved. Released under the MIT license.");
		 if(args.length != 2 ) {
			 System.out.println("\n\tUsage: java " + ParseSentences.class.getName() + " <input_xml_file> " + " <output_text_file>\n");
		 } else{
				final Lab2SAXParser p = new Lab2SAXParser();
				final String inFile = args[0];
				final String outFile = args[1];
				out.println("Parsing:\t\t" + inFile);
				out.println("Writing:\t\t" + outFile);
				p.parseDocument(inFile, outFile);

				out.println("Abstracts parsed:\t" + p.getAbstractCount());
				out.println("Sentences written:\t" + p.getSentenceCount());

				out.println("Done!");
			}
		 }


	/**
	 * Overwrites the given file with a complete sentence dump, with one
	 * sentence per line.
	 */

}
