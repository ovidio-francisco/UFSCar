package segmenters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessamento.Preprocess;
import utils.TextExtractor;

public abstract class AbstractSegmenter implements Segmenter {

	
	private Preprocess preprocess = new Preprocess();
	private String[] sentences = null;
	private List<Integer> boundaries = null;
	private String EOSMarkedText = null;
	
	
	@Override
	public void setOriginalSource(String text) {
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "setSource(String text)"));

		
		findSentences(text); 
	}

	@Override
	public void setOriginalSource(File doc) {
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "setSource(File doc)"));

		
		String text = TextExtractor.docToString(doc);
		setOriginalSource(text);
	}

	@Override
	public Preprocess getPreprocess() {
		return preprocess;
	}
	
	@Override
	public void setPreprocess(Preprocess preprocess) {
		this.preprocess = preprocess;
	}

	
	protected ArrayList<String> getSegmentedText() {
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "getSegmentedText()"));

		
		findBoudaries();
		return getRawSegmentedText(getSentences(), getBoundaries());
	}

	@Override
	public final ArrayList<String> getSegments(File source) {
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "getSegments(File source)"));

		
		
//		String text = TextExtractor.docToString(source);
		setOriginalSource(source);
		return getSegmentedText();
	}


	@Override
	public final String segmentsToString(File source) {
		
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "segmentsToString(File source)"));
		
		
		ArrayList<String> segs = getSegments(source);
		StringBuilder sb = new StringBuilder();
		
		for(String str : segs) {
			sb.append(str + "\n=======//=======//=======//=======\n\n");
		}
		
		return sb.toString();
	}
	
//	public static ArrayList<String> getRawSegmentedText(File doc, List<Integer> breaks) {
//		List<String> lines = Files.loadTxtFileToList(doc);
//		
//		return getRawSegmentedText(lines, breaks);
//	}
	
	
//	private ArrayList<String> getRawSegmentedText(List<String> lines, List<Integer> breaks) {
//		ArrayList<String> result = new ArrayList<>();
//		String seg = "";
//		
//		for(int i=0; i<lines.size(); i++) {
//			
//			if(breaks.contains(i)) {
////				result.add(seg+"-----------------------------------------------------------> ["+i+"]");
//				result.add(seg);
//				seg = lines.get(i);
//			}
//			else {
//				seg += lines.get(i);
//			}
//		}
//		
////		result.add(seg+"-----------------------------------------------------------> [?]");
//		result.add(seg);
//		
//		return result;
//	}
//	// TODO: Usar mais essa função getRawSegmentedText
	
	
	private ArrayList<String> getRawSegmentedText(String[] lines, List<Integer> breaks) {
		ArrayList<String> result = new ArrayList<>();
		String seg = "";
		
		for(int i=0; i<lines.length; i++) {
			
			if(breaks.contains(i)) {
//				result.add(seg+"-----------------------------------------------------------> ["+i+"]");
				result.add(seg);
				seg = lines[i];
			}
			else {
				seg += lines[i];
			}
		}
		
//		result.add(seg+"-----------------------------------------------------------> [?]");
		result.add(seg);
		
		return result;
	}
	// TODO: Usar mais essa função getRawSegmentedText

	
	
	
	@Override
	public String showBoundaries(List<Integer> bounds) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		
		for(int b : bounds) {
			sb.append(b + " ");
		}

		sb.append("]");
		
		return sb.toString();
	}
	

//	@Override
//	public List<Integer> getBoundaries(File doc) {
//		String txt = Files.loadTxtFile(doc);
//		return getBoundaries(txt);
//	}


	protected void findSentences(String text) {
		System.out.println(String.format("%s :: %s", "AbstractSegmenter", "findSentences(String text)"));

		
		String t = text;
		t = getPreprocess().cleanTextMeating(t);
		EOSMarkedText = getPreprocess().identifyEOS(t, EOS_MARK);

		sentences = EOSMarkedText.split(EOS_MARK);
		
		System.out.println(sentences == null ? "findSentences FAIL!" : "findSentences OK! - "+sentences.length);
		
	}
	
	@Override
	public String[] getSentences() {
		return sentences;
	}

	@Override
	public List<Integer> getBoundaries() {
		return boundaries;
	}

	protected void setBoundaries(List<Integer> boundaries) {
		this.boundaries = boundaries;
	}

	protected String getEOSMarkedText() {
		return EOSMarkedText;
	}

	

	
}
