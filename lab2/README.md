Title:	BMI 591 - Lab 2
Author:	Preston Lee <preston@asu.edu>

# BMI 591 - Lab 2


I completed the first lab in Ruby, but decided to switch to Java for this lab after having issues binding to the stanford core NLP .jar. The contents of this lab are packaged as a Maven project under an MIT licensed and are available on [my GitHub page](https://github.com/preston/bmi591).

## Part 1

I pulled the DTDs from https://www.ebi.ac.uk/Rebholz-srv/CALBC/dtd/ and wrote a SAX parser by extending the standard event-based org.xml.sax.helpers.DocumentHandler, as is typical for large document parsers to avoid needing to create a massive DOM in memory. The parser finds each sentence ('s' element) within every AbstractText element and replaces each "e" element with only the id of the element, and writing each sentence to an output file along the way. The event-driven nature of SAX provides an O(1) memory complexity regardless of the input file size. The parser also outputs some basic statistics. For the entire data set, the output is:

	Parsing:		/Users/preston/Downloads/calbc_dtds_01-12-10/175k-allcomer-xtype
	Writing:		/Users/preston/Downloads/calbc_dtds_01-12-10/175k-allcomer-xtype.sentences.txt
	10000 abstracts parsed...
	20000 abstracts parsed...
	30000 abstracts parsed...
	40000 abstracts parsed...
	50000 abstracts parsed...
	60000 abstracts parsed...
	70000 abstracts parsed...
	80000 abstracts parsed...
	90000 abstracts parsed...
	100000 abstracts parsed...
	110000 abstracts parsed...
	Abstracts parsed:	118438
	Sentences written:	911400
	Done!

It takes about 30 seconds to run, after which I have a text file with one sentence per line. For the test data set, the output is:

	Parsing:		/Users/preston/Downloads/calbc_dtds_01-12-10/175k-allcomer-xtype.test
	Writing:		/Users/preston/Downloads/calbc_dtds_01-12-10/175k-allcomer-xtype.test.sentences.txt
	10000 abstracts parsed...
	Abstracts parsed:	13097
	Sentences written:	114244
	Done!
 


## Part 2 ##

I took 15,000 sentences near the beginning of the complete data set as training data. While this was only 1% additional validates sets to test our results in future sections.


## Part 3