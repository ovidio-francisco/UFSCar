package segmenters.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.Segmenter.SegmenterAlgorithms;
import segmenters.evaluations.EvaluationData;

public class AnaliseC99 {
	
	public static void main(String args[]) {
		
		System.out.println("Analise do C99");
		
		
		ArrayList<ArrayList<EvaluationData>> result = new ArrayList<>();

		File folder = new File("./docs");
		Tests.files.clear();
		Tests.getFiles(folder);
		
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


		TestSegmenterModel c99Model = new TestSegmenterModel(SegmenterAlgorithms.C99);
		
		c99Model.setRankingSize(22);
		
		
		c99Model.setPreprocess(preprocess);
//		result.add(Tests.testAll(folder, c99Model));
		
		
		result.add(Tests.testAll(folder, c99Model));

		
		
		
		
		
		
		ArrayList<ArrayList<EvaluationData>> evaluations = Tests3.doTests(null, true);
				
		Tests3.createGreatTexTable();
		
		try {
			Tests3.createGreatCSV(evaluations);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
		
		
		
		System.out.println("done!");
		
	}

}
