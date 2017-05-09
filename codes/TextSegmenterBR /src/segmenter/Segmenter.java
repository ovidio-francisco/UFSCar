package segmenter;

import java.io.File;
import java.util.ArrayList;

public interface Segmenter {
	public enum SegmenterAlgorithms {TEXT_TILING, C99}
	
	public static final String EOS_MARK = "_EOS_";
	
	
	
	public ArrayList<String> getSegments(File source);
	public ArrayList<String> getSegments(String text);
	public String segmentsToString(File source);
	public String preprocessToString();
	public String getAlgorithmName();
	public String paramsToString();
	
	public boolean isRemoveStopWords();
	public void setRemoveStopWords(boolean removeStopWords);
	public boolean isRemoveSteam();
	public void setRemoveStem(boolean removeSteam);

	
	

}
