package segmenters.tests;

import java.io.File;

import preprocessamento.Preprocess;
import segmenters.Segmenter;
import segmenters.Segmenter.SegmenterAlgorithms;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;
import segmenters.evaluations.Evaluation;
import segmenters.evaluations.EvaluationData;

public class TestSegmenterModel {
	
	private SegmenterAlgorithms alg = null;
	
	private int       winSize = 20;
	private int       step = 3;
	
	private double    segmentsRate = 0.4;
	private int       rankingSize = 11;
	private boolean   weight = true;
	
//	private int segmentsNumber = 0;
	private Segmenter segmenter = null;
	
	private Preprocess preprocess = new Preprocess();

	
	
	public TestSegmenterModel(int winSize, int step) {
		this.alg = SegmenterAlgorithms.TEXT_TILING;
		this.segmenter = new TextTilingBR();
		this.winSize = winSize;
		this.step = step;
	}

	public TestSegmenterModel(double segmentsRate, int rankingSize, boolean weight) {
		this.alg = SegmenterAlgorithms.C99;
		this.segmenter = new C99BR();	
		this.segmentsRate = segmentsRate;
		this.rankingSize = rankingSize;
		this.weight = weight;
		
		((C99BR)this.segmenter).setnSegsRate(this.segmentsRate);
		((C99BR)this.segmenter).setRakingSize(this.rankingSize);
		((C99BR)this.segmenter).setWeitght(this.weight);

		
	}
	
	public TestSegmenterModel() {
		this.alg = SegmenterAlgorithms.SENTENCES;
		this.segmenter = new SentencesSegmenter();
	}

	
	public TestSegmenterModel(SegmenterAlgorithms alg) {
		this.alg = alg;
		
		if(alg == SegmenterAlgorithms.TEXT_TILING) {
			this.segmenter = new TextTilingBR();
			((TextTilingBR)this.segmenter).setWindowSize(this.winSize);
			((TextTilingBR)this.segmenter).setStep(this.step);
		} 

		if(alg == SegmenterAlgorithms.C99) {
			this.segmenter = new C99BR();
			((C99BR)this.segmenter).setnSegsRate(this.segmentsRate);
			((C99BR)this.segmenter).setRakingSize(this.rankingSize);
			((C99BR)this.segmenter).setWeitght(this.weight);
		}
		
		if(alg == SegmenterAlgorithms.SENTENCES) {
			this.segmenter = new SentencesSegmenter();
		}

		if(alg == SegmenterAlgorithms.MINCUT) {
			this.segmenter = new MinCutSeg();
		}

		if(alg == SegmenterAlgorithms.TEXT_SEG) {
			this.segmenter = new UISeg();
		}

		if(alg == SegmenterAlgorithms.BAYESSEG) {
			this.segmenter = new DPSeg();
		}

	}
	
	
	
	
	
	public EvaluationData test(File txt) {
		File gold = new File(txt.getAbsoluteFile()+".csv");
		
		segmenter.setPreprocess(preprocess);
		
		try {
			return Evaluation.evalueate(gold, txt, segmenter);
		} catch (Exception e) {
			System.out.println(String.format("[Erro ao testar arquivo: %s [%s %s]]", txt, e.getClass(), e.getMessage()));
			e.printStackTrace();
			return null;
		}
		
	}

	
	
	
	
	public Preprocess getPreprocess() {
		return preprocess;
	}

	public void setPreprocess(Preprocess preprocess) {
		this.preprocess = preprocess;
	}

	public void increaseWinSize(int n) {
		this.winSize += n;
	}
	
	public void increaseStep(int n) {
		this.step += n;
	}
	
//	public void increaseSegmentsNumber(int n) {
//		this.segmentsNumber += n;
//	}
	
	public void icreaseSegmentsRate(double d) {
		this.segmentsRate += d;
	}
	
	public void increaseRankingSize(int n) {
		this.rankingSize += n;
	}
	
	public void setWeight(boolean weight) {
		this.weight = weight;
	}

	public int getWinSize() {
		return winSize;
	}

	public void setWinSize(int winSize) {
		this.winSize = winSize;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

//	public int getSegmentsNumber() {
//		return segmentsNumber;
//	}
//
//	public void setSegmentsNumber(int segmentsNumber) {
//		this.segmentsNumber = segmentsNumber;
//	}

	public int getRankingSize() {
		return rankingSize;
	}

	public void setRankingSize(int rankingSize) {
		this.rankingSize = rankingSize;
//		((C99BR) segmenter).setRakingSize(rankingSize);  
	}

	public boolean isWeight() {
		return weight;
	}

	public double getSegmentsRate() {
		return segmentsRate;
	}

	public void setSegmentsRate(double segmentsRate) {
		this.segmentsRate = segmentsRate;
	}

	public SegmenterAlgorithms getAlg() {
		return alg;
	}
}
