/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;

import topicExtraction.TETStructures.IndexValue;
import topicExtraction.TETStructures.Neighbor;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
@SuppressWarnings("unused")
public class PLSA2 extends TopicExtractorOld{
    
    private int numMaxInteracoes;
    private double minimumDifference;
    
    public PLSA2(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference){
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
        
        double[] probTopic = new double[getNumTopics()];
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
        
        double[][] probDocTopic = getDocTopicMatrix();
        double[][] probTermTopic = getTermTopicMatrix();
        
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
//            M4MShowStatus.setMessage("===========================>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            
//            M4MShowStatus.setMessage("Iteração " + numIt + "---");
            
//            M4MShowStatus.setMessage("- E-Step");
            EStep(probTopic, probDocTopic, probTermTopic, probTopicDocTerm, adjListDocTerm);
            
//            M4MShowStatus.setMessage("- M-Step");
            MStep(probTopic, probDocTopic, probTermTopic, probTopicDocTerm, adjListTermDoc, adjListDocTerm, posTermDoc);
            
            double logLikelihood = getLogLikelihood(probTopic, probDocTopic, probTermTopic, adjListDocTerm);
//            M4MShowStatus.setMessage("---LogLikelihood: " + logLikelihood);
            
            if(numIt > 0){
                currentLog = logLikelihood;
//                M4MShowStatus.setMessage("Difference: " + Math.abs(currentLog - previousLog));
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
        
//        M4MShowStatus.setMessage("--P(term|topic)");
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
//        M4MShowStatus.setMessage("--P(document|topic)");
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
//        M4MShowStatus.setMessage("--P(topic)");
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
}
