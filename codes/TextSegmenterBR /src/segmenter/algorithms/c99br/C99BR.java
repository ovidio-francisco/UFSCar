package segmenter.algorithms.c99br;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessamento.Stemmer;
import preprocessamento.StopWords;
import ptstemmer.implementations.OrengoStemmer;
import preprocessamento.StopWordList;
import segmenter.AbstractSegmenter;
import segmenter.algorithms.c99br.uima.Stringx;
import utils.TextUtils;

public class C99BR extends AbstractSegmenter {
	
	
	private int nSegs = 10;
	private double nSegsRate = 1.0;
	private int rakingSize = 11;
	private boolean weitght = true;

	private StopWords stopWords_donothing = new StopWords() {
		@Override
		public boolean isStopWord(String stopWord) {
			return false;
		}
		@Override
		public List<String> getStopWords() {
			return null;
		}
	};
	
	private StopWords stopWords_real = new StopWordList(new File("stopPort.txt"));

	private Stemmer stemmer_doNothing = new Stemmer() {
		@Override
		public String stem(String word) {
			return word;
		}
	};
	
	private Stemmer stemmer_real = new Stemmer() {
		@Override
		public String stem(String word) {
			return new OrengoStemmer().wordStemming(word);
		}
	};
		
	
	@Override
	public ArrayList<String> getSegments(String text) {
		ArrayList<String> result = new ArrayList<>();

		text = cleanTextMeating(text);

		text = TextUtils.indentifyEndOfSentence(text, EOS_MARK);

		
		String[] sentences = text.split(EOS_MARK);

		nSegs =  Math.round((float)sentences.length * (float)nSegsRate);
		
		StopWords sw = isRemoveStopWords() ? stopWords_real : stopWords_donothing;
		Stemmer   st = isRemoveSteam()     ?   stemmer_real :   stemmer_doNothing;
		
		
		String[][] D = Stringx.tokenize(sentences, " ");	
		String[][][] S = weitght ? C99.segmentW(D, nSegs, rakingSize, sw, st) : 
			                       C99.segment (D, nSegs, rakingSize, sw, st);
		
		/* Print output */
		StringBuilder sb = new StringBuilder();
		for (int i=0, ie=S.length; i<ie; i++) {
			for (int j=0, je=S[i].length; j<je; j++) {
				String line = "";
				for (int k=0, ke=S[i][j].length; k<ke; k++) line += (S[i][j][k] + " ");
				sb.append(line);
			}
			result.add(sb.toString());
			sb.setLength(0);
		}
		
		return result;
	}

	@Override
	public String preprocessToString() {
		StringBuilder sb = new StringBuilder();
		
		ArrayList<String> words = C99.getWords();
		
		for(int i=words.size()-1; i>0; i--) 
			sb.append(words.get(i)+ " ");
		
		return sb.toString();
	}

	public int getnSegs() {
		return nSegs;
	}

//	public void setnSegs(int nSegs) {
//		this.nSegs = nSegs;
//	}

	public int getRakingSize() {
		return rakingSize;
	}

	public void setRakingSize(int rakingSize) {
		this.rakingSize = rakingSize;
	}

	public boolean isWeitght() {
		return weitght;
	}

	public void setWeitght(boolean weitght) {
		this.weitght = weitght;
	}

	@Override
	public String getAlgorithmName() {
		return "C99";
	}

	@Override
	public String paramsToString() {
		return String.format("nSegsRate=%4.2f nSegs=%d rakingSize=%d, weitght=%b removeSW=%b removeStem=%b", nSegsRate, nSegs, rakingSize, weitght,  isRemoveStopWords(), isRemoveSteam());
	}

	public double getnSegsRate() {
		return nSegsRate;
	}

	public void setnSegsRate(double nSegsRate) {
		this.nSegsRate = nSegsRate;
	}
	
	
	


}
