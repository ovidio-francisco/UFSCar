package segmenters.evaluations.measure;

import java.util.ArrayList;

import preprocessamento.Preprocess;
import testSementers.TestSegmenters.Metric;
import tests.EvaluationSegModel;

public class AverageSegMeasures {

	private double pk = -99;
	private double wd = -99;
	private double ac = -99;
	private double pr = -99;
	private double rc = -99;
	private double f1 = -99;
	private double segCount = -99;


	
//	private String modelLabel = "Model Label";
	private EvaluationSegModel evSegModel = null;

	public AverageSegMeasures(ArrayList<SegMeasures> sms, EvaluationSegModel evSegModel) {
		System.out.println("Creating average measures for: "+evSegModel.getSegmenter().getConfigurationLabel());
		Preprocess p = evSegModel.getSegmenter().getPreprocess();
		System.out.println(String.format("preproces = %b %b %b %b %b %b %b %b", p.isRemoveAccents(), p.isRemoveHeaders(), p.isRemoveNumbers(), p.isRemovePunctuation(), p.isRemoveShortThan(), p.isRemoveStem(), p.isRemoveStopWord(), p.isToLowCase()));

		this.pk = average(sms, Metric.PK);
		this.wd = average(sms, Metric.WINDIFF);
		this.ac = average(sms, Metric.ACURACY);
		this.pr = average(sms, Metric.PRECISION);
		this.rc = average(sms, Metric.RECALL);
		this.f1 = average(sms, Metric.F1);
		

		
		this.segCount = average(sms, Metric.AVR_SEGS_COUNT);
		
		this.evSegModel = evSegModel;
	}
	
	private double average(ArrayList<SegMeasures> segMeasures, Metric metric) {
		int count = 0;
		double value = 0;

		for(SegMeasures sm : segMeasures) {
			
			switch (metric) {
				case ACURACY   : value += sm.getAccuracy(); break;
				case PRECISION : value += sm.getPrecison(); break;
				case RECALL    : value += sm.getRecall();   break;
				case F1        : value += sm.getF1();       break;
				case WINDIFF   : value += sm.getWd();		break;
				case PK        : value += sm.getPk();       break;
				
				case AVR_SEGS_COUNT : value += sm.getSegmentsCountHyp(); break;

				default: value = -1; break;
			}
			
			count++;
		}
		
		return value/count;
	}

	public double getPk() {
		return pk;
	}
	public double getWd() {
		return wd;
	}
	public double getAc() {
		return ac;
	}
	public double getPr() {
		return pr;
	}
	public double getRc() {
		return rc;
	}
	public double getF1() {
		return f1;
	}
	public double getSegCount() {
		return segCount;
	}
	public EvaluationSegModel getEvSegModel() {
		return evSegModel;
	}


	public double metricValueByName(Metric metric) {
		double result = -99;
		
		switch (metric) {
			case PK:             result = getPk(); break;
			case WINDIFF:        result = getWd(); break;
			case ACURACY:        result = getAc(); break;
			case PRECISION:      result = getPr(); break;
			case RECALL:         result = getRc(); break;
			case F1:             result = getF1(); break;
			case AVR_SEGS_COUNT: result = getSegCount(); break;
			
			default: break;
		
		}
		
		return result;
	}

//	public String getFormatedMetricByName(Metric metric) {
//		String result = "???";
//		
//		switch (metric) {
//			case PK:             result = getPk(); break;
//			case WINDIFF:        result = getWd(); break;
//			case ACURACY:        result = getAc(); break;
//			case PRECISION:      result = getPr(); break;
//			case RECALL:         result = getRc(); break;
//			case F1:             result = getF1(); break;
//			case AVR_SEGS_COUNT: result = getSegCount(); break;
//			
//			default: break;
//		
//		}
//		
//		return result;
//	}
	

	
}



