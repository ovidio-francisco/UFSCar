package testSementers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.TextTilingBR;
import segmenters.evaluations.measure.AverageSegMeasures;
import segmenters.evaluations.measure.SegMeasures;
import tests.EvaluationSegModel;
import tests.TexArticle;
import tests.TexTable;
import utils.Files;

public class TestSegmenters {
	
	public static enum Metric {ACURACY, PRECISION, RECALL, F1, WINDIFF, PK, AVR_SEGS_COUNT};
	
	public static  File refFolder = new File("./SegReferences");
	public static  File txtFolder = new File("./txtDocs");
	private static ArrayList<File> txtFiles = new ArrayList<>();
	
	public static void main(String[] args) {

		System.out.println("% >>    Test Segmenters With The References   !! << \n\n");
		getTxtFiles(txtFolder);

		TexArticle at = new TexArticle();
		at.addTable(createTTTable());
		at.addTable(createC99Table());
		
		System.out.println(at.createTexArticle());
	}
	

	
	private static TexTable createTTTable() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		TextTilingBR tt_15_03 = new TextTilingBR();
		TextTilingBR tt_30_06 = new TextTilingBR();
		tt_30_06.setWindowSize(30);
		tt_30_06.setStep(6);
		
		EvaluationSegModel tt1 = new EvaluationSegModel(tt_15_03, getPreprocess(true));
		EvaluationSegModel tt2 = new EvaluationSegModel(tt_30_06, getPreprocess(true));

		evModels.add(tt1);
		evModels.add(tt2);

		TexTable tex = new TexTable();
//		tex.addMetric(Metric.PK);
		tex.addMetric(Metric.WINDIFF);
		tex.addMetric(Metric.ACURACY);
		tex.addMetric(Metric.PRECISION);
		tex.addMetric(Metric.RECALL);
		tex.addMetric(Metric.RECALL);

		tex.addParam("Step");
		tex.addParam("WinSize");
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			tex.addAverage(new AverageSegMeasures(sms, m));
		}

		return tex;
	}
	
	private static TexTable createC99Table() {
		
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		C99BR c991 = new C99BR();
		C99BR c992 = new C99BR();
		
		c992.setnSegsRate(0.5);
		c992.setRakingSize(15);
		
		EvaluationSegModel cm1 = new EvaluationSegModel(c991, getPreprocess(true));
		EvaluationSegModel cm2 = new EvaluationSegModel(c992, getPreprocess(true));
		
		evModels.add(cm1);
		evModels.add(cm2);
		
		TexTable tex = new TexTable();
//		tex.addMetric(Metric.PK);
		tex.addMetric(Metric.WINDIFF);
		tex.addMetric(Metric.ACURACY);
		tex.addMetric(Metric.PRECISION);
		tex.addMetric(Metric.RECALL);
		tex.addMetric(Metric.F1);
		
		for(EvaluationSegModel m : evModels) {
			ArrayList<SegMeasures> sms = m.getMeasures(txtFiles);

			tex.addAverage(new AverageSegMeasures(sms, m));
		}

		return tex;
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




