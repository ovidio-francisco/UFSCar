package segmenters.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import segmenters.AbstractSegmenter;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.wrappers.LCSegWrapper;
import utils.Files;

public class LCSeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-"; 
	
	@Override
	public ArrayList<String> getSegments(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
		
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
 		
		
		
		ArrayList<String> segments = getRawSegmentedText(tmp, getBoundaries(tmp));

		return segments;
	}

	@Override
	public String preprocessToString() {
		return "No preprocess";
	}

	@Override
	public String getAlgorithmName() {
		return "LCSeg";
	}

	@Override
	public String paramsToString() {
		return "Params ???";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Integer> getBoundaries(File doc) {
		LCSegWrapper seg = new LCSegWrapper();
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
        SegTester.preprocessText(text, false, false, false, false, 0);
		seg.num_segs_known = false;
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{text.getReferenceSeg().size()});
//		System.out.println(hyp_segs[0]);
//		System.out.println("Sentence count = " + text.getSentenceCount());

		return hyp_segs[0];
	}

	@Override
	public String getLabel() {
		return "LCSeg";
	}

	@Override
	public String getConfigurationLabel() {
		return "LCSeg";
	}

	@Override
	public String getParamByName(ParamName paramName) {
		// TODO Auto-generated method stub
		return null;
	}

}
