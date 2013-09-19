package com.prestonlee.bmi591.lab2;

import java.util.Vector;

public class Abstract {

	protected Vector<String> mSentences;

	public Abstract() {
		mSentences = new Vector<String>();
	}

	public Vector<String> getSentences() {
		return mSentences;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(getSentences().size());
		for(String s : getSentences()) {
			sb.append(s);
			sb.append('\n');
		}
		return sb.toString();
	}

}
