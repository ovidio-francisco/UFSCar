package segmenter;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.HeaderDetector;
import utils.TextExtractor;
import utils.TextUtils;

public abstract class AbstractSegmenter implements Segmenter {

	private boolean removeHeader = false;
	private boolean removePageNumbers = false;
	private int headerOccurrence = 0;
	private boolean removeExtraSpaces;
	
	private boolean removeStopWords = false;
	private boolean removeSteam = false;
	
	private boolean removeNumbers = false;
	private int minTokenSize = -1;
	private String allowedChars;
	
	private File stopWordFile = new File("stopPort.txt");


	@Override
	public final ArrayList<String> getSegments(File source) {

		String text = TextExtractor.docToString(source);
		
		return getSegments(text);
	}


	@Override
	public final String segmentsToString(File source) {
		ArrayList<String> segs = getSegments(source);
		StringBuilder sb = new StringBuilder();
		
		for(String str : segs) {
			sb.append(str + "\n=======//=======//=======//=======\n\n");
		}
		
		return sb.toString();
	}

	protected String cleanTextMeating(String text) {
		
		if (removeHeader) {
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
	

	public final boolean isRemoveHeader() {
		return removeHeader;
	}


	public final void setRemoveHeader(boolean removeHeader) {
		this.removeHeader = removeHeader;
	}


	public final boolean isRemovePageNumbers() {
		return removePageNumbers;
	}


	public final void setRemovePageNumbers(boolean removePageNumbers) {
		this.removePageNumbers = removePageNumbers;
	}


	public final int getHeaderOccurrence() {
		return headerOccurrence;
	}


	public boolean isRemoveExtraSpaces() {
		return removeExtraSpaces;
	}


	public void setRemoveExtraSpaces(boolean removeExtraSpaces) {
		this.removeExtraSpaces = removeExtraSpaces;
	}

	@Override
	public boolean isRemoveStopWords() {
		return removeStopWords;
	}

	@Override
	public void setRemoveStopWords(boolean removeStopWords) {
		this.removeStopWords = removeStopWords;
	}

	@Override
	public boolean isRemoveSteam() {
		return removeSteam;
	}

	@Override
	public void setRemoveStem(boolean removeSteam) {
		this.removeSteam = removeSteam;
	}


	public File getStopWordFile() {
		return stopWordFile;
	}


	public boolean isRemoveNumbers() {
		return removeNumbers;
	}


	public void setRemoveNumbers(boolean removeNumbers) {
		this.removeNumbers = removeNumbers;
	}


	public int getMinTokenSize() {
		return minTokenSize;
	}


	public void setMinTokenSize(int minTokenSize) {
		this.minTokenSize = minTokenSize;
	}


	public String getAllowedChars() {
		return allowedChars;
	}


	public void setAllowedChars(String allowedChars) {
		this.allowedChars = allowedChars;
	}

	
	
	
}
