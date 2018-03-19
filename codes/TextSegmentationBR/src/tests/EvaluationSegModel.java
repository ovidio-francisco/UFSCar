package tests;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import segmenters.Segmenter;
import segmenters.evaluations.measure.SegMeasures;

public class EvaluationSegModel {

//	private SegmenterAlgorithms alg = null;
	private Segmenter segmenter = null;
	public static File refFolder = new File("./SegReferences");

	
	
	public EvaluationSegModel(Segmenter segmenter, Preprocess preprocess) {
		this.segmenter = segmenter;
		this.segmenter.setPreprocess(preprocess);
	}

	public SegMeasures getMeasures(File txt) {
		File ref = new File(refFolder+"/"+txt.getName()+".csv");
		SegMeasures measure = new SegMeasures(ref, txt, segmenter); 
		
		return measure;
	}

	public ArrayList<SegMeasures> getMeasures(ArrayList<File> txts) {
		ArrayList<SegMeasures> result = new ArrayList<>();
		
		for(File f : txts) {
			result.add(getMeasures(f));
		}
		
		return result;
	}
	
	public String getModelLabel() {
//		return segmenter.getLabel();
		return segmenter.getConfigurationLabel();
	}

	@Override
	public String toString() {
		if (segmenter == null) 
			return "No segmenter assigned!";
		else
			return segmenter.getLabel() + "\n" + segmenter.getConfigurationLabel();
	}
	


	public Segmenter getSegmenter() {
		return segmenter;
	}
}

