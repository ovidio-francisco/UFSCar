/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms.NonParametric;

import topicExtraction.TETAlgorithms.TopicExtractor;
import topicExtraction.TETAlgorithms.Parametric.PLSA2_Parametric;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
@SuppressWarnings("unused")
public class PLSA_NonParametric extends TopicExtractor {
    
    private int numMaxInteracoes;
    private double minimumDifference;
    private int maxTopics;
    private double minThreshold;
    
    public PLSA_NonParametric(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference, int numMaxTopics, double minThreshold){
        super(numDocs, numTerms, numTopics);
        setNumMaxIteracoes(numMaxIteracoes);
        setMinimumDifference(minimunDifference);
        setMaxTopics(numMaxTopics);
        setMinThreshold(minThreshold);
    }
    
    public void buildTopics(Instances data){
        PLSA2_Parametric plsa = new PLSA2_Parametric(getNumDocs(), getNumTerms(), getNumTopics(), getNumMaxIteracoes(), getMinimumDifference());
        plsa.buildTopics(data);
        
        //double[][] docTopic = plsa.getDocTopic();
        
        double[] probTopic = plsa.getProbTopic();
        
        
        /*double sumProb=0;
        for(int topic=0;topic<getNumTopics();topic++){
            double sumTopic=0;
            for(int doc=0;doc<getNumTopics();doc++){
                sumTopic += docTopic[doc][topic];
            }
            probTopic[topic] = sumTopic;
            sumProb += sumTopic;
        }*/
        
//        System.out.println("teste");
        /*for(int topic=0;topic<getNumTopics();topic++){
            probTopic[topic] = probTopic[topic] / sumProb;
        }*/
        
        
    }
    
    public Integer getNumMaxIteracoes(){
        return this.numMaxInteracoes;
    }
    
    public Double getMinimumDifference(){
        return this.minimumDifference;
    }
    
    public Integer getMaxTopics(){
        return this.maxTopics;
    }
    
    public Double getMinThreshold(){
        return this.minThreshold;
    }
    
    public void setNumMaxIteracoes(int numMaxIteracoes){
        this.numMaxInteracoes = numMaxIteracoes;
    }
    
    public void setMinimumDifference(double minimumDifference){
        this.minimumDifference = minimumDifference;
    }
    
    public void setMaxTopics(int maxTopics){
        this.maxTopics = maxTopics;
    }
    
    public void setMinThreshold(double minThreshold){
        this.minThreshold = minThreshold;
    }
}
