package segmenter;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.HeaderDetector;
import preprocessamento.Preprocess;
import utils.TextExtractor;
import utils.TextUtils;

public abstract class AbstractSegmenter implements Segmenter {

	private boolean removeHeader = false;
	private boolean removePageNumbers = false;
	private int headerOccurrence = 0;
	private boolean removeExtraSpaces = false;
	
	
	private Preprocess preprocess = new Preprocess();
	
	public Preprocess getPreprocess() {
		return preprocess;
	}


	public void setPreprocess(Preprocess preprocess) {
		this.preprocess = preprocess;
	}


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
	
	@Override
	public final boolean isRemoveHeader() {
		return removeHeader;
	}

	@Override
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



//	public String getAllowedChars() {
//		return allowedChars;
//	}
//
//
//	public void setAllowedChars(String allowedChars) {
//		this.allowedChars = allowedChars;
//	}

	
	
	
}
