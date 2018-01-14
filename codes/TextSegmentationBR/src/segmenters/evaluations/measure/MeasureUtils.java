package segmenters.evaluations.measure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import preprocessamento.Preprocess;
import segmenters.Segmenter;
import segmenters.algorithms.TextTilingBR;

public class MeasureUtils {

	public static String END_SEGMENT_MARK = "_EOSEG_";
	public static String END_SENTENCE_MARK = "_EOSENT_";

	
	public static int[] getBinarySegmentation(ArrayList<String> seg) {
		int[] result = new int[seg.size()];
		
		for(int i=0; i<result.length; i++) {
			result[i] = -99;					
		}
		
		for(int i=0; i<seg.size(); i++) {
			result[i] = MeasureUtils.isEndOfSegment(seg.get(i)) ? 1 : 0;
		}

		return result;
	}
	
	public static int[] getNumeredSegmentation(ArrayList<String> seg) {
		int[] result = new int[seg.size()];
		
		for(int i=0; i<result.length; i++) {
			result[i] = -99;					
		}
		
		
		int segIndex = 1;
		for(int i=0; i<seg.size(); i++) {
			result[i] = segIndex;
					
			if(MeasureUtils.isEndOfSegment(seg.get(i))) {
				segIndex++;
			}
		}

		return result;
	}
	

	
	public static boolean isEndOfSegment(String s) {
		return s.endsWith(END_SEGMENT_MARK);
	}
	
	public static String getIndexes(ArrayList<String> array) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<array.size(); i++) {
			sb.append(String.format("%2d ", i+1));
		}
		
		return sb.toString();
	}

	public static String segmentedIndexes(ArrayList<String> array) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<array.size(); i++) {
			sb.append(String.format("%2d ", i+1));
		}
		
		
		return sb.toString();
	}

	public static String getSegmentedIndexes(ArrayList<String> seg) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<seg.size(); i++) {
			sb.append(String.format("%2d%s", i+1, MeasureUtils.isEndOfSegment(seg.get(i)) ? "|" : " "));
		}
		
		
		return sb.toString();
	}
	


	public static String arrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();
		
		for(int i : array) {
			sb.append(String.format("%2d ", i));
		}
		
		return sb.toString();
	}
	
	public static void printSegmentedSentences(ArrayList<String> seg, String separator) {
		for(int i=0; i< seg.size(); i++) {
			System.out.println(String.format("[%2d] %s", i+1, seg.get(i)));
			
			if(seg.get(i).endsWith(END_SEGMENT_MARK)) {
				System.out.println(separator);
			}
			else {
				System.out.println();
			}
		}
	}

	
	/**
	 * Reads a csv file and put the segments into an array
	 */
	public static ArrayList<String> CSVSegmentsToArray(File csvFile) {
		ArrayList<String> result = new ArrayList<>();
		
		try {
			Iterable<CSVRecord> recs = CSVFormat.DEFAULT.parse(new FileReader(csvFile));
			for(CSVRecord r : recs) {
				result.add(r.get(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	public static Preprocess getMyDefaultPreprocess() {
		Preprocess preprocess = new Preprocess();
		
		preprocess.setIdentifyEOS       (true);
		preprocess.setRemoveAccents     (true);
		preprocess.setRemoveHeaders     (true);
		preprocess.setRemoveNumbers     (true);
		preprocess.setRemovePunctuation (true);
		preprocess.setRemoveShortThan   (true);
		preprocess.setRemoveStem        (true);
		preprocess.setRemoveStopWord    (true);
		preprocess.setToLowCase         (true);			
		
		return preprocess;
	}
	
	/**
	 * Split a segment in sentences  and return it on an array
	 * @param preprocess 
	 */
	public static String[] splitInSentences(String segment, Preprocess preprocess) {
		
		String s1 = segment.trim()+END_SEGMENT_MARK;
//		String s2 = TextUtils.indentifyEndOfSentence(s1, END_SENTENCE_MARK);
		String s2 = preprocess.identifyEOS(s1, END_SENTENCE_MARK);
		
		return s2.split(END_SENTENCE_MARK);
	}

	
	/**
	 * Split each segent in sentences and return it in a string
	 */
	public static ArrayList<String> segmentsToSentences(ArrayList<String> segments, Preprocess preprocess) {
		ArrayList<String> result = new ArrayList<>();
		
		for(String seg : segments) {
			
			seg                = seg.replace('\n', ' ');
			String[] sentences = splitInSentences(seg, preprocess); 
			
			for(String sent : sentences) {
				result.add(sent.trim());
			}
		}
		
		String last = result.get(result.size()-1).replace(END_SEGMENT_MARK, "");
		result.remove(result.size()-1);
		result.add(last);
		
		return result;
	}

	
//	public static ArrayList<String> getSentences(File file, Segmenter alg) {
//		
//		if(Files.getFileExtension(file).equals("csv")) {
//			return segmentsToSentences(CSVSegmentsToArray(file), alg.getPreprocess());
//		}
//		else {
//			return segmentsToSentences(alg.getSegments(file), alg.getPreprocess());
//		}
//			
//	}
	
	public static void main(String[] args) {
		File doc = new File("Ata 32 Real.txt");
		File ref = new File(doc.getAbsoluteFile()+".csv");
		
		System.out.println("Measures\n");
		
		TextTilingBR tt = new TextTilingBR();
		tt.setStep(10);
		Segmenter segmenter = tt;
	

		ArrayList<String> segsRef = MeasureUtils.CSVSegmentsToArray(ref);
		ArrayList<String> segsHyp = segmenter.getSegments(doc);

		ArrayList<String> sentRef = MeasureUtils.segmentsToSentences(segsRef, segmenter.getPreprocess());
		ArrayList<String> sentHyp = MeasureUtils.segmentsToSentences(segsHyp, segmenter.getPreprocess());
	
		MeasureUtils.printSegmentedSentences(sentRef, "=============");
		System.out.println("\n*********\n");
		MeasureUtils.printSegmentedSentences(sentHyp, "-------------");
		
		System.out.println("Ref Ind: " + MeasureUtils.getSegmentedIndexes(sentRef));
		System.out.println("Hyp Ind: " + MeasureUtils.getSegmentedIndexes(sentHyp));
		
		System.out.println();
		
		System.out.println("    Ind: " + MeasureUtils.getIndexes(sentRef));
		System.out.println("Ref Bin: " + MeasureUtils.arrayToString(MeasureUtils.getBinarySegmentation(sentRef)));
		System.out.println("Hyp Bin: " + MeasureUtils.arrayToString(MeasureUtils.getBinarySegmentation(sentHyp)));
		
		System.out.println();
		
		System.out.println("Ref Num: " + MeasureUtils.arrayToString(MeasureUtils.getNumeredSegmentation(sentRef)));
		System.out.println("Hyp Num: " + MeasureUtils.arrayToString(MeasureUtils.getNumeredSegmentation(sentHyp)));
	
		
		System.out.println("\n\nFim");
		
		SegMeasures sm = new SegMeasures(ref, doc, segmenter);
		
		System.out.println(sm.getPk());
		System.out.println(sm.getWd());
		
		
	}



}
