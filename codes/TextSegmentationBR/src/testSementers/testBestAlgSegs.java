package testSementers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.csv.CSVPrinter;

import edu.mit.nlp.segmenter.SegmenterParams;
import preprocessamento.Preprocess;
import segmenters.Segmenter;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;
import segmenters.evaluations.measure.SegMeasures;
import tests.EvaluationSegModel;
import utils.Files;

public class testBestAlgSegs {

	private static boolean doPreprocess = true;
	private static File outFolder = new File("./bestSegTests");
	private static File txtFolder = new File("./txtDocs");
	private static ArrayList<File> txtFiles = new ArrayList<>();
	
	private static File bestOut =  new File(outFolder+"/"+"best");

	public static enum Metric {AC, F1, WD, PK};

	public static void main(String[] args) {
		System.out.println("Segment Algorithms Test\n\n");
		
		getTxtFiles(txtFolder);
		sortFiles(txtFiles);
		
		if(!outFolder.exists()) {
			outFolder.mkdir();
		}

		testMetric(Metric.AC);
		testMetric(Metric.F1);
		testMetric(Metric.PK);
		testMetric(Metric.WD);
		
	}
	
	private static void testMetric(Metric metric) {
		ArrayList<Segmenter> segs = null;
		System.out.println(String.format("\nTesting -- %s", metric));
		
		switch (metric) {
			case AC: segs = ac();	break;
			case F1: segs = f1();  	break;
			case PK: segs = pk(); 	break;
			case WD: segs = wd();  	break;
		}
		
		if (segs == null) {
			System.out.println(String.format("segs == null - %s", metric));
			return;
		}
		
		ArrayList<EvaluationSegModel> evModels = getEvaluationModels(segs);
		ArrayList<ArrayList<SegMeasures>> ttSegMeasures = getAllSegMeasures(evModels);
		outCSV(ttSegMeasures, bestOut, metric);
		
	}
	
	private static ArrayList<Segmenter> ac() {
		ArrayList<Segmenter> segs = new ArrayList<>();

		TextTilingBR tt = new TextTilingBR();
		tt.setWindowSize(30);
		tt.setStep(50);
		
		C99BR c99 = new C99BR();
		c99.setnSegsRate(0.5);
		c99.setRakingSize(3);
		c99.setWeitght(true);

		MinCutSeg mc = new MinCutSeg();
		mc.setnSegsRate(0.40);
		SegmenterParams params = new SegmenterParams();
		params.setSegLenCutoff(15);
		params.setSegLenCutoffEnabled(true);
		mc.setSegmenterParams(params);

		UISeg ts = new UISeg();
		ts.setnSegsRate(0.45);

		DPSeg bs = new DPSeg();
		bs.getWrapper().prior = 0.08;
		bs.getWrapper().dispersion = 0.3;
		bs.getWrapper().use_duration = true;
		double initialnSegsRate = 0;
		double nSegsRate = initialnSegsRate;
		bs.setnSegsRate(nSegsRate);
		bs.setNumSegsKnown(nSegsRate > 0);

	 	SentencesSegmenter ps = new SentencesSegmenter();
		
		segs.add(tt);
		segs.add(c99);
		segs.add(mc);
		segs.add(ts);
		segs.add(bs);
		segs.add(ps);
		return segs;
	}
	
	private static ArrayList<Segmenter> f1() {
		ArrayList<Segmenter> segs = new ArrayList<>();

		TextTilingBR tt = new TextTilingBR();
		tt.setWindowSize(50);
		tt.setStep(20);
		
		C99BR c99 = new C99BR();
		c99.setnSegsRate(0.5);
		c99.setRakingSize(3);
		c99.setWeitght(true);

		MinCutSeg mc = new MinCutSeg();
		mc.setnSegsRate(0.60);
		SegmenterParams params = new SegmenterParams();
		params.setSegLenCutoff(13);
		params.setSegLenCutoffEnabled(true);
		mc.setSegmenterParams(params);

		UISeg ts = new UISeg();
		ts.setnSegsRate(0.60);

		DPSeg bs = new DPSeg();
		bs.getWrapper().prior = 0.07;
		bs.getWrapper().dispersion = 0.3;
		bs.getWrapper().use_duration = true;
		double initialnSegsRate = 0.6;
		double nSegsRate = initialnSegsRate;
		bs.setnSegsRate(nSegsRate);
		bs.setNumSegsKnown(nSegsRate > 0);

	 	SentencesSegmenter ps = new SentencesSegmenter();
		
		segs.add(tt);
		segs.add(c99);
		segs.add(mc);
		segs.add(ts);
		segs.add(bs);
		segs.add(ps);
		return segs;
	}
	
