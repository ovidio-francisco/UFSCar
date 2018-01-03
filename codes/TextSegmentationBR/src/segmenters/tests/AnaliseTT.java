package segmenters.tests;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.evaluations.EvaluationData;

public class AnaliseTT {

	public static void main(String[] args) {
		System.out.println("Analise do TextTiling");
		

		File folder = new File("./docs");
		Tests.files.clear();
		Tests.getFiles(folder);
		System.out.println(Tests.files.size()+ " files");
		
		Preprocess preprocess = new Preprocess();

		preprocess.setIdentifyEOS       (true);
		preprocess.setRemoveAccents     (true);
		preprocess.setRemoveHeaders     (true);
		preprocess.setRemoveNumbers     (true);
		preprocess.setRemovePunctuation (true);
		preprocess.setRemoveShortThan   (true);
		preprocess.setRemoveStem        (true);
		preprocess.setRemoveStopWord    (true);
		preprocess.setToLowCase         (true);			

		int       winSize = 20;
		int       step = 3;

		int       stepIncrease = 1;
		
		
		
		TestSegmenterModel tTModel = null; 
		tTModel = new TestSegmenterModel(winSize, step);
		tTModel.setPreprocess(preprocess);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Step\tWD\tPK\t#Segs\n");

		while(step < 30) {
			step += stepIncrease;
			
			tTModel = new TestSegmenterModel(winSize, step);

			ArrayList<EvaluationData> ev = Tests.testAll(folder, tTModel);

			double mediaPk     = Tests.media(ev, Tests.Metric.PK);
			double mediaWD     = Tests.media(ev, Tests.Metric.WINDIFF);
			double mediaSegs   = Tests.media(ev, Tests.Metric.AVR_SEGS_COUNT);

			sb.append(String.format("%d\t%f\t%f\t%.2f\n", step ,mediaWD, mediaPk, mediaSegs));

			
		}
		

		System.out.println("Result:\n\n" + sb);

	}

}
