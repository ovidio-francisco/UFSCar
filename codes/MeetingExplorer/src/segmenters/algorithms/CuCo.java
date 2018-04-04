package segmenters.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import segmenters.AbstractSegmenter;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.mcmc.CuCoSeg;
import utils.Files;

public class CuCo extends AbstractSegmenter {
	private static final String EOS = "-EOF-";
	
	@Override
	public ArrayList<String> getSegments(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("temp.txt");
		text = getPreprocess().identifyEOS(text, EOS);
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
 		
 		
		ArrayList<String> segments = new ArrayList<>();
		try {
			segments = getRawSegmentedText(tmp, getBoundaries(tmp));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tmp.delete();
		
		return segments;
	}

	@Override
	public String preprocessToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String paramsToString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	MyTextWrapper[] texts = new MyTextWrapper[1];

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Integer> getBoundaries(File doc) throws Exception {
//		BayesWrapper seg = new BayesWrapper();
		CuCoSeg seg = new CuCoSeg();
		
		String config = "config/cue.config";
		seg.initialize(config); //fazer o initialize para o DPSeg
		
		
        //actually load them?
		SegTester tester = new SegTester(config);
        texts[0] = tester.loadText(doc.getAbsolutePath());
		
		
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
//        SegTester.preprocessText(text, false, false, false, false, 0);
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{4});
//		System.out.println(hyp_segs[0]);
//		System.out.println("Sentence count = " + text.getSentenceCount());

		return hyp_segs[0];
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConfigurationLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParamByName(ParamName paramName) {
		// TODO Auto-generated method stub
		return null;
	}

}
