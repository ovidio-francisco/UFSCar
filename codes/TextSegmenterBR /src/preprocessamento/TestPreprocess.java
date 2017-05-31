package preprocessamento;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

import utils.Files;
import utils.TextUtils;

public class TestPreprocess {

		
	private static ArrayList<File> files = new ArrayList<>();
	private static ArrayList<String> texts = new ArrayList<>();
	
	private static ArrayList<ArrayList<ArrayList<String>>> docs = new ArrayList<>();
	
	private static Preprocess preprocess = new Preprocess();
	

	
	
	private static int wordRepetition(ArrayList<ArrayList<String>> doc){
		int result = 0;

		HashMap<String, Integer> tokens = new HashMap<>();
		
		for(ArrayList<String> seg : doc) {
			for(String t : seg)
				if(tokens.containsKey(tokens)) 
					tokens.put(t, tokens.get(t)+1);
				else
					tokens.put(t, 1);
		}
		
	
		
		return result;
	}
	
	private static void loadDocs() {
		files.clear();
		getFiles(new File("./testes-preprocessamento"));
		
		for(File f : files) {

			String txt = Files.loadTxtFile(f);
			texts.add(txt);
		}
		

		if (preprocess.isRemoveHeaders()) {
			for(int i=0; i<texts.size(); i++) {
				HeaderDetector headerDetector = new HeaderDetector();
				headerDetector.detectHeader(texts.get(i));
				texts.set(i, headerDetector.removeHeader());
				
			}
		}
		
	}
	
	private static void loadArrays() {
		final String EOS = "_EOS_";
		
		for(String txt : texts) {

			txt = TextUtils.indentifyEndOfSentence(txt, EOS);
			ArrayList<String> StringSentences = TextUtils.splitSentences(txt, EOS);
			
			ArrayList<ArrayList<String>> arraySentences = new ArrayList<>();

			for(String stringSentence : StringSentences) {
				arraySentences.add(getTokens(stringSentence));
			}
			
			docs.add(arraySentences);
		}
	}
	


	
	private static void showSizes() {
		String medias = "";
		
		int totalPalavras  = 0;
		float sumMedias = 0;
		
		for(ArrayList<ArrayList<String>> doc : docs) {
			int nWords = 0;
			float mediaSentencas = 0;
			
			for(ArrayList<String> sentence : doc) {
				nWords += sentence.size();
			}
			
			mediaSentencas = (float)nWords / (float)doc.size();
		
			totalPalavras  += nWords;
			sumMedias      += mediaSentencas;
		}
		
		float mediaDocs = (float)totalPalavras / docs.size();
		float mediaSent = sumMedias / docs.size();
		
		medias =            String.format("MÉDIA  --------------------> DOCS: %.3f | SENTENÇAS: %.3f", mediaDocs , mediaSent);

		System.out.print(medias +  "\n");
		System.out.print("\n");
		
	}
	
	private static void showTextsSizes() {
		
		int sum = 0;
		
		for(String txt : texts) {
			int total = getTokens(txt).size();
			sum += total;
		}

		float media = (float)sum / (float)texts.size();
		System.out.println(String.format("MÉDIAS --------------------> DOCS: %.3f", media));
		System.out.println();
		
	}

	private static void showWords(ArrayList<ArrayList<String>> doc) {
		StringBuilder sb = new StringBuilder();
		
		for(ArrayList<String> sentence : doc) {
			for(String word : sentence) {
				sb.append("["+word+"]" + " ");
			}
			sb.append("\n");
		}

		System.out.println(sb);
	}
	
	
	
	private static void getFiles(File folder) {
		
		File files[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt") || pathname.isDirectory();
			}
		});

		for(File f : files) {
			if(f.isDirectory())
				getFiles(f);
			else
				TestPreprocess.files.add(f);
		}
		
	}

	private static ArrayList<String> getTokens(String txt) {
//		return txt.split(" +");
//		http://stackoverflow.com/questions/10079415/splitting-a-string-with-multiple-spaces
		
		ArrayList<String> result = new ArrayList<>();

		String[] tokens = txt.split("\\s+");
		
		for(String t:tokens) {
			result.add(t);
		}
		
		return result;
	}
	
	
	
	public static void preprocess() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					if (preprocess.remove(sentence.get(i))) {
						sentence.remove(i);
						i--;
					}
					else {
						sentence.set(i, preprocess.transform(sentence.get(i)));
					}
				}
			}
		}		
	}
	
	
	public static void main(String[] args) {

		Cleaner.setNumbersAllowed();
		
		System.out.println("Testando Preprocessamento\n");
		
		preprocess.setRemoveHeaders(true);
		preprocess.setRemoveShortThan(true);
		preprocess.setRemoveAccents(true);
		preprocess.setRemovePunctuation(true);
		preprocess.setToLowCase(true);
		preprocess.setRemoveStopWord(true);
		preprocess.setRemoveNumbers(true);
		preprocess.setRemoveStem(true);
		
		loadDocs();					System.out.print("Loading docs       \n"); 	
		loadArrays();				System.out.print("Spliting sentences \n");
		preprocess();
		showTextsSizes();

		showSizes();
		
		showWords(docs.get(0));
	
	}

//	43.561	
//	49.024
	
	
	
}


