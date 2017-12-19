package segmenters;

import java.io.File;
import java.util.Vector;

import segmenters.algorithms.TextTilingBR;

public class TesteCurvaSimilaridade {

	public static void main(String[] args) {

		System.out.println("Testando a curva de similaridade no TextTiling\n\n");
		
		
		TextTilingBR tt = new TextTilingBR();
		
		File textFile = new File("legiao.txt");
//		File textFile = new File("simpleDoc.txt");
//		File textFile = new File("Ata 32.txt");
		
		
//		int c = 1;
//		for(String s : Files.loadTxtFileToList(textFile)) {
//			System.out.println(c++ + " -- " + s + "\n");
//		}
		
//		System.out.println(
				tt.segmentsToString(textFile)
//				)
				;


		float[] sim_score = tt.getSim_score();
		int[]   site_loc  = tt.getSite_loc();
		float[] depth_scr = tt.getDepth_score();
		Vector<Integer> segmentation = tt.getSegmentation();
		
//		System.out.println("score -->" + score.size());
		System.out.println("sim_score     --> " + sim_score.length);
		System.out.println("site_loc      --> " + site_loc.length);
		System.out.println("depth_score   --> " + depth_scr.length);
		System.out.println("segmentation  --> " + segmentation.size());
		System.out.println("win size      --> " + tt.getWindowSize());
		System.out.println("Bounds Count  --> " + tt.getBoundariesCount());
		
//		System.out.println("\n\nsim_score\tdepth_score");
//		for(int i=0; i < sim_score.length; i++) {
//			System.out.println(String.format("%f\t%f", sim_score[i],depth_scr[i]));
//		}

		System.out.print("\nsim_score ");
		for(float s : sim_score) {
			System.out.print(String.format("%f ", s));
		}
		System.out.print("\ndepth_score ");
		for(float s : depth_scr) {
			System.out.print(String.format("%f ", s));
		}
		System.out.print("\nsite_loc ");
		for(int s : site_loc) {
			System.out.print(String.format("%d ", s));
		}
		System.out.print("\nsegmentation ");
		for(int s : segmentation) {
			System.out.print(String.format("%d ", s));
		}
		
		System.out.println("\n\n");
		
	}

}
