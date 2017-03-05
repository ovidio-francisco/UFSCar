package segmenter.algorithms.c99br.interfaces;

import java.io.File;

import segmenter.algorithms.c99br.C99BR;

public class Main {

	
	public static void main(String args[]) {
		System.out.println("C99BR");
		
//		StopWords stopWords = new StopWords() {
//			
//			@Override
//			public boolean isStopWord(String stopWord) {
//				return false;
//			}
//			
//			@Override
//			public List<String> getStopWords() {
//				return null;
//			}
//		};
//		
//		Stemmer stemmer = new Stemmer() {
//			
//			@Override
//			public String stem(String word) {
//				return word;
//			}
//		};
//		
//		int n = -1;
//		int s = 15;
//		
//		InputStream in = null;
//
//		try {
//			in = new FileInputStream(new File("ata.txt"));
//		} catch (FileNotFoundException e) {
//			
//			e.printStackTrace();
//		}
//		
////		String text = "AAA AAA AAA AAA AAA AAA AAA\n\n BBB BBB BBB BBB BBB BBB \n\nCCC CCC CCC CCC CCC CCC\n\n";
//		
//		
////		String[][] D = Stringx.tokenize(text.split("\n"), " ");
//		String[][] D = Stringx.tokenize(LineInput.load(new LineInput(in)), " ");
//
//		
//		String[][][] S = C99.segment(D, n, s, stopWords, stemmer);
//		
//		/* Print output */
//		final String sep = "==========";
//		String line;
//		LineOutput out = new LineOutput(System.out);
//		for (int i=0, ie=S.length; i<ie; i++) {
//			out.println(sep);
//			for (int j=0, je=S[i].length; j<je; j++) {
//				line = "";
//				for (int k=0, ke=S[i][j].length; k<ke; k++) line += (S[i][j][k] + " ");
//				out.println(line);
//			}
//		}
//		out.println(sep);
//		out.close();
		
		
		
		
		
//		String[] paragrafs = text.split("\n");
//		
//		
//		String[][] document = new String[paragrafs.length][];
//		
//		for(int i = 0; i<paragrafs.length; i++){
//			document[i] = paragrafs;
//		}
//		
//		String segments[][][] = C99.segmentW(document, n, s, stopWords, stemmer);
//		
//		int segmentCount = segments.length;
//		System.out.println("segments count = "+segmentCount);
//		
//		List<Integer> list = ListFactory.createNewList();
//		int sentenceIndex = 0;
//		
//		
//		for(int i=0; i<segmentCount; i++) {
//			
//			list.add(sentenceIndex);
//			System.out.println("== "+sentenceIndex);
//			sentenceIndex  += segments[i].length;
//			
//		}
//		
		
		
		
//		for(String[][] a1 : segments) {
//			System.out.println(a1.length);
//			for(String[] a2 : a1) {
//				System.out.println(a2.length);
//				for(String s1 : a2) {
////					System.out.println(s1.length());
//
//					System.out.println(s1+"\n");
//				}
//			}
//		}
		
		
		C99BR c99 = new C99BR();
		
//		ArrayList<String> segs = c99.getSegments(new File("ata.txt"));
//		
//		for(String s1 : segs) {
////			System.out.println(s1);
//		}		
		
		System.out.println(c99.segmentsToString(new File("ata.txt")));
		
//		System.out.println(segs.size());
	}
	
	
}


