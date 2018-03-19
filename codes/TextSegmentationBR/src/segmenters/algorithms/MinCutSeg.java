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
		text = getPreprocess().identifyEOS(text, EOS);
		
		int sentences_count = StringUtils.countMatches(text, EOS)+1; 
		int num_segs = sentences_count/2;

 		/** Cria um arquivo com o texto preprocessado */
		String[] pt = text.split(EOS);
		
		for(int i=0; i<pt.length; i++) {
			pt[i] = preprocessLines(pt[i]);
		}
		for(int i=0; i<pt.length-1; i++) {
			pt[i] = pt[i]+"\n";
		}
 		
 		File tmp1 = new File("tempMinCut.txt");
 		Files.saveLinesToTxtFile(pt, tmp1);

 		
 		
 		/** Pega os bounds do texto arquivo com o texto preprocessado*/
 		List<Integer> bounds = getBoundaries(tmp1, num_segs);
 		
 		/** Pega as linas do texto original*/
 		String[] originalLines = text.split(EOS);
 		List<String> lines = new ArrayList<String>();
		for(String s : originalLines) {
			lines.add(s);
		}

		
 		ArrayList<String> segments = getRawSegmentedText(lines, bounds);
		
		tmp1.delete();

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
//        int nu = text.getReferenceSeg().size();
//        System.out.println(String.format("%d", nu));
//	      List[] hyp_segs = segmenter.segmentTexts(texts,MyTextWrapper.getNumSegs(texts));
	}

	@Override
	public String getLabel() {
		return "MinCut";
	}

	@Override
	public String getConfigurationLabel() {
		return "MinCut";
	}

	@Override
	public String getParamByName(ParamName paramName) {
		// TODO Auto-generated method stub
		return null;
	}

}
