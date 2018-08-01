package testSementers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.csv.CSVPrinter;

import edu.mit.nlp.segmenter.SegmenterParams;
import preprocessamento.Preprocess;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;
import segmenters.evaluations.measure.SegMeasures;
import tests.EvaluationSegModel;
import utils.Files;

public class TestAlgSegs {
	
	private static File outFolder = new File("./segTests");
	private static File txtFolder = new File("./txtDocs");
	
	private static File textTilingOut =  new File(outFolder+"/"+"textTiling");
	private static File c99Out       =  new File(outFolder+"/"+"c99");
	private static File minCutOut       =  new File(outFolder+"/"+"minCut");
	private static File textSegOut       =  new File(outFolder+"/"+"textSeg");
	private static File bayesSegOut       =  new File(outFolder+"/"+"bayesSeg");
	private static File pseudoSegOut       =  new File(outFolder+"/"+"pseudoSeg");
	
	private static ArrayList<File> txtFiles = new ArrayList<>();
	private static boolean doPreprocess = true;

	public static enum Metric {AC, F1, WD, PK};

	
	public static void main(String[] args) {
		System.out.println("Segment Algorithms Test\n\n");
		
		if(!outFolder.exists()) {
			outFolder.mkdir();
		}
		
		getTxtFiles(txtFolder);
		sortFiles(txtFiles);
	
		System.out.println(" Making the Models\n\n ");
		
		ArrayList<EvaluationSegModel> ttEvModels = testTextTiling();
		ArrayList<EvaluationSegModel> c99EvModels = testC99();
		ArrayList<EvaluationSegModel> minCutEvModels = testMinCut();
		ArrayList<EvaluationSegModel> textSegEvModels = testTextSeg();
		ArrayList<EvaluationSegModel> bayesSegEvModels = testBayesSeg();
		ArrayList<EvaluationSegModel> pseudoSegEvModels = testPseudoSeg();

		System.out.println(" \n\n Testing the Models\n\n ");

		ArrayList<ArrayList<SegMeasures>> ttSegMeasures = getAllSegMeasures(ttEvModels);
		ArrayList<ArrayList<SegMeasures>> c99SegMeasures = getAllSegMeasures(c99EvModels);
		ArrayList<ArrayList<SegMeasures>> minCutSegMeasures = getAllSegMeasures(minCutEvModels);
		ArrayList<ArrayList<SegMeasures>> textSegSegMeasures = getAllSegMeasures(textSegEvModels);
		ArrayList<ArrayList<SegMeasures>> bayesSegSegMeasures = getAllSegMeasures(bayesSegEvModels);
		ArrayList<ArrayList<SegMeasures>> pseudoSegSegMeasures = getAllSegMeasures(pseudoSegEvModels);

		System.out.println(" \n\n Writing the CSVs\n\n ");

		outCSV(ttSegMeasures, textTilingOut);
		outCSV(c99SegMeasures, c99Out);
		outCSV(minCutSegMeasures, minCutOut);
		outCSV(textSegSegMeasures, textSegOut);
		outCSV(bayesSegSegMeasures, bayesSegOut);
		outCSV(pseudoSegSegMeasures, pseudoSegOut);
		
		System.out.println(" \n\n Finished !\n\n ");

	}

	private static ArrayList<EvaluationSegModel> testTextTiling() {
		System.out.println("\n=== TextTiling ===\n");
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		
		int initial_step = 20;
		int step = initial_step;
		int final_step = 65;
		int increment_step = 10;

		int initial_winsize = 30;
		int winsize = initial_winsize;
		int final_winsize = 65;
		int increment_winsize = 10;
		
		int count = 0;
	
		while (step <= final_step) {
	
			winsize = initial_winsize;
			while (winsize <= final_winsize) {
				
				TextTilingBR tt = new TextTilingBR();
				tt.setStep(step);
				tt.setWindowSize(winsize);
				
				evModels.add(new EvaluationSegModel(tt, getPreprocessDoAnything(doPreprocess)));
				System.out.println(String.format("TextTiling step:%d winsize: %d", step, winsize));
				count++;
				
				winsize += increment_winsize;
			}
		
			step += increment_step;
		}
		System.out.println(String.format("TextTiling - %d modelos", count));
	
		
		return evModels;
		
//		ArrayList<ArrayList<SegMeasures>> allSegMeasures = getAllSegMeasures(evModels);
		
//		return allSegMeasures;
		
	}

