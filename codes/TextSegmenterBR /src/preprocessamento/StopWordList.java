package preprocessamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.Files;

public class StopWordList implements StopWords {

	List<String> list = new ArrayList<>();
	

	public StopWordList(File file) {
		list = Files.loadTxtFileToList(file);
		System.out.println(list.size());
	}
	
	@Override
	public boolean isStopWord(String word) {
		return list.contains(Cleaner.clean(word.toLowerCase()));
	}

	@Override
	public List<String> getStopWords() {
		return list;
	}

}
