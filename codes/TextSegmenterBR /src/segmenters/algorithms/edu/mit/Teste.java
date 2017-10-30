package segmenters.algorithms.edu.mit;

import java.io.File;

import segmenters.Segmenter;
import segmenters.algorithms.UISeg;

public class Teste {

	public static void main(String[] args) {
		System.out.println("Testando os segmentadores do artido do MIT");
		
		
		Segmenter seg = new UISeg();
//		Segmenter seg = new DPSeg();
//		Segmenter seg = new MinCutSeg();
		
		File doc = new File("lorem-ipsum.md");
//		File doc = new File("Ata 32.txt");
		
		
//		ArrayList<String> segments = seg.getSegments(new File("lorem-ipsum.md"));
		
		System.out.println(seg.segmentsToString(doc));
	}

}
