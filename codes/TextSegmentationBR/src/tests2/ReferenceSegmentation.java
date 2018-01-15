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
		System.out.println("Creating the Golden Refernce Segmentation\n");
		
		
//		File csvFile = new File("./docs/Fabio/Ata 29 - 25a Reunião Odinária PPGCCS.txt.csv");
//		System.out.println(showReferenceSegmentation(csvFile));
//		System.out.println("\n===\n\n");

		
		File folder = new File("./docs3");
		System.out.println(createReferenceSegmentation(folder));
		
		File txtFile = new File("./txtDocs/01 Ata 30 - 26a Reunião Odinária PPGCCS.txt");
		fileToSentences(txtFile);
		
//		createDocsInSentences(new File("./txtDocs"));
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
		
		File[] folders = getFolders(folder);
		
		for(File d : folders) {
//			System.out.println(String.format("+ %s", d));
			
			File[] files = getFiles(d);
			for(File f : files) {
//				System.out.println(String.format("  - %s", f.getName()));
				
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
		
		ArrayList<String> names = new ArrayList<>(fileNames.keySet());
		sortNames(names);
		
//		for(String name : names) {
//			System.out.println(String.format(">> [%d] %s", fileNames.get(name), name));
//		}
		
		System.out.println(String.format("%d names", names.size()));

		ArrayList<ArrayList<File>> allRefs = new ArrayList<>();
		
		for(String name : names) {
			ArrayList<File> refs = new ArrayList<>();
			
			for(File dir : folders) {
				File ref = new File(dir+"/"+name);
				if(ref.exists())
					refs.add(ref);
//				System.out.println(String.format("-->%s -- %b", ref, ref.exists()));
			}
			allRefs.add(refs);
		}
		
//		for(ArrayList<File> r : allRefs) {
//			for(File f : r) {
//				System.out.println(String.format("==> %s", f));
//			}
//			System.out.println(".......");
//		}
	
		StringBuilder sb = new StringBuilder();
		
		for(ArrayList<File> r : allRefs) {
			for(File f : r) {
				
				ArrayList<String> refSegments  = MeasureUtils.CSVSegmentsToArray(f);
				ArrayList<String> refSentences = MeasureUtils.segmentsToSentences(refSegments, MeasureUtils.getMyDefaultPreprocess());
				
				String bin = getBinarySegmentation(refSentences);

				
				sb.append(String.format("\n%s >> %s", f, bin));
			}
			sb.append("\n.........");
		}
		
		System.out.println(String.format("\n%s\n", sb));
		
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
	
	public static String fileToSentences(File txtFile) {
		Preprocess preproces = MeasureUtils.getMyDefaultPreprocess();
		
		String txt = Files.loadTxtFile(txtFile);
		txt = txt.replace('\n', ' ');
		
		String s1 = preproces.identifyEOS(txt, MeasureUtils.END_SENTENCE_MARK);
		
		String[] s2 = s1.split(MeasureUtils.END_SENTENCE_MARK);
		
		StringBuilder sb =  new StringBuilder();
		for(String s : s2) {
			sb.append(s.trim()+"\n");
		}
		
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private static void createDocsInSentences(File folder) {
		File listFiles[] = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt");
			}
		});

		
		for(File f : listFiles) {
			File docToSentences = new File("./docsInSentences/"+f.getName()+".sentences");
			System.out.println(f + " ---->> " + docToSentences);
			
			String sentences = fileToSentences(f);
			
			Files.saveTxtFile(sentences, docToSentences);
			
			
		}
		
	}
	
}
