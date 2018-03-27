package tests;

import java.util.ArrayList;

import segmenters.Segmenter.ParamName;
import segmenters.evaluations.measure.AverageSegMeasuresList;
import segmenters.evaluations.measure.SumarySegMeasures;
import segmenters.evaluations.measure.SumarySegMeasures.Statistic;
import testSementers.TestSegmenters.Metric;

public class TexTable {

	private static final String longTableFooter = "\\end{longtable} \n";

	private String modelColumn = "Algoritmo";
	
	private ArrayList<Metric> metrics = new ArrayList<>();
	private ArrayList<ParamName> params = new ArrayList<>();
	private ArrayList<SumarySegMeasures> averages = new ArrayList<>();

	public void setModelColumnl(String modelColumn) {
		this.modelColumn = modelColumn;
	}

//	public void addMetric(Metric metric) {
//		metrics.add(metric);
//	}
	
	
	
	public String createTexTable() {
		String table =  
				        longTableHeader() +
				        createLines() +
				        
				        longTableFooter;
		return table;
	}


	

	public TexTable(ArrayList<Metric> metrics, ArrayList<ParamName> params, ArrayList<SumarySegMeasures> averages) {
		this.metrics = metrics;
		this.params = params;
		this.averages = averages;
	}

	private String longTableHeader() {
		String header = "\\tiny\\begin{longtable}[c]{"; 
		
		if(!modelColumn.isEmpty())
			header += "|l|";
		
		for(int i=0; i<params.size(); i++) {
			header += "c|";
		}
		
		for(int i=0; i<metrics.size(); i++) {
			header += "c|c|";
		}
		
		header += "} \n";
		header += "\\hline \n";
		
		header += modelColumn;
		for(ParamName c : params) {
			header += String.format(" & %s", ParamNameToString(c));
		}
		for(Metric c : metrics) {
			header += String.format(" & %s", MetricToString(c));
			header += String.format(" & $\\sigma$%s", MetricToString(c));
		}
		
		header += "\\\\ \\hline \n";
		
		return header;
	}
	
	
	private String createLines() {
		StringBuilder lines = new StringBuilder();
		AverageSegMeasuresList averageList = new AverageSegMeasuresList(averages);
		
		for(SumarySegMeasures a : averages) {
			String params = "";
//			boolean someBest = false;
			params = getParamsValues(a.getEvSegModel());
			String l = "";
			
			l += a.getEvSegModel().getModelLabel() + " & ";
			l += params;
			
			for(int i=0; i< metrics.size(); i++) {
				double mean = a.metricValueByName(Statistic.MEAN, metrics.get(i));
				double stdD = a.metricValueByName(Statistic.STD_DEV, metrics.get(i));
				boolean isBest = averageList.isBest(metrics.get(i), mean);
//				someBest |= isBest;
				String txtMean = formatedMetricValue(mean, isBest) + " & ";
				String txtStdD = formatedMetricValue(stdD, isBest) + ((i==metrics.size()-1) ? "  \\\\ \\hline \n " : " & ");
				l += txtMean + txtStdD;
			}
			
//			if (someBest) 
				lines.append(l);
		}
		
		return lines.toString();
	}
	
	private String getParamsValues(EvaluationSegModel ev) {
		String result = "";

		for(ParamName p : params) {
			result += String.format("%s & ", ev.getSegmenter().getParamByName(p));
		}
		
		return result;
	}
	
	public ArrayList<ParamName> getParams() {
		return params;
	}

	public void setParams(ArrayList<ParamName> params) {
		this.params = params;
	}

	private String ParamNameToString(ParamName paramName) {
		String result = "???";
		
		switch (paramName) {
			case NSEG:		  	 result = "\\# Segs";		break;
			case NSEGRATE:	 	 result = "Seg Rate";		break;
			case RANKINGSIZE:	 result = "Raking Size";		break;
			case STEP:		  	 result = "Step";			break;
			case WEITGHT:	  	 result = "Weitght";			break;
			case WINSIZE:	  	 result = "Win Size";		break;
			case SMOOTHINGRANGE: result = "Smoothing Range"; break;
			case LENCUTOFF: 	 result = "LenCutoff"; 		break;
			case PRIOR: 		 result = "Prior"; 			break;
			case DISPERSION: 	 result = "Dispertion"; 		break;
			case NUM_SEGS_KNOWN: result = "\\#SegsKnown"; 	break;
		
			default: break;
		}
		return result;
	}
	

	private String MetricToString(Metric metric) {
		String result = "???";
		
		switch (metric) {
		case ACURACY:		 result = "Acurácia"; break;
		case AVR_SEGS_COUNT: result = "\\#Segs"; break;
		case F1:			 result = "$F^1$"; break;
		case PK:			 result = "$P_k$"; break;
		case PRECISION:		 result = "Precisão"; break;
		case RECALL:		 result = "Revocação"; break;
		case WINDIFF:		 result = "$WinDiff$"; break;
		case KAPPA:			 result = "$Kappa$"; break;
		default:			 break;
		}
		return result;
	}

	private String formatedMetricValue(double value, boolean isBest) {
		return String.format(isBest ? "\\cellcolor{gray!20} \\textbf{%.3f}" : "%.3f" , value);
	}
	
	
	public ArrayList<Metric> getMetrics() {
		return metrics;
	}
	public void setMetrics(ArrayList<Metric> metrics) {
		this.metrics = metrics;
	}

	public ArrayList<SumarySegMeasures> getAverages() {
		return averages;
	}

	public void setAverages(ArrayList<SumarySegMeasures> averages) {
		this.averages = averages;
	}
	
	

}
