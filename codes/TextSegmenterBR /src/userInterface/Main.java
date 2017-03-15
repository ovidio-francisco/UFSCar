package userInterface;

import java.io.File;
import java.util.ArrayList;

import preprocessamento.Cleaner;
import preprocessamento.Stemmer;
import preprocessamento.StopWordList;
import preprocessamento.StopWords;
import ptstemmer.Stemmer.StemmerType;

public class Main {

	public static void main(String[] args) {
//		new ViewSegmentationFrame();
//		new EvaluateSegmentationsFrame();
//		new TopicsFrame();
		
//		String text = TextExtractor.docToString(new File("test.txt"));
//		System.out.println(TextUtils.removePageNumbers(text));
		
//		String s = "aaa    bbb      ccc     ddd eee  fff    ggg hhh        jjj           kkk lll   ";
//		
//		
//		System.out.println(String.format("[%s]", TextUtils.restrictChar(s, ' ', 2)));
		
		String txt = "O samba é um gênero musical, de raízes africanas, surgido no Brasil e considerado uma das principais manifestações culturais populares brasileiras.";

		
		String[] ss = txt.split(" ");
		
		
		StopWords sw = new StopWordList(new File("stopPort.txt"));
		
		
		for(String s : ss) {
			System.out.println(s + " - " +sw.isStopWord(s));
//			System.out.println(s);
		}
		System.out.println("\n\n=============\n\n");

		for(String s : ss) {
			if (!sw.isStopWord(s)) 
					System.out.println(s);
		}
		
		System.out.println("\n\n=============\n\n");
		
		
//		for(String stopw : sw.getStopWords()) {
//			System.out.println(stopw);
//		}
		
		ptstemmer.Stemmer stt = ptstemmer.Stemmer.StemmerFactory(StemmerType.ORENGO);
		
		Stemmer st = new Stemmer() {
			
			@Override
			public String stem(String word) {
				return null;
			}
		};
		
		
		
		for(String s : ss) {
			
			String sss = Cleaner.removePunctuation(s);
			
			if (!sw.isStopWord(sss)) 
					System.out.println(stt.wordStemming(sss));
		}
		
		System.out.println("\n\n=============\n\n");
		
	}
	

	
}




