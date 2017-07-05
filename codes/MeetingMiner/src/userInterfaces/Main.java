package userInterfaces;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import segmenter.Segmenter;
import segmenter.algorithms.texttile.TextTilingBR;

public class Main {

	public static void main(String[] args) {

		System.out.println("Meeting Miner");
		
		JFileChooser fc = new JFileChooser();
		
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			
			Segmenter segmenter = new TextTilingBR();
			
			ArrayList<String> segs = segmenter.getSegments(f);
			
			for(String s : segs) {
				System.out.println("\n============================\n"+s);
			}
			
		}
		
		
		
	}

}
