package segmenters.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import segmenters.AbstractSegmenter;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.wrappers.MCSWrapper;
import utils.Files;

public class MinCutSeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-";
	
	@Override
	public ArrayList<String> getSegments(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
		int sentences_count = StringUtils.countMatches(text, EOS)+1; 
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
 		
 		int num_segs = sentences_count/2;
		ArrayList<String> segments = getRawSegmentedText(tmp, getBoundaries(tmp, num_segs));

		tmp.delete();
		
		return segments;
	}

	@Override
	public String preprocessToString() {
		return "No preprocess";
	}

	@Override
	public String getAlgorithmName() {
		return "MinCutSeg";
	}

	@Override
	public String paramsToString() {
		return "Params ???";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Integer> getBoundaries(File doc, int num_segs) {
		MCSWrapper seg = new MCSWrapper();
		String config = "config/mcsopt.ai.config";
		
		seg.initialize(config); //fazer o initialize para o DPSeg
		
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
        SegTester.preprocessText(text, false, false, false, false, 0);
        
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{num_segs});

		return hyp_segs[0];
	}

	@Override
	public String getLabel() {
		return "MinCut";
	}

	@Override
	public String getConfigurationLabel() {
		return "MinCut";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Integer> getBoundaries(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);	
 	
		MCSWrapper seg = new MCSWrapper();
		String config = "config/mcsopt.ai.config";
		
		seg.initialize(config); //fazer o initialize para o DPSeg
		
		MyTextWrapper textw = new MyTextWrapper(tmp.getPath());
        SegTester.preprocessText(textw, false, false, false, false, 0);
        
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{textw}, new int[]{4});

		return hyp_segs[0];
	}

}




//int nu = text.getReferenceSeg().size();
//System.out.println(String.format("%d", nu));
//List[] hyp_segs = segmenter.segmentTexts(texts,MyTextWrapper.getNumSegs(texts));