package segmenter.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import segmenter.evaluations.EvaluationData;
import segmenter.tests.Tests.Metric;

public class Tests2 {

	public static void doTests() {
		
		File folder = new File("./docs");

		Tests.files.clear();
		Tests.getFiles(folder);

//		C99 Acuracy				C-40-11-t	C-40-9-t
		TestSegmenterModel bestC99_Acuracy = new TestSegmenterModel(0.4, 11, true);
		TestSegmenterModel viceC99_Acuracy = new TestSegmenterModel(0.4, 9, true);
				
//		C99 F1					C-60-9-t	C-80-9-t
		TestSegmenterModel bestC99_F1 = new TestSegmenterModel(0.6, 9, true);
		TestSegmenterModel viceC99_F1 = new TestSegmenterModel(0.8, 9, true);
				
//		C99 Precision			C-40-11-t	C-40-9-t
		TestSegmenterModel bestC99_Precision = new TestSegmenterModel(0.4, 11, true);
		TestSegmenterModel viceC99_Precision = new TestSegmenterModel(0.4, 9, true);
				
//		C99 Recall				C-100-9-f	C-100-9-t
		TestSegmenterModel bestC99_Recall = new TestSegmenterModel(1.0, 9, false);
		TestSegmenterModel viceC99_Recall = new TestSegmenterModel(1.0, 9, true);
				
//		C99 Pk					C-40-11-t	C-40-9-t
		TestSegmenterModel bestC99_Pk = new TestSegmenterModel(0.4, 11, true);
		TestSegmenterModel viceC99_Pk = new TestSegmenterModel(0.4, 9, true);
				
				
//		C99 Windiff				C-40-11-t	C-40-9-t
		TestSegmenterModel bestC99_Windiff = new TestSegmenterModel(0.4, 11, true);
		TestSegmenterModel viceC99_Windiff = new TestSegmenterModel(0.4, 9, true);
				
				
//		TextTiling Acuracy		TT-50-9		TT-60-12
		TestSegmenterModel bestTT_Acuracy = new TestSegmenterModel(50, 9);
		TestSegmenterModel viceTT_Acuracy = new TestSegmenterModel(60, 12);
				
				
//		TextTiling F1			TT-50-3		TT-40-3
		TestSegmenterModel bestTT_F1 = new TestSegmenterModel(50, 3);
		TestSegmenterModel viceTT_F1 = new TestSegmenterModel(40, 3);
				
				
//		TextTiling Precision	TT-60-12	TT-50-9
		TestSegmenterModel bestTT_Precision = new TestSegmenterModel(60, 12);
		TestSegmenterModel viceTT_Precision = new TestSegmenterModel(50, 9);
				
				
//		TextTiling Recall		TT-50-3		TT-20-3
		TestSegmenterModel bestTT_Recall = new TestSegmenterModel(50, 3);
		TestSegmenterModel viceTT_Recall = new TestSegmenterModel(20, 3);
				
				
//		TextTiling Pk			TT-30-9		TT-30-12
		TestSegmenterModel bestTT_Pk = new TestSegmenterModel(30, 9);
		TestSegmenterModel viceTT_Pk = new TestSegmenterModel(30, 12);
				
				
//		TextTiling Windiff		TT-50-9		TT-60-12
		TestSegmenterModel bestTT_Windiff = new TestSegmenterModel(50, 9);
		TestSegmenterModel viceTT_Windiff = new TestSegmenterModel(60, 12);
		
		
		
		
		ArrayList<ArrayList<EvaluationData>> resultAcuracy 		= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultF1 			= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultPrecision	= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultRecall 		= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultPk 			= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultWindiff 		= new ArrayList<>();
		
		resultAcuracy.add(Tests.testAll(folder, bestC99_Acuracy));
		resultAcuracy.add(Tests.testAll(folder, viceC99_Acuracy));
		resultAcuracy.add(Tests.testAll(folder, bestTT_Acuracy));
		resultAcuracy.add(Tests.testAll(folder, viceTT_Acuracy));
		

		resultF1.add(Tests.testAll(folder, bestC99_F1));
		resultF1.add(Tests.testAll(folder, viceC99_F1));
		resultF1.add(Tests.testAll(folder, bestTT_F1));
		resultF1.add(Tests.testAll(folder, viceTT_F1));
	
		resultPrecision.add(Tests.testAll(folder, bestC99_Precision));
		resultPrecision.add(Tests.testAll(folder, viceC99_Precision));
		resultPrecision.add(Tests.testAll(folder, bestTT_Precision));
		resultPrecision.add(Tests.testAll(folder, viceTT_Precision));
	
		resultRecall.add(Tests.testAll(folder, bestC99_Recall));
		resultRecall.add(Tests.testAll(folder, viceC99_Recall));
		resultRecall.add(Tests.testAll(folder, bestTT_Recall));
		resultRecall.add(Tests.testAll(folder, viceTT_Recall));
	
		resultPk.add(Tests.testAll(folder, bestC99_Pk));
		resultPk.add(Tests.testAll(folder, viceC99_Pk));
		resultPk.add(Tests.testAll(folder, bestTT_Pk));
		resultPk.add(Tests.testAll(folder, viceTT_Pk));
	
		resultWindiff.add(Tests.testAll(folder, bestC99_Windiff));
		resultWindiff.add(Tests.testAll(folder, viceC99_Windiff));
		resultWindiff.add(Tests.testAll(folder, bestTT_Windiff));
		resultWindiff.add(Tests.testAll(folder, viceTT_Windiff));
	
	
		try {
			Tests.createCSV(resultAcuracy   , Metric.ACURACY);
			Tests.createCSV(resultPrecision , Metric.PRECISION);
			Tests.createCSV(resultRecall    , Metric.RECALL);
			Tests.createCSV(resultF1        , Metric.F1);
			Tests.createCSV(resultWindiff   , Metric.WINDIFF);
			Tests.createCSV(resultPk        , Metric.PK);
		} catch (IOException e) {
			e.printStackTrace();
		}


	
	}
	
}
