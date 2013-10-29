package com.prestonlee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Based on the template provided with the lab assignment.
 * 
 * @author Preston Lee <preston@asu.edu>
 */
public class PartB {

	// TODO Increase this whenever you add a new attribute
	protected static int NUM_ATTRIBUTES = 2;

	private static final String ATTRIBUTE_UNIGRAM = "unigram";
	// Number of folds for cross validation
	static int NUM_FOLDS = 5;
	protected NaiveBayes nbModel = new NaiveBayes();
	protected Instances adrInstances;
	protected FastVector attributes;
	protected StringToWordVector string2VectorFilter = new StringToWordVector();

	public static void main(final String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("The input files are missing: binaryannotations.tsv and texts.tsv");
		}

		final String annotationFilePath = args[0];
		final String textFilePath = args[1];
		final String stopWordsPath = args[2];
		final PartB classifier = new PartB();
		classifier.initialize();
		classifier.createInstances(annotationFilePath, textFilePath, stopWordsPath);
		classifier.crossValidate();
	}

	public void initialize() throws Exception {
		attributes = new FastVector(NUM_ATTRIBUTES);
		// unigram feature
		attributes.addElement(new Attribute(ATTRIBUTE_UNIGRAM, (FastVector) null));

		// TODO add new features here

		// Class labels
		FastVector classValues = new FastVector(2);
		classValues.addElement("noADR");
		classValues.addElement("hasADR");
		attributes.addElement(new Attribute("HasADR", classValues));

		adrInstances = new Instances("instances", attributes, 0);
		// i.e. The last column holds the label
		adrInstances.setClassIndex(adrInstances.numAttributes() - 1);
		string2VectorFilter.setLowerCaseTokens(true);
	}

	public Instance addInstance(String klass, String text) throws Exception {

		Instance instance = new Instance(NUM_ATTRIBUTES);
		instance.setDataset(adrInstances);

		// Unigram as an example :
		Attribute unigramsAttribute = adrInstances.attribute(ATTRIBUTE_UNIGRAM);
		int unigramFeatureValue = unigramsAttribute.addStringValue(text);
		instance.setValue(unigramsAttribute, unigramFeatureValue);

		// TODO add call to your attribute calculators

		instance.setClassValue(klass);

		adrInstances.add(instance);

		return instance;
	}

	public void createInstances(final String trainAnnotationFile, final String trainTextFile, final String stopWordsPath) throws Exception {
		final List<String> stopWords = readFileLineByLine(new File(stopWordsPath));
		final List<String> textLines = readFileLineByLine(new File(trainTextFile));
		final HashMap<String, String> idToTextMap = new HashMap<String, String>();
		String id;
		String s;
		for (String textLine : textLines) {
			String[] textParts = textLine.split("\t");
			id = textParts[0];
			s = textParts[1];
			System.out.println("PRE\t" + s);
			for (String q : stopWords) {
				s = s.replace(' ' + q + ' ', " ");
			}
			System.out.println("POST\t" + s);
			idToTextMap.put(id, s);
		}

		final List<String> annotationLines = readFileLineByLine(new File(trainAnnotationFile));

		for (String annotationLine : annotationLines) {
			String[] annotationParts = annotationLine.split("\t");
			String relatedText = idToTextMap.get(annotationParts[0]);
			addInstance(annotationParts[1], relatedText);
		}
	}

	public void crossValidate() throws Exception {
		string2VectorFilter.setInputFormat(adrInstances);
		Instances filteredData = Filter.useFilter(adrInstances, string2VectorFilter);

		nbModel.buildClassifier(filteredData);
		Evaluation eval = new Evaluation(filteredData);
		eval.crossValidateModel(nbModel, filteredData, NUM_FOLDS, new Random(1));

		System.out.println(eval.toSummaryString());
		System.out.println(eval.toMatrixString());
		System.out.println(eval.toClassDetailsString());

	}

	/**
	 * Code for reading a file line by line. you probably don't want to modify
	 * anything here,
	 **/
	private static List<String> readFileLineByLine(File f) {
		try {
			List<String> contents = new ArrayList<String>();

			BufferedReader input = new BufferedReader(new FileReader(f));
			for (String line = input.readLine(); line != null; line = input.readLine()) {
				contents.add(line);
			}
			input.close();

			return contents;

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

}
