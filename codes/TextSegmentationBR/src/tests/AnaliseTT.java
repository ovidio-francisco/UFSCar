package tests;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.algorithms.TextTilingBR;
import segmenters.evaluations.measure.MeasureUtils;
import segmenters.evaluations.measure.SegMeasures;

public class AnaliseTT {

	private static Preprocess preprocess = new Preprocess();
	private static File folder = new File("./SegReferences");

	public static void main(String[] args) {

		System.out.println("Analise do TextTiling");

		preprocess = MeasureUtils.getMyDefaultPreprocess();
		
		varingStep();
		
		
	}
	
	private static void varingStep() {
		int       winSize = 20;
		int       step = 3;

		int       stepIncrease = 10;
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("Step\tWD\tPK\t#Segs\n");

		while(step < 30) {
			step += stepIncrease;
			
			TextTilingBR tt = new TextTilingBR();
			tt.setWindowSize(winSize);
			tt.setPreprocess(preprocess);
			tt.setStep(step);
			ArrayList<SegMeasures> sms = SegTests.testAllFiles(folder, tt);
			
			double mediaPk     = SegTests.media(sms, SegTests.Metric.PK);
			double mediaWD     = SegTests.media(sms, SegTests.Metric.WINDIFF);
			double mediaSegs   = SegTests.media(sms, SegTests.Metric.AVR_SEGS_COUNT);

			sb.append(String.format("%d\t%f\t%f\t%.2f\n", step ,mediaWD, mediaPk, mediaSegs));
		}
		

		System.out.println("\nResult:\n" + sb);
		
	}

}
