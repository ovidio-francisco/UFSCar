package segmenters.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.csv.CSVPrinter;

import ptstemmer.Stemmer;
import ptstemmer.implementations.OrengoStemmer;
import segmenters.AbstractSegmenter;
import segmenters.algorithms.texttile.struct.RawText;
import utils.Files;
/**
 * An implementation of Marti Hearst's text tiling algorithm.
 * Creation date: (07/12/99 01:31:36)
 * @author: Freddy Choi
 */


public class TextTilingBR extends AbstractSegmenter {
	/* Program parameters */
	private int windowSize = 100;        // Size of the sliding window
	private int step = 10;               // Step size
		
	/* Data sets */
	private RawText collection = new RawText();   // A collection for segmentation
//	private Stopword stopwords = new Stopword(); // A stopword list for noise reduction
	

	/* Token -> stem dictionary */
	@SuppressWarnings("rawtypes")
	private Hashtable<String, Comparable> preprocessOf = new Hashtable<String, Comparable>(); // Token -> stem

	/* Similarity scores and the corresponding locations */
	private float[] sim_score = new float[0];
	private int[] site_loc = new int[0];

	/* Depth scores */
	private float[] depth_score = new float[0];

	/* Segment boundaries */
	private Vector<Integer> segmentation = new Vector<Integer>();
	
	
/**
 * TextTiling constructor comment.
 */
//public TextTiling() {
//	super();
//}


/**
 * Segment a collection
 * Creation date: (07/12/99 01:33:32)
 * @param c uk.ac.man.cs.choif.nlp.struct.RawText
 * @param s uk.ac.man.cs.choif.nlp.stopword.Stopword
 */
//public TextTilingBR(RawText c, Stopword s) {
//	collection = c;
//	stopwords = s;
//	preprocess();
//}



/**
 * Add a term to a block
 * Creation date: (07/12/99 01:41:24)
 * @param term java.lang.String
 * @param B java.util.Hashtable
 */
protected void blockAdd(final String term, Hashtable<String, Integer> B) {
	Integer freq = B.get(term);
	
	if (freq == null) freq = new Integer(1);
	else freq = new Integer(freq.intValue() + 1);

	B.put(term, freq);
}


/**
 * Compute the cosine similarity measure for two blocks
 * Creation date: (07/12/99 01:49:16)
 * @return float
 * @param B1 java.util.Hashtable
 * @param B2 java.util.Hashtable
 */
protected float blockCosine(final Hashtable<String, Integer> B1, final Hashtable<String, Integer> B2) {
	
	windows.add(B1);
	
	
	/* 1. Declare variables */
	int W; // Weight of a term (temporary variable)
	int sq_b1 = 0; // Sum of squared weights for B1
	int sq_b2 = 0; // Sum of squared weights for B2
	int sum_b = 0; // Sum of product of weights for common terms in B1 and B2
	
	/* 2. Compute the squared sum of term weights for B1 */
	for (Enumeration<Integer> e=B1.elements(); e.hasMoreElements();) {
		W = e.nextElement().intValue();
		sq_b1 += (W * W);
	}

	/* 3. Compute the squared sum of term weights for B2 */
	for (Enumeration<Integer> e=B2.elements(); e.hasMoreElements();) {
		W = e.nextElement().intValue();
		sq_b2 += (W * W);
	}

	/* 4. Compute sum of term weights for common terms in B1 and B2 */

	/* 4.1. Union of terms in B1 and B2 */
	Hashtable<String, Boolean> union = new Hashtable<String, Boolean>(B1.size() + B2.size());
	for (Enumeration<String> e=B1.keys(); e.hasMoreElements();) union.put(e.nextElement(), new Boolean(true));
	for (Enumeration<String> e=B2.keys(); e.hasMoreElements();) union.put(e.nextElement(), new Boolean(true));

	/* 4.2. Compute sum */
	Integer W1; // Weight of a term in B1 (temporary variable)
	Integer W2; // Weight of a term in B2 (temporary variable)
	String term; // A term (temporary variable)
	for (Enumeration<String> e=union.keys(); e.hasMoreElements();) {
		term = e.nextElement();
		W1 = B1.get(term);
		W2 = B2.get(term);
		if (W1!=null && W2!=null) sum_b += (W1.intValue() * W2.intValue());
	}
	
	/* 5. Compute similarity */
	float sim;
	sim = (float) sum_b / (float) Math.sqrt(sq_b1 * sq_b2);
		
	return sim;
}


/**
 * Remove a term from the block
 * Creation date: (07/12/99 01:46:39)
 * @param term java.lang.String
 * @param B java.util.Hashtable
 */
protected void blockRemove(final String term, Hashtable<String, Integer> B) {
	Integer freq = B.get(term);

	if (freq != null) {
		if (freq.intValue() == 1) B.remove(term);
		else B.put(term, new Integer(freq.intValue() - 1));
	}
}


/**
 * Identify the boundaries
 * Creation date: (07/12/99 07:05:04)
 */
protected void boundaryIdentification() {
	/* Declare variables */
	float mean = 0; // Mean depth score
	float sd = 0; // S.D. of depth score
	float threshold; // Threshold to use for determining boundaries
	int neighbours = 3; // The area to check before assigning boundary
	
	/* Compute mean and s.d. from depth scores */
	for (int i=depth_score.length; i-->0;) mean += depth_score[i];
	mean = mean / depth_score.length;

	for (int i=depth_score.length; i-->0;) sd += Math.pow(depth_score[i] - mean, 2);
	sd = sd / depth_score.length;

	/* Compute threshold */
	threshold = mean - sd / 2;

	/* Identify segments in pseudo-sentence terms */
	Vector<Integer> pseudo_boundaries = new Vector<Integer>();
	boolean largest = true; // Is the potential boundary the largest in the local area?
	for (int i=depth_score.length; i-->0;) {

		/* Found a potential boundary */
		if (depth_score[i] >= threshold) {
		
			/* Check if the nearby area has anything better */
			largest = true;
			
			/* Scan left */
			for (int j=neighbours; largest && j>0 && (i-j)>0; j--) {
				if (depth_score[i-j] > depth_score[i]) largest=false;
			}

			/* Scan right */
			for (int j=neighbours; largest && j>0 && (i+j)<depth_score.length; j--) {
				if (depth_score[i+j] > depth_score[i]) largest=false;
			}

			/* Lets make the decision */
			if (largest) pseudo_boundaries.addElement(new Integer(site_loc[i]));
		}
	}
	
	/* Convert pseudo boundaries into real boundaries.
	We use the nearest true boundary. */

	/* Convert real boundaries into array for faster access */
	int[] true_boundaries = new int[collection.boundaries.size()];
	for (int i=true_boundaries.length; i-->0;) true_boundaries[i]= ((Integer) collection.boundaries.elementAt(i)).intValue();
	
	int pseudo_boundary;
	int distance; // Distance between pseudo and true boundary
	int smallest_distance; // Shortest distance
	int closest_boundary; // Nearest real boundary
	for (int i=pseudo_boundaries.size(); i-->0;) {
		pseudo_boundary = pseudo_boundaries.elementAt(i).intValue();

		/* This is pretty moronic, but it works. Can definitely be improved */
		smallest_distance = Integer.MAX_VALUE;
		closest_boundary = true_boundaries[0];
		for (int j=true_boundaries.length; j-->0;) {
			distance = Math.abs(true_boundaries[j] - pseudo_boundary);
			if (distance <= smallest_distance) {
				smallest_distance = distance;
				closest_boundary = true_boundaries[j];
			}
		}

		segmentation.addElement(new Integer(closest_boundary));
	}
}


/**
 * Compute depth score after applying similarityDetermination()
 * Creation date: (07/12/99 06:54:32)
 */
protected void depthScore() {
	/* Declare variables */
	float maxima = 0; // Local maxima
	float dleft = 0; // Difference for the left side
	float dright = 0; // Difference for the right side

	/* For each position, compute depth score */
	depth_score = new float[sim_score.length];
	for (int i=sim_score.length; i-->0;) {

		/* Scan left */
		maxima = sim_score[i];
		for (int j=i; j>0 && sim_score[j] >= maxima; j--) maxima = sim_score[j];
		dleft = maxima - sim_score[i];

		/* Scan right */
		maxima = sim_score[i];
		for (int j=i; j<sim_score.length && sim_score[j] >= maxima; j++) maxima = sim_score[j];
		dright = maxima - sim_score[i];

		/* Declare depth score */
		depth_score[i] = dleft + dright;
	}
}


/**
 * Generate text output with topic boundary markers.
 * Creation date: (07/12/99 07:39:00)
 */
public void genOutput(/*RawText c, Vector<Integer> seg*/) {
	/* Declare variables */
	Vector<?> text = collection.text; // The text
	Vector<?> sentence = collection.boundaries; // Sentence boundaries
	int start, end; // Sentence boundaries
	
	/* The implicit boundary at the beginning of the file */
	System.out.println("==========");

	/* Print all the sentences */
	for (int i=1; i<sentence.size(); i++) {

		/* Get sentence boundaries */
		start = ((Integer) sentence.elementAt(i-1)).intValue();
		end = ((Integer) sentence.elementAt(i)).intValue();

		/* If start is a topic boundary, print marker */
		if (segmentation.contains(new Integer(start))) 
			System.out.println("==========");

		/* Print a sentence */
		for (int j=start; j<end; j++) {
			System.out.print(text.elementAt(j) + " ");
		}
		
		System.out.println();
	}

	/* The implicit boundary at the end of the file */
	System.out.println("==========");
}


/**
 * Decide whether word i is worth using as feature for segmentation.
 * Creation date: (07/12/99 23:39:51)
 * @return boolean
 * @param i int
 */
protected boolean include(int i) {
	
	
	/* Noise reduction by filtering out everything but nouns and verbs - 
	Best but requires POS tagging
	String pos = (String) C.pos.elementAt(i);
	return (pos.startsWith("N") || pos.startsWith("V")); */
	
	/* Noise reduction by stopword removal - OK */
	String token = (String) collection.text.elementAt(i);  
	
//	if(preprocessToken(token).length() < minTokenSize) return false; // commented on 27/08/2017
	
	
//	if (stopwords == null || !isRemoveStopWords()) return true; // commented on 27/08/2017
	
//	return true;
	
//	return !stopwords.isStopword(token.toLowerCase()); // commented on 27/08/2017
	
	
	return !getPreprocess().remove(token);

	/* No noise reduction -- Worst
	return true; */
}


/**
 * 
 * Creation date: (07/12/99 04:20:08)
 * @param args java.lang.String[]
 */
//public static void main(String[] args) {
//	/* Print header */
//	String header = "";
//	header += "##############################################################\n";
//	header += "# This is JTextTile, a Java implementation of Marti Hearst's #\n";
//	header += "# TextTiling algorithm. Free for educational, research and   #\n";
//	header += "# other non-profit making uses only.                         #\n";
//	header += "# Freddy Choi, Artificial Intelligence Group, Department of  #\n";
//	header += "# Computer Science, University of Manchester.                #\n";
//	header += "# Website : http://www.cs.man.ac.uk/~choif                   #\n";
//	header += "# E:mail  : choif@cs.man.ac.uk                               #\n";
//	header += "# Copyright 1999                                             #\n";
//	header += "##############################################################";
//	System.out.println(header);
//
//	
//	/* Obtain variables */
//	try {
//		int window = (Integer.valueOf(args[0])).intValue();
//		int step2 = (Integer.valueOf(args[1])).intValue();
//		String stopwordList = args[2];
//		System.out.println("# Stopword list : " + stopwordList);
//		System.out.println("# Window        : " + window);
//		System.out.println("# Step          : " + step2);
//
//		/* Load data */
//		Stopword s = new Stopword(stopwordList);
//		RawText c = new RawText(System.in);
//
//		/* A bit of error checking */
//		System.out.println("# Collection    : " + c.text.size());
//		if (c.text.size() <= (window * 2)) {
//			System.err.println("# Fatal error : Window size (" + window + " * 2 = " + (window * 2) + ") larger then collection (" + c.text.size() + ")");
//			System.exit(1);
//		}
//
//		/* Lets boogie */
//		System.out.println();
//		TextTiling t = new TextTiling(c, s);		// Initialise text tiling algorithm with collection
//		t.windowSize = window;								// Set window size according to user parameter
//		t.step = step2;									// Set step size according to user parameter
//		t.similarityDetermination();				// Compute similarity scores
//		t.depthScore();								// Compute depth scores using the similarity scores
//		t.boundaryIdentification();					// Identify the boundaries
//		t.genOutput(c, t.segmentation);				// Generate segmented output
//		System.exit(0);
//	}
//	catch (Exception e) {
//		System.err.println("# Fatal error : " + e);
//		e.printStackTrace(System.err);
//		System.err.println("# Fatal error : Require parameters <window size> <step size> <stopword list>");
//		System.exit(1);
//	}
//
//}


/**
 * Perform some preprocessing to save execution time
 * Creation date: (07/12/99 03:21:34)
 */
protected void preprocess() {
	
	/* Declare variables */
	Vector<?> text = collection.text; // Text of the collection
//	Stemmer stemmer = new Porter(); // Stemming algorithm
	
	String token; // A token

	/* Construct a dictionary of tokens */
	for (int i=text.size(); i-->0;) {
		token = (String) text.elementAt(i);
		preprocessOf.put(token, new Integer(0));
	}

	/* Complete mapping token -> stem */
	for (Enumeration<String> e=preprocessOf.keys(); e.hasMoreElements();) {
		token = e.nextElement();
		
		
		preprocessOf.put(token, preprocessToken(token));
		
	}
}


/**
 * Compute the similarity score.
 * Creation date: (07/12/99 03:17:31)
 */
protected void similarityDetermination() {
	/* Declare variables */
	Vector<?> text = collection.text; // The source text
	Hashtable<String, Integer> left = new Hashtable<String, Integer>(); // Left sliding window
	Hashtable<String, Integer> right = new Hashtable<String, Integer>(); // Right sliding window
	Vector<Float> score = new Vector<Float>(); // Scores
	Vector<Integer> site = new Vector<Integer>(); // Locations

	
	/* Initialise windows */
	for (int i=windowSize; i-->0;) {
		blockAdd((String) preprocessOf.get((String) text.elementAt(i)), left);
	}
	
	for (int i=windowSize*2; i-->windowSize;) {
		blockAdd((String) preprocessOf.get((String) text.elementAt(i)), right);
	}

	
	
	/* Slide window and compute score */
	final int end = text.size() - windowSize; // Last index to check
	String token; //  A stem
	int step2=0; // Step counter
	int i; // Counter

	for (i=windowSize; i<end; i++) {
		/* Compute score for a step */
		if (step2 == 0) {
			score.addElement(new Float(blockCosine(left, right)));
			site.addElement(new Integer(i));
			step2 = step;
		}

		/* Remove word which is at the very left of the left window */
		if (include(i-windowSize)) {
			blockRemove((String) preprocessOf.get((String) text.elementAt(i-windowSize)), left);
		}

		/* Add current word to the left window and remove it from the right window */
		if (include(i)) {
			token = (String) text.elementAt(i);
			blockAdd((String) preprocessOf.get(token), left);
			blockRemove((String) preprocessOf.get(token), right);
		}

		/* Add the first word after the very right of the right window */
		if (include(i+windowSize)) {
			blockAdd((String) preprocessOf.get((String) text.elementAt(i+windowSize)), right);
		}

		step2--;
	}
	/* Compute score for the last step */
	if (step2 == 0) {
		score.addElement(new Float(blockCosine(left, right)));
		site.addElement(new Integer(i));
		step2 = step;
	}

	/* Smoothing with a window size of 3 */
	sim_score = new float[score.size()-2];
	site_loc = new int[site.size()-2];
	for (int j=0; j<sim_score.length; j++) {
		sim_score[j] = (score.elementAt(j).floatValue() + score.elementAt(j+1).floatValue() + score.elementAt(j+2).floatValue()) / 3;
		site_loc[j] = site.elementAt(j+1).intValue();
	}
	
	
}


/* ****************************************************************************************
 * Created by Ovidio José Francisco                                                       *                  
 * Date: 25-01-2017                                                                       * 
 * ****************************************************************************************/

//	public enum StemmingAlgorithms {PORTER, ORENGO, NONE}

