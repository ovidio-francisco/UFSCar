package vsm;


import java.util.HashMap;
import java.util.Set;

/**
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 * @author swapneel
 *
 */
public class VectorSpaceModel {
	
	/**
	 * The corpus of documents.
	 */
	private Corpus corpus;
	
	/**
	 * The tf-idf weight vectors.
	 * The hashmap maps a document to another hashmap.
	 * The second hashmap maps a term to its tf-idf weight for this document.
	 */
	private HashMap<String, HashMap<String, Double>> tfIdfWeights;
	
	/**
	 * The constructor.
	 * It will take a corpus of documents.
	 * Using the corpus, it will generate tf-idf vectors for each document.
	 * @param corpus the corpus of documents
	 */
	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new HashMap<String, HashMap<String, Double>>();
		
		createTfIdfWeights();
	}

	/**
	 * This creates the tf-idf vectors.
	 */
	private void createTfIdfWeights() {
//		System.out.println("Creating the tf-idf weight vectors");
		Set<String> terms = corpus.getInvertedIndex().keySet();
		
		for (Document document : corpus.getDocuments()) {
			HashMap<String, Double> weights = new HashMap<String, Double>();
			
			for (String term : terms) {
				double tf = document.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);
				
				double weight = tf * idf;
				
				weights.put(term, weight);
			}
			tfIdfWeights.put(document.getTxt(), weights);
		}
	}
	/**
	 * This creates the tf-idf vectors.
	 */
	private HashMap<String, Double> createQueryTfIdfWeights(String q) {
//		System.out.println("Creating the tf-idf QUERY weight vector");
		
		Document query = new Document(q);
		Set<String> terms = corpus.getInvertedIndex().keySet();
		HashMap<String, Double> weights = new HashMap<String, Double>();

		for (String term : terms) {
			double tf = query.getTermFrequency(term);
			double idf = corpus.getInverseDocumentFrequency(term);
			
			double weight = tf * idf;
			weights.put(term, weight);
		}
		
		return weights;
	}
	
	/**
	 * This method will return the magnitude of a vector.
	 * @param document the document whose magnitude is calculated.
	 * @return the magnitude
	 */
	private double getMagnitude(Document document) {
		double magnitude = 0;
		HashMap<String, Double> weights = tfIdfWeights.get(document.getTxt());
		
		for (double weight : weights.values()) {
			magnitude += weight * weight;
		}
		
		return Math.sqrt(magnitude);
	}
	
	private double getMagnitude(HashMap<String, Double>  weights) {
		double magnitude = 0;
//		HashMap<String, Double> weights = tfIdfWeights.get(document);
		
		for (double weight : weights.values()) {
			magnitude += weight * weight;
		}
		
		return Math.sqrt(magnitude);
	}

	private double getDotProduct(Document d1, Document d2) {
		double product = 0;
		HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
		HashMap<String, Double> weights2 = tfIdfWeights.get(d2);
		
		for (String term : weights1.keySet()) {
			product += weights1.get(term) * weights2.get(term);
		}
		
		return product;
	}

	private double getDotProduct(HashMap<String, Double> wq, HashMap<String, Double> wd) {
		double product = 0;
		
		for (String term : wq.keySet()) {
			product += wq.get(term) * wd.get(term);
		}
		
		return product;
	}
	
	/**
	 * This will return the cosine similarity of two documents.
	 * This will range from 0 (not similar) to 1 (very similar).
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the cosine similarity
	 */
	public double cosineSimilarity(Document d1, Document d2) {
		
		return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
	}

	
	public double cosineSimilarity(String q, Document d) {
		
		HashMap<String, Double> wq = createQueryTfIdfWeights(q);
		
		double a = getDotProduct(wq, tfIdfWeights.get(d.getTxt()));
		double b = (getMagnitude(wq) * getMagnitude(d));
		
//		System.out.println(String.format("-->%f -->%f", a, b));
		
		if (a == 0.0 || b==0.0) return 0.0;
		
		return a / b;
	}
	
}

