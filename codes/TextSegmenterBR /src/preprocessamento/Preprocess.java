package preprocessamento;

import java.io.File;

import ptstemmer.implementations.OrengoStemmer;
import utils.TextUtils;

public class Preprocess {
	
	private static StopWordList stopWords = new StopWordList(new File("stopPort.txt"));
	private static ptstemmer.Stemmer stemmer =  new OrengoStemmer();
	
	private boolean removeStopWord    = false;
	private boolean removeShortThan   = false;
	
	private boolean removeHeaders     = false;
	private boolean removeStem        = false;
	private boolean removeNumbers     = false;
	private boolean removePunctuation = false;
	private boolean removeAccents     = false;
	private boolean toLowCase         = false;
	private boolean identifyEOS       = false;
	
	private boolean useNewIdentifyEOSMethod = false;
	
	
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
		
		if (useNewIdentifyEOSMethod) {
			txt = txt.replaceAll("\n", "\n ");
			txt = TextUtils.indentifyEndOfSentence(txt, eosMark);
		}
		else {
			txt = txt.replaceAll("\\. ", " \\. " + eosMark);
		}
		
		
		return txt;
	}
	
	
	
	public boolean isUseNewIdentifyEOSMethod() {
		return useNewIdentifyEOSMethod;
	}

	public void setUseNewIdentifyEOSMethod(boolean useNewIdentifyEOSMethod) {
		this.useNewIdentifyEOSMethod = useNewIdentifyEOSMethod;
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
}
