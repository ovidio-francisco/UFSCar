package testSementers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.Segmenter.ParamName;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.TextTilingBR;
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
	private static ArrayList<File> txtFiles = new ArrayList<>();
	
	private static ArrayList<Metric> metrics = new ArrayList<>();

	private static ArrayList<CsvOut> csvOuts = new ArrayList<>();
	private static TexArticle article = new TexArticle();
	
	
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
		
		createTTTest();
		createC99Test();
		createMinCutTest();
		
		System.out.println(article.createTexArticle());
		
		for(CsvOut csv : csvOuts) {
			try {
				csv.saveCsvOut();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	
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
		
		TexTable tex = new TexTable();
		tex.setMetrics(metrics);
		tex.setParams(params);
		tex.setAverages(averages);
		
		CsvOut csvOut = new CsvOut();
		csvOut.setMetrics(metrics);
		csvOut.setParams(params);
		csvOut.setAverages(averages);
		csvOut.setCsvFile(new File(folder+"/tt.csv"));
		
		article.addTable(tex);
		csvOuts.add(csvOut);

	}
	
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
		
		TexTable tex = new TexTable();
		tex.setMetrics(metrics);
		tex.setParams(params);
		tex.setAverages(averages);

		CsvOut csvOut = new CsvOut();
		csvOut.setMetrics(metrics);
		csvOut.setParams(params);
		csvOut.setAverages(averages);
		csvOut.setCsvFile(new File(folder+"/c99.csv"));
		
		article.addTable(tex);
		csvOuts.add(csvOut);
	}
	
	
	
	private static void createMinCutTest() {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		
		MinCutSeg mc1 = new MinCutSeg();
		MinCutSeg mc2 = new MinCutSeg();
		
		EvaluationSegModel emc1 = new EvaluationSegModel(mc1, getPreprocess(true));
		EvaluationSegModel emc2 = new EvaluationSegModel(mc2, getPreprocess(true));

		evModels.add(emc1);
		evModels.add(emc2);
		
		ArrayList<AverageSegMeasures> averages = new ArrayList<>();
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			averages.add(new AverageSegMeasures(sms, m));
		}
		
		TexTable tex = new TexTable();
		tex.setMetrics(metrics);
//		tex.setParams(params);
		tex.setAverages(averages);

		CsvOut csvOut = new CsvOut();
		csvOut.setMetrics(metrics);
//		csvOut.setParams(params);
		csvOut.setAverages(averages);
		csvOut.setCsvFile(new File(folder+"/mc.csv"));
		
		article.addTable(tex);
		csvOuts.add(csvOut);

	}
	
	
	private static Preprocess getPreprocess(boolean b) {
		return new Preprocess();
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




