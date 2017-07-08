package topicExtraction;

import java.util.ArrayList;

public class MMTopic {
	
	private ArrayList<String> descriptors = new ArrayList<>();
	private ArrayList<String> documents   = new ArrayList<>();
	

	
	public static MMTopic getTopics(StringBuffer[] descTopics, StringBuffer[] docsPerTopics, int numTopics,	int[] orderedTopics) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public void addDescriptor(String descriptor) {
		descriptors.add(descriptor);
	}
	
	public void addDocument(String document) {
		documents.add(document);
	}
	
	
	public ArrayList<String> getDescriptors() {
		return descriptors;
	}
	public ArrayList<String> getDocuments() {
		return documents;
	}

	
	
	
	
}
