package segmenter;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;

public interface Segmenter {
	public enum SegmenterAlgorithms {TEXT_TILING, C99}
	
	public static final String EOS_MARK = "_EOS_";
	
	public Preprocess getPreprocess();
	
	public ArrayList<String> getSegments(File source);
	public ArrayList<String> getSegments(String text);
	public String segmentsToString(File source);
	public String preprocessToString();
	public String getAlgorithmName();
	public String paramsToString();

	public boolean isRemoveHeader();
	public void setRemoveHeader(boolean removeHeader);
	
}
