package preprocessamento;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import ptstemmer.implementations.OrengoStemmer;
import utils.Files;
import utils.TextUtils;

public class Preprocess {

		
	private static ArrayList<File> files = new ArrayList<>();
	private static ArrayList<String> texts = new ArrayList<>();
	
	private static ArrayList<ArrayList<ArrayList<String>>> docs = new ArrayList<>();
	
	private static StopWordList stopWords = new StopWordList(new File("stopPort.txt"));
	private static ptstemmer.Stemmer stemmer =  new OrengoStemmer();
	
	private static boolean removeStopWord    = false;
	private static boolean removeShortThan   = false;
	
	private static boolean removeHeaders     = false;
	private static boolean removeStem        = false;
	private static boolean removeNumbers     = false;
	private static boolean removePunctuation = false;
	private static boolean removeAccents     = false;
	private static boolean toLowCase         = false;
	private static boolean identifyEOS       = false;
	
	
//	private static int wordRepetition(){
//		int result = 0;
//		
//		return result;
//	}
	
	private static void loadDocs() {
		files.clear();
		getFiles(new File("./testes-preprocessamento"));
		
		for(File f : files) {

			String txt = Files.loadTxtFile(f);
			texts.add(txt);
		}
		

		if (removeHeaders) {
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
				Preprocess.files.add(f);
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
	
	
	public static boolean remove(String token) {
		
		token = token.toLowerCase();
		token = Cleaner.removePunctuation(token);
		token = Cleaner.clean(token);
		
		boolean result = false;
		
		result |= (removeShortThan && token.length() < 3);		
		result |= (removeStopWord  && stopWords.isStopWord(token));
		result |= (removeNumbers   && TextUtils.isANumber(token));
		
		return  result;
	}
	
	public static String transform(String token) {
		String result = token;
		
		if (toLowCase)           result = result.toLowerCase();
		if (removePunctuation)   result = Cleaner.removePunctuation(result);
		if (removeAccents)       result = Cleaner.clean(result);
		if (removeStem)          result = stemmer.wordStemming(result);
		
		return result;
	}
	
	public static void preprocess() {
		for(ArrayList<ArrayList<String>> doc : docs) {
			for(ArrayList<String> sentence : doc) {
				for(int i=0; i<sentence.size(); i++) {
					if (remove(sentence.get(i))) {
						sentence.remove(i);
						i--;
					}
					else {
						sentence.set(i, transform(sentence.get(i)));
					}
				}
			}
		}		
	}
	
	
	public static void main(String[] args) {

		Cleaner.setNumbersAllowed();
		
		System.out.println("Testando Preprocessamento\n");
		
		setRemoveHeaders(true);
		setRemoveShortThan(true);
		setRemoveAccents(true);
		setRemovePunctuation(true);
		setToLowCase(true);
		setRemoveStopWord(true);
		setRemoveNumbers(true);
		setRemoveStem(true);
		
		loadDocs();					System.out.print("Loading docs       \n"); 	
		loadArrays();				System.out.print("Spliting sentences \n");
		preprocess();
		showTextsSizes();

		showSizes();
		
		showWords(docs.get(0));
	
	}

//	43.561	
//	49.024
	
	public static boolean isRemoveStopWord() {
		return removeStopWord;
	}

	public static void setRemoveStopWord(boolean removeStopWord) {
		Preprocess.removeStopWord = removeStopWord;
	}

	public static boolean isRemoveHeaders() {
		return removeHeaders;
	}

	public static void setRemoveHeaders(boolean removeHeaders) {
		Preprocess.removeHeaders = removeHeaders;
	}

	public static boolean isRemoveStem() {
		return removeStem;
	}

	public static void setRemoveStem(boolean removeStem) {
		Preprocess.removeStem = removeStem;
	}

	public static boolean isRemoveNumbers() {
		return removeNumbers;
	}

	public static void setRemoveNumbers(boolean removeNumbers) {
		Preprocess.removeNumbers = removeNumbers;
	}

	public static boolean isRemovePunctuation() {
		return removePunctuation;
	}

	public static void setRemovePunctuation(boolean removePunctuation) {
		Preprocess.removePunctuation = removePunctuation;
	}

	public static boolean isRemoveAccents() {
		return removeAccents;
	}

	public static void setRemoveAccents(boolean removeAccents) {
		Preprocess.removeAccents = removeAccents;
	}

	public static boolean isToLowCase() {
		return toLowCase;
	}

	public static void setToLowCase(boolean toLowCase) {
		Preprocess.toLowCase = toLowCase;
	}

	public static boolean isIdentifyEOS() {
		return identifyEOS;
	}

	public static void setIdentifyEOS(boolean identifyEOS) {
		Preprocess.identifyEOS = identifyEOS;
	}

	public static boolean isRemoveShortThan() {
		return removeShortThan;
	}

	public static void setRemoveShortThan(boolean removeShortThan) {
		Preprocess.removeShortThan = removeShortThan;
	}
	
	
	
}


