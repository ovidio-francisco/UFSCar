package segmenters.algorithms.edu.mit;

import java.io.File;
import java.util.ArrayList;

import segmenters.Segmenter;
import segmenters.algorithms.DPSeg;

public class Teste {

	public static void main(String[] args) {
		System.out.println("Testando os segmentadores do artido do MIT");
		
		
		Segmenter seg = new DPSeg();
//		Segmenter seg = new MinCutSeg();
		
		ArrayList<String> segments = seg.getSegments(new File("Ata 32.txt"));
		
		System.out.println(seg.segmentsToString(new File("Ata 32.txt")));
	}

}
