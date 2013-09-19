Title:	BMI 591 - Lab 2
Author:	Preston Lee <preston@asu.edu>

# BMI 591 - Lab 2


I completed the first lab in Ruby, but decided to switch to Java for this lab after having issues binding to the stanford core NLP .jar. The contents of this lab are packaged as a Maven project under an MIT licensed and are available on [my GitHub page](https://github.com/preston/bmi591).

## Part 1

I pulled the DTDs from https://www.ebi.ac.uk/Rebholz-srv/CALBC/dtd/ and wrote a SAX parser by extending the standard event-based org.xml.sax.helpers.DocumentHandler, as is typical for large document parsers to avoid needing to create a massive DOM in memory.
