package segmenters.evaluations.measure;

import java.util.ArrayList;

import preprocessamento.Preprocess;
import testSementers.TestSegmenters.Metric;
import tests.EvaluationSegModel;

public class SumarySegMeasures {

	public enum Statistic {MEAN, STD_DEV};
	
	private double pkMean = -99;
	private double wdMean = -99;
	private double kpMean = -99;
	private double acMean = -99;
	private double prMean = -99;
	private double rcMean = -99;
	private double f1Mean = -99;
	private double segCount = -99;

	private double pkStdDev = -99;
	private double wdStdDev = -99;
	private double kpStdDev = -99;
	private double acStdDev = -99;
	private double prStdDev = -99;
	private double rcStdDev = -99;
	private double f1StdDev = -99;
	private double segCountStdDev = -99;

	
	public static void main(String args[]) {
		System.out.println("Standar Deviation");
		
		double numbs[] = {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4};
		double n1[] = {9, 2, 5, 4, 12, 7};

		
		System.out.println(stdDeviation(numbs));
		System.out.println(stdDeviation(n1));
		System.out.println(stdDeviation(new double[] {100,300,200,200,300,300,300,100,100,200}));
	}
	
	
	private EvaluationSegModel evSegModel = null;

	public SumarySegMeasures(ArrayList<SegMeasures> sms, EvaluationSegModel evSegModel) {
		System.out.println("Creating average measures for: "+evSegModel.getSegmenter().getConfigurationLabel());
		Preprocess p = evSegModel.getSegmenter().getPreprocess();
		System.out.println(String.format("preproces = %b %b %b %b %b %b %b %b", p.isRemoveAccents(), p.isRemoveHeaders(), p.isRemoveNumbers(), p.isRemovePunctuation(), p.isRemoveShortThan(), p.isRemoveStem(), p.isRemoveStopWord(), p.isToLowCase()));

		average(sms, Metric.PK);
		average(sms, Metric.WINDIFF);
		average(sms, Metric.KAPPA);
		average(sms, Metric.ACURACY);
		average(sms, Metric.PRECISION);
		average(sms, Metric.RECALL);
		average(sms, Metric.F1);
		
		average(sms, Metric.AVR_SEGS_COUNT);

//		this.pkStdDev = standardDeviation(sms, Metric.PK);
//		this.wdStdDev = standardDeviation(sms, Metric.WINDIFF);
//		this.acStdDev = standardDeviation(sms, Metric.ACURACY);
//		this.prStdDev = standardDeviation(sms, Metric.PRECISION);
//		this.rcStdDev = standardDeviation(sms, Metric.RECALL);
//		this.f1StdDev = standardDeviation(sms, Metric.F1);
//		
		this.evSegModel = evSegModel;
	}
	
	private double average(ArrayList<SegMeasures> segMeasures, Metric metric) {
		ArrayList<Double> values = new ArrayList<>();
		
		for(SegMeasures sm : segMeasures) {
			
			double value = 0;
			switch (metric) {
				case ACURACY   : value = sm.getAccuracy(); 	break;
				case PRECISION : value = sm.getPrecison(); 	break;
				case RECALL    : value = sm.getRecall();   	break;
				case F1        : value = sm.getF1();       	break;
				case WINDIFF   : value = sm.getWd();		break;
				case PK        : value = sm.getPk();     	break;
				case KAPPA     : value = sm.getKappa();  	break;
				
				case AVR_SEGS_COUNT : value = sm.getSegmentsCountHyp(); break;
			}
			
			values.add(value);
		}
		
		double mean = 0;
		for(double v : values) {
			mean += v;
		}
		mean = mean / values.size();
		
		double stdDev = 0;
		
		
		double variance = 0;
		for(double m : values) {
			variance += Math.pow((m-mean), 2);
		}
		variance = variance / values.size();
		stdDev= Math.sqrt(variance);


		switch (metric) {
			case ACURACY   : this.acMean = mean; this.acStdDev = stdDev;break;
			case PRECISION : this.prMean = mean; this.prStdDev = stdDev;break;
			case RECALL    : this.rcMean = mean; this.rcStdDev = stdDev;break;
			case F1        : this.f1Mean = mean; this.f1StdDev = stdDev;break;
			case WINDIFF   : this.wdMean = mean; this.wdStdDev = stdDev;break;
			case PK        : this.pkMean = mean; this.pkStdDev = stdDev;break;
			case KAPPA     : this.kpMean = mean; this.kpStdDev = stdDev;break;
		
			case AVR_SEGS_COUNT : this.segCount = mean; this.segCountStdDev = stdDev; break;

			default: break;
		}

		
		return mean;
	}
	
//	private double standardDeviation(ArrayList<SegMeasures> segMeasures, Metric metric) {
//		
//		return -1;
//	}

	public double getPkMean() {
		return pkMean;
	}
	public double getWdMean() {
		return wdMean;
	}
	public double getAcMean() {
		return acMean;
	}
	public double getPrMean() {
		return prMean;
	}
	public double getRcMean() {
		return rcMean;
	}
	public double getF1Mean() {
		return f1Mean;
	}
	public double getSegCountMean() {
		return segCount;
	}
	public EvaluationSegModel getEvSegModel() {
		return evSegModel;
	}


	
	
	public double getKpMean() {
		return kpMean;
	}

	public double getKpStdDev() {
		return kpStdDev;
	}

	public double getAcStdDev() {
		return acStdDev;
	}

	public double getPkStdDev() {
		return pkStdDev;
	}

	public double getWdStdDev() {
		return wdStdDev;
	}

	public double getPrStdDev() {
		return prStdDev;
	}

	public double getRcStdDev() {
		return rcStdDev;
	}

	public double getF1StdDev() {
		return f1StdDev;
	}

	public double getSegCountStdDev() {
		return segCountStdDev;
	}

	public double metricValueByName(Statistic statistic, Metric metric) {
		double result = -99;
		
		switch (statistic) {
		case MEAN:
			switch (metric) {
				case PK:             result = getPkMean(); break;
				case WINDIFF:        result = getWdMean(); break;
				case KAPPA:          result = getKpMean(); break;
				case ACURACY:        result = getAcMean(); break;
				case PRECISION:      result = getPrMean(); break;
				case RECALL:         result = getRcMean(); break;
				case F1:             result = getF1Mean(); break;
				case AVR_SEGS_COUNT: result = getSegCountMean(); break;
			}
			break;
			case STD_DEV:
				switch (metric) {
				case PK:             result = getPkStdDev(); break;
				case WINDIFF:        result = getWdStdDev(); break;
				case KAPPA:          result = getKpStdDev(); break;
				case ACURACY:        result = getAcStdDev(); break;
				case PRECISION:      result = getPrStdDev(); break;
				case RECALL:         result = getRcStdDev(); break;
				case F1:             result = getF1StdDev(); break;
				case AVR_SEGS_COUNT: result = getSegCountStdDev(); break;
			}
			break;
		}
		
		return result;
	}


	
	private static double stdDeviation(double[] numbs) {
		double stdDev = 0;

		double mean = 0;
		
		for (double m : numbs) {
			mean += m;
		}
		
		mean = mean /numbs.length;
		
		double variance = 0;
		for(double m : numbs) {
			variance += Math.pow((m-mean), 2);
		}
		
		variance = variance / numbs.length;
		stdDev= Math.sqrt(variance);
		
		return stdDev;
	}
}



