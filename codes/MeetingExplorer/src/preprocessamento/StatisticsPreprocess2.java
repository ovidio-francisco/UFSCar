package preprocessamento;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import utils.Files;

public class StatisticsPreprocess2 {
	
	private static ArrayList<File> files = new ArrayList<>();
	
	private static Preprocess preprocess = new Preprocess();

	
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
				StatisticsPreprocess2.files.add(f);
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

	private static String removeHeaderAndFooters(String txt) {

		return preprocess.cleanTextMeating(txt);
		
	}
	
	private static ArrayList<String> filter(ArrayList<String> tokens) {
		ArrayList<String> result = new ArrayList<>();
		
		for(String t : tokens) {
			if(!preprocess.remove(t)) {
				result.add(t);
			}
		}
		
		return result;
	}
	
	
	private static String getStatistics(File f) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(f.getName());
		
		String txt = Files.loadTxtFile(f);
		sb.append("\n\t On Load = \t\t" + getTokens(txt).size());
		

		preprocess.setRemovePageNumbers(true);
		preprocess.setRemoveHeaders(true);
		preprocess.setRemoveExtraSpaces(true);
		txt = removeHeaderAndFooters(txt);
		sb.append("\n\t Header Removal = \t" + getTokens(txt).size());
		
		preprocess.setRemoveAccents(true);
		preprocess.setRemoveNumbers(true);
		preprocess.setRemovePunctuation(true);
		preprocess.setRemoveShortThan(true);
		ArrayList<String> tokens = getTokens(txt);
		tokens = filter(tokens);
		sb.append("\n\t Term Reduction = \t" + tokens.size());
		
		
		return sb.toString();
	}
	

	public static void main(String[] args) {
		System.out.println("Testando Preprocessamento2\n");

		
		Cleaner.setNumbersAllowed();
		
		files.clear();
		getFiles(new File("./testes-preprocessamento"));
		
		
		
		for(File f: files) {
			System.out.println(getStatistics(f));
		}
		

		
		
	}

}
