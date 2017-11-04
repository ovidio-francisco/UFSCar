package segmenters.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import segmenters.AbstractSegmenter;
import utils.Files;

public class UISeg extends AbstractSegmenter {

	@Override
	public ArrayList<String> getSegments(String text) {
		
//		text = getPreprocess().cleanTextMeating(text);
//		text = getPreprocess().identifyEOS(text, EOS);
// 		text = text.replaceAll(EOS, "\n");
 		File tmp = new File("temp.txt");
// 		File out = new File("out.txt");
// 		Files.saveTxtFile(text, tmp);

 		findSentences(text); 
 		String[] sents = getSentences();
		Files.saveLinesToTxtFile(sents, tmp);
		
//		ArrayList<String> result = new ArrayList<>();
//		try {
//			result = getSegs(tmp, out);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
		
 		setOriginalSource(tmp);
		ArrayList<String> segments = getSegmentedText();

		tmp.delete();
		
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

//
//	private ArrayList<String> getSegs(File temp, File out) throws IOException, InterruptedException {
//		
//        List<String> commands = new ArrayList<String>();
//        commands.add("/bin/csh");
//        commands.add("Seg");
//
//        ProcessBuilder pb = new ProcessBuilder(commands);
//        pb.directory(new File("/ext4Data/UFSCar/codes/baselines/uiseg/"));
//        pb.redirectInput(temp);
//        pb.redirectOutput(out);
//        pb.redirectErrorStream(true);
//        Process process = pb.start();
//        
//        //Check result
//        if (process.waitFor() != 0) {
//            System.exit(1);
//            System.err.println(commands);
//        }
//
//        List<String> text = Files.loadTxtFileToList(out); 
//        StringBuilder sb = new StringBuilder();
//        ArrayList<String> segs = new ArrayList<>();
//
//        for(String line : text) {
//        	if(line.startsWith("================[")) {
//        		if (!sb.toString().trim().isEmpty()) {
//        			segs.add(sb.toString().trim());
//        			sb.setLength(0);
//        		}
//        	}
//        	else {
//        		sb.append(line);
//        	}
//
//        }
//        segs.add(sb.toString().trim());
//
//		return segs;
//	}
	
	
	@Override
	public String getLabel() {
		return "TextSeg";
	}
	@Override
	public String getConfigurationLabel() {
		return "TextSeg";
	}
	
	
	
//	public List<Integer> findBoundaries(String text) throws IOException, InterruptedException {
//		ArrayList<Integer> result = new ArrayList<>();
//		
//		text = getPreprocess().cleanTextMeating(text);
//		text = getPreprocess().identifyEOS(text, EOS);
// 		text = text.replaceAll(EOS, "\n");
// 		File tmp = new File("temp.txt");
// 		File out = new File("out.txt");
// 		Files.saveTxtFile(text, tmp);
//
//
//        List<String> commands = new ArrayList<String>();
//        commands.add("/bin/csh");
//        commands.add("Seg");
//
//        ProcessBuilder pb = new ProcessBuilder(commands);
//        pb.directory(new File("/ext4Data/UFSCar/codes/baselines/uiseg/"));
//        pb.redirectInput(tmp);
//        pb.redirectOutput(out);
//        pb.redirectErrorStream(true);
//        Process process = pb.start();
//        
//        //Check result
//        if (process.waitFor() != 0) {
//            System.exit(1);
//            System.err.println(commands);
//        }
//
//        List<String> lines = Files.loadTxtFileToList(out); 
//        int i = 0;
//        
//        for(String line : lines) {
//        	if(line.startsWith("================[")) {
//        		result.add(i);
//        	}
//        	i++;
//        }
//
//		return result;
//	}
	
//	@Override
//	public List<Integer> getBoundaries(String text) {
//		try {
//			return findBoundaries(text);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
	private ArrayList<Integer> findBounds() throws IOException, InterruptedException {
		ArrayList<Integer> b = new ArrayList<>();
		
//		text = getPreprocess().cleanTextMeating(text);
//		text = getPreprocess().identifyEOS(text, EOS);
// 		text = text.replaceAll(EOS, "\n");
 		File tmp = new File("temp.txt");
 		File out = new File("out.txt");
// 		Files.saveTxtFile(text, tmp);
 		
 		Files.saveLinesToTxtFile(getSentences(), tmp);


        List<String> commands = new ArrayList<String>();
        commands.add("/bin/csh");
        commands.add("Seg");

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File("/ext4Data/UFSCar/codes/baselines/uiseg/"));
        pb.redirectInput(tmp);
        pb.redirectOutput(out);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        //Check result
        if (process.waitFor() != 0) {
            System.exit(1);
            System.err.println(commands);
        }

        List<String> lines = Files.loadTxtFileToList(out); 
        int i = 0;
        
        for(String line : lines) {
        	if(line.startsWith("================[")) {
        		b.add(i);
        	}
        	i++;
        }

		return b;
		
	}
	
	@Override
	public void findBoudaries() {
        try {
			setBoundaries(findBounds());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	
	
}