	private int    segmentsCount = 0;
//	public static String EOS_MARK = "^^";
	private Stemmer stemmer = null;
	private ArrayList<Hashtable<String, Integer>> windows = new ArrayList<>();
//	private int minTokenSize = 2;

	


public TextTilingBR() {
	
	this.setWindowSize(15);  
	this.setStep(3);
	
//	this.setStopwords(getStopWordFile());
	
	this.setStemmer(new OrengoStemmer());
}

//public TextTilingBR(int windowSize, int step, File stopWordList, StemmingAlgorithms stem) {
//	this.setWindowSize(windowSize);
//	this.setStep(step);
//	
//	this.setStopwords(stopWordList);
//	this.setStemmer(stem);
//}

//public TextTilingBR(int windowSize, int step, boolean removeStopWords, boolean removeStemming) {
//	this.setWindowSize(15);  
//	this.setStep(3);
//}





public void segmentToFileParts(File source) {
	
	ArrayList<String> lines = getSegments(source);
	
	int i = 1;
	for(String str : lines) {
		Files.saveTxtFile(str, new File(source.getName() + ".part_"+i+".txt"));
		i++;
	}

}

public float[] getSim_score() {
	return sim_score;
}


public int[] getSite_loc() {
	return site_loc;
}


public float[] getDepth_score() {
	return depth_score;
}

public int getBoundariesCount() {
	return collection.boundaries.size();
}


/**
 * Create the segements of the document
 * 
 * @return the segments
 */
public void segmentation() {
	
	/* A bit of error checking */
	if (collection.text.size() <= (windowSize  * 2)) {
		System.err.println("# Fatal error : Window size (" + windowSize + " * 2 = " + (windowSize * 2) + ") larger then collection (" + collection.text.size() + ")");
		System.exit(1);
	}	
	
	similarityDetermination();				// Compute similarity scores
	depthScore();							// Compute depth scores using the similarity scores
	boundaryIdentification();             	// Identify the boundaries
//	genOutput();
}




@Override
public ArrayList<String> getSegments(String txt) {
	
	txt = getPreprocess().cleanTextMeating(txt);
	txt = getPreprocess().identifyEOS(txt, TextTilingBR.EOS_MARK);

	
	setSource(txt);
	segmentation();
	
	
	/* Declare variables */
	Vector<?> text = collection.text; // The text
	Vector<?> sentence = collection.boundaries; // Sentence boundaries
	int start, end; // Sentence boundaries
	
	ArrayList<String> segments = new ArrayList<>();
	StringBuilder sb = new StringBuilder();
	
	/* Print all the sentences */
	for (int i=1; i<sentence.size(); i++) {

		/* Get sentence boundaries */
		start = ((Integer) sentence.elementAt(i-1)).intValue();
		end = ((Integer) sentence.elementAt(i)).intValue();

		/* If start is a topic boundary, print marker */
		if (segmentation.contains(new Integer(start))) {
			segments.add(sb.toString());
			sb.setLength(0);
		}

		/* Print a sentence */
		for (int j=start; j<end; j++) {
			sb.append(text.elementAt(j) + " ");
		}
		
		sb.append("\n");
	}

	segments.add(sb.toString());
	
	ArrayList<String> result = new ArrayList<>();
	
	for(String seg : segments) {
		if (seg.trim().length() > 0)
			result.add(seg);
	}
	
	segmentsCount = result.size();
	return result;
}

public void segmentToTxtFile(File source, File segmentsFile) {
	Files.saveTxtFile(segmentsToString(source), segmentsFile);
}

public void segmentToCsvFile(File source, File csvFile) {
	ArrayList<String> segs = getSegments(source);
	
	CSVPrinter csvout = Files.getCSVPrinter(csvFile);
	
	for(String seg : segs) {
		try {
			csvout.print(seg);
			csvout.println();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	Files.closeCSV(csvout);
}


public String preprocessToString() {
	Vector<?> text = collection.text; // The source text
	StringBuilder sb = new StringBuilder();

	
	for(int i=0; i<text.size(); i++) {
		String token = (String) text.elementAt(i);
		if(include(i)) {
			String preprocessedToken = preprocessToken(token); 
			
//			preprocessedToken = "["+preprocessedToken+"]";
			
			sb.append(preprocessedToken + " ");
		}
	}
	
	return sb.toString();
}



public String preprocessToken(String token) {
//	String preproecessedToken = token.trim().toLowerCase();
//	
//	if(stemmer != null && isRemoveSteam()) { 
//		preproecessedToken = stemmer.wordStemming(preproecessedToken);
//	}
//	
//	if (true) {
//		preproecessedToken = Cleaner.clean(preproecessedToken);
//	}
//	
//	preproecessedToken = preproecessedToken.trim();
//	preproecessedToken = preproecessedToken.replace(' ', '·');
	
	
	String preproecessedToken = getPreprocess().transform(token);

	return preproecessedToken;
}



public int getWindowSize() {
	return windowSize;
}


public void setWindowSize(int windowSize) {
	this.windowSize = windowSize;
}


public int getStep() {
	return step;
}


public void setStep(int step) {
	this.step = step;
}


public RawText getCollection() {
	return collection;
}


public void setCollection(RawText collection) {
	this.collection = collection;
	preprocess();
}

public void setSource(File source) {
	collection = new RawText(source);
	preprocess();
	
}

public void setSource(String source) {
	collection = new RawText(source);
	preprocess();
	
}

//public Stopword getStopwords() {
//	return stopwords;
//}


//public void setStopwords(File stopwordsList) {
//	this.stopwords = (stopwordsList != null) ? new Stopword(stopwordsList.getName()) : null;
//}


public Vector<Integer> getSegmentation() {
	return segmentation;
}



public int getSegmentsCount() {
	return segmentsCount;
}



public Stemmer getStemmer() {
	return stemmer;
}



public void setStemmer(Stemmer stemmer) {
	this.stemmer = stemmer;
}

//
//public int getMinTokenSize() {
//	return minTokenSize;
//}
//
//
//
//public void setMinTokenSize(int minTokenSize) {
//	this.minTokenSize = minTokenSize;
//}

//public void setStemmer(StemmingAlgorithms stem) {
//	switch (stem) {
//	case ORENGO:
//		this.stemmer = new OrengoStemmer();
//		break;
//	case PORTER:
//		this.stemmer = new PorterStemmer();
//		break;
//
//	default:
//		this.stemmer = null;
//		break;
//	}
//}



@Override
public String getAlgorithmName() {
	return "TextTiling";
}



@Override
public String paramsToString() {
	return String.format("winSize=%d step=%d | %s", windowSize, step, getPreprocess().configToString());
}


@Override
public String getLabel() {
	return String.format("TextTiling") ;
}


@Override
public String getConfigurationLabel() {
	return String.format("TT %2d %2d", this.getWindowSize(), this.getStep()) ;
}

	

}
