package segmenters.algorithms.edu.mit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessamento.Preprocess;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.Segmenter;
import segmenters.algorithms.edu.mit.nlp.segmenter.dp.BayesWrapper;
import utils.Files;


public class Main {

	private static final String EOS = "-EOF-"; 
	
	public static void main(String[] args) {
		System.out.println("Novo Segmenter");

		File doc = new File("Ata 32.txt");
		File tmp = new File("temp.txt");
		File leg = new File("legiao.txt");
		
		String text = Files.loadTxtFile(doc);
		text = new Preprocess().identifyEOS(text, EOS);
 		System.out.println("Senteces count --> " + text.split(EOS).length);
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
 		
		
		
		List<String> segments = getRawSegmentedText(tmp, getBoundaries(tmp));
		System.out.println("Sengments count --> " + segments.size());
		
		
		for(int i=0; i<segments.size(); i++) {
			System.out.println(segments.get(i));
		}
		
		
		System.out.println("=================\n"+text);
	}
	
	
	public static List<Integer> getBoundaries(File doc) {
		Segmenter seg = new BayesWrapper();
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
        SegTester.preprocessText(text, false, false, false, false, 0);
		((BayesWrapper)seg).num_segs_known = false;
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{text.getReferenceSeg().size()});
		System.out.println(hyp_segs[0]);
		System.out.println("Sentence count = " + text.getSentenceCount());
//		System.out.println(text.getRawSegmentedText(hyp_segs[0]));

		return hyp_segs[0];
	}
	
	
	public static List<String> getRawSegmentedText(File doc, List<Integer> breaks) {
		List<String> lines = Files.loadTxtFileToList(doc);
		
		return getRawSegmentedText(lines, breaks);
	}
	public static List<String> getRawSegmentedText(List<String> lines, List<Integer> breaks) {
		List<String> result = new ArrayList<>();
		String seg = "";
		
		for(int i=0; i<lines.size(); i++) {
			
			if(breaks.contains(i)) {
//				result.add(seg+"-----------------------------------------------------------> ["+i+"]");
				result.add(seg);
				seg = lines.get(i);
			}
			else {
				seg += lines.get(i);
			}
		}
		
//		result.add(seg+"-----------------------------------------------------------> [?]");
		result.add(seg);
		
		return result;
	}
}
