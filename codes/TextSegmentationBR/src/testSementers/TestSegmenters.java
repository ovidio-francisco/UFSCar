package testSementers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import edu.mit.nlp.segmenter.SegmenterParams;
import preprocessamento.Preprocess;
import segmenters.Segmenter.ParamName;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;
import segmenters.evaluations.measure.AverageSegMeasures;
import segmenters.evaluations.measure.SegMeasures;
import tests.CsvOut;
import tests.EvaluationSegModel;
import tests.TexArticle;
import tests.TexTable;
import utils.Files;

public class TestSegmenters {
	
	public static enum Metric {ACURACY, PRECISION, RECALL, F1, WINDIFF, PK, AVR_SEGS_COUNT};
	
	private static File folder = new File("./analysis");
	
	public static  File refFolder = new File("./SegReferences");
	public static  File txtFolder = new File("./txtDocs");
	public static  File articleFile = new File(folder + "/article.tex");
	private static ArrayList<File> txtFiles = new ArrayList<>();
	
	private static ArrayList<Metric> metrics = new ArrayList<>();

	private static ArrayList<CsvOut> csvOuts = new ArrayList<>();
	private static TexArticle article = new TexArticle();
	

	private static boolean doPreprocess = true;

	
	public static void main(String[] args) {

		System.out.println("% >>    Test Segmenters With The References    << \n\n");
		getTxtFiles(txtFolder);

		metrics.add(Metric.WINDIFF);
		metrics.add(Metric.PK);
		metrics.add(Metric.ACURACY);
		metrics.add(Metric.PRECISION);
		metrics.add(Metric.RECALL);
		metrics.add(Metric.F1);
		metrics.add(Metric.AVR_SEGS_COUNT);
		
		
//		createTT_Models();
//		createC99_Models();
//		createMinCut_Models();
//		createBayesSeg_Models();
//		createUISeg_Models();
//		createSentencesSegmenter_Models();
		
		analise_NSegRate();
		
		System.out.println("\n\n\n");
		System.out.println(article.createTexArticle());
		
		article.save(articleFile);
		
		for(CsvOut csv : csvOuts) {
			try {
				csv.saveCsvOut();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private static void analise_NSegRate() {
		
		ArrayList<EvaluationSegModel> evModelsC99 = new ArrayList<>();
		ArrayList<EvaluationSegModel> evModelsMin = new ArrayList<>();
		ArrayList<EvaluationSegModel> evModelsUIS = new ArrayList<>();
		ArrayList<EvaluationSegModel> evModelsDPS = new ArrayList<>();

		int count = 0;
		
		double initialnSegsRate = 0.1;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.99;
		double increasenSegsRate = 0.05;
		
		int rakingSize = 3;
		boolean weight = true;
		int lenCutoff = 9;

		while (nSegsRate <= finalnSegsRate) {
			
			C99BR c99 = new C99BR();
			c99.setRakingSize(rakingSize);
			c99.setnSegsRate(nSegsRate);
			c99.setWeitght(weight);
			evModelsC99.add(new EvaluationSegModel(c99, getDefaultPreprocess(true)));
			System.out.println(String.format("C99 nSegsRate:%.3f rankingSize:%d", nSegsRate, rakingSize));

			
			MinCutSeg minCutSeg = new MinCutSeg();
			minCutSeg.setnSegsRate(nSegsRate);
			SegmenterParams params = new SegmenterParams();
			params.setSegLenCutoff(lenCutoff);
			params.setSegLenCutoffEnabled(true);
			minCutSeg.setSegmenterParams(params);
			evModelsMin.add(new EvaluationSegModel(minCutSeg, getDefaultPreprocess(true)));
			System.out.println(String.format("MinCutSeg nSegsRate:%.3f lenCutoff:%d", nSegsRate, lenCutoff));


			UISeg uiseg = new UISeg();
			uiseg.setnSegsRate(nSegsRate);
			evModelsUIS.add(new EvaluationSegModel(uiseg, getDefaultPreprocess(true)));
			System.out.println(String.format("TextSeg ------"));
			
			double dispersion = 0.5;
			double prior = 0.08;
			
			DPSeg dpseg = new DPSeg();
			dpseg.getWrapper().dispersion = dispersion;
			dpseg.getWrapper().prior = prior;
			dpseg.getWrapper().use_duration = true;
			dpseg.setnSegsRate(nSegsRate);
			dpseg.setNumSegsKnown(nSegsRate > 0);

			evModelsDPS.add(new EvaluationSegModel(dpseg, getDefaultPreprocess(true)));
			
			System.out.println(String.format("BayesSeg prior:%f dispersion:%f", dpseg.getWrapper().prior, dpseg.getWrapper().dispersion));

			
			nSegsRate += increasenSegsRate;
			count++;
		}
		System.out.println(String.format("%d Modelos", count));
		
		
		ArrayList<ParamName> params = new ArrayList<>();
		
		params.add(ParamName.NSEGRATE);

		System.out.println("Influência de NSegRate em C99");
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		for(EvaluationSegModel m : evModelsC99) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);
			averages.add(new AverageSegMeasures(sms, m));
		}
		System.out.println("Influência de NSegRate em MinCut");
		for(EvaluationSegModel m : evModelsMin) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);
			averages.add(new AverageSegMeasures(sms, m));
		}
		System.out.println("Influência de NSegRate em UISeg");
		for(EvaluationSegModel m : evModelsUIS) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);
			averages.add(new AverageSegMeasures(sms, m));
		}
		System.out.println("Influência de NSegRate em BayesSeg");
		for(EvaluationSegModel m : evModelsDPS) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);
			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/analiseNSegRate.csv"), metrics, params, averages);
		articleFile = new File(folder + "/analise-NSegRate.tex");
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	
	private static void createTT_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		
		int initial_step = 20;
		int step = initial_step;
		int final_step = 65;
		int increment_step = 10;

		int initial_winsize = 30;
		int winsize = initial_winsize;
		int final_winsize = 55;
		int increment_winsize = 5;
		
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
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.STEP);
		params.add(ParamName.WINSIZE);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/tt.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	

	private static void createC99_Models() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		
		double initialnSegsRate = 0.2;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.1;

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

		ArrayList<ParamName> params = new ArrayList<>();
		
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.RANKINGSIZE);
		params.add(ParamName.WEITGHT);

		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/c99.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}

	
	private static void createMinCut_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
		double initialnSegsRate = 0.2;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.1;
		
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
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.LENCUTOFF);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/minCut.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	
	
	private static void createUISeg_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();


		
		double initialnSegsRate = 0;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.9;
		double increasenSegsRate = 0.1;

		
		int count = 0;

		while (nSegsRate < finalnSegsRate) {
			
			UISeg uiseg = new UISeg();
			uiseg.setnSegsRate(nSegsRate);
			
			evModels.add(new EvaluationSegModel(uiseg, getPreprocessDoAnything(doPreprocess)));

			nSegsRate += increasenSegsRate;
			
			count++;
		}
			
		System.out.println(String.format("TextSeg - %d modelos", count));
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NSEGRATE);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/uiseg.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
		
	}
	
	
	private static void createBayesSeg_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		int count = 0;

		double initialprior = 0.08;
		double prior = initialprior;
		double finalprior = 0.11;
		double increaseprior = 0.01;
		
		double initialnSegsRate = 0;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.9;
		double increasenSegsRate = 0.3;

		double initialdispersion = 0.1;
		double dispersion = initialdispersion;
		double finaldispersion = 0.7;
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
		
		
		System.out.println(String.format("MinCutSeg - %d modelos", count));
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NUM_SEGS_KNOWN);
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.PRIOR);
		params.add(ParamName.DISPERSION);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/bayes.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
		
	}
	
	
	private static void createSentencesSegmenter_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		

		evModels.add(new EvaluationSegModel(new SentencesSegmenter(), getPreprocessDoAnything(doPreprocess)));
		
		ArrayList<ParamName> params = new ArrayList<>();
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}

		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/sentences.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
		
	}
	
	
	private static Preprocess getDefaultPreprocess(boolean b) {
		return new Preprocess();
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
}


