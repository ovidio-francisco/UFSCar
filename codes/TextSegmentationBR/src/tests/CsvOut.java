package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;

import segmenters.Segmenter.ParamName;
import segmenters.evaluations.measure.SumarySegMeasures;
import segmenters.evaluations.measure.SumarySegMeasures.Statistic;
import testSementers.TestSegmenters.Metric;
import utils.Files;

public class CsvOut {
	
	
	private File csvFile = new File("./analysis/CsvOut.csv");
	
	private ArrayList<Metric> metrics = new ArrayList<>();
	private ArrayList<ParamName> params = new ArrayList<>();
	private ArrayList<SumarySegMeasures> averages = new ArrayList<>();

	
	public void saveCsvOut() throws IOException {
		CSVPrinter csv = Files.getCSVPrinter(csvFile);
		
		csv.print("Algoritmo");

		for(ParamName p : params) {
			csv.print(p);
		}
		for(Metric m : metrics) {
			csv.print(m);
			csv.print(m+"'");
		}
		csv.println();
		
		for(SumarySegMeasures a : averages) {
			csv.print(a.getEvSegModel().getModelLabel());
			for(ParamName p : params) {
				String paramValue = String.format("%s", a.getEvSegModel().getSegmenter().getParamByName(p));
				csv.print(paramValue);
			}
			for(Metric m : metrics) {
				String metricMean = String.format("%f", a.metricValueByName(Statistic.MEAN    ,m));
				String metricStdD = String.format("%f", a.metricValueByName(Statistic.STD_DEV ,m));
				csv.print(metricMean);
				csv.print(metricStdD);
			}
			
			csv.println();
		}

		Files.closeCSV(csv);
	}

	

	public CsvOut(File csvFile, ArrayList<Metric> metrics, ArrayList<ParamName> params,
			ArrayList<SumarySegMeasures> averages) {
		this.csvFile = csvFile;
		this.metrics = metrics;
		this.params = params;
		this.averages = averages;
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


	public ArrayList<SumarySegMeasures> getAverages() {
		return averages;
	}


	public void setAverages(ArrayList<SumarySegMeasures> averages) {
		this.averages = averages;
	}
	
	

}
