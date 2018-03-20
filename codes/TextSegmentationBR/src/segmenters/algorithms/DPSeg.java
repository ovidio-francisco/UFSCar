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
	
	private int nSegs = 10;
	private double nSegsRate = 0.5;
	private int nSentences = 0;
	private boolean numSegsKnown = false;

	
	
	@Override
	public ArrayList<String> getSegments(String text) {
		
		text = getPreprocess().cleanTextMeating(text);
		text = getPreprocess().identifyEOS(text, EOS);
		
 		/** Cria um arquivo com o texto preprocessado */
		String[] pt = text.split(EOS);
		nSentences = pt.length;
		
		for(int i=0; i<pt.length; i++) {
			pt[i] = preprocessLines(pt[i]);
		}
		for(int i=0; i<pt.length-1; i++) {
			pt[i] = pt[i]+"\n";
		}
 		
 		File tmp1 = new File("temp1.txt");
 		Files.saveLinesToTxtFile(pt, tmp1);
 		
 		/** Pega os bounds do texto arquivo com o texto preprocessado*/
 		List<Integer> bounds = getBoundaries(tmp1);
 		
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
		return "DPSeg";
	}

	@Override
	public String paramsToString() {
		return "Params ???";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Integer> getBoundaries(File doc) {
		MyTextWrapper text = new MyTextWrapper(doc.getPath());
        SegTester.preprocessText(text, false, false, false, false, 0);
        
//      seg.initialize("config/dp.config");
//		seg.num_segs_known = false;  
		seg.num_segs_known = numSegsKnown;  
        
        if (seg.num_segs_known) {
        	nSegs =  Math.round((float)nSentences * (float)nSegsRate);
//        	System.out.println(seg.num_segs_known+"======"+nSentences+"---------------------------"+nSegs +"....."+nSegsRate);
        }
        
		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{nSegs});

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

	@Override
	public String getParamByName(ParamName paramName) {

		switch (paramName) {
			case PRIOR: return String.format("%.4f", getWrapper().prior);			
			case DISPERSION: return String.format("%.4f", getWrapper().dispersion);			
			case NUM_SEGS_KNOWN: return String.format("%b", getWrapper().num_segs_known);			
			case NSEGRATE: return nSegsRate <= 0 ? "Auto" : String.format("%.3f", nSegsRate);			
			default: return "???"; 
		}

	}
	
	/*by OJF */
	private BayesWrapper seg = new BayesWrapper();

	public BayesWrapper getWrapper() {
		return seg;
	}
	public int getnSegs() {
		return nSegs;
	}
	public void setnSegs(int nSegs) {
		this.nSegs = nSegs;
	}
	public double getnSegsRate() {
		return nSegsRate;
	}
	public void setnSegsRate(double nSegsRate) {
		this.nSegsRate = nSegsRate;
	}
	public int getnSentences() {
		return nSentences;
	}

	public boolean isNumSegsKnown() {
		return numSegsKnown;
	}

	public void setNumSegsKnown(boolean numSegsKnown) {
		this.numSegsKnown = numSegsKnown;
	}
	
}