/*


private static void createTT_Models() {
	ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
	ArrayList<TextTilingBR> segmenters = new ArrayList<>();
	
	int initial_step = 5;
	int step = initial_step;
	int final_step = 80;
	int increment_step = 5;
	
	int count = 0;
	
	while (step <= final_step) {

		TextTilingBR tt = new TextTilingBR();
		tt.setStep(step);
		segmenters.add(tt);
		
		evModels.add(new EvaluationSegModel(tt, getPreprocess(true)));
		
		System.out.println(String.format("TextTiling step:%d", step));
		
		count++;
		step += increment_step;
	}
	
	
	int initial_winsize = 5;
	int winsize = initial_winsize;
	int final_winsize = 95;
	int increment_winsize = 5;
	
	while (winsize <= final_winsize) {
		
		TextTilingBR tt = new TextTilingBR();
		tt.setWindowSize(winsize);
		segmenters.add(tt);
		
		evModels.add(new EvaluationSegModel(tt, getPreprocess(true)));
		
		count++;
		winsize += increment_winsize;
	}

	System.out.println(String.format("%d modelos", count));
	
	ArrayList<ParamName> params = new ArrayList<>();

	params.add(ParamName.STEP);
	params.add(ParamName.WINSIZE);
	
	ArrayList<AverageSegMeasures> averages = new ArrayList<>();
	
	for(EvaluationSegModel m : evModels) {
		ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

		averages.add(new AverageSegMeasures(sms, m));
	}
	
	TexTable tex = new TexTable(metrics, params, averages);
//	tex.setMetrics(metrics);
//	tex.setParams(params);
//	tex.setAverages(averages);
	
	CsvOut csvOut = new CsvOut(new File(folder+"/tt.csv"), metrics, params, averages);
//	csvOut.setMetrics(metrics);
//	csvOut.setParams(params);
//	csvOut.setAverages(averages);
//	csvOut.setCsvFile(new File(folder+"/tt.csv"));
	
	article.addTable(tex);
	csvOuts.add(csvOut);
	
}





	@SuppressWarnings("unused")
	private static void createTTTest() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		TextTilingBR tt_15_03 = new TextTilingBR();
		TextTilingBR tt_30_06 = new TextTilingBR();
		tt_30_06.setWindowSize(30);
		tt_30_06.setStep(6);
		
		EvaluationSegModel tt1 = new EvaluationSegModel(tt_15_03, getPreprocess(true));
		EvaluationSegModel tt2 = new EvaluationSegModel(tt_30_06, getPreprocess(true));

		evModels.add(tt1);
		evModels.add(tt2);
		
		ArrayList<ParamName> params = new ArrayList<>();

		params.add(ParamName.STEP);
		params.add(ParamName.WINSIZE);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
//		TexTable tex = new TexTable();
//		tex.setMetrics(metrics);
//		tex.setParams(params);
//		tex.setAverages(averages);
//		
//		CsvOut csvOut = new CsvOut();
//		csvOut.setMetrics(metrics);
//		csvOut.setParams(params);
//		csvOut.setAverages(averages);
//		csvOut.setCsvFile(new File(folder+"/tt.csv"));
//		
//		article.addTable(tex);
//		csvOuts.add(csvOut);

	}
	

	

	private static void createC99_Models() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
		
		double initialnSegsRate = 0.2;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.1;
//		
//		
//		while(nSegsRate <= finalnSegsRate) {
//			C99BR c99 = new C99BR();
//			
//			c99.setnSegsRate(nSegsRate);
//			
//			evModels.add(new EvaluationSegModel(c99, getPreprocess(true)));
//			
//			System.out.println(String.format("C99 nSegsRate:%.3f", nSegsRate));
//
//			
//			nSegsRate += increasenSegsRate;
//			
//		}

		int initialrakingSize = 3;
		int rakingSize = initialrakingSize;
		int finalrakingSize = 13;
		int increaserakingSize = 2;
		
		while(rakingSize <= finalrakingSize) {
			C99BR c99 = new C99BR();
			
			c99.setRakingSize(rakingSize);
			c99.setnSegsRate(0.5);
			
			evModels.add(new EvaluationSegModel(c99, getPreprocess(true)));
			
			System.out.println(String.format("C99 nSegsRate:%d", rakingSize));

			rakingSize += increaserakingSize;
		}

		ArrayList<ParamName> params = new ArrayList<>();
		
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.RANKINGSIZE);
		params.add(ParamName.WEITGHT);

		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/c99.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	

	
	@SuppressWarnings("unused")
	private static void createC99Test() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		C99BR c991 = new C99BR();
		C99BR c992 = new C99BR();
		
		c992.setnSegsRate(0.5);
		c992.setRakingSize(15);
		
		EvaluationSegModel cm1 = new EvaluationSegModel(c991, getPreprocess(true));
		EvaluationSegModel cm2 = new EvaluationSegModel(c992, getPreprocess(true));
		
		evModels.add(cm1);
		evModels.add(cm2);
		
		ArrayList<ParamName> params = new ArrayList<>();
		
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.RANKINGSIZE);
		params.add(ParamName.WEITGHT);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/c99.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	

		private static void createMinCut_Models() {

		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();
	
		double initialnSegsRate = 0.2;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.8;
		double increasenSegsRate = 0.2;
		
//		double initialparzenAlpha = 0.1;
//		double parzenAlpha = initialparzenAlpha;
//		double finalparzenAlpha = 0.9;
//		double increaseparzenAlpha = 0.1;

//		while(parzenAlpha <= finalparzenAlpha) {
//
//			MinCutSeg minCutSeg = new MinCutSeg();
//			minCutSeg.setnSegsRate(0.4);
//			SegmenterParams params = new SegmenterParams();
//			params.setParzenAlpha(parzenAlpha);
//			minCutSeg.setSegmenterParams(params);
//			
//			evModels.add(new EvaluationSegModel(minCutSeg, getPreprocess(true)));
//			
//			System.out.println(String.format("MinCutSeg parzenAlpha:%.3f", parzenAlpha));
//			
//			parzenAlpha += increaseparzenAlpha;
//		}
		
		
		int count = 0;
		
//		while(nSegsRate <= finalnSegsRate) {
//			
//			MinCutSeg minCutSeg = new MinCutSeg();
//			minCutSeg.setnSegsRate(nSegsRate);
//			
//			evModels.add(new EvaluationSegModel(minCutSeg, getPreprocess(true)));
//			
//			System.out.println(String.format("MinCutSeg nSegsRate:%.3f", nSegsRate));
//			
//			nSegsRate += increasenSegsRate;
//			count++;
//		}

		
		int initialsmoothingRange = 0;
		int smoothingRange = initialsmoothingRange;
		int finalsmoothingRange = 10;
		int increasesmoothingRange = 1;
		
		
		while(smoothingRange <= finalsmoothingRange) {
			MinCutSeg minCutSeg = new MinCutSeg();
			minCutSeg.setnSegsRate(0.4);
			if(smoothingRange > 0 ) {
				SegmenterParams params = new SegmenterParams();
				params.setParzenSmoothingRange(smoothingRange);
				params.setParzenSmoothingEnabled(true);
				minCutSeg.setSegmenterParams(params);
			}
			
			evModels.add(new EvaluationSegModel(minCutSeg, getPreprocess(true)));
			
			System.out.println(String.format("MinCutSeg smoothingRange:%d", smoothingRange));
			smoothingRange += increasesmoothingRange;
		}
		
//		int initiallenCutoff = 5;
//		int lenCutoff = initiallenCutoff;
//		int finallenCutoff = 15;
//		int increaselenCutoff = 2;
//		
//		while (lenCutoff <= finallenCutoff) {
//			MinCutSeg minCutSeg = new MinCutSeg();
//			minCutSeg.setnSegsRate(0.4);
//			SegmenterParams params = new SegmenterParams();
//			params.setSegLenCutoff(lenCutoff);
//			params.setSegLenCutoffEnabled(true);
//
//			minCutSeg.setSegmenterParams(params);
//			
//			evModels.add(new EvaluationSegModel(minCutSeg, getPreprocess(true)));
//			
//			System.out.println(String.format("MinCutSeg lenCutoff:%d", lenCutoff));
//			
//			lenCutoff += increaselenCutoff;
//		}
		
		
		
		
		System.out.println(String.format("MinCutSeg - %d modelos", count));
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.SMOOTHINGRANGE);
		params.add(ParamName.LENCUTOFF);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/minCut.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
		
	}

		@SuppressWarnings("unused")
	private static void createMinCutTest() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		
		MinCutSeg mc0 = new MinCutSeg();
		MinCutSeg mc1 = new MinCutSeg();
		MinCutSeg mc2 = new MinCutSeg();
		MinCutSeg mc3 = new MinCutSeg();
		MinCutSeg mc4 = new MinCutSeg();
		MinCutSeg mc5 = new MinCutSeg();
		MinCutSeg mc6 = new MinCutSeg();

		mc0.setnSegsRate(0.75);
		
		mc2.setnSegsRate(0.75);
		SegmenterParams segparams2 = new SegmenterParams();
		segparams2.setMinSegmentLength(50);
		segparams2.setMinSegmentLengthEnforced(true);
		mc2.setSegmenterParams(segparams2);
		
		
		
		SegmenterParams segparams3 = new SegmenterParams();
		mc3.setSegmenterParams(segparams3);
		
		SegmenterParams segparams4 = new SegmenterParams();
		segparams4.setSegLenCutoff(50);
		segparams4.setSegLenCutoffEnabled(true);
		mc4.setSegmenterParams(segparams4);

		
		SegmenterParams segparams5 = new SegmenterParams();
		segparams5.setParzenAlpha(0.8);
		segparams5.setParzenSmoothingEnabled(true);
		segparams5.setParzenSmoothingRange(50);
		segparams5.setTransLMInterpolationEnabled(true);
		mc5.setSegmenterParams(segparams5);


		SegmenterParams segparams6 = new SegmenterParams();
		segparams6.setNbestListGenerated(true);
		System.out.println(""+segparams6.getNbestListSize());
		segparams6.setNbestListSize(40);
		mc6.setSegmenterParams(segparams6);

		EvaluationSegModel emc0 = new EvaluationSegModel(mc0, getPreprocess(true));
		EvaluationSegModel emc1 = new EvaluationSegModel(mc1, getPreprocess(true));
		EvaluationSegModel emc2 = new EvaluationSegModel(mc2, getPreprocess(true));
		EvaluationSegModel emc3 = new EvaluationSegModel(mc3, getPreprocess(true));
		EvaluationSegModel emc4 = new EvaluationSegModel(mc4, getPreprocess(true));
		EvaluationSegModel emc5 = new EvaluationSegModel(mc5, getPreprocess(true));
		EvaluationSegModel emc6 = new EvaluationSegModel(mc6, getPreprocess(true));
		
		evModels.add(emc0);
		evModels.add(emc1);
		evModels.add(emc2);
		evModels.add(emc3);
		evModels.add(emc4);
		evModels.add(emc5);
		evModels.add(emc6);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}

		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NSEGRATE);
//		params.add(ParamName.SMOOTHINGRANGE);

		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/mc.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}

	
	
	private static void createBayesSeg_Models() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		int count = 0;

//		double initialprior = 0.07;
//		double prior = initialprior;
//		double finalprior = 0.12;
//		double increaseprior = 0.01;
////		double increaseprior = 0.005;
//		
//		while(prior <= finalprior) {
//			
//			DPSeg dpseg = new DPSeg();
//			dpseg.getWrapper().prior = prior;
//			
//			evModels.add(new EvaluationSegModel(dpseg, getPreprocess(true)));
//			System.out.println(String.format("BayesSeg prior:%f", dpseg.getWrapper().prior));
//
//			prior += increaseprior;
//			count++;
//		}
		
		
		
		
////		double initialdispersion = 0.01;
////		double dispersion = initialdispersion;
////		double finaldispersion = 0.2;
////		double increasedispersion = 0.02;
//
//		double initialdispersion = 0.1;
//		double dispersion = initialdispersion;
//		double finaldispersion = 2;
//		double increasedispersion = 0.2;
//		
//		while(dispersion <= finaldispersion) {
//			
//			DPSeg dpseg = new DPSeg();
//			dpseg.getWrapper().dispersion = dispersion;
//			dpseg.getWrapper().prior = 0.0950;
//			dpseg.getWrapper().use_duration = true;
//			
////			dpseg.getWrapper().num_segs_known = true;
//			
//			evModels.add(new EvaluationSegModel(dpseg, getPreprocess(true)));
//			System.out.println(String.format("BayesSeg prior:%f dispersion:%f", dpseg.getWrapper().prior, dpseg.getWrapper().dispersion));
//			
//			dispersion += increasedispersion;
//			count++;
//		}
		
		
		
		double initialnSegsRate = 0;
		double nSegsRate = initialnSegsRate;
		double finalnSegsRate = 0.7;
		double increasenSegsRate = 0.1;

		while (nSegsRate <= finalnSegsRate) {
			DPSeg dpseg = new DPSeg();
			dpseg.getWrapper().dispersion = 0.5;
			dpseg.getWrapper().prior = 0.0950;
			dpseg.getWrapper().use_duration = true;
			dpseg.setnSegsRate(nSegsRate);
			
			if (nSegsRate <=0) {
				dpseg.setNumSegsKnown(false);
			}
			else {
				dpseg.setNumSegsKnown(true);
//				dpseg.getWrapper().num_segs_known = true;
			}
			
			evModels.add(new EvaluationSegModel(dpseg, getPreprocess(true)));
			
			System.out.println(String.format("BayesSeg prior:%f dispersion:%f", dpseg.getWrapper().prior, dpseg.getWrapper().dispersion));
			
			nSegsRate += increasenSegsRate;
			count++;
		}
		
		
		System.out.println(String.format("MinCutSeg - %d modelos", count));
		
		ArrayList<ParamName> params = new ArrayList<>();
		params.add(ParamName.NUM_SEGS_KNOWN);
		params.add(ParamName.NSEGRATE);
		params.add(ParamName.PRIOR);
		params.add(ParamName.DISPERSION);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable(metrics, params, averages);
		CsvOut csvOut = new CsvOut(new File(folder+"/bayes.csv"), metrics, params, averages);
		
		article.addTable(tex);
		csvOuts.add(csvOut);
		
	}
	
	
	
	
	
	
 */

