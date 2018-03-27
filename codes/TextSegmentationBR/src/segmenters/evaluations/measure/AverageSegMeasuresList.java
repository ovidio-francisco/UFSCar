package segmenters.evaluations.measure;

import java.util.ArrayList;

import testSementers.TestSegmenters.Metric;

public class AverageSegMeasuresList {
	private double bestPk = -99;
	private double bestWd = -99;
	private double bestkp = -99;
	private double bestAc = -99;
	private double bestPr = -99;
	private double bestRc = -99;
	private double bestF1 = -99;
	
	private ArrayList<SumarySegMeasures> list = new ArrayList<>();

	public AverageSegMeasuresList(ArrayList<SumarySegMeasures> list) {
		this.list = list;
		
		this.bestPk = getBest(this.list, Metric.PK);
		this.bestWd = getBest(this.list, Metric.WINDIFF);
		this.bestkp = getBest(this.list, Metric.KAPPA);
		this.bestAc = getBest(this.list, Metric.ACURACY);
		this.bestPr = getBest(this.list, Metric.PRECISION);
		this.bestRc = getBest(this.list, Metric.RECALL);
		this.bestF1 = getBest(this.list, Metric.F1);
	}
	
	public double getBest(ArrayList<SumarySegMeasures> segMeasures, Metric metric) {
		double result = -99;
		
		ArrayList<Double> allvalues = new ArrayList<>();
		
		for(SumarySegMeasures a : segMeasures) {
			switch (metric) {
			case ACURACY:			allvalues.add(a.getAcMean()); 	break;
			case F1:				allvalues.add(a.getF1Mean());	break;
			case PK:				allvalues.add(a.getPkMean());	break;
			case PRECISION:			allvalues.add(a.getPrMean()); 	break;
			case RECALL:			allvalues.add(a.getRcMean());	break;
			case WINDIFF:			allvalues.add(a.getWdMean());	break;
			case KAPPA:				allvalues.add(a.getKpMean());	break;
			default:				break;
			}
		}
		
		if(metric == Metric.WINDIFF || metric == Metric.PK)
			result = java.util.Collections.min(allvalues);
		else
			result = java.util.Collections.max(allvalues);
		
		return result;
	}
	
	public boolean isBest(Metric metric, double value) {
		switch (metric) {
		case ACURACY:		return value == this.bestAc;  	
		case F1:			return value == this.bestF1; 	
		case PK:			return value == this.bestPk; 	
		case PRECISION:		return value == this.bestPr;	
		case RECALL:		return value == this.bestRc; 	
		case WINDIFF:		return value == this.bestWd;	
		case KAPPA:			return value == this.bestkp;	
		default:			return false; 
		}
	}
	
	
	
}
