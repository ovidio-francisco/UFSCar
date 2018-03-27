package segmenters.evaluations.measure;

import java.io.File;
import java.util.ArrayList;

import segmenters.Segmenter;

public class SegMeasures {
	
	private File doc = null;	
	private File ref = null;
	private Segmenter segmenter = null;
	
	private ArrayList<String> segmentsRef = null;
	private ArrayList<String> segmentsHyp = null;
	
	private ArrayList<String> sentencesRef = null;
	private ArrayList<String> sentencesHyp = null;
	
	private double pk = -1;
	private double wd = -1;
	private double kp = -1;
	
	private int tp;
	private int tn;
	private int fp;
	private int fn;
	
	private int total;
	
	private double accuracy;
	private double precision;
	private double recall;
	private double f1;

	public SegMeasures(File ref, File doc, Segmenter segmenter) {
		this.doc = doc;
		this.ref = ref;
		this.segmenter = segmenter;
		
		segmentsRef = MeasureUtils.CSVSegmentsToArray(ref);
		segmentsHyp = segmenter.getSegments(doc);
		
		sentencesRef = MeasureUtils.segmentsToSentences(segmentsRef, segmenter.getPreprocess());
		sentencesHyp = MeasureUtils.segmentsToSentences(segmentsHyp, segmenter.getPreprocess());
		
		int[] numbRef = MeasureUtils.getNumeredSegmentation(sentencesRef);
		int[] numbHyp = MeasureUtils.getNumeredSegmentation(sentencesHyp);
		
		int[] binRef = MeasureUtils.getBinarySegmentation(sentencesRef);
		int[] binHyp = MeasureUtils.getBinarySegmentation(sentencesHyp);
		
		calcTraditionalMeasures(binRef, binHyp);
		
		this.pk = pkMeasure(numbRef, numbHyp);
		this.wd = wdMeasure(numbRef, numbHyp);		
		this.kp = KappaSegMeasure(binRef, binHyp);
	}
	

	private double pkMeasure(int[] ref, int[] hyp) {
		int k = (int) Math.round((double) ref.length / (2 * ref[ref.length-1]));
		double num_misses = 0.0;

		for (int i = k; i < ref.length; i++){
		    if ((ref[i] == ref[i-k]) != (hyp[i] == hyp[i-k])) num_misses++;
		}
		
		return num_misses / (ref.length - k);
	}
	
	private double wdMeasure(int[] ref, int[] hyp) {
		int k = (int) Math.round((double) ref.length / (2 * ref[ref.length-1]));
		double num_misses = 0.0;

		for (int i = k; i < ref.length; i++){
			num_misses += Math.abs((ref[i] - ref[i-k]) - (hyp[i] - hyp[i-k]));
		}
		
		return num_misses / (ref.length - k);
	}
	
	private double KappaSegMeasure(int[] ref, int[] hyp) {
		double k = new KappaSeg(ref, hyp).findKappa();
//		System.out.println(String.format("kappa = %f", k));
		return k;
	}

	
	private void calcTraditionalMeasures(int[] ref, int[] hyp) {
		tp = tn = fp = fn = 0;
		
		total = ref.length;
			
		for(int i=0; i<ref.length; i++) {
			tp += ((ref[i] == 1) && (hyp[i] == 1)) ? 1 : 0;  // ref says Yes and hyp says Yes
			tn += ((ref[i] == 0) && (hyp[i] == 0)) ? 1 : 0;  // ref says No  and hyp says No
			
			fp += ((ref[i] == 0) && (hyp[i] == 1)) ? 1 : 0;  // ref says No  and hyp says Yes
			fn += ((ref[i] == 1) && (hyp[i] == 0)) ? 1 : 0;  // ref says Yes and hyp says No  
		}
		
		
		this.accuracy   = (double)(tp+tn)/ (double) total;
		this.precision  = (double)tp     / (double)(tp+fp);
		this.recall     = (double)tp     / (double)(tp+fn);
		this.f1         = (double)(2*tp) / (double)((2*tp)+fp+fn);
	}

	public String getModelLabel() {
		return segmenter.getLabel();
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
		return sentencesRef;
	}

	public ArrayList<String> getSegHyp() {
		return sentencesHyp;
	}

	public double getPk() {
		return pk;
	}

	public double getWd() {
		return wd;
	}
	
	public double getKappa() {
		return kp;
	}

	public int getTp() {
		return tp;
	}

	public int getTn() {
		return tn;
	}

	public int getFp() {
		return fp;
	}

	public int getFn() {
		return fn;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getPrecison() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getF1() {
		return f1;
	}


	public double getSegmentsCountHyp() {
		return segmentsHyp.size();
	}

	public double getSegmentsCountRef() {
		return segmentsHyp.size();
	}

}
