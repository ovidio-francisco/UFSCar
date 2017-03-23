package segmenter.tests;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.csv.CSVPrinter;

import segmenter.evaluations.EvaluationData;
import utils.Files;

public class Tests {
	
	public enum Metric {ACURACY, PRECISION, RECALL, F1, WINDIFF, PK};
	
	static ArrayList<File> files = new ArrayList<>();
	
	public static ArrayList<ArrayList<EvaluationData>> doTestes(File folder) {
		ArrayList<ArrayList<EvaluationData>> result = new ArrayList<>();

		files.clear();
		getFiles(folder);

		
		/*
		 * TextTiling Variando WinSize
		 */
		int initialWinSize   = 20;  
		int limitWinSize     = 60;
		int increaseWinSize  = 10;  
		
		int initialStep   = 3;  
		int limitStep     = 12;   
		int increaseStep  = 3;  
		

		TestSegmenterModel textTilingModel = new TestSegmenterModel(initialWinSize, initialStep);
		while(textTilingModel.getWinSize() <= limitWinSize ) {
			
			textTilingModel.setStep(initialStep);
			while(textTilingModel.getStep() <= limitStep) {
				
				result.add(testAll(folder, textTilingModel));

				
				textTilingModel.increaseStep(increaseStep);
			}
			
			
			textTilingModel.increaseWinSize(increaseWinSize);
		}

		
		/*
		 * C99 Variando Numero de Segmentos
		 */
		
		double initialSegsRate = 0.2;
		double increaseSegsRate =0.2;
		double limitSegsRate = 1.0;
		
		int initialRankingSize   = 9;  
		int increaseRankingSize  = 2;  
		int limitRankingSize     = 11;  
		
		boolean initialWeight = false;

		TestSegmenterModel c99Model = new TestSegmenterModel(initialSegsRate, initialRankingSize, initialWeight);
		while(c99Model.getSegmentsRate() <= limitSegsRate ) {
			
			c99Model.setRankingSize(initialRankingSize);
			while(c99Model.getRankingSize() <= limitRankingSize) {

				
				c99Model.setWeight(initialWeight);
				result.add(testAll(folder, c99Model));
					
				c99Model.setWeight(!initialWeight);
				result.add(testAll(folder, c99Model));
				
				c99Model.increaseRankingSize(increaseRankingSize);
			}
			
//			c99Model.increaseSegmentsNumber(increaseNSegs);
			c99Model.icreaseSegmentsRate(increaseSegsRate);
		}
		
		
		try {
			createCSV(result, Metric.ACURACY);
			createCSV(result, Metric.PRECISION);
			createCSV(result, Metric.RECALL);
			createCSV(result, Metric.F1);
			createCSV(result, Metric.WINDIFF);
			createCSV(result, Metric.PK);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println(String.format("\n%d modelos!", result.size()));
		
		return result;
	}
	
	static void createCSV(ArrayList<ArrayList<EvaluationData>> evaluations, Metric metric) throws IOException {

		File csvFile;
		
		File folder = new File("./analysis/");
		if(!folder.exists()) folder.mkdirs();
		
		switch (metric) {
			case ACURACY   : csvFile = new File("./analysis/acuracy.csv"   ); break;
			case PRECISION : csvFile = new File("./analysis/precision.csv" ); break;
			case RECALL    : csvFile = new File("./analysis/recall.csv"    ); break;
			case F1        : csvFile = new File("./analysis/f1.csv"        ); break;
			case WINDIFF   : csvFile = new File("./analysis/windiff.csv"   ); break;
			case PK        : csvFile = new File("./analysis/pk.csv"        ); break;

			default: csvFile = new File("erro.csv"); break;
		}
		
		
		System.out.println(String.format("Criando CSV: %s", metric));
		
		
		CSVPrinter csv = Files.getCSVPrinter(csvFile);

		System.out.print("\nDataSet/Algorithm ");
		csv.print(metric + " " + "DataSet\\Algorithm");
		
		for(ArrayList<EvaluationData> ev : evaluations) {
			String alg = ev.get(0).getAlg_Param();
			System.out.print(alg+" ");
			csv.print(alg);
		}
		csv.println();
		
		for(int c=0; c<evaluations.get(0).size(); c++) {
			
			File real = evaluations.get(0).get(c).getReal();
			String dataSet = real.getParentFile().getName()+"/"+real.getName();
			
			
			System.out.print("\n--- "+ dataSet);
			csv.print(dataSet);
			
			for(int l=0; l<evaluations.size(); l++) {
				
				Object value = -1;
				
				switch (metric) {
					case ACURACY   : value = evaluations.get(l).get(c).getAcuracy();    break;
					case PRECISION : value = evaluations.get(l).get(c).getPrecision();  break;
					case RECALL    : value = evaluations.get(l).get(c).getRecall();     break;
					case F1        : value = evaluations.get(l).get(c).getF1();         break;
					case WINDIFF   : value = evaluations.get(l).get(c).getWindowDiff(); break;
					case PK        : value = evaluations.get(l).get(c).getPk();         break;
	
					default: value = -1; break;
				}
				
				
				String svalue = String.format(Locale.US,"%f", value);
				
				
				System.out.print(svalue);
				csv.print(svalue);
			}
			
			csv.println();
		}
		
		Files.closeCSV(csv);
	
	}
	

	static ArrayList<EvaluationData> testAll(File folder, TestSegmenterModel model) {
		ArrayList<EvaluationData> result = new ArrayList<>();
		
		for(File f : files) {
			EvaluationData ev = model.test(f);
			
			result.add(ev);
			
			System.out.println(String.format("Segmentando : %s - %s", ev.getAlg_Param(), f.getName()));
			
		}
		
		for(EvaluationData ev : result) {
			if(ev.getBoundariesReal().size() != ev.getBoundariesHypo().size())
				System.out.println(String.format("Diferença na quantidade de segmentos: %2d %2d - %s", ev.getBoundariesReal().size(), ev.getBoundariesHypo().size(), ev.getReal()));
		}
		

		return result;
	}
	
	static void getFiles(File folder) {
		
		File files[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt") || pathname.isDirectory();
			}
		});

		for(File f : files) {
			if(f.isDirectory())
				getFiles(f);
			else
				Tests.files.add(f);
		}
		
	}

}
