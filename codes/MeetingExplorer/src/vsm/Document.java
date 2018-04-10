package vsm;


import java.io.File;
import java.util.HashMap;
import java.util.Set;

import preprocessamento.Preprocess;
import utils.Files;
import utils.TextUtils;

/**
 * This class represents one document.
 * It will keep track of the term frequencies.
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {
	
	/**
	 * A hashmap for term frequencies.
	 * Maps a term to the number of times this terms appears in this document. 
	 */
	private HashMap<String, Integer> termFrequency;
	
	private File file = null;
	private String txt = null;

	private double similarity = 0.0;
	
	private static Preprocess preprocess = Preprocess.getPreprocessDoAnything(true);

	
	
	public Document(File file) {
		this.file = file;
		init(Files.loadTxtFile(file));
	}
	
	public Document(String txt) {
		init(txt);
	}
	
	private void init(String txt) {
		this.txt = txt;
		termFrequency = new HashMap<String, Integer>();
		
		readTxtAndPreProcess();
	}
	
	private void readTxtAndPreProcess() {
		String[] words = TextUtils.getTokens(txt);
		
		for(String w : words) {
			String filteredWord = preprocess.remove(w) ? "" : preprocess.transform(w);
			
			
			if (!(filteredWord.equalsIgnoreCase(""))) {
//			System.out.println(String.format("Preprocessing ... %s --> '%s'", w, filteredWord));
				if (termFrequency.containsKey(filteredWord)) {
					int oldCount = termFrequency.get(filteredWord);
					termFrequency.put(filteredWord, ++oldCount);
				} else {
					termFrequency.put(filteredWord, 1);
				}
			}
		}
		
	}
	
	
	
	
	public static Preprocess getPreprocess() {
		return preprocess;
	}

	/**
	 * This method will return the term frequency for a given word.
	 * If this document doesn't contain the word, it will return 0
	 * @param word The word to look for
	 * @return the term frequency for this word in this document
	 */
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method will return a set of all the terms which occur in this document.
	 * @return a set of all terms in this document
	 */
	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	@Override
	/**
	 * The overriden method from the Comparable interface.
	 */
		public int compareTo(Document other) {
//		return filename.compareTo(other.getFileName());
//		return file.getName().compareTo(other.getFileName());
		return other.txt.compareTo(txt);
		
	}

	/**
	 * @return the filename
	 */
	@SuppressWarnings("unused")
	private String getFileName() {
		return file.getName();
	}
	
	/**
	 * This method is used for pretty-printing a Document object.
	 * @return the filename
	 */
	public String toString() {
		return file.getName();
	}

	public String getTxt() {
		return txt;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	
	
	
}











