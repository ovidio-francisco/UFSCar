package segmenters;

import java.io.File;

import preprocessamento.Preprocess;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;

public class Teste {

	public static void main(String[] args) {
		System.out.println("Testando os segmentadores do artigo do MIT\n");
		
		Preprocess preprocess = new Preprocess();
		preprocess.setIdentifyEOS       (true);
		preprocess.setRemoveAccents     (true);
		preprocess.setRemoveHeaders     (true);
		preprocess.setRemoveNumbers     (true);
		preprocess.setRemovePunctuation (true);
		preprocess.setRemoveShortThan   (true);
		preprocess.setRemoveStem        (true);
		preprocess.setRemoveStopWord    (true);
		preprocess.setToLowCase         (true);	
		
//		File doc = new File("Ata 32-int.txt");
		File doc = new File("Ata 32.txt");
//		File doc = new File("lorem-ipsum.md");

		Segmenter seg = new SentencesSegmenter();
		
		switch (1) {
			case 1: seg = new UISeg();        break;
			case 2: seg = new DPSeg();        break;
			case 3: seg = new MinCutSeg();    break;
			case 4: seg = new TextTilingBR(); break;
			case 5: seg = new C99BR();        break;

			default: break;
		}
		
		seg.setPreprocess(preprocess);
		
		System.out.println(String.format("===== %s =====\n", seg.getLabel()));
		System.out.println(seg.segmentsToString(doc));
//		System.out.println(seg.getAlgorithmName());
//		System.out.println(seg.showBoundaries(seg.getBoundaries()));
	}

}
