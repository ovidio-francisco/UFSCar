/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms.Parametric;

import topicExtraction.TETAlgorithms.Proximity;
import topicExtraction.TETAlgorithms.TopicExtractor;
import topicExtraction.TETStructures.NeighborHash;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class KMeans_Parametric extends TopicExtractor{
    
    private int numMaxInteracoes;
    private double minimumDifference;
    private boolean centroidLabeling;
    private NeighborHash[] centroids;
    private NeighborHash[] adjacencyListDocTerm;
    private NeighborHash[] adjacencyListTermDoc;
    
    double[][] docTopic;
    double[][] termTopic;
    
    public KMeans_Parametric(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference, boolean centroidLabeling){
        super(numDocs, numTerms, numTopics);
        setNumMaxIteracoes(numMaxIteracoes);
        setMinimumDifference(minimunDifference);
        setCentroidLabeling(centroidLabeling);
    }
    
    public void buildTopics(Instances data){

        double[] maxValueTerms = new double[getNumTerms()];

        // Criando a estrutura em hash para calcular as similaridades
        adjacencyListDocTerm = new NeighborHash[getNumDocs()];
        for(int doc=0;doc<getNumDocs();doc++){
            adjacencyListDocTerm[doc] = new NeighborHash();
        }
        
        adjacencyListTermDoc = new NeighborHash[getNumTerms()];
        for(int term=0;term<getNumTerms();term++){
            adjacencyListTermDoc[term] = new NeighborHash();
        }
        
        // Normalizando
        double[] totalWeightDoc = new double[getNumDocs()];
        for(int doc=0;doc<getNumDocs();doc++){
            for(int term=1;term<getNumTerms();term++){
                if(data.instance(doc).value(term) > 0){
                    totalWeightDoc[doc] += data.instance(doc).value(term);
                }    
            }
        }

        for(int doc=0;doc<getNumDocs();doc++){
            for(int term=1;term<getNumTerms();term++){
                double value = data.instance(doc).value(term);
                if(value > 0){
                    value = value / totalWeightDoc[doc]; // caso queira normalizar (se não é só comentar)
                    adjacencyListDocTerm[doc].AddNeighbor(term,value); 
                    adjacencyListTermDoc[term].AddNeighbor(doc,value);
                    if(maxValueTerms[term] < value){ // Armazenando o range de valores dos termos para a inicialização dos centroides
                        maxValueTerms[term] = value;
                    }
                }    
            }
        }
        
        // Criando os centroides;
        centroids = new NeighborHash[getNumTopics()];
        for(int topic=0;topic<getNumTopics();topic++){
            centroids[topic] = new NeighborHash();
            for(int term=1;term<getNumTerms();term++){
                double value = Math.random() * maxValueTerms[term];
                centroids[topic].AddNeighbor(term,value);
            }
        }


        docTopic = getDocTopicMatrix();
        termTopic = getTermTopicMatrix();
        
        //Zerando as matrizes
        for(int topic=0;topic<getNumTopics();topic++){
            for(int doc=0;doc<getNumDocs();doc++){
                docTopic[doc][topic] = 0;
            }
            for(int term=0;term<getNumTerms();term++){
                termTopic[term][topic] = 0;
            }
        }
        
        
        ArrayList<Integer>[] docsPerTopic = new ArrayList[getNumTopics()];
        int numIt = 0;
        boolean sair = false;
        
        while(sair == false){
            System.out.println("Iteration: " + (numIt + 1));
            
            double acmDiff = 0;
            
            
            for(int topic=0;topic<getNumTopics();topic++){
                docsPerTopic[topic] = new ArrayList<Integer>();
            }
            
            // Calculando a proximidade de cada documento para cada centroide
            for(int doc=0;doc<getNumDocs();doc++){
                double maxValue = -1;
                int idMaxValue = -1;
                double[] tempTopic = new double[getNumTopics()];
                for(int topic=0;topic<getNumTopics();topic++){
                    double prox = Proximity.calcDistCosseno(adjacencyListDocTerm[doc],centroids[topic]);
                    if(prox > maxValue){
                        maxValue = prox;
                        idMaxValue = topic;
                    }
                }
                
                tempTopic[idMaxValue] = 1;
                docsPerTopic[idMaxValue].add(doc);
                for(int topic=0;topic<getNumTopics();topic++){
                    if(Math.abs(tempTopic[topic] - docTopic[doc][topic]) == 1){
                        acmDiff++;
                    }
                    docTopic[doc][topic] = tempTopic[topic];
                }
            }
            
            acmDiff = (acmDiff / (double)getNumDocs()) * 100;
            
            System.out.println("- % of changes: " + acmDiff);
            
            
            
            // Recalculando os centroides;
            for(int topic=0;topic<getNumTopics();topic++){
                // Frequencia total dos termos dos documentos de um determinado tópico
                double[] avgTerms = new double[getNumTerms()];
                for(int doc=0;doc<docsPerTopic[topic].size();doc++){
                    int idDoc = docsPerTopic[topic].get(doc);
                    HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                    Object[] keys = neighbors.keySet().toArray();
                    for(int term=0;term<keys.length;term++){
                        int idTerm = (int)keys[term];
                        avgTerms[idTerm] += neighbors.get(idTerm);
                    }
                }
                
                //Tirando a média das frequências dos termos por tópico;
                for(int term=1;term<getNumTerms();term++){
                    if(docsPerTopic[topic].size() == 0){
                        avgTerms[term] = 0;
                    }else{
                        avgTerms[term] = (double)avgTerms[term] / (double)docsPerTopic[topic].size();
                    }
                }
                
                //Criando o novo centróide do tópico
                centroids[topic] = new NeighborHash();
                for(int term=1;term<getNumTerms();term++){
                    if(avgTerms[term] > 0){
                        centroids[topic].AddNeighbor(term,avgTerms[term]);
                    }
                }
                
            }
            
            // Calculando a cohesão -- Não interfere no processo de agrupamento -- pode ser comentada
            double cohesion = 0;
            for(int topic=0;topic<getNumTopics();topic++){
                for(int doc=0;doc<docsPerTopic[topic].size();doc++){
                    int idDoc = docsPerTopic[topic].get(doc);
                    double prox = Proximity.calcDistCosseno(adjacencyListDocTerm[idDoc],centroids[topic]);
                    cohesion += prox;
                }
            }
            System.out.println("- Total Cohesion: " + cohesion);
            
            if(acmDiff < getMinDifference()){
                sair = true;
            }
            
            numIt++;
            if(numIt >= getNumMaxIteracoes()){
                sair = true;
            }
        }
        
        
        
        //Calculando o peso de cada termo para cada tópico
        double[] totalTopic = new double[getNumTopics()];
        
        //Pegando os valores mais representativos de cada centroide
        if(isCentroidLabeling()){
            for(int topic=0;topic<getNumTopics();topic++){
                double total = 0;
                HashMap<Integer,Double> neighbors = centroids[topic].getNeighbors();
                Object[] chaves = neighbors.keySet().toArray();
                for(int chave=0;chave<chaves.length;chave++){
                    int index = (int)chaves[chave];
                    double value = neighbors.get(index);
                    termTopic[index][topic] = value;
                    total += value;
                }
                totalTopic[topic] = total;
            }
        }else{
            // Extraindo descritores de acordo com a medida F1
            for(int topic=0;topic<getNumTopics();topic++){
                int hit =0;
                int noise = 0;
                int lost = 0;
                HashMap<Integer,Double> neighborsCentroid = centroids[topic].getNeighbors();
                Object[] chavesCentroid = neighborsCentroid.keySet().toArray();
                for(int chaveCentroid=0;chaveCentroid<chavesCentroid.length;chaveCentroid++){
                    int idTerm = (int)chavesCentroid[chaveCentroid];
                    HashMap<Integer,Double> neighborsTerm = adjacencyListTermDoc[idTerm].getNeighbors();
                    Object[] chavesTerm = neighborsTerm.keySet().toArray();
                    for(int chaveTerm=0;chaveTerm<chavesTerm.length;chaveTerm++){
                        int idDoc = (int)chavesTerm[chaveTerm];
                        if(docTopic[idDoc][topic] == 1){
                            hit++;
                        }else{
                            noise++;
                        }
                    }
                    lost = docsPerTopic[topic].size() - hit;
                    double precision = (double)hit / (double)(hit + noise);
                    double recall = (double)hit / (double)(hit + lost);
                    double f1 = (2 * precision * recall) / (precision + recall);
                    termTopic[idTerm][topic] = f1;
                    //termTopic[idTerm][topic] = recall;
                    //termTopic[idTerm][topic] = f1 * neighborsCentroid.get(idTerm);
                }
            }
            
            //Mutual Information
            /*for(int topic=0;topic<getNumTopics();topic++){
                double pTermTopic = 0;
                double pTerm;
                double pTopic;
                
                pTopic = (double)docsPerTopic[topic].size() / (double)getNumDocs();
                
                HashMap<Integer,Double> neighborsCentroid = centroids[topic].getNeighbors();
                Object[] chavesCentroid = neighborsCentroid.keySet().toArray();
                for(int chaveCentroid=0;chaveCentroid<chavesCentroid.length;chaveCentroid++){
                    int idTerm = (int)chavesCentroid[chaveCentroid];
                    HashMap<Integer,Double> neighborsTerm = adjacencyListTermDoc[idTerm].getNeighbors();
                    Object[] chavesTerm = neighborsTerm.keySet().toArray();
                    pTerm = (double)chavesTerm.length / (double)getNumDocs();
                    double hit = 0;
                    for(int chaveTerm=0;chaveTerm<chavesTerm.length;chaveTerm++){
                        int idDoc = (int)chavesTerm[chaveTerm];
                        if(docTopic[idDoc][topic] == 1){
                            hit++;
                        }
                    }
                    pTermTopic = (double)hit / (double)getNumDocs();
                    double MI = pTermTopic * (Math.log10(pTermTopic / (pTerm * pTopic)) / Math.log10(2));
                    
                    termTopic[idTerm][topic] = MI;
                }
            }*/
        }
        
        
        /*for(int topic=0;topic<getNumTopics();topic++){
            for(int term=1;term<getNumTerms();term++){
                termTopic[term][topic] = termTopic[term][topic] / totalTopic[topic];
            }
        }*/
        
        
        //Calculando o peso de cada documento para cada tópico
        for(int doc=0;doc<getNumDocs();doc++){
            for(int topic=0;topic<getNumTopics();topic++){
                docTopic[doc][topic] = Proximity.calcDistCosseno(adjacencyListDocTerm[doc],centroids[topic]);;
            }
        }
        
        this.setDocTopicMatrix(docTopic);
        this.setTermTopicMatrix(termTopic);
    }
    
    public void setCentroidLabeling(boolean centroidLabeling){
        this.centroidLabeling = centroidLabeling;
    }
    
    public void setNumMaxIteracoes(int numMaxIteracoes){
        this.numMaxInteracoes = numMaxIteracoes;
    }
    
    public void setMinimumDifference(double minDiff){
        this.minimumDifference = minDiff;
    }
    
    public boolean isCentroidLabeling(){
        return this.centroidLabeling;
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
    
    public NeighborHash[] getCentroids(){
        return this.centroids;
    }
    
    public NeighborHash[] getAdjacencyListDocTerm(){
        return this.adjacencyListDocTerm;
    }
    
    public NeighborHash[] getAdjacencyListTermDoc(){
        return this.adjacencyListTermDoc;
    }
    
}
