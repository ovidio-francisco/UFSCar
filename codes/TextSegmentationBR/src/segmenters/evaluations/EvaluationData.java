package segmenters.evaluations;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import segmenters.Segmenter;
import segmenters.evaluations.measure.PkMeasure;
import segmenters.evaluations.measure.WinPR;
import segmenters.evaluations.measure.WindowdiffMeasure;

public class EvaluationData {
	private static int count = 0;
	
	private int    id = -1;
	private double realSegmentsCount = 0;
	private double testSegmentsCount = 0;
	private double truePositives  = 0;
	private double falsePositives = 0;
	private double trueNegatives  = 0;
	private double falseNegatives = 0;
	private double acuracy = 0;
	private double precision = 0;
	private double recall = 0;
	private double f1 = 0;
	private double total = 0;
	private File real = null;
	private File test = null;
	private Segmenter alg;
	
	private String matrix = "";
	
//	@SuppressWarnings("unused")
//	private WinPR winPR = new WinPR();
	
	private double windowDiff = 0;
//	private int    windowSize = 0;
	private double pk = 0;

	private ArrayList<Integer> boundariesReal;
	private ArrayList<Integer> boundariesHypo;	
	
	public EvaluationData(LinkedList<Integer> gold, LinkedList<Integer> hypo, ArrayList<Integer> boundariesReal, ArrayList<Integer> boundariesHypo, File fileReal, File fileTest, Segmenter alg) {
		EvaluationData.count++;
		
		this.id   = count-1;
		this.real = fileReal;
		this.test = fileTest;
		this.alg  = alg;
		
		this.boundariesReal = boundariesReal;
		this.boundariesHypo = boundariesHypo;
				

		int len = boundariesReal.size() < boundariesHypo.size() ? boundariesReal.size() : boundariesHypo.size();
		double tp=0, fp=0, fn=0;
		double tn=0;
		double ap=0, pp=0; 	
		
		for(int i=0; i<len; i++) {
			
			int r = boundariesReal.get(i), t = boundariesHypo.get(i);
					
			if(r==1) ap++;    // actualy   positivies
			if(t==1) pp++;    // predicted positivies
			
			if (r==1 && t==1) tp++;
			if (r==0 && t==1) fp++;
			if (r==1 && t==0) fn++;
			if (r==0 && t==0) tn++;
			
		}
		
		this.realSegmentsCount = ap;
		this.testSegmentsCount = pp;
		this.truePositives  = tp;
		this.falsePositives = fp;
		this.trueNegatives  = tn;
		this.falseNegatives = fn;
		this.total = tp+fp+tn+fn;

		this.acuracy    = (tp+tn)/total;
		this.precision  = tp / (tp+fp);
		this.recall     = tp / (tp+fn);
		this.f1         = (2*tp)/((2*tp)+fp+fn);
		this.windowDiff = WinDiffMeasure(gold, hypo);
//		this.pk         = pkMeasure((int)total, boundariesReal, boundariesHypo);
		this.pk         = pkMeasure(len, boundariesReal, boundariesHypo);
		

		
		
		StringBuilder sb = new StringBuilder();
//		sb.append("\nindex: 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9");
		sb.append("\ngold : ");
		for(Integer i : boundariesReal) sb.append(String.format("%d ", i));
		sb.append("\nhypo : ");
		for(Integer i : boundariesHypo) sb.append(String.format("%d ", i));
		sb.append("\n");
		
		
//		sb.append("\nBoundary positions + Candidate boundaries");
//		sb.append("\ngold: ");
//		for(Integer i : gold) sb.append(String.format("%2d ", i));
//		sb.append("\nhypo: ");
//		for(Integer i : hypo) sb.append(String.format("%2d ", i));
//		sb.append("\n");

		matrix = sb.toString();

	}
	
	private double WinDiffMeasure(LinkedList<Integer> gold, LinkedList<Integer> hypo) {
		gold.add((int)total);
		hypo.add((int)total);
//		winPR      = WinPR.calculateWinPR(gold, hypo);
//		windowSize = WinPR.windowSize(gold);
		
		return WinPR.calculateWindowDiff( gold, hypo, WinPR.windowSize(gold) );		
	}
	
	private double pkMeasure(int sentenceCount, ArrayList<Integer> boundariesReal, ArrayList<Integer> boundariesHypo) {
		PkMeasure pk = new PkMeasure();
		
		StringBuilder sbRefe = new StringBuilder();
		StringBuilder sbHypo = new StringBuilder();
		
		for(int i=0; i<boundariesReal.size(); i++) {
			if(boundariesReal.get(i) == 1) sbRefe.append(String.format("%d ", i));
		}
		
		for(int i=0; i<boundariesHypo.size(); i++) {
			if(boundariesHypo.get(i) == 1) sbHypo.append(String.format("%d ", i));
		}
		
		String r = sbRefe.toString().trim();
		String h = sbHypo.toString().trim();
		
//		System.out.println(String.format("----------------- %d, '%s', '%s'", sentenceCount, r, h));
		
		pk.read(sentenceCount, r, h);
		
		segmenters.evaluations.measure.WindowdiffMeasure wd = new WindowdiffMeasure();
		wd.read(sentenceCount, r, h);
		
		return pk.compute();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("Id = %d\t - ", id));
		sb.append(String.format("%s - Automatic\n"                  , test.getParentFile().getName()+"/"+ test.getName()));
		sb.append(String.format("Algorithm: %s - %s\n"              , alg.getAlgorithmName(), alg.paramsToString()));
//		sb.append(String.format("%s - Judge      \n"                , real.getName()));
		
