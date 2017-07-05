package preprocessamento;

import java.io.File;

import ptstemmer.implementations.OrengoStemmer;
import utils.TextUtils;

public class Preprocess {
	
	private static StopWordList stopWords = new StopWordList(new File("stopPort.txt"));
	private static ptstemmer.Stemmer stemmer =  new OrengoStemmer();
	
	private boolean toLowCase         = true;

	private boolean identifyEOS       = true;

	private boolean removeStopWord    = false;
	private boolean removeShortThan   = false;
	
	private boolean removeHeaders     = false;
	private boolean removeStem        = false;
	private boolean removeNumbers     = false;
	private boolean removePunctuation = false;
	private boolean removeAccents     = false;
	
	private boolean removePageNumbers = false;
	private boolean removeExtraSpaces = false;
	
	private int headerOccurrence = 0;

	
	
	public  boolean remove(String token) {
		
		token = token.toLowerCase();
		token = Cleaner.removePunctuation(token);
		token = Cleaner.clean(token);
		token = token.trim();
		
		boolean result = false;
		
		result |= (removeShortThan && token.length() < 3);		
		result |= (removeStopWord  && stopWords.isStopWord(token));
		result |= (removeNumbers   && TextUtils.isANumber(token));
		
		return  result;
	}
	
	public  String transform(String token) {
		String result = token;
		
		if (toLowCase)           result = result.toLowerCase();
		if (removePunctuation)   result = Cleaner.removePunctuation(result);
		if (removeStem)          result = stemmer.wordStemming(result);
		if (removeAccents)       result = Cleaner.clean(result);
		
		return result.trim();
	}

	public String identifyEOS(String txt, String eosMark) {

//		txt = txt.replaceAll("\n", "\n ");
		
		if (identifyEOS) {
			txt = TextUtils.indentifyEndOfSentence(txt, eosMark);
		}
		else {
			txt = TextUtils.indentifyEndOfSentence_byPeriods(txt, eosMark);
		}
		
		return txt;
	}
	
	
	public String cleanTextMeating(String text) {
		
		if (removeHeaders) {
			HeaderDetector headerDetector = new HeaderDetector();
			headerDetector.detectHeader(text);
			text = headerDetector.removeHeader();
			headerOccurrence = headerDetector.getHeaderOccurrence();
		}
		
		if (removePageNumbers) {
			text = TextUtils.removePageNumbers(text);
		}
		
		if (removeExtraSpaces) {
			text = text.trim();

			text = TextUtils.restrictChar(text, ' ' , 1);
			
			while (text.contains("\n ")) text = text.replace("\n ", "\n");
			text = TextUtils.restrictChar(text, '\n', 2);
		}
		
		return text;
	}	
	
	public String configToString() {
		return String.format("%s %s %s %s %s %s %s %s %s", 
				
				"lc=" + (toLowCase         ? "T" : "F"),
				"rp=" + (removePunctuation ? "T" : "F"),
				"ra=" + (removeAccents     ? "T" : "F"),
				"rs=" + (removeShortThan   ? "T" : "F"),
				"rn=" + (removeNumbers     ? "T" : "F"),
				"rh=" + (removeHeaders     ? "T" : "F"),
				"ieof=" + (identifyEOS       ? "T" : "F"),
				"rsw=" + (removeStopWord    ? "T" : "F"),
				"rst=" + (removeStem        ? "T" : "F")
				
				);
	}
	
	
	public static StopWordList getStopWords() {
		return stopWords;
	}
	public static void setStopWords(StopWordList stopWords) {
		Preprocess.stopWords = stopWords;
	}
	public static ptstemmer.Stemmer getStemmer() {
		return stemmer;
	}
	public static void setStemmer(ptstemmer.Stemmer stemmer) {
		Preprocess.stemmer = stemmer;
	}
	public boolean isRemoveStopWord() {
		return removeStopWord;
	}
	public void setRemoveStopWord(boolean removeStopWord) {
		this.removeStopWord = removeStopWord;
	}
	public boolean isRemoveShortThan() {
		return removeShortThan;
	}
	public void setRemoveShortThan(boolean removeShortThan) {
		this.removeShortThan = removeShortThan;
	}
	public boolean isRemoveHeaders() {
		return removeHeaders;
	}
	public void setRemoveHeaders(boolean removeHeaders) {
		this.removeHeaders = removeHeaders;
	}
	public boolean isRemoveStem() {
		return removeStem;
	}
	public void setRemoveStem(boolean removeStem) {
		this.removeStem = removeStem;
	}
	public boolean isRemoveNumbers() {
		return removeNumbers;
	}
	public void setRemoveNumbers(boolean removeNumbers) {
		this.removeNumbers = removeNumbers;
	}
	public boolean isRemovePunctuation() {
		return removePunctuation;
	}
	public void setRemovePunctuation(boolean removePunctuation) {
		this.removePunctuation = removePunctuation;
	}
	public boolean isRemoveAccents() {
		return removeAccents;
	}
	public void setRemoveAccents(boolean removeAccents) {
		this.removeAccents = removeAccents;
	}
	public boolean isToLowCase() {
		return toLowCase;
	}
	public void setToLowCase(boolean toLowCase) {
		this.toLowCase = toLowCase;
	}
	public boolean isIdentifyEOS() {
		return identifyEOS;
	}
	public void setIdentifyEOS(boolean identifyEOS) {
		this.identifyEOS = identifyEOS;
	}


	public boolean isRemovePageNumbers() {
		return removePageNumbers;
	}


	public void setRemovePageNumbers(boolean removePageNumbers) {
		this.removePageNumbers = removePageNumbers;
	}


	public boolean isRemoveExtraSpaces() {
		return removeExtraSpaces;
	}


	public void setRemoveExtraSpaces(boolean removeExtraSpaces) {
		this.removeExtraSpaces = removeExtraSpaces;
	}


	public int getHeaderOccurrence() {
		return headerOccurrence;
	}
	
	
	
	
}
