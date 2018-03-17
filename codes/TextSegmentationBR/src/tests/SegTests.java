package tests;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import segmenters.Segmenter;
import segmenters.evaluations.measure.SegMeasures;
import utils.Files;

public class SegTests {
	
	public static enum Metric {ACURACY, PRECISION, RECALL, F1, WINDIFF, PK, AVR_SEGS_COUNT};

	private static ArrayList<File> files = new ArrayList<>();
	
	
	
	public static ArrayList<SegMeasures> testAllFiles(File folder, Segmenter segmenter) {
		
		SegTests.getFiles().clear();
		SegTests.getFiles(folder);
		
		System.out.println(files.size() + " Reference files\n");
//		for (File f : files) {
//			System.out.println(String.format("file --> %s", f));
//		}
		
		ArrayList<SegMeasures> result = new ArrayList<>();
		
		for(File doc : files) {

			System.out.println(String.format("Segmenting: %s -- %s", segmenter.getConfigurationLabel(), doc.getName()));
			
			File ref = new File(doc.getAbsoluteFile()+".csv");
			
			SegMeasures sm = new SegMeasures(ref, doc, segmenter);
			result.add(sm);
		}
		
		return result;
	}
	
	
	public static void getFiles(File folder) {
		
		File listFiles[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt") || pathname.isDirectory();
			}
		});

		for(File f : listFiles) {
			if(f.isDirectory())
				getFiles(f);
			else
				files.add(f);
		}
		
	}
	
	public static double media(ArrayList<SegMeasures> sms, Metric metric) {
		int count = 0;
		double value = 0;

		for(SegMeasures sm : sms) {
			
			switch (metric) {
				case ACURACY   : value += sm.getAccuracy(); break;
				case PRECISION : value += sm.getPrecison(); break;
				case RECALL    : value += sm.getRecall();   break;
				case F1        : value += sm.getF1();       break;
				case WINDIFF   : value += sm.getWd();		break;
				case PK        : value += sm.getPk();       break;
				
				case AVR_SEGS_COUNT : value += sm.getSegmentsCountHyp(); break;

				default: value = -1; break;
			}
			
			count++;
		}
		
		return value/count;
	}


	public static void main(String[] args) {

		System.out.println("Segmentation Tests\n");
		
	}


	public static ArrayList<File> getFiles() {
		return files;
	}

}
