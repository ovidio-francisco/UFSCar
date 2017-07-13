package meetingMiner;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import utils.Files;

public class Segment {

	private File originalDocument;
	private File segmentDoc;
	private String text = "";
	
	private ArrayList<MMTopic> relationedTopics = new ArrayList<>();        // Tópicos que indicam estar relacionados com esse segmento 

	
	private ArrayList<String> topicDescriptors = new ArrayList<>();        // Descritores relacionados com esse segmento

	
	public static ArrayList<Segment> getAllSegments(ArrayList<MMTopic> topics) {
		ArrayList<Segment> result = new ArrayList<>();
		
		File folder = Files.getSegmentedDocs();
		File[] segmentDocs = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt");
			}
		});

		
		
		for(File segmentDoc : segmentDocs) {
			System.out.println("Criando objeto Segment para :" + segmentDoc);
			
			Segment segment = new Segment();
			segment.segmentDoc = segmentDoc;
			
			for(MMTopic topic : topics) {
				if (topic.containsSegmentDoc(new File(segmentDoc.getName()))) {
					segment.relationedTopics.add(topic);
					
					for(String descriptor : topic.getDescriptors()) {
						segment.topicDescriptors.add(descriptor);
					}
					
				}
			}
			
			result.add(segment);
			
		}
		
		
		return result;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Segment doc : "+segmentDoc.getName()+"\n");
		sb.append("Text:" + text+"\n");
		
		sb.append("Descriptors: ");
		
		for(String t : topicDescriptors) {
			sb.append(t+ "; ");
		}
		
		
		return sb.toString();
	}
	
	
	
}
