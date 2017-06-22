package segmenter.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.csv.CSVPrinter;
import preprocessamento.Preprocess;
import segmenter.Segmenter.SegmenterAlgorithms;
import segmenter.evaluations.EvaluationData;
import utils.Files;

public class Tests3 {
	
	public static ArrayList<ArrayList<EvaluationData>> doTests(SegmenterAlgorithms algorithm, boolean useMyPreprocess) {
		
		ArrayList<ArrayList<EvaluationData>> result = new ArrayList<>();

		File folder = new File("./docs");

		Tests.files.clear();
		Tests.getFiles(folder);
		
		System.out.println(Tests.files.size());
		
		Preprocess preprocess = new Preprocess();

		if(useMyPreprocess) {
			preprocess.setIdentifyEOS       (true);
			preprocess.setRemoveAccents     (true);
			preprocess.setRemoveHeaders     (true);
			preprocess.setRemoveNumbers     (true);
			preprocess.setRemovePunctuation (true);
			preprocess.setRemoveShortThan   (true);
			preprocess.setRemoveStem        (true);
			preprocess.setRemoveStopWord    (true);
			preprocess.setToLowCase         (true);			
		} else {
			preprocess.setIdentifyEOS       (true);
			preprocess.setRemoveAccents     (false);
			preprocess.setRemoveHeaders     (false);
			preprocess.setRemoveNumbers     (false);
			preprocess.setRemovePunctuation (false);
			preprocess.setRemoveShortThan   (false);
			preprocess.setRemoveStem        (false);
			preprocess.setRemoveStopWord    (false);
			preprocess.setToLowCase         (false);						
		}
		
		
		if (algorithm == SegmenterAlgorithms.TEXT_TILING || algorithm == null) {
			
			int initialWinSize   = 20;  
			int limitWinSize     = 60;
			int increaseWinSize  = 10;  
			
			int initialStep   = 3;  
			int limitStep     = 12;   
			int increaseStep  = 3;  
			
			
			TestSegmenterModel textTilingModel = new TestSegmenterModel(initialWinSize, initialStep);
			textTilingModel.setPreprocess(preprocess);
			
			while(textTilingModel.getWinSize() <= limitWinSize ) {
				
				textTilingModel.setStep(initialStep);
				while(textTilingModel.getStep() <= limitStep) {
					
					result.add(Tests.testAll(folder, textTilingModel));
					
					textTilingModel.increaseStep(increaseStep);
				}
				
				textTilingModel.increaseWinSize(increaseWinSize);
			}
			
		}
		
		if (algorithm == SegmenterAlgorithms.C99 || algorithm == null) {
			
			double initialSegsRate = 0.2;
			double increaseSegsRate =0.1;
			double limitSegsRate = 0.8;
			
			int initialRankingSize   = 9;  
			int increaseRankingSize  = 2;  
			int limitRankingSize     = 11;  
			
			boolean initialWeight = false;
			
			TestSegmenterModel c99Model = new TestSegmenterModel(initialSegsRate, initialRankingSize, initialWeight);
			c99Model.setPreprocess(preprocess);
			while(c99Model.getSegmentsRate() <= limitSegsRate ) {
				
				c99Model.setRankingSize(initialRankingSize);
				while(c99Model.getRankingSize() <= limitRankingSize) {
					
					
					c99Model.setWeight(initialWeight);
					result.add(Tests.testAll(folder, c99Model));
					
					c99Model.setWeight(!initialWeight);
					result.add(Tests.testAll(folder, c99Model));
					
					c99Model.increaseRankingSize(increaseRankingSize);
				}
				
				c99Model.icreaseSegmentsRate(increaseSegsRate);
			}
			
		}

		
		return result;
	
	}