	private static ArrayList<EvaluationSegModel> testC99() {
		System.out.println("\n=== C99 ===\n");
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
		double initialnSegsRate = 0.1;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.6;
		double increasenSegsRate = 0.2;
	
		int initialrakingSize = 3;
		int rakingSize = initialrakingSize;
		int finalrakingSize = 7;
		int increaserakingSize = 2;
		
		boolean weight = true;
	
		int count = 0;
	
		for(int i=0; i<=1; i++) {
			System.out.println(String.format("weight=%b", weight));
			weight = i==0;
			
			rakingSize = initialrakingSize;
			while(rakingSize <= finalrakingSize) {
	
				nSegsRate = initialnSegsRate;
				while(nSegsRate <= finalnSegsRate) {
					C99BR c99 = new C99BR();
					c99.setRakingSize(rakingSize);
					c99.setnSegsRate(nSegsRate);
					c99.setWeitght(weight);
					
					evModels.add(new EvaluationSegModel(c99, getPreprocessDoAnything(doPreprocess)));
					System.out.println(String.format("C99 nSegsRate:%.3f rankingSize:%d", nSegsRate, rakingSize));
					
					nSegsRate += increasenSegsRate;
					count++;
				}
	
				rakingSize += increaserakingSize;
			}
		}
		System.out.println(String.format("C99 - %d modelos", count));
	
		return evModels;
		
//		ArrayList<ArrayList<SegMeasures>> allSegMeasures = getAllSegMeasures(evModels);
//		return allSegMeasures;
	}

	private static ArrayList<EvaluationSegModel> testMinCut() {
		System.out.println("\n=== MinCutSeg ===\n");
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
		double initialnSegsRate = 0.2;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.2;
		
		int initiallenCutoff = 5;
		int lenCutoff = initiallenCutoff;
		int finallenCutoff = 15;
		int increaselenCutoff = 2;
	
		int count = 0;
		
		while(nSegsRate <= finalnSegsRate) {
			
			lenCutoff = initiallenCutoff;
			
			while (lenCutoff <= finallenCutoff) {
				MinCutSeg minCutSeg = new MinCutSeg();
				minCutSeg.setnSegsRate(nSegsRate);
				SegmenterParams params = new SegmenterParams();
				params.setSegLenCutoff(lenCutoff);
				params.setSegLenCutoffEnabled(true);
				minCutSeg.setSegmenterParams(params);
				
				evModels.add(new EvaluationSegModel(minCutSeg, getPreprocessDoAnything(doPreprocess)));
				
				System.out.println(String.format("MinCutSeg nSegsRate:%.3f lenCutoff:%d", nSegsRate, lenCutoff));
				lenCutoff += increaselenCutoff;
				count++;
			}
			
			nSegsRate += increasenSegsRate;
		}
	
		System.out.println(String.format("MinCutSeg - %d modelos", count));
		
		return evModels;
		
//		ArrayList<ArrayList<SegMeasures>> allSegMeasures = getAllSegMeasures(evModels);
//		return allSegMeasures;
	
	}

	private static ArrayList<EvaluationSegModel> testTextSeg() {
		System.out.println("\n=== TextSeg===\n");
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		
		double initialnSegsRate = 0.1;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.9;
		double increasenSegsRate = 0.05;
	
		int count = 0;
	
		while (nSegsRate < finalnSegsRate) {
			
			UISeg uiseg = new UISeg();
			uiseg.setnSegsRate(nSegsRate);
			
			evModels.add(new EvaluationSegModel(uiseg, getPreprocessDoAnything(doPreprocess)));
	
			nSegsRate += increasenSegsRate;
			
			System.out.println(String.format("TextSeg nSegsRate:%.3f", nSegsRate));

			count++;
		}
			
		System.out.println(String.format("TextSeg - %d modelos", count));
	
		return evModels;
		
//		ArrayList<ArrayList<SegMeasures>> allSegMeasures = getAllSegMeasures(evModels);
//		return allSegMeasures;
	}

