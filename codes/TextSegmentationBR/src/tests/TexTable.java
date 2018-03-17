package tests;

import java.util.ArrayList;

import segmenters.algorithms.TextTilingBR;
import segmenters.evaluations.measure.AverageSegMeasures;
import testSementers.TestSegmenters.Metric;

public class TexTable {

	private ArrayList<Metric> metrics = new ArrayList<>();
	private String modelColumn = "Algoritmo";
	
	private ArrayList<String> params = new ArrayList<>();
	ArrayList<AverageSegMeasures> averages = new ArrayList<>();

	public void setModelColumnl(String modelColumn) {
		this.modelColumn = modelColumn;
	}

	public void addMetric(Metric metric) {
		metrics.add(metric);
	}
	
	public String createTexTable() {
		String table =  
				        longTableHeader() +
				        createLines() +
				        
				        longTableFooter;
		return table;
	}


	
	private String longTableHeader() {
		String header = "\\begin{longtable}[c]{"; 
		
		if(!modelColumn.isEmpty())
			header += "|l|";
		
		for(int i=0; i<params.size(); i++) {
			header += "c|";
		}
		
		for(int i=0; i<metrics.size(); i++) {
			header += "c|";
		}
		
		header += "} \n";
		header += "\\hline \n";
		
		header += modelColumn;
		for(String c : params) {
			header += String.format(" & %s", c);
		}
		for(Metric c : metrics) {
			header += String.format(" & %s", c);
		}
		
		header += "\\\\ \\hline \n";
		
		return header;
	}
	
	
	private String createLines() {
		StringBuilder lines = new StringBuilder();
		
		for(AverageSegMeasures a : averages) {
			String params = "";
			
			params = getParamsValues(a.getEvSegModel());
			
			String l = "";
			
			l += a.getEvSegModel().getModelLabel() + " & ";
			l += params;
			if (metrics.contains(Metric.PK)) l += String.format("%f & ", a.getPk());
			l += String.format("%f & ", a.getWd());
			l += String.format("%f & ", a.getAc());
			l += String.format("%f & ", a.getPr());
			l += String.format("%f & ", a.getRc());
			l += String.format("%f \\\\ \\hline \n", a.getF1());
			
			lines.append(l);
		}
		
		
		return lines.toString();
	}
	
	private String getParamsValues(EvaluationSegModel ev) {
		String result = "";
		
		if (ev.getSegmenter() instanceof TextTilingBR) {
			TextTilingBR tt = (TextTilingBR)ev.getSegmenter();
			
			if (params.contains("Step")) {
				result += String.format("%d & ", tt.getStep());
			}
			
			if (params.contains("WinSize")) {
				result += String.format("%d & ", tt.getWindowSize());
			}
		}
		
		return result;
	}
	
	public void addParam(String param) {
		params.add(param);
	}

	public ArrayList<String> getParams() {
		return params;
	}
	
	public void addAverage(AverageSegMeasures average) {
		averages.add(average);
	}
	
	private static final String longTableFooter = "\\end{longtable} \n";
	
}
