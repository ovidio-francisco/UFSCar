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

public class Tester {

	public static void main(String[] args) {
		
		File folder = new File("./docs");
		
		Document query = new Document(new File(folder+"/"+"Generos-Musicais-Query.txt"));
//		Document query = new Document(new File(folder+"/"+"ds_IBM"));
//		Document query = new Document("Data Science defines a discipline that incorporates applying varying degrees");		
		Document d1    = new Document(new File(folder+"/"+"ds_2012"));
		Document d2    = new Document(new File(folder+"/"+"ds_2013"));
		Document d3    = new Document(new File(folder+"/"+"ds_2014"));
		Document d4    = new Document(new File(folder+"/"+"ds_2015"));
		Document d5    = new Document(new File(folder+"/"+"Generos-Musicais.txt"));

	
		ArrayList<Document> documents = new ArrayList<Document>();
		documents.add(query);
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
			System.out.println(vectorSpace.cosineSimilarity(query, doc));
		}
	}

}
//
//
//
//Reading file: ./docs/ds_IBM and preprocessing
//Reading file: ./docs/ds_2012 and preprocessing
//Reading file: ./docs/ds_2013 and preprocessing
//Reading file: ./docs/ds_2014 and preprocessing
//Reading file: ./docs/ds_2015 and preprocessing
//Creating the inverted index
//Creating the tf-idf weight vectors
//
//Comparing to ./docs/ds_2012
//0.021102506125924007
//
//Comparing to ./docs/ds_2013
//0.02330740229579876
//
//Comparing to ./docs/ds_2014
//0.024636020768414847 
//
//Comparing to ./docs/ds_2015
//0.04737279910599297