		sb.append(String.format("Candidate boundaries        %.0f\n", total));
		sb.append(String.format("Judge     boundaries        %.0f\n", realSegmentsCount));
		sb.append(String.format("Automatic boundaries        %.0f\n", testSegmentsCount));
		sb.append("\n");
//		sb.append(String.format("True  Positives             %.0f\n", truePositives));
//		sb.append(String.format("False Positives             %.0f\n", falsePositives));
//		sb.append(String.format("True  Negatives             %.0f\n", trueNegatives));
//		sb.append(String.format("False Negatives             %.0f\n", falseNegatives));

		sb.append(String.format("tp fp                       %.0f %.0f\n", truePositives , falsePositives));
		sb.append(String.format("fn tn                       %.0f %.0f\n", falseNegatives, trueNegatives));
		
		
		sb.append(String.format("Acuracy                     %f\n", acuracy));
		sb.append(String.format("Precision                   %f\n", precision));
		sb.append(String.format("Recall                      %f\n", recall));
		sb.append(String.format("F1                          %f\n", f1));
		
		sb.append(String.format("Window_Diff                 %f\n", windowDiff));
		sb.append(String.format("Pk                          %f\n", pk));
//		sb.append(String.format("Window_Diff Prime           %f\n", winPR.winDiffPrime()));
		
		sb.append(matrix);
		sb.append(String.format("# ===========//==========//==========\n\n"));

		return sb.toString();
	}
	
	
//	public String getAlg_Param() {
//		String alg = "" + getAlg().getLabel();
//		
//		if(getAlg() instanceof TextTilingBR) {
//			alg = "TT";
////			alg += "_";
////			alg += "ws=";
//			alg += "_";
//			alg += ((TextTilingBR)getAlg()).getWindowSize();
////			alg += "st=";
//			alg += "_";
//			alg += ((TextTilingBR)getAlg()).getStep();
//		}
//		
//		if(getAlg() instanceof C99BR) {
//			alg = "C99";
//			alg += "_";
//			alg += String.format("%d", (int)(100*((C99BR)getAlg()).getnSegsRate())); 
//			alg += "_";
//			alg += ((C99BR)getAlg()).getRakingSize();
//			alg += "_";
//			alg += ((C99BR)getAlg()).isWeitght();
//		}
//		
//		if(getAlg() instanceof SentencesSegmenter) {
//			alg = "Sentenças";
//		}
//		
//		String preprocess = "_" + (getAlg().getPreprocess().isRemoveStopWord() ? "T" : "F");
//		
//		alg = alg+preprocess;
//
//		return alg;
//	}
	
	
//	public String getConfigurationLabel() {
//		String alg = "";
//		
//		if(getAlg() instanceof TextTilingBR) {
//			alg = "TT";
//			alg += " " + String.format("%2d", ((TextTilingBR)getAlg()).getWindowSize()) ;
//			alg += " " + String.format("%2d", ((TextTilingBR)getAlg()).getStep());
//		}
//		
//		if(getAlg() instanceof C99BR) {
//			alg = "C99";
//			alg += " " + String.format("%2d", (int)(100*((C99BR)getAlg()).getnSegsRate())); 
//			alg += " " + String.format("%2d", ((C99BR)getAlg()).getRakingSize());
//			alg += " " + String.format("%s", ((C99BR)getAlg()).isWeitght() ? "T":"F");
//		}
//		
//		if(getAlg() instanceof SentencesSegmenter) {
//			alg = "Sentenças só";
//		}
//		String preprocess = " " + (getAlg().getPreprocess().isRemoveStopWord() ? "T" : "F");
		
//		alg = alg+preprocess;
//
//		return alg.getConfigurationLabel();
//	}
//	public String getAlgLabel() {
//		return alg.getLabel();
//	}
	
	
	

	public double getRealSegmentsCount() {
		return realSegmentsCount;
	}
	public double getTestSegmentsCount() {
		return testSegmentsCount;
	}
	public double getTruePositives() {
		return truePositives;
	}
	public double getFalsePositives() {
		return falsePositives;
	}

	public double getTrueNegatives() {
		return trueNegatives;
	}

	public double getFalseNegatives() {
		return falseNegatives;
	}

	public double getTotal() {
		return total;
	}

	public double getAcuracy() {
		return acuracy;
	}

	public double getF1() {
		return f1;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	

	public File getReal() {
		return real;
	}


	public File getTest() {
		return test;
	}

	public double getWindowDiff() {
		return windowDiff;
	}

	
	
//	public int getWindowSize() {
//		return windowSize;
//	}

	
	
	public double getPk() {
		return pk;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Integer> getBoundariesReal() {
		return boundariesReal;
	}

	public ArrayList<Integer> getBoundariesHypo() {
		return boundariesHypo;
	}

	public Segmenter getAlg() {
		return alg;
	}

	
	
	
	
}
