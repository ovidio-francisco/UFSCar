package segmenters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessamento.Preprocess;
import utils.Files;
import utils.ShowStatus;
import utils.TextExtractor;
import utils.TextUtils;

public abstract class AbstractSegmenter implements Segmenter {

	
	private Preprocess preprocess = new Preprocess();
	
	
	protected String preprocessLines(String line) {
		StringBuilder sb = new StringBuilder();
		
		String[] tokens = TextUtils.getTokens(line);
		
		for(int i=0; i<tokens.length; i++) {
			sb.append(getPreprocess().transform(tokens[i]) + " "); 
		}
		
		return sb.toString();
	}

	
	
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
		
		int c = 1;
		String sep = "================";
		for(String str : segs) {
//			sb.append(str + "\n=======//=======//=======//=======\n");
			sb.append(String.format("%s[%d]%s\n%s\n", sep,c++,sep,str));
		}
		
		return sb.toString();
	}
	
	public static ArrayList<String> getRawSegmentedText(File doc, List<Integer> breaks) {
		List<String> lines = Files.loadTxtFileToList(doc);
		
		return getRawSegmentedText(lines, breaks);
	}
	public static ArrayList<String> getRawSegmentedText(List<String> lines, List<Integer> breaks) {
		ArrayList<String> result = new ArrayList<>();
		String seg = "";
		
		for(int i=0; i<lines.size(); i++) {
			
			if(breaks.contains(i)) {
//				result.add(seg+"-----------------------------------------------------------> ["+i+"]");
				result.add(seg);
				seg = lines.get(i);
			}
			else {
				seg += lines.get(i);
			}
		}
		
//		result.add(seg+"-----------------------------------------------------------> [?]");
		result.add(seg);
		
		return result;
	}
	// TODO: Usar mais essa função getRawSegmentedText
        
        
        
        
	@Override
	public int segmentToFiles(File source, File Folder) {
		ArrayList<String> segs = getSegments(source);
		
		int i=1;
		for(String str : segs) {
			File newFile = new File(Folder+"/"+source.getName()+"_seg-"+ String.format("%03d", i++) + ".txt");
			Files.saveTxtFile(str, newFile);
		}
		
		
		ShowStatus.setMessage(String.format(getLabel() + " -- Segmentando o arquivo %s em %d partes.", source, segs.size()));
		
		return segs.size();
	}

}
