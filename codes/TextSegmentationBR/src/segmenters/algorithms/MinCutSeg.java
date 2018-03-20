package segmenters.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.mit.nlp.segmenter.SegmenterParams;
import segmenters.AbstractSegmenter;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.SegTester;
import segmenters.algorithms.edu.mit.nlp.segmenter.wrappers.MCSWrapper;
import utils.Files;

public class MinCutSeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-";
	
	private SegmenterParams segmenterParams = null;
	private double nSegsRate = 0.5;	
	
	public SegmenterParams getSegmenterParams() {
		return segmenterParams;
	}

	public void setSegmenterParams(SegmenterParams segmenterParams) {
		this.segmenterParams = segmenterParams;
	}

	@Override
	public ArrayList<String> getSegments(String text) {
		
		text = getPreprocess().cleanTextMeating(text);
		text = getPreprocess().identifyEOS(text, EOS);
		
		int sentences_count = StringUtils.countMatches(text, EOS)+1; 
		int num_segs = (int)(sentences_count * nSegsRate);
		
//		System.out.println(String.format("num_segs ========== %d", num_segs));

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
		
		if(segmenterParams != null) {
			seg.configure(segmenterParams);
//			System.out.println("Configured by OJF -----------------------------");
		}
		
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
		return String.format("MinCutSeg SRate:%.2f LCO:%d", nSegsRate, segmenterParams.getSegLenCutoff());
	}

	@Override
	public String getParamByName(ParamName paramName) {

		switch (paramName) {
			case NSEGRATE: return String.format("%.3f", nSegsRate);			
//			case PARZEMALPHA: return String.format("%.3f", segmenterParams.getParzenAlpha());			
			case LENCUTOFF: return (segmenterParams == null) ? "disabilitado" : String.format("%d", segmenterParams.getSegLenCutoff());			
//			case SMOOTHINGRANGE: return (segmenterParams == null) ? "disabilitado" : String.format("%d", segmenterParams.getParzenSmoothingRange());			
			default: return "???"; 
		}
	}

	public double getnSegsRate() {
		return nSegsRate;
	}

	public void setnSegsRate(double nSegsRate) {
		this.nSegsRate = nSegsRate;
	}
	
	

}
