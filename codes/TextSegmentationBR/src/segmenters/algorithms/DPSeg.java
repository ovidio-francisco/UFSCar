package segmenters.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import segmenters.AbstractSegmenter;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.dp.BayesWrapper;
import utils.Files;

public class DPSeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-"; 
	
	public DPSeg() {
	}

	@Override
	public ArrayList<String> getSegments(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
		
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
		
 		ArrayList<String> segments = getRawSegmentedText(tmp, getBoundaries1(tmp));
		
		tmp.delete();

		return segments;
	}

	@Override
	public String preprocessToString() {
		return "No preprocess";
	}

	@Override
	public String getAlgorithmName() {
		return "DPSeg";
	}

	@Override
	public String paramsToString() {
		return "Params ???";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Integer> getBoundaries1(File doc) {
		BayesWrapper seg = new BayesWrapper();
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
        SegTester.preprocessText(text, false, false, false, false, 0);
		seg.num_segs_known = false;
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{4});

		return hyp_segs[0];
	}

	@Override
	public String getLabel() {
		return "BayesSeg";
	}

	@Override
	public String getConfigurationLabel() {
		return "BayesSeg";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Integer> getBoundaries(String text) {
		
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
		
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
		
		BayesWrapper seg = new BayesWrapper();
		MyTextWrapper textw = new MyTextWrapper(tmp.getPath());
		tmp.delete();
        SegTester.preprocessText(textw, false, false, false, false, 0);
		seg.num_segs_known = false;
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{textw}, new int[]{4});

		return hyp_segs[0];
	}
}