	private static ArrayList<Segmenter> pk() {
		ArrayList<Segmenter> segs = new ArrayList<>();

		TextTilingBR tt = new TextTilingBR();
		tt.setWindowSize(60);
		tt.setStep(50);
		
		C99BR c99 = new C99BR();
		c99.setnSegsRate(0.5);
		c99.setRakingSize(3);
		c99.setWeitght(true);

		MinCutSeg mc = new MinCutSeg();
		mc.setnSegsRate(0.40);
		SegmenterParams params = new SegmenterParams();
		params.setSegLenCutoff(15);
		params.setSegLenCutoffEnabled(true);
		mc.setSegmenterParams(params);

		UISeg ts = new UISeg();
		ts.setnSegsRate(0.50);

		DPSeg bs = new DPSeg();
		bs.getWrapper().prior = 0.08;
		bs.getWrapper().dispersion = 0.3;
		bs.getWrapper().use_duration = true;
		double initialnSegsRate = 0.0;
		double nSegsRate = initialnSegsRate;
		bs.setnSegsRate(nSegsRate);
		bs.setNumSegsKnown(nSegsRate > 0);

		SentencesSegmenter ps = new SentencesSegmenter();

		segs.add(tt);
		segs.add(c99);
		segs.add(mc);
		segs.add(ts);
		segs.add(bs);
		segs.add(ps);
		return segs;
	}

	private static ArrayList<Segmenter> wd() {
		ArrayList<Segmenter> segs = new ArrayList<>();

		TextTilingBR tt = new TextTilingBR();
		tt.setWindowSize(60);
		tt.setStep(50);
		
		C99BR c99 = new C99BR();
		c99.setnSegsRate(0.5);
		c99.setRakingSize(3);
		c99.setWeitght(true);

		MinCutSeg mc = new MinCutSeg();
		mc.setnSegsRate(0.40);
		SegmenterParams params = new SegmenterParams();
		params.setSegLenCutoff(15);
		params.setSegLenCutoffEnabled(true);
		mc.setSegmenterParams(params);

		UISeg ts = new UISeg();
		ts.setnSegsRate(0.45);

		DPSeg bs = new DPSeg();
		bs.getWrapper().prior = 0.07;
		bs.getWrapper().dispersion = 0.3;
		bs.getWrapper().use_duration = true;
		double initialnSegsRate = 0.0;
		double nSegsRate = initialnSegsRate;
		bs.setnSegsRate(nSegsRate);
		bs.setNumSegsKnown(nSegsRate > 0);

	 	SentencesSegmenter ps = new SentencesSegmenter();
		
		segs.add(tt);
		segs.add(c99);
		segs.add(mc);
		segs.add(ts);
		segs.add(bs);
		segs.add(ps);
		return segs;
	}

	
	private static ArrayList<EvaluationSegModel> getEvaluationModels(ArrayList<Segmenter> segs) {
		ArrayList<EvaluationSegModel> evModels = new ArrayList<>();

		for(Segmenter seg : segs) {
			if (seg instanceof DPSeg) {
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
				
				EvaluationSegModel ev = new EvaluationSegModel(seg, preprocess);
				evModels.add(ev);
				
			}
			else {
				
				EvaluationSegModel ev = new EvaluationSegModel(seg, getPreprocessDoAnything(doPreprocess));
				evModels.add(ev);
			}
		}
		
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
	
	private static void sortFiles(ArrayList<File> files) {
		Collections.sort(files);
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


}
