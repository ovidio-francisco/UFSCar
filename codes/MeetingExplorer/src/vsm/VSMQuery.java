package vsm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VSMQuery {
	
	private ArrayList<Document> documents = new ArrayList<Document>();
	private VectorSpaceModel vsm = null;

	public VSMQuery(ArrayList<Document> documents) {
		this.documents = documents;
		vsm = new VectorSpaceModel(new Corpus(this.documents));
	}
	
	public VSMQuery(String[] strings) {
		
		for(String s : strings) {
			this.documents.add(new Document(s));
		}
		
		vsm = new VectorSpaceModel(new Corpus(this.documents));
	}
	

	public double getSimilarity(String query, Document doc) {

		return vsm.cosineSimilarity(query, doc);
	}

	public double getSimilarity(String query, String string) {

		return vsm.cosineSimilarity(query, new Document(string));
	}
	
	public Document getSimilarest(String query, int pos) {
		for(Document d : documents) {
			
			double simi = getSimilarity(query, d);
//			String txt = d.getTxt();
			
//			System.out.println(String.format("simi=%f --> %s", simi, txt));

			d.setSimilarity(simi);
		}
		
		Collections.sort(documents, new Comparator<Document>(
				) {
					@Override
					public int compare(Document d1, Document d2) {
						if (d1.getSimilarity() == d2.getSimilarity()) return 0;
						
						return (d1.getSimilarity() - d2.getSimilarity()) > 0 ? -1 : +1;
						
					}
		});
		
//		for(Document d : documents) {
//			System.out.println(String.format("%f --> %s", d.getSimilarity(), d.getTxt()));
//		}
		
		return documents.get(pos);
	}

}
