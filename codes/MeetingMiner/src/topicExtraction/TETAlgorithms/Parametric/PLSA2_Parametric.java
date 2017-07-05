/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms.Parametric;

import topicExtraction.TETAlgorithms.TopicExtractor;
import topicExtraction.TETStructures.IndexValue;
import topicExtraction.TETStructures.Neighbor;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class PLSA2_Parametric extends TopicExtractor{
    
    private int numMaxInteracoes;
    private double minimumDifference;
    
    double[][] docTopic;
    double[][] termTopic;
    
    double[] probTopic;
    
    public PLSA2_Parametric(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference){
        super(numDocs, numTerms, numTopics);
        setNumMaxIteracoes(numMaxIteracoes);
        setMinimumDifference(minimunDifference);
    }
    
    public void buildTopics(Instances data){
        
        Neighbor[] adjListDocTerm = getAdjListDocTerm(data);
        Neighbor[] adjListTermDoc = getAdjListTermDoc(data);
        
        //crindo um mapeamente para determinar rapidamente a posição de um determinado termo na lista de adjacências ajdListDocTerm
        HashMap<String,Integer> posTermDoc = new HashMap<String,Integer>();
        for(int doc=0;doc<getNumDocs();doc++){
            ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                String key = doc + "," + neighbors.get(term).index;
                posTermDoc.put(key, term);
            }
        }
        
        probTopic = new double[getNumTopics()];
        //Inicializando probTopic
        for(int topic=0;topic<getNumTopics();topic++){
            probTopic[topic] = 1.0d / (double)getNumTopics();
        }
        
        double[][][] probTopicDocTerm = new double[getNumTopics()][getNumDocs()][];
        for(int topic=0;topic<getNumTopics();topic++){
            for(int doc=0;doc<getNumDocs();doc++){
                ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
                probTopicDocTerm[topic][doc] = new double[neighbors.size()];
            }
        }
        
        double[][] DocTopic = getDocTopicMatrix();
        double[][] TermTopic = getTermTopicMatrix();
        
        int numDocs = getNumDocs();
        int numTerms = getNumTerms();
        int numTopics = getNumTopics();
        
        double[][] docTopicsTemp = new double[numDocs][numTopics];
        //double[][] termTopicsTemp = new double[numDocs][numTopics];
        
        int numIt = 0; // armazena o número de iterações
        boolean sair = false;
        
        double previousLog = 0;
        double currentLog = 0;
        
        while(sair == false){
            System.out.println("Iteração " + numIt + "---");
            
            System.out.println("- E-Step");
            EStep(probTopic, DocTopic, TermTopic, probTopicDocTerm, adjListDocTerm);
            
            System.out.println("- M-Step");
            MStep(probTopic, DocTopic, TermTopic, probTopicDocTerm, adjListTermDoc, adjListDocTerm, posTermDoc);
            
            double logLikelihood = getLogLikelihood(probTopic, DocTopic, TermTopic, adjListDocTerm);
            System.out.println("---LogLikelihood: " + logLikelihood);
            
            if(numIt > 0){
                currentLog = logLikelihood;
                System.out.println("Difference: " + Math.abs(currentLog - previousLog));
                if(Math.abs(currentLog - previousLog) < this.getMinDifference()){
                    sair = true;
                }
                previousLog = currentLog;
            }
            
            
            numIt++;
            if(numIt >= getNumMaxIteracoes()){
                sair = true;
            }
        }

    }
    
    private void EStep(double[] probTopic, double[][] probDocTopic, double[][] probTermTopic, double[][][] probTopicDocTerm, Neighbor[] adjListDocTerm){
        for(int doc=0;doc<getNumDocs();doc++){
            ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                double norm=0;
                for(int topic=0;topic<getNumTopics();topic++){
                    double valor = probTopic[topic] * probDocTopic[doc][topic] * probTermTopic[neighbors.get(term).index][topic];
                    probTopicDocTerm[topic][doc][term] = valor;
                    norm += valor;
                }
                for(int topic=0;topic<getNumTopics();topic++){
                    probTopicDocTerm[topic][doc][term] /= norm;
                }
            }
        }
    }
    
    private void MStep(double[] probTopic, double[][] probDocTopic, double[][] probTermTopic, double[][][] probTopicDocTerm, Neighbor[] adjListTermDoc, Neighbor[] adjListDocTerm, HashMap<String,Integer> posTermDoc){
        //p(w|z) => probTermTopic
        System.out.println("--P(term|topic)");
        for(int topic=0;topic<getNumTopics();topic++){
            double norm = 0;
            for(int term=1;term<getNumTerms();term++){
                double sum=0;
                ArrayList<IndexValue> neighbors = adjListTermDoc[term].getNeighbors();
                for(int doc=0;doc<neighbors.size();doc++){
                    int positionDoc = posTermDoc.get(neighbors.get(doc).index + "," + term);
                    //int positionDoc = getPositionDoc(term,adjListDocTerm[neighbors.get(doc).index].getNeighbors());
                    sum += neighbors.get(doc).value * probTopicDocTerm[topic][neighbors.get(doc).index][positionDoc];
                }
                probTermTopic[term][topic] = sum;
                norm += sum;
            }
            for(int term=1;term<getNumTerms();term++){
                probTermTopic[term][topic] /= norm;
            }
        }
        
        //p(d|z) => probDocTopic
        System.out.println("--P(document|topic)");
        for(int topic=0;topic<getNumTopics();topic++){
            double norm = 0;
            for(int doc=0;doc<getNumDocs();doc++){
                double sum = 0;
                ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    sum += neighbors.get(term).value * probTopicDocTerm[topic][doc][term];
                }
                probDocTopic[doc][topic] = sum;
                norm += sum;
            }
            for(int doc=0;doc<getNumDocs();doc++){
                probDocTopic[doc][topic] /= norm;
            }
        }
        
        //p(z) => probTopic
        System.out.println("--P(topic)");
        double norm=0;
        for(int topic=0;topic<getNumTopics();topic++){
            double sum=0;
            for(int doc=0;doc<getNumDocs();doc++){
                sum += probDocTopic[doc][topic];
            }
            probTopic[topic] = sum;
            norm += sum;
        }
        for(int topic=0;topic<getNumTopics();topic++){
            probTopic[topic] /= norm;
        }
    }
    
    private int getPositionDoc(int idTerm, ArrayList<IndexValue> neighbors){
        int ind = -1;
        for(int term=0;term<neighbors.size();term++){
            IndexValue idVal = neighbors.get(term);
            if(idVal.index == idTerm){
                return term;
            }
        }
        return ind;
    }
    
    private double getLogLikelihood(double[] probTopic, double[][] probDocTopic, double[][] probTermTopic, Neighbor[] adjListDocTerm){
        double logL = 0;
        for(int doc=0;doc<getNumDocs();doc++){
            ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                double sum=0;
                for(int topic=0;topic<getNumTopics();topic++){
                    sum += probTopic[topic] * probDocTopic[doc][topic] * probTermTopic[neighbors.get(term).index][topic];
                }
                logL += neighbors.get(term).value * Math.log(sum);
            }
        }
        return logL;
    }
    
    public void setNumMaxIteracoes(int numMaxIteracoes){
        this.numMaxInteracoes = numMaxIteracoes;
    }
    
    public void setMinimumDifference(double minDiff){
        this.minimumDifference = minDiff;
    }
    
    public Integer getNumMaxIteracoes(){
        return this.numMaxInteracoes;
    }
    
    public Double getMinDifference(){
        return this.minimumDifference;
    }
    
    public double[][] getDocTopic(){
        return this.docTopic;
    }
    
    public double[] getProbTopic(){
        return this.probTopic;
    }
}
