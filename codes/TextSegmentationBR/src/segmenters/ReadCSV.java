package segmenters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import preprocessamento.Cleaner;

public class ReadCSV {
	
	
	public static void main(String args[]) {
		
		System.out.println("CSV\n");
		
		File csvFile = new File("./docs/Fabio/Ata 32 - 28a Reunião Odinária PPGCCS.txt.csv");
		
		
		
		ArrayList<String> segs = CSVSegmentsToArray(csvFile);
		
		for(String s : segs) {
			String txt = s;
			txt = txt.replace("\n", " $");
			txt = txt.replace("\t", " →");
			txt = Cleaner.removeDoubleSpaces(txt);
			
			String[] t = txt.split(" ");
			
			System.out.println(String.format("\n----------------------\n%s\n[%d]", txt,t.length));
//			for(String r: t){
//				System.out.println(String.format("[%s]", r));
//			}
			
			System.out.println(String.format("\n>>>>%s<<<<\n", t[t.length-1]));
			
			ArrayList<Integer> bounds = bounds(csvFile);
			
			for(int i : bounds) {
				System.out.println(String.format("{{%d}}", i));
			}
			
		}
		
	}
	
	
	/**
	 * Reads a csv file and put the segments into an array
	 */
	public static ArrayList<String> CSVSegmentsToArray(File csvFile) {
		ArrayList<String> result = new ArrayList<>();
		
		try {
			Iterable<CSVRecord> recs = CSVFormat.DEFAULT.parse(new FileReader(csvFile));
			for(CSVRecord r : recs) {
				result.add(r.get(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static ArrayList<Integer> bounds(File csvFile) {
		ArrayList<Integer> result = new ArrayList<>();
		
		
		int bound = 0;
		
		ArrayList<String> segs = CSVSegmentsToArray(csvFile);
		for(String s : segs) {
			String txt = s;
			txt = txt.replace("\n", " $");
			txt = txt.replace("\t", " →");
			txt = Cleaner.removeDoubleSpaces(txt);
			
			String[] t = txt.split(" ");
			
			int b = t.length-1;
			bound += b;
			
			System.out.println(String.format("\n>>>>%s<<<<\n", t[b]));
			
			result.add(bound);
		}

		return result;
	}

}
