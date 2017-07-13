package meetingMiner;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import utils.Files;

public class Segment {

	private File originalDocument;
	private File segmentDoc;
	private String text = "";
	private ArrayList<MMTopic> relationedTopics = new ArrayList<>();        // Tópicos que indicam estar relacionados com esse segmento 
	private HashMap<String, Integer> topicDescriptors = new HashMap<>();     // Descritores relacionados com esse segmento

	
	public static ArrayList<Segment> getAllSegments(ArrayList<MMTopic> topics) {
		ArrayList<Segment> result = new ArrayList<>();
		
		File folder = Files.getSegmentedDocs();
		File[] segmentDocs = listFiles(folder);
		
		for(File segmentDoc : segmentDocs) {
			System.out.println("Criando objeto Segment para :" + segmentDoc);
			
			Segment segment = new Segment();
			segment.segmentDoc = segmentDoc;
			segment.originalDocument = extractOriginalDocFromSegmentDoc(segmentDoc);
			segment.text = Files.loadTxtFile(segmentDoc).trim();
			
			for(MMTopic topic : topics) {
				if (topic.containsSegmentDoc(new File(segmentDoc.getName()))) {
					segment.relationedTopics.add(topic);
					
					for(String descriptor : topic.getDescriptors()) {
						segment.addDescriptor(descriptor);
					}
				}
			}
			
			result.add(segment);
		}
		
		return result;
	}
	
	private static File extractOriginalDocFromSegmentDoc(File segmentDoc) {
		return new File(segmentDoc.getName().replaceAll(".txt_seg-[0-9]{3,5}.txt", ""));
	}

	public int descriptorFrequency(String descriptor) {
		return topicDescriptors.get(descriptor);
	}
	
	private void addDescriptor(String descriptor) {
		if (topicDescriptors.containsKey(descriptor)) {
			topicDescriptors.put(descriptor, topicDescriptors.get(descriptor)+1);
		}
		else {
			topicDescriptors.put(descriptor, 1);
		}
	}
	
	private static File[] listFiles(File folder) {
		return folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return Files.getFileExtension(f).equals("txt");
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Segment doc : "+segmentDoc.getName()+"\n");
		sb.append("Original doc: "+originalDocument+"\n");
		sb.append("Text: " + text+"\n");
		
		sb.append("Descriptors: ");
		for(String desc : topicDescriptors.keySet()) {
			sb.append(String.format("%s; ", desc));
		}
		
		
		return sb.toString();
	}

	public File getOriginalDocument() {
		return originalDocument;
	}
	
	public File getSegmentDoc() {
		return segmentDoc;
	}

	public String getText() {
		return text;
	}

		
	public Collection<String> getDescriptors() {
		return Collections.unmodifiableCollection(topicDescriptors.keySet());
	}
//		http://www.javapractices.com/topic/TopicAction.do?Id=173
	
	
}
