package segmenter.evaluations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import preprocessamento.Preprocess;
import segmenter.Segmenter;
import utils.Files;

public class Evaluation {
	
	public static String END_SEGMENT_MARK = "_EOSEG_";
	public static String END_SENTENCE_MARK = "_EOSENT_";
	

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
	
	/**
	 * Reads a csv file split the segments into sentences and put the sentences into an array
	 */
//	public static ArrayList<String> CSVSegmentsToSentences(File csvFile) {
//
//		return segmentsToSentences(CSVSegmentsToArray(csvFile));
//	}
	
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
	public static ArrayList<String> segmentsToSentences2(ArrayList<String> segments, Preprocess preprocess) {
		ArrayList<String> result = new ArrayList<>();
		
		
		String txt = "";
		for(String seg : segments) {
//			aqui!
//			seg                = seg.replace('\n', ' ');
			
			txt += seg;
		}

		String[] sentences = splitInSentences(txt, preprocess); 
		
		for(String sent : sentences) {
			result.add(sent.trim());
		}
		
		String last = result.get(result.size()-1).replace(END_SEGMENT_MARK, "");
		result.remove(result.size()-1);
		result.add(last);
		
		System.out.println("_________________________________>" + result.size());
		
		
		return result;
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
	
	private static String[] splitInTokens(String segment) {
		String s1 = segment.trim()+END_SEGMENT_MARK;
		return s1.split(" ");
	}
	
	private static ArrayList<String> segmentsToTokens(ArrayList<String> segments) {
		ArrayList<String> result = new ArrayList<>();
		
		for(String seg : segments) {
			
			seg                = seg.replace('\n', ' ');
			String[] sentences = splitInTokens(seg); 
			
			for(String sent : sentences) {
				String token = sent.trim();
				if(token.length() > 0 )
					result.add(token);
			}
		}
		
		String last = result.get(result.size()-1).replace(END_SEGMENT_MARK, "");
		result.remove(result.size()-1);
		result.add(last);
		
		return result;
	}


	private static ArrayList<String> CSVSegmentsToTokens(File csvFile) {
		return segmentsToTokens(CSVSegmentsToArray(csvFile));
	}


	public static ArrayList<String> getSentences(File file, Segmenter alg) {
		
		if(Files.getFileExtension(file).equals("csv")) {
			return segmentsToSentences(CSVSegmentsToArray(file), alg.getPreprocess());
		}
		else {
			return segmentsToSentences(alg.getSegments(file), alg.getPreprocess());
		}
			
	}
	
	public static ArrayList<String>  getTokens(File file, Segmenter alg) {
		if(Files.getFileExtension(file).equals("csv")) {
			return CSVSegmentsToTokens(file);
		}
		else {
			return segmentsToTokens(alg.getSegments(file));
		}

	}
	
	public static EvaluationData evalueate(File real, File test, Segmenter alg) {
		
		/*cacaca*/
		
		ArrayList<String> realSegs = getSentences(real, alg);
		ArrayList<String> testSegs = getSentences(test, alg);

		
		if(realSegs.size() != testSegs.size()) {
			int l = realSegs.size() < testSegs.size() ? realSegs.size() : testSegs.size();
			
			System.out.println("--->" + test);
			System.out.println(String.format("=========> Diferença na qtd de sentenças (%d != %d)", realSegs.size(), testSegs.size()));
			
			for(int i = 0; i<l ; i++) {
				System.out.println(String.format("-->%s\n-->%s\n\n", realSegs.get(i), testSegs.get(i)));
			}
			
		}
		
		
		
		ArrayList<Integer> boundariesReal = new ArrayList<>();
		ArrayList<Integer> boundariesHypo = new ArrayList<>();
		
		for(String s : realSegs) {
			boundariesReal.add(s.endsWith(END_SEGMENT_MARK) ? 1 : 0);
		}
		boundariesReal.remove(boundariesReal.size()-1);
		
		for(String s : testSegs) {
			boundariesHypo.add(s.endsWith(END_SEGMENT_MARK) ? 1 : 0);
		}
		boundariesHypo.remove(boundariesHypo.size()-1);
		
		int i = 0;
		
		LinkedList<Integer> gold = new LinkedList<>();
		LinkedList<Integer> hypo = new LinkedList<>();
		
		i = 0;
		for(String s : realSegs) {
			if(s.endsWith(END_SEGMENT_MARK)) {
				gold.add(i);
			}
			i++;
		}
//		gold.add(i);
		
		i = 0;
		for(String s : testSegs) {
			if(s.endsWith(END_SEGMENT_MARK)) {
				hypo.add(i);
			}
			i++;
		}
//		hypo.add(i);
		
//		System.out.println(String.format("%d $$ %d", gold.size(), hypo.size()));
//		TODO: verificar porque gold e hypo estão com zero elementos!
		
		return new EvaluationData(gold, hypo, boundariesReal, boundariesHypo, real, test, alg);
	}
}



