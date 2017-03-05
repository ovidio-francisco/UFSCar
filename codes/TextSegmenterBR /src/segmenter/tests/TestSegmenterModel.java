package segmenter.tests;

import java.io.File;

import segmenter.Segmenter;
import segmenter.Segmenter.SegmenterAlgorithms;
import segmenter.algorithms.c99br.C99BR;
import segmenter.algorithms.texttile.TextTilingBR;
import segmenter.evaluations.Evaluation;
import segmenter.evaluations.EvaluationData;

public class TestSegmenterModel {
	
	private SegmenterAlgorithms alg = null;
	private int winSize = 0;
	private int step = 0;
//	private int segmentsNumber = 0;
	private double segmentsRate = 0.75;
	private int rankingSize = 0;
	private boolean weight = false;
	

	public TestSegmenterModel(int winSize, int step) {
		this.winSize = winSize;
		this.step = step;
		this.alg = SegmenterAlgorithms.TEXT_TILING;
		
//		((TextTilingBR)this.alg).setWindowSize(winSize);
//		((TextTilingBR)this.alg).setStep(step);
	}

	public TestSegmenterModel(double segmentsRate, int rankingSize, boolean weight) {
//		this.segmentsNumber = segmentsNumber;
		this.segmentsRate = segmentsRate;
		this.rankingSize = rankingSize;
		this.weight = weight;
		this.alg = SegmenterAlgorithms.C99;
		
//		((C99BR)this.alg).setnSegs(segmentsNumber);
//		((C99BR)this.alg).setRakingSize(rankingSize);
//		((C99BR)this.alg).setWeitght(weight);
		
	}
	
	public EvaluationData test(File txt) {
		File gold = new File(txt.getAbsoluteFile()+".csv");
		
		Segmenter seg = null;

		if(alg == SegmenterAlgorithms.TEXT_TILING) {
			seg = new TextTilingBR();
			((TextTilingBR)seg).setWindowSize(this.winSize);
			((TextTilingBR)seg).setStep(this.step);
		} 

		if(alg == SegmenterAlgorithms.C99) {
			seg = new C99BR();
//			((C99BR)seg).setnSegs(this.segmentsNumber);
			((C99BR)seg).setnSegsRate(this.segmentsRate);
			((C99BR)seg).setRakingSize(this.rankingSize);
			((C99BR)seg).setWeitght(this.weight);
		}
		
		try {
			return Evaluation.evalueate(gold, txt, seg);
		} catch (Exception e) {
			System.out.println(String.format("[Erro ao testar arquivo: %s [%s %s]]", txt, e.getClass(), e.getMessage()));
			e.printStackTrace();
			return null;
		}
		
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

	
	
	
	
	

	
	
	
	

}