	public static void createGreatCSV(ArrayList<ArrayList<EvaluationData>> evaluations) throws IOException {
		
		File csvFile = new File("./analysis/medidas.csv");
		
		CSVPrinter csv = Files.getCSVPrinter(csvFile);
		csv.print("");
		csv.print("Pk");
		csv.print("WD");
		csv.print("A");
		csv.print("P");
		csv.print("R");
		csv.print("F1");
		csv.println();

		
		
		String format = "%.3f";
		
		System.out.println("Great CSV " + evaluations.size());
		
		for(ArrayList<EvaluationData> ev : evaluations) {
			String alg = ev.get(0).getConfigurationLabel();

			String pk  = String.format(format, Tests.media(ev, Tests.Metric.PK));
			String win = String.format(format, Tests.media(ev, Tests.Metric.WINDIFF));
			String acu = String.format(format, Tests.media(ev, Tests.Metric.ACURACY));
			String pre = String.format(format, Tests.media(ev, Tests.Metric.PRECISION));
			String rev = String.format(format, Tests.media(ev, Tests.Metric.RECALL));
			String f1  = String.format(format, Tests.media(ev, Tests.Metric.F1));
			
			System.out.println(alg+" "+acu);
			
			csv.print(alg);
			csv.print(pk);
			csv.print(win);
			csv.print(acu);
			csv.print(pre);
			csv.print(rev);
			csv.print(f1);
			
			csv.println();
		}
		
		Files.closeCSV(csv);
	}
	
	private static String texBold(String s) {
		return "\\textbf{"+s+"}";
	}
	
	private static String texCell(double v, String format, boolean bold) {
		String s = String.format(format, v);
		
		return bold ? texBold(s) : s;
	}
	
	private static void addTexColumn(ArrayList<String> lines, ArrayList<Double> col, String head, String format, double best) {
		
		String sep = " & ";
		
		lines.set(0, lines.get(0) + sep + texBold(head));
		
		for(int i=0; i<col.size(); i++) {
			lines.set(i+1, lines.get(i+1)+ sep + texCell(col.get(i), format, col.get(i)==best));
		}
		
	}
	
	private static void closeLines(ArrayList<String> lines) {

		String end = "\\\\ \\hline";
		
		for(int i=0; i<lines.size(); i++) {
			lines.set(i, lines.get(i) + end);
		}
		
	}
	
	public static void createGreatTexTable(ArrayList<ArrayList<EvaluationData>> evaluations) {
		File medidas = new File("./analysis/medidas.tex");
		String format = "%.3f";
		
		System.out.println("Great TEX " + evaluations.size());

		ArrayList<Double> pkList = new ArrayList<>();
		ArrayList<Double> wdList = new ArrayList<>();
		ArrayList<Double> acList = new ArrayList<>();
		ArrayList<Double> prList = new ArrayList<>();
		ArrayList<Double> rcList = new ArrayList<>();
		ArrayList<Double> f1List = new ArrayList<>();
		
		ArrayList<String> configs = new ArrayList<>();
		
		for(ArrayList<EvaluationData> ev : evaluations) {
			configs.add(ev.get(0).getConfigurationLabel());
			
			pkList.add(Tests.media(ev, Tests.Metric.PK));
			wdList.add(Tests.media(ev, Tests.Metric.WINDIFF));
			acList.add(Tests.media(ev, Tests.Metric.ACURACY));
			prList.add(Tests.media(ev, Tests.Metric.PRECISION));
			rcList.add(Tests.media(ev, Tests.Metric.RECALL));
			f1List.add(Tests.media(ev, Tests.Metric.F1));
		}
		
		double pkBest = Collections.min(pkList);
		double wdBest = Collections.min(wdList);
		double acBest = Collections.max(acList);;
		double prBest = Collections.max(prList);
		double rcBest = Collections.max(rcList);
		double f1Best = Collections.max(f1List);
		
		

		ArrayList<String> lines = new ArrayList<>();
		lines.add("Configuração");
		for(String cfg : configs) { lines.add(cfg); }
		
		addTexColumn(lines, pkList, "Pk", format, pkBest);
		addTexColumn(lines, wdList, "WD", format, wdBest);
		addTexColumn(lines, acList, "A ", format, acBest);
		addTexColumn(lines, prList, "P ", format, prBest);
		addTexColumn(lines, rcList, "R ", format, rcBest);
		addTexColumn(lines, f1List, "F1", format, f1Best);
		
		closeLines(lines);
		
		
		String table = "\\documentclass{article} \n" + 
		                "\\begin{document} \n" +
                        "\\begin{tabular}{|l|c|c|c|c|c|c|} \n"+ 
		                "\\hline \n";
		
		for(String l : lines) table += l + "\n";
		
		table += "\\end{tabular} \n" +
                 "\\end{document} \n ";
		
		System.out.println(table);
		
		Files.saveTxtFile(table, medidas);
	}
	
	public static ArrayList<String> createTexTableLines(SegmenterAlgorithms algorithm, boolean useMyPreprocess, boolean includeAlgLabel) {
		ArrayList<String> result = new ArrayList<>();
		
		return result;
	}
	
}






