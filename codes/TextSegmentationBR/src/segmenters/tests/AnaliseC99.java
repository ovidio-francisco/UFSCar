package segmenters.tests;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.evaluations.EvaluationData;

public class AnaliseC99 {
	
	public static void main(String args[]) {
		
		System.out.println("Analise do C99");
		

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

		
		double    segmentsRate = 0.15;
		int       rankingSize = 11;
		boolean   weight = true;

		double segmentesRateIncrease = 0.05;
		
		TestSegmenterModel c99Model = null; 
		c99Model = new TestSegmenterModel(segmentsRate, rankingSize, weight);
		c99Model.setPreprocess(preprocess);
		
		StringBuilder sb = new StringBuilder();
		sb.append("SegRate\tWinDiff\tPk\n");
		
		while ((segmentsRate*100) < 95) {

			segmentsRate += segmentesRateIncrease;
			c99Model = new TestSegmenterModel(segmentsRate, rankingSize, weight);
			ArrayList<EvaluationData> ev = Tests.testAll(folder, c99Model);

			double mediaPk = Tests.media(ev, Tests.Metric.PK);
			double mediaWD = Tests.media(ev, Tests.Metric.WINDIFF);
//			double mediaSegs   = Tests.media(ev, Tests.Metric.AVR_SEGS_COUNT);

			sb.append(String.format("%.0f%%\t%f\t%f\n", segmentsRate*100,mediaWD, mediaPk));
			
		}
		
		

		System.out.println("Result:\n\n" + sb);
		
		

		
		System.out.println("done!");
		
	}

}
