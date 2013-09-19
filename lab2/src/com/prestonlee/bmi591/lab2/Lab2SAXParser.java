package com.prestonlee.bmi591.lab2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Written to take the 175k-allcomer-xtype data set and parse out each sentence
 * ('s' element) within every AbstractText element, replace each "e" element
 * with the id of the element, constantly writing to an output file along the
 * way. This is implemented as an event-driven SAX parser to provide O(1) memory
 * complexity regardless of the input file size. :)
 * 
 * The DTD files must be present in the same directory as your input file:
 * https://www.ebi.ac.uk/Rebholz-srv/CALBC/dtd/
 * 
 * @author Preston Lee <preston@asu.edu>
 * 
 */
public class Lab2SAXParser extends DefaultHandler {

	protected Abstract mCurrentAbstract;
	protected StringBuilder mCurrentSentence;
	protected Attributes mCurrentElement;
	protected PrintWriter mOutFile;

	protected int mAbstractCount;
	protected int mSentenceCount;

	protected final static int PROGRESS_INTERVAL = 10000;

	public void reset(final String pOutFile) throws FileNotFoundException {
		mCurrentAbstract = null;
		mCurrentSentence = null;
		mCurrentElement = null;
		mOutFile = new PrintWriter(pOutFile);
		mAbstractCount = 0;
		mSentenceCount = 0;
	}

	public int getAbstractCount() {
		return mAbstractCount;
	}

	public int getSentenceCount() {
		return mSentenceCount;
	}

	public void parseDocument(final String pInFile, final String pOutFile) throws ParserConfigurationException, SAXException, IOException {
		reset(pOutFile);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(pInFile, this);
		mOutFile.flush();
		mOutFile.close();
	}

	@Override
	public void startElement(final String s, final String s1, final String elementName, final Attributes attributes) throws SAXException {
		if (elementName.equalsIgnoreCase("AbstractText")) {
			mCurrentAbstract = new Abstract();
		} else if (elementName.equalsIgnoreCase("s")) {
			// New sentence. :)
			mCurrentSentence = new StringBuilder();
		} else if (elementName.equalsIgnoreCase("e")) {
			// New element. :)
			String id = attributes.getValue("id");
			mCurrentElement = attributes;
			mCurrentSentence.append(id);
		}
	}

	@Override
	public void endElement(final String s, final String s1, final String element) throws SAXException {
		if (element.equalsIgnoreCase("AbstractText")) {
			for (String sen : mCurrentAbstract.getSentences()) {
				mOutFile.print(sen);
				mOutFile.print("\n");
			}
			mCurrentAbstract = null;
			mAbstractCount += 1;
			if (mAbstractCount > 0 && (mAbstractCount % PROGRESS_INTERVAL == 0)) {
				System.out.println(mAbstractCount + " abstracts parsed...");
			}
		} else if (mCurrentAbstract != null && element.equalsIgnoreCase("s")) {
			mCurrentAbstract.getSentences().add(mCurrentSentence.toString());
			mCurrentSentence = null;
			mSentenceCount += 1;
		} else if (mCurrentSentence != null && element.equalsIgnoreCase("e")) {
			mCurrentElement = null;
		}
	}

	@Override
	public void characters(final char[] ac, final int i, final int j) throws SAXException {
		String s = new String(ac, i, j);
		if (mCurrentElement != null) {
			// Discard the element text.
		} else if (mCurrentSentence != null) {
			// Append it to the end of the current sentence.
			mCurrentSentence.append(s);
		}
	}

}
