package topicExtraction.TETAlgorithms.Parametric;

import topicExtraction.TETAlgorithms.TopicExtractor;
import topicExtraction.TETStructures.IndexValue;
import topicExtraction.TETStructures.Neighbor;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import weka.core.Instances;

/**
 * Gibbs sampler for estimating the best assignments of topics for words and
 * documents in a corpus. The algorithm is introduced in Tom Griffiths' paper
 * "Gibbs sampling in the generative model of Latent Dirichlet Allocation"
 * (2002).
 * 
 * @author heinrich
 */
public class LDA_Gibbs_Parametric extends TopicExtractor{

    private int numMaxInterations;
    private double alpha;
    private double beta;
    private int burn;
    private int sampleLag;
    
    public LDA_Gibbs_Parametric(int numDocs, int numTerms, int numTopics, int numMaxIterations, int burnPeriod, double alpha, double beta){
        super(numDocs, numTerms, numTopics);
        setAlpha(alpha);
        setBeta(beta);
        setBurn(burnPeriod);
        setNumMaxIterations(numMaxIterations);
    }
    
    public void buildTopics(Instances data){
        Neighbor[] adjListDocTerm = getAdjListDocTerm(data);
        Neighbor[] adjListTermDoc = getAdjListTermDoc(data);

        int numDocs = getNumDocs();
        int numTerms = getNumTerms();
        int numTopics = getNumTopics();
        
        int z[][]; /* topic assignments for each word */
        int[][] nw; /* cwt[i][j] number of instances of word i (term?) assigned to topic j.*/
        int[][] nd; /* na[i][j] number of words in document i assigned to topic j. */
        int[] nwsum; /* nwsum[j] total number of words assigned to topic j.*/
        int[] ndsum; /* nasum[i] total number of words in document i.*/
        
        nw = new int[numTerms][numTopics];
        nd = new int[numDocs][numTopics];
        nwsum = new int[numTopics];
        ndsum = new int[numDocs];

        // The z_i are are initialised to values in [1,K] to determine the
        // initial state of the Markov chain.
        
        z = new int[numDocs][];
        for (int doc=0;doc<numDocs;doc++) {
            ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
            int sizeDoc = neighbors.size();
            z[doc] = new int[sizeDoc];
            for(int term=0;term<sizeDoc;term++){
                int topic = (int) (Math.random() * numTopics);
                z[doc][term] = topic;
                // number of instances of word i assigned to topic j
                nw[neighbors.get(term).index][topic]++;
                // number of words in document i assigned to topic j.
                nd[doc][topic]++;
                // total number of words assigned to topic j.
                nwsum[topic]++;
            }
            // total number of words in document i
            ndsum[doc] = sizeDoc;
        }
        // Fim do estado inicial
        
        
        // init sampler statistics
        double[][] docTopic = getDocTopicMatrix();
        double[][] termTopic = getTermTopicMatrix();
        
        //Zerando as matrizes
        for(int topic=0;topic<getNumTopics();topic++){
            for(int doc=0;doc<getNumDocs();doc++){
                docTopic[doc][topic] = 0;
            }
            for(int term=0;term<getNumTerms();term++){
                termTopic[term][topic] = 0;
            }
        }
        
        int numIt = 0;
        boolean sair = false;
        
        while(sair == false){
            System.out.println("Iteration: " + (numIt + 1));
            // for all z_i
            for (int doc=0;doc<numDocs;doc++){
                ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    int topic = sampleFullConditional(doc, term, neighbors.get(term).index, z, nw, nd, nwsum, ndsum);
                    z[doc][term] = topic;
                }
            }
            
            if(numIt >= getBurn()){
                System.out.println("Aqui -- Começou o armazenamento das estatísticas");
                for (int doc=0;doc<getNumDocs();doc++) {
                    for (int topic=0;topic<getNumTopics();topic++) {
                        docTopic[doc][topic] += (double)(nd[doc][topic] + alpha) / (double)(ndsum[doc] + getNumTopics() * alpha);
                    }
                }
                for (int topic = 0; topic < getNumTopics(); topic++) {
                    for (int term = 1; term < getNumTerms(); term++) {
                        termTopic[term][topic] += (double)(nw[term][topic] + beta) / (double)(nwsum[topic] + getNumTerms() * beta);
                    }
                }
            }
            
            //Análises de critério de parada;
            numIt++;
            if(numIt >= getNumMaxIterations()){
                sair = true;
            }
        }
        
        //Padronizando as matrizes
        System.out.println("Aqui");
        for(int topic=0;topic<numTopics;topic++){
            double total = 0;
            for(int term=1;term<numTerms;term++){
                total += termTopic[term][topic];
            }
            for(int term=1;term<numTerms;term++){
                termTopic[term][topic] = termTopic[term][topic]/total;
            }
        }
        
        for(int doc=0;doc<numDocs;doc++){
            double total = 0;
            for(int topic=0;topic<numTopics;topic++){
                total += docTopic[doc][topic];
            }
            for(int topic=0;topic<numTopics;topic++){
                docTopic[doc][topic] = docTopic[doc][topic] / total;
            }
        }
        
        System.out.println("Fim");
    }
    
    
    private int sampleFullConditional(int doc, int posTerm, int idTerm, int z[][], int[][] nw, int[][] nd, int[] nwsum, int[] ndsum) {
        // remove z_i from the count variables
        int topic = z[doc][posTerm];
        nw[idTerm][topic]--;
        nd[doc][topic]--;
        nwsum[topic]--;
        ndsum[doc]--;

        // do multinomial sampling via cumulative method:
        double[] p = new double[getNumTopics()];
        for (int top=0;top<getNumTopics();top++) {
            p[top] = (nw[idTerm][top] + beta) / (nwsum[top] + getNumTerms() * beta)
                * (nd[doc][top] + alpha) / (ndsum[doc] + getNumTopics() * alpha);
        }
        // cumulate multinomial parameters
        for (int top = 1; top < getNumTopics(); top++) {
            p[top] += p[top - 1];
        }
        // scaled sample because of unnormalised p[]
        double u = Math.random() * p[getNumTopics() - 1];
        for (topic = 0; topic < getNumTopics(); topic++) {
            if (u < p[topic])
                break;
        }

        // add newly estimated z_i to count variables
        nw[idTerm][topic]++;
        nd[doc][topic]++;
        nwsum[topic]++;
        ndsum[doc]++;

        return topic;
    }
    
    /**
     * Add to the statistics the values of theta and phi for the current state.
     */

    
    
    public void setNumMaxIterations(int numMaxIterations){
        this.numMaxInterations = numMaxIterations;
    }
    
    private void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    private void setBeta(double beta){
        this.beta = beta;
    }
    
    private void setBurn(int burn){
        this.burn = burn;
    }
    
    private void setSampleLag(int sampleLag){
        this.sampleLag = sampleLag;
    }
    
    public Integer getNumMaxIterations(){
        return this.numMaxInterations;
    }
    
    private double getAlpha(){
       return this.alpha; 
    }
    
    private double getBeta(){
       return this.beta;
    }
    
    private double getBurn(){
        return this.burn;
    }
    
    private int getSampleLag(){
        return this.sampleLag;
    }
}
