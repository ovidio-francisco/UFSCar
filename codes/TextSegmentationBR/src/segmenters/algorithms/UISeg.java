package segmenters.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import segmenters.AbstractSegmenter;
import utils.Files;

public class UISeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-";

	@Override
	public ArrayList<String> getSegments(String text) {
	
		ArrayList<String> segs = getSegments2(text);	

		return segs;
	}

	public ArrayList<String> getSegments2(String text) {
		
		text = getPreprocess().cleanTextMeating(text);
		text = getPreprocess().identifyEOS(text, EOS);

 		/** Cria um arquivo com o texto preprocessado */
		String[] pt = text.split(EOS);
		
		for(int i=0; i<pt.length; i++) {
			pt[i] = preprocessLines(pt[i]);
		}
		for(int i=0; i<pt.length-1; i++) {
			pt[i] = pt[i]+"\n";
		}
 		
 		File out = new File("outUI-22.txt");
 		File tmp1 = new File("tempUI-22.txt");
 		Files.saveLinesToTxtFile(pt, tmp1);
 		
 		/** Pega os bounds do texto arquivo com o texto preprocessado*/
 		List<Integer> bounds = null;
		try {
			bounds = findBounds(tmp1, out);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
 		
 		/** Pega as linas do texto original*/
 		String[] originalLines = text.split(EOS);
 		List<String> lines = new ArrayList<String>();
		for(String s : originalLines) {
			lines.add(s);
		}

		
 		ArrayList<String> segments = getRawSegmentedText(lines, bounds);
		
//		tmp1.delete();

 		return segments;
	}
	
	
	
	
	@Override
	public String preprocessToString() {
		return "No preprocess";
	}

	@Override
	public String getAlgorithmName() {
		return "TextSeg";
	}

	@Override
	public String paramsToString() {
		return "Params ???";
	}


	@Override
	public String getLabel() {
		return "TextSeg";
	}
	@Override
	public String getConfigurationLabel() {
		return "TextSeg";
	}
	
	
	
	
	private ArrayList<Integer> findBounds(File temp, File out) throws IOException, InterruptedException {
		ArrayList<Integer> b = new ArrayList<>();
		
		
        List<String> commands = new ArrayList<String>();
        commands.add("/bin/csh");
        commands.add("Seg");

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File("/ext4Data/UFSCar/codes/baselines/uiseg/"));
        pb.redirectInput(temp);
        pb.redirectOutput(out);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        //Check result
        if (process.waitFor() != 0) {
            System.exit(1);
            System.err.println(commands);
        }

        List<String> lines = Files.loadTxtFileToList(out); 
        int l = 0;
        for(int i=1; i<lines.size(); i++) {
        	if(lines.get(i).startsWith("================[")) {
        		b.add(l);
        	}
        	else {
        		l++;
        	}
        		
        }
		return b;
	}
}
