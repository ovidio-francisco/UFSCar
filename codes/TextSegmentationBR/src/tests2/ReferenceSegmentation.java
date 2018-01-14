package tests2;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import preprocessamento.Preprocess;
import segmenters.evaluations.measure.MeasureUtils;
import utils.Files;

public class ReferenceSegmentation {
	
	public static void main(String[] args) {
		System.out.println("Creating the Golden Refernce Segmentation");
		
//		File csvFile = new File("./docs/Fabio/Ata 29 - 25a Reunião Odinária PPGCCS.txt.csv");
		
//		System.out.println(showReferenceSegmentation(csvFile));

//		System.out.println("\n===\n\n");
		
		
		File folder = new File("./docs2");
		
		System.out.println(createReferenceSegmentation(folder));
		
	}
	
	
	public static String getSegmentedSentences(ArrayList<String> sentences) {
		String result = "";
		
		int segCount = 1;
		
		for (int i=0; i<sentences.size(); i++) {
			result += String.format("[%2d] %s\n", i, sentences.get(i));
			if (sentences.get(i).endsWith(MeasureUtils.END_SEGMENT_MARK)) {
				
				result += String.format("========== [%2d] ==========\n", segCount++);
			}
		}
		
		return result;
	}
	
	public static String getIndexSentences(ArrayList<String> sentences) {
		String result = "";
		
		for(int i=0; i<sentences.size(); i++) {
			String n = String.format("%d", i);
			result += String.format("%c ", n.toCharArray()[n.length()-1]);
		}
		
		return result+"\n";
	}

	public static String getBinarySegmentation(ArrayList<String> sentences) {
		String result = "";

		int[] binRef = MeasureUtils.getBinarySegmentation(sentences);
		
		for(int i : binRef) {
			result += String.format("%d ", i);
		}
		
		return result;
	}

	
	public static File[] getFiles(File folder) {
		
		File listFiles[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("csv");
			}
		});

		return listFiles;
	}

	public static File[] getFolders(File folder) {
		File listFolders[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		return listFolders;
	}
	
	public static String createReferenceSegmentation(File folder) {
		String result = "";
		
		HashMap<String, Integer> fileNames =  new HashMap<>();
		

//		csvFiles.clear();
//		getFiles(folder);
//		
//	
//		Preprocess preprocess = MeasureUtils.getMyDefaultPreprocess();
//
//		for(File f : csvFiles) {
//			ArrayList<String> refSegments  = MeasureUtils.CSVSegmentsToArray(f);
//			ArrayList<String> refSentences = MeasureUtils.segmentsToSentences(refSegments, preprocess);
//			
//			String bin = getBinarySegmentation(refSentences);
//			
//			System.out.println(String.format("--> [%s] - %s ", bin, f));
//		}
		
		File[] folders = getFolders(folder);
		
		for(File d : folders) {
			System.out.println(String.format("+ %s", d));
			
			File[] files = getFiles(d);
			for(File f : files) {
				System.out.println(String.format("  - %s", f.getName()));
				
				if(fileNames.containsKey(f.getName())) {
					Integer count = fileNames.get(f.getName());
					fileNames.put(f.getName(), count+1);
				}
				else {
					fileNames.put(f.getName(), 1);
				}
			}
		}
		
		System.out.println("\n\n----\n\n");
		
		for(String name : fileNames.keySet()) {
			System.out.println(String.format("[%d] - %s", fileNames.get(name), name));
		}
		
		ArrayList<String> names = new ArrayList<>(fileNames.keySet());
		sortNames(names);
		
		for(String name : names) {
			System.out.println(String.format(">> %s", name));
		}
		
		
		return result;
	}

    public static void sortNames(ArrayList<String> names) {
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String f1, String f2) {
                return f1.compareTo(f2);
            }
        } );

    }
	
	public static String showReferenceSegmentation(File csvFile) {
		String result = "";
	
		Preprocess preprocess = MeasureUtils.getMyDefaultPreprocess();
		
		ArrayList<String> refSegments  = MeasureUtils.CSVSegmentsToArray(csvFile);
		ArrayList<String> refSentences = MeasureUtils.segmentsToSentences(refSegments, preprocess);

		result += getSegmentedSentences(refSentences);
		result += "\n";
		result += getIndexSentences(refSentences);
		result += getBinarySegmentation(refSentences);
		
		return result;
	}

}
