package meetingMiner;

import java.io.File;
import java.util.ArrayList;

public class MMTopic {
	
	private ArrayList<String> descriptors = new ArrayList<>();
	private ArrayList<File> segmentsdoc   = new ArrayList<>();
	

	public static ArrayList<MMTopic> getTopics(StringBuffer[] descTopics, StringBuffer[] docsPerTopics, int numTopics,	int[] orderedTopics) {

		ArrayList<MMTopic> result = new ArrayList<>();
		
        for(int i=0;i<numTopics;i++){
            
        	MMTopic topic = new MMTopic();
        	
            int indTopic = orderedTopics[i];
            
            for(String desc: descTopics[indTopic].toString().split(";")) {
            	if (!desc.trim().isEmpty())
            		topic.descriptors.add(desc.trim());
            }
            
            for(String doc : docsPerTopics[indTopic].toString().split(";")) {
                topic.segmentsdoc.add(new File(doc));
            }

            result.add(topic);
        }

		return result;
	}
	
	
	public boolean containsDescriptor(String desc) {
		return descriptors.contains(desc);
	}
		
	public boolean containsSegmentDoc(File doc) {
		return segmentsdoc.contains(doc);
	}	
	
	public ArrayList<String> getDescriptors() {
		return descriptors;
	}
	public ArrayList<File> getSegmentsDoc() {
		return segmentsdoc;
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
		for(File f : segmentsdoc) {
			sb.append("\n"+f.getName()+";");
		}
		sb.append("\n");
		
		return sb.toString();
	}
	
}
