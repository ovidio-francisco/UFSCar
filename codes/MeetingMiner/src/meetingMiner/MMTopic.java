package meetingMiner;

import java.io.File;
import java.util.ArrayList;

public class MMTopic {
	
	private ArrayList<String> descriptors = new ArrayList<>();
	private ArrayList<File> segmentsDoc   = new ArrayList<>();
	

//	public static ArrayList<MMTopic> getTopics(StringBuffer[] descTopics, StringBuffer[] docsPerTopics, int numTopics,	int[] orderedTopics) {
//
//
//		return result;
//	}
	
	
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
