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
	
	private static final int MIN_AGREEMENT = 3;
	
	public static void main(String[] args) {
		System.out.println("Creating the Golden Refernce Segmentation\n");
		
		
//		File csvFile = new File("./docs/Fabio/Ata 29 - 25a Reunião Odinária PPGCCS.txt.csv");
//		System.out.println(showReferenceSegmentation(csvFile));
//		System.out.println("\n===\n\n");

		
		File folder = new File("./docs3");
		File refFolder = new File("./SegReferences");
		
		if(!refFolder.exists()) {
			refFolder.mkdirs();
		}
		
		System.out.println(createReferenceSegmentation(folder, refFolder));
		
//		File txtFile = new File("./txtDocs/01 Ata 30 - 26a Reunião Odinária PPGCCS.txt");
//		fileToSentences(txtFile);
		
//		createDocsInSentences(new File("./txtDocs"));
	}
	
//	public static void createReferenceSegmentation() {
//		
//	}
	
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

//	public static int[] getIntegersBinarySegmentation(File file) {
//		ArrayList<String> refSegments  = MeasureUtils.CSVSegmentsToArray(file);
//		ArrayList<String> refSentences = MeasureUtils.segmentsToSentences(refSegments, MeasureUtils.getMyDefaultPreprocess());
//		
//		int[] binRef = MeasureUtils.getBinarySegmentation(refSentences);
//		
//		return binRef;
//	}
	
	public static String getStringBinarySegmentation(File file) {
		String result = "";

		int[] binRef = MeasureUtils.getIntegersBinarySegmentation(file);
		
		for(int i : binRef) {
			result += String.format("%d ", i);
		}
		
		return result;
		
		
		
//		ArrayList<String> refSegments  = MeasureUtils.CSVSegmentsToArray(file);
//		ArrayList<String> refSentences = MeasureUtils.segmentsToSentences(refSegments, MeasureUtils.getMyDefaultPreprocess());
//		
//		String bin = getStringBinarySegmentation(refSentences);
//
//		return bin;
	}

	
//	public static String getStringBinarySegmentation(ArrayList<String> sentences) {
//		String result = "";
//
//		int[] binRef = MeasureUtils.getBinarySegmentation(sentences);
//		
//		for(int i : binRef) {
//			result += String.format("%d ", i);
//		}
//		
//		return result;
//	}


	
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
	
	private static void createReferenceSegmentationFiles(File refFolder, ArrayList<ArrayList<File>> allAnotations) {
//		StringBuilder sb = new StringBuilder();
//		for(ArrayList<File> aa : allAnotations) {
//			for(File a : aa) {
//				
//				String bin = getStringBinarySegmentation(a);
//				
//				sb.append(String.format("\n%s >> %s", a, bin));
//			}
//			sb.append("\n.........");
//		}
//		
//		System.out.println(String.format("\n%s\n", sb));
		
		StringBuilder table = new StringBuilder();
		
		System.out.println(allAnotations.size() + " Anotations");
		
		table.append(allAnotations.size() + " Nomes de Aquivos de referência\n\n");
		
		for(ArrayList<File> sameNameFiles: allAnotations) {
//			System.out.println(sameNameFiles.size()+" Anotadores ");
			
			
			String name = sameNameFiles.get(0).getName();
			File refFile = new File(refFolder+"/"+name);
			ArrayList<Integer> refSeg = new ArrayList<>();
			
			System.out.println("\n"+name);
			table.append(name+"\n");
			
			ArrayList<int[]> annotations = new ArrayList<>();
			for(File fileAnnotation : sameNameFiles) {
				int[] binSegmentaion = MeasureUtils.getIntegersBinarySegmentation(fileAnnotation); 
				annotations.add(binSegmentaion);
				int qtdSegments = MeasureUtils.countSegments(binSegmentaion);
//				table.append("\tAnotador N "+ qtdSegments + " segmentos | " + binSegmentaion.length +" sentenças \n");
				table.append(String.format("\tAnotador N %3d segmetnos | %3d sentenças\n", qtdSegments, binSegmentaion.length));
				System.out.println(String.format("%s", getStringBinarySegmentation(fileAnnotation)));
			}
			
//			for(int[] a : annotations) {
//				int count = 0;
//				for(int isEOS : a) {
//					if(isEOS == 1) count++;
//				}
//
//				refSeg.add(count >= MIN_AGREEMENT ? 1 : 0);
//			}
			
			int len = annotations.get(0).length;
			for(int i=0; i<len; i++) {
				int count = 0;
				for(int[] a : annotations) {
					count += a[i];
				}
				refSeg.add(count >= MIN_AGREEMENT ? 1 : 0);
			}

			for(int i=0; i<len; i++) {
				System.out.print("--");
			}
			System.out.println();
			
			for(int i : refSeg) {
				System.out.print(String.format("%d ", i));
			}
			
			System.out.println(String.format("\n-->%s\n", refFile));
			
			
			
			saveRefFile(refFile, refSeg);
		}
		
		System.out.println(table);
	}
	
	
	
	private static void saveRefFile(File refFile, ArrayList<Integer> refSeg) {
		String txtName = refFile.getName().substring(0, refFile.getName().length()-".csv".length());
		String[] sentences = fileToSentencesArray(new File("./txtDocs/"+txtName));
		
		if(sentences.length != refSeg.size()) {
			System.out.println(String.format("Diferença entre sentenças identificadas no arquivo '%s' e na segmentação de referência. %d==%d", refFile, sentences.length, refSeg.size()));
		}
		
		StringBuilder sb = new StringBuilder();
		String seg = "";
		
		int i= 0;
		for(i= 0; i<sentences.length; i++) {
			if(refSeg.get(i) == 1) {
				seg += sentences[i];
				sb.append(String.format("\"%s\",automatic-reference\n", seg));
				seg = "";
			}
			else {
				seg += sentences[i];
			}
		}
		seg += sentences[i-1];
		sb.append(String.format("\"%s\",automatic-reference\n", seg));

		Files.saveTxtFile(sb.toString(), refFile);
		
		
	}

	public static String createReferenceSegmentation(File folder, File refFolder) {
		String result = "";
		
		
		/** Mapping the File Names */
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

		
		/** Get all the annotation files. And the final Reference Files*/
		ArrayList<ArrayList<File>> allAnotations = new ArrayList<>();
		for(String name : names) {
			ArrayList<File> refs = new ArrayList<>();
			
			for(File dir : folders) {
				File ref = new File(dir+"/"+name);
				if(ref.exists())
					refs.add(ref);
			}
			allAnotations.add(refs);
		}
		
		
		/** Get binary annotations and compile the Refernce Segmentation */
		createReferenceSegmentationFiles(refFolder, allAnotations);
		
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
		result += getStringBinarySegmentation(csvFile);
		
		return result;
	}
	
	public static String fileToSentences(File txtFile) {
		String[] s2 = fileToSentencesArray(txtFile);
		
		StringBuilder sb =  new StringBuilder();
		for(String s : s2) {
			sb.append(s.trim()+"\n");
		}
		
		return sb.toString();
	}

	public static String[] fileToSentencesArray(File txtFile) {
		Preprocess preproces = MeasureUtils.getMyDefaultPreprocess();
		
		String txt = Files.loadTxtFile(txtFile);
		txt = txt.replace('\n', ' ');
		
		String s1 = preproces.identifyEOS(txt, MeasureUtils.END_SENTENCE_MARK);
		
		
		return s1.split(MeasureUtils.END_SENTENCE_MARK);
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
