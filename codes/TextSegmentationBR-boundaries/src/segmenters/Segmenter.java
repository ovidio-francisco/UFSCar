package segmenters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessamento.Preprocess;

public interface Segmenter {
	public enum SegmenterAlgorithms {SENTENCES, TEXT_TILING, C99, MINCUT, TEXT_SEG, BAYESSEG}
	
	public static final String EOS_MARK = "_EOS_";
	
	public Preprocess getPreprocess();
	public void setPreprocess(Preprocess preprocess);
	
	public ArrayList<String> getSegments(File source);
	public ArrayList<String> getSegments(String text);
	public List<Integer> getBoundaries();
	public String segmentsToString(File source);
	public String preprocessToString();
	public String getAlgorithmName();
	public String paramsToString();
	public String getLabel();
	public String getConfigurationLabel();
	public void findBoudaries();
//	public List<Integer> getBoundaries(String text);
//	public List<Integer> getBoundaries(File doc);
	public String showBoundaries(List<Integer> bounds);
	public String[] getSentences();
	public void setOriginalSource(String text);
	public void setOriginalSource(File doc);
}


// TODO: Organizar essas chamadas a getSegments e getBoudaries
// O processo em geral, deve fornecer um array de ints (Boudaries)
// Deverá ter uma chamada que só gera esses boudaries (public void doSegments()), outra que as retorna (int[] getBoundaries()), 
// e outra que retorna os segmentos em formato de strigs (ArrayList<String> getSegments())
// A rotina getSegments() deve usar a rotina getRawSegments() na classe abstrata, 
// que pega o texto e as boundaries e devolve os segmentos




