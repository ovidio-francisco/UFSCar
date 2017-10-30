package segmenters.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import segmenters.AbstractSegmenter;
import utils.Files;

public class UISeg extends AbstractSegmenter {
	private static final String EOS = "-EOF-";

	@Override
	public ArrayList<String> getSegments(String text) {
		text = getPreprocess().cleanTextMeating(text);
		File tmp = new File("/home/ovidiojf/temp.txt");
		File out = new File("/home/ovidiojf/out1.txt");
		File uiseg = new File("/ext4Data/UFSCar/codes/baselines/uiseg/ojf.sh");
		text = getPreprocess().identifyEOS(text, EOS);
		int sentences_count = StringUtils.countMatches(text, EOS)+1; 
 		text = text.replaceAll(EOS, "\n");
 		Files.saveTxtFile(text, tmp);
 		
 		int num_segs = sentences_count/2;
 		
 		System.out.println("#sentencess = " + sentences_count);
 		System.out.println("#segs = " + num_segs);
//		ArrayList<String> segments = getRawSegmentedText(tmp, getBoundaries(tmp, num_segs));
		ArrayList<String> segments = new ArrayList<>();
 		
 		String ui_path = "/ext4Data/UFSCar/codes/baselines/uiseg/Seg";
// 		String ui_path = uiseg.getAbsolutePath();
 		String cmd = "sh -c " + ui_path + " < " + tmp.getAbsolutePath() + " > " + out.getAbsolutePath();
// 		String cmd = uiseg.getAbsolutePath();
 		try {
 			System.out.println(cmd);
 			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

//		tmp.delete();

 		System.out.println(out.exists());
 		System.out.println(uiseg.exists());
 		
		return segments;
	}

	@Override
	public String preprocessToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String paramsToString() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private List<Integer> getBoundaries(File doc, int num_segs) {
//		UIWrapper seg = new UIWrapper();
//		String config = "config/ui.config";
//
//		seg.initialize(config); //fazer o initialize para o DPSeg
//		
//		MyTextWrapper text = new MyTextWrapper(doc.getPath());
//        SegTester.preprocessText(text, false, false, false, false, 0);
//		List[] hyp_segs = seg.segmentTexts(new MyTextWrapper[]{text}, new int[]{num_segs});
////		System.out.println(hyp_segs[0]);
////		System.out.println("Sentence count = " + text.getSentenceCount());
//
//		return hyp_segs[0];
//	}

}
