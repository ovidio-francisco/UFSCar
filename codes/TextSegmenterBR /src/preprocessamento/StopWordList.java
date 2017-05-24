package preprocessamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ptstemmer.Stemmer.StemmerType;
import utils.Files;

public class StopWordList implements StopWords {

	List<String> list = new ArrayList<>();
	

	public StopWordList(File file) {
		list = Files.loadTxtFileToList(file);
	}
	
	@Override
	public boolean isStopWord(String word) {
		return list.contains(Cleaner.clean(word.toLowerCase()).trim());
	}

	@Override
	public List<String> getStopWords() {
		return list;
	}
	

	public static void showExample() {
		StopWords sw = new StopWordList(new File("stopPort.txt"));
		
		System.out.println(String.format("Usando lista com %d palavras\n", sw.getStopWords().size()));
		
		String txt = "O samba é um gênero musical, de raízes africanas, surgido no Brasil e considerado uma das principais manifestações culturais populares brasileiras.";
		System.out.println("Texto completo: \n" + txt);
		
		String[] ss = txt.split(" ");
		
		

		System.out.println("\n === Limpando ===\n");
		
		for(int i=0; i< ss.length; i++) {
			ss[i] = Cleaner.removePunctuation(ss[i]);
		}

		for(int i=0; i< ss.length; i++) {
			ss[i] = Cleaner.clean(ss[i]);
		}
		
		for(String s : ss) {
			System.out.println(String.format("%s", s));
		}
		

		System.out.println("\n === Apenas não stopWords ===\n");
		for(String s : ss) {
			if (!sw.isStopWord(s)) 
					System.out.println(s);
		}
		
		
		
		System.out.println("\n === Remoção de Stem ===\n");
		ptstemmer.Stemmer stt = ptstemmer.Stemmer.StemmerFactory(StemmerType.ORENGO);
		
		
		for(String s : ss) {
			
			String sss = Cleaner.removePunctuation(s);
			
			if (!sw.isStopWord(sss)) 
					System.out.println(stt.wordStemming(sss));
		}
		
	}

}
