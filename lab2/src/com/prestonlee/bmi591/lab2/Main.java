package com.prestonlee.bmi591.lab2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Vector;
import static java.lang.System.*;

/**
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class Main {

	public static void main(final String[] args) throws Exception {
		// if(args.length != 2 )
		final Lab2Parser p = new Lab2Parser();
		final String inFile = args[0];
		final String outFile = args[1];
		out.println("Parsing:\t\t" + inFile);
		out.println("Writing:\t\t" + outFile);
		p.parseDocument(inFile, outFile);

		out.println("Abstracts parsed:\t" + p.getAbstractCount());
		out.println("Sentences written:\t" + p.getSentenceCount());

		out.println("Done!");
	}

	/**
	 * Overwrites the given file with a complete sentence dump, with one
	 * sentence per line.
	 */

}
