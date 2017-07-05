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
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class Bisecting_KMeans_Parametric extends TopicExtractor {
    
    private int numMaxInteracoes;
    private double minimumDifference;
    private boolean centroidLabeling;
    //private NeighborHash[] centroids;
    private ArrayList<NeighborHash> centroids;
    private NeighborHash[] adjacencyListDocTerm;
    private NeighborHash[] adjacencyListTermDoc;
    
    double[][] docTopic;
    double[][] termTopic;
    
    public Bisecting_KMeans_Parametric(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference, boolean centroidLabeling){
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
                    /*if(maxValueTerms[term] < value){ // Armazenando o range de valores dos termos para a inicialização dos centroides
                        maxValueTerms[term] = value;
                    }*/
                }    
            }
        }

        int currentNumTopics = 1;
        
        // Definindo todos os elementos como pertencentes à um único grupo
        centroids = new ArrayList<NeighborHash>();
        centroids.add(new NeighborHash());
        for(int term=1;term<getNumTerms();term++){
            double value = Math.random();
            centroids.get(0).AddNeighbor(term,value);
        }
        ArrayList<ArrayList<Integer>> docsPerTopic = new ArrayList<ArrayList<Integer>>();
        docsPerTopic.add(new ArrayList<Integer>());
        for(int doc=0;doc<getNumDocs();doc++){
            docsPerTopic.get(0).add(doc);
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
        
        
        
        boolean sair1 = false;
        while(sair1 == false){
            
            System.out.println("Current Number of Topics: " + centroids.size());
            
            System.out.println("Computing group to be divide..");
            int idTopicDivision = findIDTopicDivision(docsPerTopic);
            System.out.println("Group to be divided: " + idTopicDivision);
            
            System.out.println("Computing centroids...");
            NeighborHash centroid1 = new NeighborHash();
            NeighborHash centroid2 = new NeighborHash();
            centroids.add(centroid1);
            centroids.add(centroid2);
            
            int topicIni = centroids.size() - 2;
            int topicFim = centroids.size();
            for(int topic=topicIni;topic<topicFim;topic++){
                docsPerTopic.add(new ArrayList<Integer>());
            }
            
            Random random = new Random();
            for(int doc=0;doc<docsPerTopic.get(idTopicDivision).size();doc++){
                int idDoc = docsPerTopic.get(idTopicDivision).get(doc);
                int idTopic = random.nextInt(2);
                if(idTopic == 0){
                    docsPerTopic.get(topicIni).add(idDoc);
                    /*double numDocsCentroid = docsPerTopic.get(topicIni).size();
                    HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                    Object[] keys = neighbors.keySet().toArray();
                    for(int term=0;term<keys.length;term++){
                        int idTerm = (int)keys[term];
                        double freqTerm = adjacencyListDocTerm[idDoc].getNeighbor(idTerm);
                        if(centroid1.getNeighbors().containsKey(idTerm)){
                            double value = centroid1.getNeighbor(idTerm);
                            value = (((numDocsCentroid - 1) * value) + freqTerm) / (double)numDocsCentroid;
                            centroid1.getNeighbors().remove(idTerm);
                            centroid1.getNeighbors().put(idTerm, value);
                        }else{
                            centroid1.getNeighbors().put(idTerm, freqTerm);
                        }
                    }*/
                }else{
                    docsPerTopic.get(topicFim-1).add(idDoc);
                    double numDocsCentroid = docsPerTopic.get(topicFim-1).size();
                    HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                    Object[] keys = neighbors.keySet().toArray();
                    /*for(int term=0;term<keys.length;term++){
                        int idTerm = (int)keys[term];
                        double freqTerm = adjacencyListDocTerm[idDoc].getNeighbor(idTerm);
                        if(centroid2.getNeighbors().containsKey(idTerm)){
                            double value = centroid2.getNeighbor(idTerm);
                            value = (((numDocsCentroid - 1) * value) + freqTerm) / (double)numDocsCentroid;
                            centroid2.getNeighbors().remove(idTerm);
                            centroid2.getNeighbors().put(idTerm, value);
                        }else{
                            centroid2.getNeighbors().put(idTerm, freqTerm);
                        }
                    }*/
                }
            }    

            
            // Substituir isso pela média incremental
            for(int topic=topicIni;topic<topicFim;topic++){
                // Frequencia total dos termos dos documentos de um determinado tópico
                double[] avgTerms = new double[getNumTerms()];
                for(int doc=0;doc<docsPerTopic.get(topic).size();doc++){
                    int idDoc = docsPerTopic.get(topic).get(doc);
                    HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                    Object[] keys = neighbors.keySet().toArray();
                    for(int term=0;term<keys.length;term++){
                        int idTerm = (int)keys[term];
                        avgTerms[idTerm] += neighbors.get(idTerm);
                    }
                }

                //Tirando a média das frequências dos termos por tópico;
                for(int term=1;term<getNumTerms();term++){
                    if(docsPerTopic.get(topic).size() == 0){
                        avgTerms[term] = 0;
                    }else{
                        avgTerms[term] = (double)avgTerms[term] / (double)docsPerTopic.get(topic).size();
                    }
                }

                //Criando o novo centróide do tópico
                NeighborHash newCentroid =  new NeighborHash();
                for(int term=1;term<getNumTerms();term++){
                    if(avgTerms[term] > 0){
                        newCentroid.AddNeighbor(term,avgTerms[term]);
                    }
                }
                centroids.set(topic, newCentroid);
            }
            
            System.out.println("Centroids computed...");
            
            double acmDiff = 0;

            
            
            int numIt = 0;
            boolean sair2 = false;
            
            double[][] tempDocTopic = new double[getNumDocs()][2];
            
            while(sair2 == false){
                System.out.println("Iteration: " + (numIt + 1));
                System.out.println("Calculando a distância entre os objetos e os centroides...");
                //Zerando o DocsPerTopic a cada Iteração
                for(int topic=topicIni;topic<topicFim;topic++){
                    docsPerTopic.set(topic, new ArrayList<Integer>());
                }
                
                for(int doc=0;doc<docsPerTopic.get(idTopicDivision).size();doc++){
                    int idDoc = docsPerTopic.get(idTopicDivision).get(doc);
                    
                    double maxValue = -1;
                    int idMaxValue = -1;
                    double[] tempTopic = new double[2];
                    for(int topic=topicIni;topic<topicFim;topic++){
                        double prox = Proximity.calcDistCosseno(adjacencyListDocTerm[idDoc],centroids.get(topic));
                        if(prox > maxValue){
                            maxValue = prox;
                            idMaxValue = topic;
                        }
                    }

                    tempTopic[idMaxValue - topicIni] = 1;
                    docsPerTopic.get(idMaxValue).add(idDoc);
                    for(int topic=topicIni;topic<topicFim;topic++){
                        if(Math.abs(tempTopic[topic - topicIni] - tempDocTopic[idDoc][topic - topicIni]) == 1){
                            acmDiff++;
                        }
                        tempDocTopic[idDoc][topic - topicIni] = tempTopic[topic - topicIni];
                    }
                    
                    
                    /*double numDocsCentroid = docsPerTopic.get(idMaxValue).size();
                    HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                    Object[] keys = neighbors.keySet().toArray();
                    for(int term=0;term<keys.length;term++){
                        int idTerm = (int)keys[term];
                        double freqTerm = adjacencyListDocTerm[idDoc].getNeighbor(idTerm);
                        if(centroids.get(idMaxValue).getNeighbors().containsKey(idTerm)){
                            double value = centroids.get(idMaxValue).getNeighbor(idTerm);
                            value = (((numDocsCentroid - 1) * value) + freqTerm) / (double)numDocsCentroid;
                            centroids.get(idMaxValue).getNeighbors().remove(idTerm);
                            centroids.get(idMaxValue).getNeighbors().put(idTerm, value);
                        }else{
                            centroids.get(idMaxValue).getNeighbors().put(idTerm, freqTerm);
                        }
                    }*/
                    
                }

                acmDiff = (acmDiff / (double)getNumDocs()) * 100;

                System.out.println("- % of changes: " + acmDiff);
                 
                System.out.println("Recalculando os centroides...");
                 // Recalculando os centroides;
                for(int topic=topicIni;topic<topicFim;topic++){
                    // Frequencia total dos termos dos documentos de um determinado tópico
                    double[] avgTerms = new double[getNumTerms()];
                    for(int doc=0;doc<docsPerTopic.get(topic).size();doc++){
                        int idDoc = docsPerTopic.get(topic).get(doc);
                        HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                        Object[] keys = neighbors.keySet().toArray();
                        for(int term=0;term<keys.length;term++){
                            int idTerm = (int)keys[term];
                            avgTerms[idTerm] += neighbors.get(idTerm);
                        }
                    }

                    //Tirando a média das frequências dos termos por tópico;
                    for(int term=1;term<getNumTerms();term++){
                        if(docsPerTopic.get(topic).size() == 0){
                            avgTerms[term] = 0;
                        }else{
                            avgTerms[term] = (double)avgTerms[term] / (double)docsPerTopic.get(topic).size();
                        }
                    }

                    //Criando o novo centróide do tópico
                    NeighborHash newCentroid =  new NeighborHash();
                    for(int term=1;term<getNumTerms();term++){
                        if(avgTerms[term] > 0){
                            newCentroid.AddNeighbor(term,avgTerms[term]);
                        }
                    }
                    centroids.set(topic, newCentroid);
                }
                
                System.out.println("Calculando a coesão...");
                // Calculando a cohesão -- Não interfere no processo de agrupamento -- pode ser comentada
                double cohesion = 0;
                for(int topic=topicIni;topic<topicFim;topic++){
                    for(int doc=0;doc<docsPerTopic.get(topic).size();doc++){
                        int idDoc = docsPerTopic.get(topic).get(doc);
                        double prox = Proximity.calcDistCosseno(adjacencyListDocTerm[idDoc],centroids.get(topic));
                        cohesion += prox;
                    }
                }
                System.out.println("- Total Cohesion: " + cohesion);

                
                
                if(acmDiff < getMinDifference()){
                    sair2 = true;
                }

                if(numIt >= getNumMaxIteracoes()){
                    sair2 = true;
                }
                
                numIt++;
                acmDiff = 0;
            }

            if(centroids.size() > getNumTopics()){
                sair1 = true;
            }
            
            //Tem que remover o docsPerTopic antigo depois
            docsPerTopic.remove(idTopicDivision);
            centroids.remove(idTopicDivision);
        }
        
        
        
        
        //Calculando o peso de cada termo para cada tópico
        double[] totalTopic = new double[getNumTopics()];
        
        for(int topic=0;topic<getNumTopics();topic++){
                double total = 0;
                HashMap<Integer,Double> neighbors = centroids.get(topic).getNeighbors();
                Object[] chaves = neighbors.keySet().toArray();
                for(int chave=0;chave<chaves.length;chave++){
                    int index = (int)chaves[chave];
                    double value = neighbors.get(index);
                    termTopic[index][topic] = value;
                    total += value;
                }
                totalTopic[topic] = total;
            }
        
        //Pegando os valores mais representativos de cada centroide
        if(isCentroidLabeling()){
            for(int topic=0;topic<getNumTopics();topic++){
                double total = 0;
                HashMap<Integer,Double> neighbors = centroids.get(topic).getNeighbors();
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
                for(int doc=0;doc<docsPerTopic.get(topic).size();doc++){
                    int idDoc = docsPerTopic.get(topic).get(doc);
                    docTopic[idDoc][topic] = 1; 
                }
                
                int hit =0;
                int noise = 0;
                int lost = 0;
                HashMap<Integer,Double> neighborsCentroid = centroids.get(topic).getNeighbors();
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
                    lost = docsPerTopic.get(topic).size() - hit;
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
                
        for(int topic=0;topic<getNumTopics();topic++){
            for(int term=1;term<getNumTerms();term++){
                termTopic[term][topic] = termTopic[term][topic] / totalTopic[topic];
            }
        }
        
        //Calculando o peso de cada documento para cada tópico
        for(int doc=0;doc<getNumDocs();doc++){
            for(int topic=0;topic<getNumTopics();topic++){
                docTopic[doc][topic] = Proximity.calcDistCosseno(adjacencyListDocTerm[doc],centroids.get(topic));
            }
        }
        
        this.setDocTopicMatrix(docTopic);
        this.setTermTopicMatrix(termTopic);
    }
    
    public int findIDTopicDivision(ArrayList<ArrayList<Integer>> docsPerTopic){
        int idTopic = -1;
        int currentNumTopics = docsPerTopic.size();
        
        double maxSSE = -1;
        
        for(int topic=0;topic<currentNumTopics;topic++){
            double SSE = 0;
            for(int doc=0;doc<docsPerTopic.get(topic).size();doc++){
                int idDoc = docsPerTopic.get(topic).get(doc);
                double dist = 1 - Proximity.calcDistCosseno(adjacencyListDocTerm[idDoc],centroids.get(topic));
                SSE += dist * dist;
            }
            //SSE = SSE / (double)docsPerTopic.get(topic).size();
            if(SSE > maxSSE){
                maxSSE = SSE;
                idTopic = topic;
            }
        }
        return idTopic;
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
    
    public ArrayList<NeighborHash> getCentroids(){
        return this.centroids;
    }
    
    public NeighborHash[] getAdjacencyListDocTerm(){
        return this.adjacencyListDocTerm;
    }
    
    public NeighborHash[] getAdjacencyListTermDoc(){
        return this.adjacencyListTermDoc;
    }
    
}
