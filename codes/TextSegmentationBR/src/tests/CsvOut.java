package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;

import segmenters.Segmenter.ParamName;
import segmenters.evaluations.measure.AverageSegMeasures;
import testSementers.TestSegmenters.Metric;
import utils.Files;

public class CsvOut {
	
	
	private File csvFile = new File("./analysis/CsvOut.csv");
	
	private ArrayList<Metric> metrics = new ArrayList<>();
	private ArrayList<ParamName> params = new ArrayList<>();
	private ArrayList<AverageSegMeasures> averages = new ArrayList<>();

	
	public void saveCsvOut() throws IOException {
		CSVPrinter csv = Files.getCSVPrinter(csvFile);
		
		csv.print("Algoritmo");

		for(ParamName p : params) {
			csv.print(p);
		}
		for(Metric m : metrics) {
			csv.print(m);
		}
		csv.println();
		
		for(AverageSegMeasures a : averages) {
			csv.print(a.getEvSegModel().getModelLabel());
			for(ParamName p : params) {
				String paramValue = String.format("%s", a.getEvSegModel().getSegmenter().getParamByName(p));
				csv.print(paramValue);
			}
			for(Metric m : metrics) {
				String metricValue = String.format("%f", a.metricValueByName(m));
				csv.print(metricValue);
			}
			
			csv.println();
		}

		Files.closeCSV(csv);
	}


	public File getCsvFile() {
		return csvFile;
	}


	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}


	public ArrayList<Metric> getMetrics() {
		return metrics;
	}


	public void setMetrics(ArrayList<Metric> metrics) {
		this.metrics = metrics;
	}


	public ArrayList<ParamName> getParams() {
		return params;
	}


	public void setParams(ArrayList<ParamName> params) {
		this.params = params;
	}


	public ArrayList<AverageSegMeasures> getAverages() {
		return averages;
	}


	public void setAverages(ArrayList<AverageSegMeasures> averages) {
		this.averages = averages;
	}
	
	

}
