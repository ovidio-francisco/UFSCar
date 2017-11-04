package segmenters;

import java.io.File;

import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.UISeg;
import segmenters.algorithms.c99br.C99BR;
import segmenters.algorithms.texttile.TextTilingBR;

public class Teste {

	public static void main(String[] args) {
		System.out.println("Testando os segmentadores do artigo do MIT\n");
		
		
		File doc = new File("Ata 32.txt");
//		File doc = new File("lorem-ipsum.md");

		Segmenter seg = new SentencesSegmenter();
		
		switch (4) {
			case 1: seg = new UISeg();        break;
			case 2: seg = new DPSeg();        break;
			case 3: seg = new MinCutSeg();    break;
			case 4: seg = new TextTilingBR(); break;
			case 5: seg = new C99BR();        break;

			default: break;
		}
		
		
		System.out.println(seg.segmentsToString(doc));
//		System.out.println(seg.getAlgorithmName());
//		System.out.println(seg.showBoundaries(seg.getBoundaries()));
	}

}
