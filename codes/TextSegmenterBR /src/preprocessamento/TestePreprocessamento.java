package preprocessamento;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import ptstemmer.implementations.OrengoStemmer;
import utils.Files;
import utils.TextUtils;

public class TestePreprocessamento {

		
	private static ArrayList<File> files = new ArrayList<>();
	private static ArrayList<String> texts = new ArrayList<>();
	private static ArrayList<ArrayList<ArrayList<String>>> docs = new ArrayList<>();
	
	private static StopWordList stopWords = new StopWordList(new File("stopPort.txt"));
	private static ptstemmer.Stemmer stemmer =  new OrengoStemmer();
	
//	private static int wordRepetition(){
//		int result = 0;
//		
//		return result;
//	}
	
	private static void loadDocs() {
		files.clear();
		getFiles(new File("./testes-preprocessamento"));
		
		String txt = Files.loadTxtFile(files.get(0));
		
		texts.add(txt);
		
	}
	
	private static void loadArrays() {

		final String EOS = "_EOS_";
		
		for(String txt : texts) {

			txt = TextUtils.indentifyEndOfSentence(txt, EOS);
			ArrayList<String> StringSentences = TextUtils.splitSentences(txt, EOS);
			
			ArrayList<ArrayList<String>> arraySentendes = new ArrayList<>();

			for(String stringSentence : StringSentences) {
				arraySentendes.add(getTokens(stringSentence));
			}
			
			docs.add(arraySentendes);
			
		}
		
		
		
	}
	
	private static void removeStopWords() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					if (stopWords.isStopWord(sentence.get(i))) {
						sentence.remove(i);
						i--;
					}
				}
			}
		}
	}

	private static void removeStem() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					sentence.set(i, stemmer.wordStemming(sentence.get(i)));
				}
			}
		}		
	}
	
	private static void removeShortThan(int minSize) {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					if (sentence.get(i).length() < minSize) {
						sentence.remove(i);
						i--;
					}
				}
			}
		}		
	}
	
	private static void removeHeaderAndFooters() {
		
		for(int i=0; i<texts.size(); i++) {

			HeaderDetector headerDetector = new HeaderDetector();
			headerDetector.detectHeader(texts.get(i));
			texts.set(i, headerDetector.removeHeader());
			
		}
		

	}

	private static void removeNumbers() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					if (TextUtils.isANumber(sentence.get(i))) {
						sentence.remove(i);
						i--;
					}
				}
			}
		}
		
	}
	
	private static void removePunctuation() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					sentence.set(i, Cleaner.removePunctuation(sentence.get(i)));
				}
			}
		}
	}

	private static void removeAccents() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					sentence.set(i, Cleaner.clean(sentence.get(i)));
				}
			}
		}		
	}
	
	private static void toLowCase() {
		
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					sentence.set(i, sentence.get(i).toLowerCase());
				}
			}
		}
		
	}
	
	private static void showSizes() {
		String sizes = "";
		for(ArrayList<ArrayList<String>> doc : docs) {
			int docSize = 0;
			String sentencesSizes = "";
			for(ArrayList<String> sentence : doc) {
				sentencesSizes += " " + sentence.size();
				docSize += sentence.size();
			}
			sizes += "  Total: " + docSize + " = " +sentencesSizes;
		}
		System.out.println(sizes);
	}
	
	private static void showTextsSizes() {
		
		for(String txt : texts) {
			
			System.out.println("  Total= " + getTokens(txt).size());
		}
		
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
				TestePreprocessamento.files.add(f);
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
	
	public static void main(String[] args) {

		Cleaner.setNumbersAllowed();
		
		System.out.println("Testando Preprocessamento\n");

		
		loadDocs();					System.out.print("Loading docs         "); 	showTextsSizes();
		
		removeHeaderAndFooters();	System.out.print("Removing Headers     "); 	showTextsSizes();	
		
		loadArrays();				System.out.print("Spliting sentences \n");	
		
		toLowCase();
		removePunctuation();		System.out.print("Removing Punctuation ");	showSizes();
		removeAccents();			System.out.print("Removing Accents     ");	showSizes();
		removeStopWords();          System.out.print("Removing StopWords   ");	showSizes();
		removeNumbers();            System.out.print("Removing Numbers     ");	showSizes();
		removeShortThan(3);			System.out.print("Removing Shorters    ");	showSizes();
		removeStem();               System.out.print("Removing Stem        ");	showSizes();

//		showWords(docs.get(0));
		
	}

}


