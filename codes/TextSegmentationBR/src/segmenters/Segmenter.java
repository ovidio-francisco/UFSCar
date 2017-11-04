package segmenters;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;

public interface Segmenter {
	public enum SegmenterAlgorithms {SENTENCES, TEXT_TILING, C99, MINCUT, TEXT_SEG, BAYESSEG}
	
	public static final String EOS_MARK = "_EOS_";
	
	public Preprocess getPreprocess();
	public void setPreprocess(Preprocess preprocess);
	
	public ArrayList<String> getSegments(File source);
	public ArrayList<String> getSegments(String text);
	public String segmentsToString(File source);
	public String preprocessToString();
	public String getAlgorithmName();
	public String paramsToString();
	public String getLabel();
	public String getConfigurationLabel();
}
