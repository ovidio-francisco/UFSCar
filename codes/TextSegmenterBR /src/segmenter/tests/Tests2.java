package segmenter.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenter.evaluations.EvaluationData;
import segmenter.tests.Tests.Metric;

public class Tests2 {

	public static void doTests() {
		
		File folder = new File("./docs");

		Tests.files.clear();
		Tests.getFiles(folder);
		
		Preprocess myPreprocess = new Preprocess();
		Preprocess noPreprocess = new Preprocess();

		myPreprocess.setIdentifyEOS       (true);
		myPreprocess.setRemoveAccents     (true);
		myPreprocess.setRemoveHeaders     (true);
		myPreprocess.setRemoveNumbers     (true);
		myPreprocess.setRemovePunctuation (true);
		myPreprocess.setRemoveShortThan   (true);
		myPreprocess.setRemoveStem        (true);
		myPreprocess.setRemoveStopWord    (true);
		myPreprocess.setToLowCase         (true);			

		noPreprocess.setIdentifyEOS       (true);
		noPreprocess.setRemoveAccents     (false);
		noPreprocess.setRemoveHeaders     (false);
		noPreprocess.setRemoveNumbers     (false);
		noPreprocess.setRemovePunctuation (false);
		noPreprocess.setRemoveShortThan   (false);
		noPreprocess.setRemoveStem        (false);
		noPreprocess.setRemoveStopWord    (false);
		noPreprocess.setToLowCase         (false);						

		
		
// ## Acuracy
// ppC99 Acuracy           C9-60-9-t       |   0.609
// ppTextTiling Acuracy    TT-40-9         |   0.603   
// wpC99 Acuracy           C9-60-9-t       |   0.588   
// wpTextTiling Acuracy    TT-50-6         |   0.612   
		TestSegmenterModel acuracy_ppC9 = new TestSegmenterModel(0.6, 9, true);
		TestSegmenterModel acuracy_ppTT = new TestSegmenterModel(40, 9); 
		TestSegmenterModel acuracy_wpC9 = new TestSegmenterModel(0.6, 9, true);
		TestSegmenterModel acuracy_wpTT = new TestSegmenterModel(50, 6); 		
// ## F1
// ppC99 F1                C9-80-11-t      |   0.655   
// ppTextTiling F1         TT-40-3         |   0.648   
// wpC99 F1                C9-80-9-t       |   0.638   
// wpTextTiling F1         TT-30-6         |   0.605   
		TestSegmenterModel f1_ppC9 = new TestSegmenterModel(0.8, 11, true);
		TestSegmenterModel f1_ppTT = new TestSegmenterModel(40, 3);
		TestSegmenterModel f1_wpC9 = new TestSegmenterModel(0.8, 9, true);
		TestSegmenterModel f1_wpTT = new TestSegmenterModel(30, 6);
// ## Precision
// ppC99 Precision         C9-20-11-f      |   0.720   
// ppTextTiling Precision  TT-50-12        |   0.613
// wpC99 Precision         C9-40-9-t       |   0.645   
// wpTextTiling Precision  TT-40-9         |   0.611   
		TestSegmenterModel precision_ppC9 = new TestSegmenterModel(0.2, 11, false);
		TestSegmenterModel precision_ppTT = new TestSegmenterModel(50, 12);
		TestSegmenterModel precision_wpC9 = new TestSegmenterModel(0.4, 9, true);
		TestSegmenterModel precision_wpTT = new TestSegmenterModel(40, 9);
// ## Recall
// ppC99 Recall            C9-80-11-t      |   0.897   
// ppTextTiling Recall     TT-20-3         |   0.917   
// wpC99 Recall            C9-80-9-t       |   0.869
// wpTextTiling Recall     TT-20-3         |   0.886   
		TestSegmenterModel recall_ppC9 = new TestSegmenterModel(0.8, 11, true);
		TestSegmenterModel recall_ppTT = new TestSegmenterModel(20, 3);
		TestSegmenterModel recall_wpC9 = new TestSegmenterModel(0.8, 9, true);
		TestSegmenterModel recall_wpTT = new TestSegmenterModel(20, 3);
// ## Pk
// ppC99 Pk                C9-20-11-f      |   0.116   
// ppTextTiling Pk         TT-50-9         |   0.144
// wpC99 Pk                C9-20-9-t       |   0.134   
// wpTextTiling Pk         TT-50-9         |   0.142   
		TestSegmenterModel pk_ppC9 = new TestSegmenterModel(0.2, 11, false);
		TestSegmenterModel pk_ppTT = new TestSegmenterModel(50, 9);
		TestSegmenterModel pk_wpC9 = new TestSegmenterModel(0.2, 9, true);
		TestSegmenterModel pk_wpTT = new TestSegmenterModel(50, 9);
// ## Windiff
// ppC99 Windiff           C9-60-9-t       |   0.390   
// ppTextTiling Windiff    TT-40-9         |   0.396   
// wpC99 Windiff           C9-60-9-t       |   0.411   
// wpTextTiling Windiff    TT-50-6         |   0.387 
		TestSegmenterModel windiff_ppC9 = new TestSegmenterModel(0.6, 9, true);
		TestSegmenterModel windiff_ppTT = new TestSegmenterModel(40, 9);
		TestSegmenterModel windiff_wpC9 = new TestSegmenterModel(0.6, 9, true);
		TestSegmenterModel windiff_wpTT = new TestSegmenterModel(50, 6);
		
		

// With MyPreprocess
		acuracy_ppC9   .setPreprocess(myPreprocess);
		acuracy_ppTT   .setPreprocess(myPreprocess);
		f1_ppC9        .setPreprocess(myPreprocess);
		f1_ppTT        .setPreprocess(myPreprocess);
		precision_ppC9 .setPreprocess(myPreprocess);
		precision_ppTT .setPreprocess(myPreprocess);
		recall_ppC9    .setPreprocess(myPreprocess);
		recall_ppTT    .setPreprocess(myPreprocess);
		pk_ppC9        .setPreprocess(myPreprocess);
		pk_ppTT        .setPreprocess(myPreprocess);
		windiff_ppC9   .setPreprocess(myPreprocess);
		windiff_ppTT   .setPreprocess(myPreprocess);
// Without Preprocess
		acuracy_wpC9   .setPreprocess(noPreprocess);
		acuracy_wpTT   .setPreprocess(noPreprocess);
		f1_wpC9        .setPreprocess(noPreprocess);
		f1_wpTT        .setPreprocess(noPreprocess);
		precision_wpC9 .setPreprocess(noPreprocess);
		precision_wpTT .setPreprocess(noPreprocess);
		recall_wpC9    .setPreprocess(noPreprocess);
		recall_wpTT    .setPreprocess(noPreprocess);
		pk_wpC9        .setPreprocess(noPreprocess);
		pk_wpTT        .setPreprocess(noPreprocess);
		windiff_wpC9   .setPreprocess(noPreprocess);
		windiff_wpTT   .setPreprocess(noPreprocess);
		
		
		
		
		ArrayList<ArrayList<EvaluationData>> resultAcuracy 		= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultF1 			= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultPrecision	= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultRecall 		= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultPk 			= new ArrayList<>();
		ArrayList<ArrayList<EvaluationData>> resultWindiff 		= new ArrayList<>();
		
		resultAcuracy.add(Tests.testAll(folder, acuracy_ppC9));
		resultAcuracy.add(Tests.testAll(folder, acuracy_ppTT));
		resultAcuracy.add(Tests.testAll(folder, acuracy_wpC9));
		resultAcuracy.add(Tests.testAll(folder, acuracy_wpTT));
		

		resultF1.add(Tests.testAll(folder, f1_ppC9));
		resultF1.add(Tests.testAll(folder, f1_ppTT));
		resultF1.add(Tests.testAll(folder, f1_wpC9));
		resultF1.add(Tests.testAll(folder, f1_wpTT));
	
		resultPrecision.add(Tests.testAll(folder, precision_ppC9));
		resultPrecision.add(Tests.testAll(folder, precision_ppTT));
		resultPrecision.add(Tests.testAll(folder, precision_wpC9));
		resultPrecision.add(Tests.testAll(folder, precision_wpTT));
	
		resultRecall.add(Tests.testAll(folder, recall_ppC9));
		resultRecall.add(Tests.testAll(folder, recall_ppTT));
		resultRecall.add(Tests.testAll(folder, recall_wpC9));
		resultRecall.add(Tests.testAll(folder, recall_wpTT));
	
		resultPk.add(Tests.testAll(folder, pk_ppC9));
		resultPk.add(Tests.testAll(folder, pk_ppTT));
		resultPk.add(Tests.testAll(folder, pk_wpC9));
		resultPk.add(Tests.testAll(folder, pk_wpTT));
	
		resultWindiff.add(Tests.testAll(folder, windiff_ppC9));
		resultWindiff.add(Tests.testAll(folder, windiff_ppTT));
		resultWindiff.add(Tests.testAll(folder, windiff_wpC9));
		resultWindiff.add(Tests.testAll(folder, windiff_wpTT));
	
	
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
