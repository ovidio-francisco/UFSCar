package vsm.tests;

import java.io.File;
import java.util.ArrayList;

import vsm.Document;
import vsm.VSMQuery;

public class TestVSM {

	public static void main(String[] args) {
		System.out.println("Document / Query Similarity\n");
		File folder = new File("./docs");
		
		String q = "O Brasil é um país que tem uma relação muito forte com a música, especialmente";
		Document d1    = new Document(new File(folder+"/"+"ds_2012"));
		Document d2    = new Document(new File(folder+"/"+"ds_2013"));
		Document d3    = new Document(new File(folder+"/"+"ds_2014"));
		Document d4    = new Document(new File(folder+"/"+"ds_2015"));
		Document d5    = new Document(new File(folder+"/"+"Generos-Musicais.txt"));
	
		ArrayList<Document> documents = new ArrayList<Document>();
		documents.add(d1);
		documents.add(d2);
		documents.add(d3);
		documents.add(d4);
		documents.add(d5);

		VSMQuery vsm = new VSMQuery(documents);
		
		double simi = vsm.getSimilarity(q, d5);
		
		System.out.println(String.format("\nSimilarity = %f", simi));
	}

}
