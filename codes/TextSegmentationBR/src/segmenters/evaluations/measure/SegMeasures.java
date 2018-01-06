package segmenters.evaluations.measure;

import java.io.File;
import java.util.ArrayList;

import segmenters.Segmenter;

public class SegMeasures {
	
	private File doc = null;
	private File ref = null;
	private Segmenter segmenter = null;

	private ArrayList<String> segRef = null;
	private ArrayList<String> segHyp = null;
	
	private double pk = -1;
	private double wd = -1;
	

	public SegMeasures(File ref, File doc, Segmenter segmenter) {
		this.doc = doc;
		this.ref = ref;
		this.segmenter = segmenter;
		
		segRef = MeasureUtils.getSentences(ref, segmenter);
		segHyp = MeasureUtils.getSentences(doc, segmenter);
		
		int[] numbRef = MeasureUtils.getNumeredSegmentation(segRef);
		int[] numbHyp = MeasureUtils.getNumeredSegmentation(segHyp);
		
		this.pk = pkMeasure(numbRef, numbHyp);
		this.wd = wdMeasure(numbRef, numbHyp);		
	}
	

	private static double pkMeasure(int[] ref, int[] hyp) {
		int k = (int) Math.round((double) ref.length / (2 * ref[ref.length-1]));
		double num_misses = 0.0;

		for (int i = k; i < ref.length; i++){
		    if ((ref[i] == ref[i-k]) != (hyp[i] == hyp[i-k])) num_misses++;
		}
		
		return num_misses / (ref.length - k);
	}
	
	private static double wdMeasure(int[] ref, int[] hyp) {
		int k = (int) Math.round((double) ref.length / (2 * ref[ref.length-1]));
		double num_misses = 0.0;

		for (int i = k; i < ref.length; i++){
			num_misses += Math.abs((ref[i] - ref[i-k]) - (hyp[i] - hyp[i-k]));
		}
		
		return num_misses / (ref.length - k);
	}


	public File getDoc() {
		return doc;
	}

	public File getRef() {
		return ref;
	}

	public Segmenter getSegmenter() {
		return segmenter;
	}

	public ArrayList<String> getSegRef() {
		return segRef;
	}

	public ArrayList<String> getSegHyp() {
		return segHyp;
	}

	public double getPk() {
		return pk;
	}

	public double getWd() {
		return wd;
	}

}
