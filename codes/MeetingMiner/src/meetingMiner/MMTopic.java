package meetingMiner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import preprocessamento.Preprocess;

public class MMTopic {
	
	private ArrayList<String> descriptors = new ArrayList<>();
	private ArrayList<File> segmentsDoc   = new ArrayList<>();
	
	
	public ArrayList<String> descriptorsIntersection(ArrayList<String> descs) {
		ArrayList<String> result = new ArrayList<>();
		
		result.addAll(descriptors);
		result.retainAll(descs);
		
		return result;
	}
	
	
	public Set<String> descriptorsStemedIntersection(Set<String> descs) {
		
		HashMap<String, String> stems = new HashMap<>();
		for(String s : descriptors) {
			stems.put(s, Preprocess.getStemmer().wordStemming(s));
		}

		ArrayList<String> descsStems = new ArrayList<>();
		for(String s : descs) {
			descsStems.add(Preprocess.getStemmer().wordStemming(s));
		}
		
		stems.values().retainAll(descsStems);

		return stems.keySet();
	}
	
	
	public boolean containsDescriptor(String desc) {
		return descriptors.contains(desc);
	}
		
	public boolean containsSegmentDoc(File doc) {
		return segmentsDoc.contains(doc);
	}	
	
	public ArrayList<String> getDescriptors() {
		return descriptors;
	}
	
	public ArrayList<File> getSegmentsDoc() {
		return segmentsDoc;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Descriptors: ");
		for(String s : descriptors) {
			sb.append(s+"; ");
		}
		sb.append("\n");

		sb.append("Documents: ");
		for(File f : segmentsDoc) {
			sb.append("\n"+f.getName()+";");
		}
		sb.append("\n");
		
		return sb.toString();
	}

	public void addDescriptor(String desc) {
		descriptors.add(desc.trim());
	}

	public void addSegmentDoc(File file) {
		segmentsDoc.add(file);
	}
	
}
