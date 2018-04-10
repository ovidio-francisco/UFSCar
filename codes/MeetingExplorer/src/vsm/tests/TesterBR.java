package vsm.tests;


import java.io.File;
import java.util.ArrayList;

import vsm.Corpus;
import vsm.Document;
import vsm.VectorSpaceModel;

/**
 * the tester class.
 * @author swapneel
 */

//https://github.com/hcutler/tf-idf
//https://courses.cs.washington.edu/courses/cse373/17au/project3/project3-2.html	

public class TesterBR {

	public static void main(String[] args) {
		
		File folder = new File("./docs");
		
		String q = "O Brasil é um país que tem uma relação muito forte com a música, especialmente";
		
//		Document query = new Document("O Brasil é um país que tem uma relação muito forte com a música, especialmente");		
		Document d1    = new Document(new File(folder+"/"+"ds_2012"));
		Document d2    = new Document(new File(folder+"/"+"ds_2013"));
		Document d3    = new Document(new File(folder+"/"+"ds_2014"));
		Document d4    = new Document(new File(folder+"/"+"ds_2015"));
		Document d5    = new Document(new File(folder+"/"+"Generos-Musicais.txt"));

	
		ArrayList<Document> documents = new ArrayList<Document>();
//		documents.add(query);
		documents.add(d1);
		documents.add(d2);
		documents.add(d3);
		documents.add(d4);
		documents.add(d5);
		
		Corpus corpus = new Corpus(documents);
		
		VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
		
		for(int i = 1; i < documents.size(); i++) {
			Document doc = documents.get(i);
			System.out.println("\nComparing to " + doc);
			System.out.println(vectorSpace.cosineSimilarity(q, doc));
		}
	}

}
