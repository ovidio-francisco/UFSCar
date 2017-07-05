package segmenter;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Preprocess;
import utils.TextExtractor;

public abstract class AbstractSegmenter implements Segmenter {

	
	private Preprocess preprocess = new Preprocess();
	
	@Override
	public Preprocess getPreprocess() {
		return preprocess;
	}
	
	@Override
	public void setPreprocess(Preprocess preprocess) {
		this.preprocess = preprocess;
	}


	@Override
	public final ArrayList<String> getSegments(File source) {

		String text = TextExtractor.docToString(source);
		
		return getSegments(text);
	}


	@Override
	public final String segmentsToString(File source) {
		ArrayList<String> segs = getSegments(source);
		StringBuilder sb = new StringBuilder();
		
		for(String str : segs) {
			sb.append(str + "\n=======//=======//=======//=======\n\n");
		}
		
		return sb.toString();
	}

}