	private static ArrayList<EvaluationSegModel> testBayesSeg() {
		System.out.println("\n=== BayesSeg ===\n");
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		int count = 0;

		double initialprior = 0.07;
		double prior = initialprior;
		double finalprior = 0.09;
		double increaseprior = 0.01;
		
		double initialnSegsRate = 0;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.3;

		double initialdispersion = 0.3;
		double dispersion = initialdispersion;
		double finaldispersion = 0.5;
		double increasedispersion = 0.2;

		while (nSegsRate <= finalnSegsRate) {
			
			dispersion = initialdispersion;
			while(dispersion <= finaldispersion) {
				
			 	prior = initialprior;
				while(prior <= finalprior) {
				
					DPSeg dpseg = new DPSeg();
					dpseg.getWrapper().dispersion = dispersion;
					dpseg.getWrapper().prior = prior;
					dpseg.getWrapper().use_duration = true;
					dpseg.setnSegsRate(nSegsRate);
					dpseg.setNumSegsKnown(nSegsRate > 0);
					
					Preprocess preprocess = getPreprocessDoAnything(doPreprocess);
					if (doPreprocess) {
						preprocess.setIdentifyEOS       (true);
						preprocess.setRemoveAccents     (false);
						preprocess.setRemoveHeaders     (true);
						preprocess.setRemoveNumbers     (true);
						preprocess.setRemovePunctuation (false);
						preprocess.setRemoveShortThan   (true);
						preprocess.setRemoveStem        (false);
						preprocess.setRemoveStopWord    (true);
						preprocess.setToLowCase         (true);			
					}

					
					evModels.add(new EvaluationSegModel(dpseg, preprocess));
					
					System.out.println(String.format("BayesSeg prior:%f dispersion:%f", dpseg.getWrapper().prior, dpseg.getWrapper().dispersion));
					count++;

					prior += increaseprior;
				}
				
				dispersion += increasedispersion;
			}
			
			nSegsRate += increasenSegsRate;
		}
		
		System.out.println(String.format("BayesSeg - %d modelos", count));

		return evModels;

//		ArrayList<ArrayList<SegMeasures>> allSegMeasures = getAllSegMeasures(evModels);
//		return allSegMeasures;
	}

	
	private static ArrayList<EvaluationSegModel> testPseudoSeg() {
		System.out.println("\n=== PseudoSeg ===\n");
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
		evModels.add(new EvaluationSegModel(new SentencesSegmenter(), getPreprocessDoAnything(doPreprocess)));
	
		return evModels;
	}

	private static ArrayList<ArrayList<SegMeasures>> getAllSegMeasures(ArrayList<EvaluationSegModel> evModels) {
		ArrayList<ArrayList<SegMeasures>> allSegMeasures = new ArrayList<>();

		for(EvaluationSegModel ev : evModels) {
			System.out.println(String.format("%s", ev.getModelLabel()));
			ArrayList<SegMeasures> sms = ev.getMeasures(txtFiles);
			
			allSegMeasures.add(sms);
		}

		return allSegMeasures;
	}
	
	private static void outCSV(ArrayList<ArrayList<SegMeasures>> allSegMeasures, File csvFile) {
		outCSV(allSegMeasures, csvFile, Metric.WD);
		outCSV(allSegMeasures, csvFile, Metric.PK);
		outCSV(allSegMeasures, csvFile, Metric.AC);
		outCSV(allSegMeasures, csvFile, Metric.F1);
	}

	
	private static void outCSV(ArrayList<ArrayList<SegMeasures>> allSegMeasures, File csvFile, Metric metric) {
		csvFile = new File(csvFile+"-"+metric+".csv");
		CSVPrinter csv = Files.getCSVPrinter(csvFile);

		try {
			int modelsCount = allSegMeasures.size();
			int docCount = allSegMeasures.get(0).size();
			System.out.println(String.format("%s --> %d models -- %d docs", csvFile, modelsCount, docCount));
			
			csv.print("Doc");
			for(ArrayList<SegMeasures> sms : allSegMeasures) {
				csv.print(sms.get(0).getSegmenter().getConfigurationLabel());
			}
			csv.println();
			

			for(int i=0; i<docCount; i++) {
				csv.print(allSegMeasures.get(0).get(i).getDoc().getName());
				
				for(int j=0; j<modelsCount; j++) {

					double d = 0;
					switch (metric) {
						case WD: d = allSegMeasures.get(j).get(i).getWd();       break;
						case PK: d = allSegMeasures.get(j).get(i).getPk();       break;
						case AC: d = allSegMeasures.get(j).get(i).getAccuracy(); break;
						case F1: d = allSegMeasures.get(j).get(i).getF1();       break;
					}
					
					String val = String.format("%.3f", d);
					csv.print(val);
				}
				
				csv.println();
			}
									
			Files.closeCSV(csv);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	private static void getTxtFiles(File folder) {
		File listFiles[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt") || pathname.isDirectory();
			}
		});

		for(File f : listFiles) {
			if(f.isDirectory())
				getTxtFiles(f);
			else
				txtFiles.add(f);
		}
	}
	
	private static Preprocess getPreprocessDoAnything(boolean doanything) {
		Preprocess preprocess = new Preprocess();

		if(doanything) {
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
		
		return preprocess;
	}
	
	
	private static void sortFiles(ArrayList<File> files) {
		Collections.sort(files);
	}


}


