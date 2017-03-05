/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TETAlgorithms.NonParametric;

import TETAlgorithms.Parametric.KMeans_Parametric;
import TETAlgorithms.TopicExtractorOld;
import TETStructures.NeighborHash;
import java.util.HashMap;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class KMeans_NonParametric extends TopicExtractorOld{
    
    private int numMaxInteracoes;
    private double minimumDifference;
    private boolean centroidLabeling;
    
    private int maxK;
    private int minK;
    private int step;
    
    private int bestValueK;
    
    private boolean movingAverage;
    private int windowSize;
    private double minPercVariation;
    
    public KMeans_NonParametric(int numDocs, int numTerms, int numTopics, int numMaxIteracoes, double minimunDifference, boolean centroidLabeling, int minK, int maxK, int step, boolean movingAverage, int windowSize, double percMinVariation){
        super(numDocs, numTerms, numTopics);
        setNumMaxIteracoes(numMaxIteracoes);
        setMinimumDifference(minimunDifference);
        setCentroidLabeling(centroidLabeling);
        setMinK(minK);
        setMaxK(maxK);
        setStep(step);
        setMovingAverage(true);
        setWindowSize(windowSize);
        setMinPercVariation(percMinVariation);
    }
    
    public void buildTopics(Instances data){
        
        HashMap<Integer,Double> kSilhouette = new HashMap<Integer,Double>();
        
        double previousCohesion = 0;
        double currentCohesion = 0;
        
        double[] movingVector = new double[getWindowSize()];
        
        for(int k=minK;k<=maxK;k=k+step){
            this.setNumTopics(k);
            System.out.println("k Value: " + k);
            KMeans_Parametric kmeans = new KMeans_Parametric(this.getNumDocs(), this.getNumTerms(), this.getNumTopics(), this.getNumMaxIteracoes(), this.getMinDifference(), this.isCentroidLabeling());
            kmeans.buildTopics(data);
            NeighborHash[] centroids = kmeans.getCentroids();
            NeighborHash[] adjacencyListDocTerm = kmeans.getAdjacencyListDocTerm();
            NeighborHash[] adjacencyListTermDoc = kmeans.getAdjacencyListTermDoc();
            double[][] docTopic = kmeans.getDocTopic();
            
            double avgSilhouette = 0;
            double avgCohesion = 0;
            
            for(int doc=0;doc<getNumDocs();doc++){
                //Pegando o grupo ao qual o documento pertence
                int idTopic = -1;
                double maxValue = -30000;
                for(int topic=0;topic<getNumTopics();topic++){
                    if(docTopic[doc][topic] > maxValue){
                        idTopic = topic;
                        maxValue = docTopic[doc][topic];
                    }
                }
                
                double aValue = 1 - docTopic[doc][idTopic]; // Componente a(i) do coeficiente de silhueta
                avgCohesion += docTopic[doc][idTopic];
                //Pegando o grupo mais próximo ao documento atual
                int idSecondTopic = -1;
                double secondMaxValue = -30000;
                for(int topic=0;topic<getNumTopics();topic++){
                    if(topic == idTopic){
                        continue;
                    }
                    if(docTopic[doc][topic] > secondMaxValue){
                        idSecondTopic = topic;
                        secondMaxValue = docTopic[doc][topic];
                    }
                }
                double bValue = 1 - docTopic[doc][idSecondTopic];
                
                double silhouette = (bValue - aValue) / Math.max(aValue, bValue);
                avgSilhouette += silhouette;
            }
            
            avgCohesion = avgCohesion / (double)getNumDocs();
            avgSilhouette = avgSilhouette / (double)getNumDocs();
            System.out.println("[Cohesion;Silhouette]: " + avgCohesion + ";" + avgSilhouette);
            kSilhouette.put(k, avgSilhouette);
            
            if(getMovingAverage()){
                previousCohesion = currentCohesion;
                movingVector = shiftVector(movingVector);
                movingVector[getWindowSize() - 1] = avgCohesion;
                currentCohesion = getMovingAverage(movingVector);
                double variation;
                if(previousCohesion == 0){
                    variation = 10000000;
                }else{
                    variation = Math.abs((currentCohesion/previousCohesion) - 1);
                }
                
                System.out.println("Variation: " + variation);
                if(variation <= getMinPercVariation()){
                    break;
                }    
            }
        }
        
        //Obtendo o melhor valor de K
        Object[] keys = kSilhouette.keySet().toArray();
        int indK = -1;
        double maxValue = -1;
        for(int key=0;key<keys.length;key++){
            int k = (int)keys[key];
            double value = kSilhouette.get(k);
            if(value > maxValue){
                indK = k;
                maxValue = value;
            }
        }
        
        setBestValueK(indK);
        System.out.println("Best Value of K: " + indK);
    }
    
    public double[] shiftVector(double[] oldVector){
        double[] newVector = new double[getWindowSize()];
        for(int pos=1;pos<getWindowSize();pos++){
            newVector[pos - 1] = oldVector[pos];
        }
        return newVector;
    }

    public double getMovingAverage(double[] movingVector){
        double movingAverage = 0;
        for(int pos=0;pos<getWindowSize();pos++){
            movingAverage += movingVector[pos];
        }
        movingAverage = movingAverage / (double) getWindowSize();
        return movingAverage;
    }
    
    public void setMinPercVariation(double minPercVariation){
        this.minPercVariation = minPercVariation;
    }
    
    public void setMovingAverage(boolean movingAverage){
        this.movingAverage = movingAverage;
    }
    
    public void setWindowSize(int windowSize){
        this.windowSize = windowSize;
    }
    
    public void setBestValueK(int bestValueK){
        this.bestValueK = bestValueK;
    }
    
    public void setMaxK(int maxK){
        this.maxK = maxK;
    }
    
    public void setMinK(int minK){
        this.minK = minK;
    }
    
    public void setStep(int step){
        this.step = step;
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
    
    public Integer getBestValueK(){
        return this.bestValueK;
    }
    
    public Integer getNumMaxIteracoes(){
        return this.numMaxInteracoes;
    }
    
    public Double getMinDifference(){
        return this.minimumDifference;
    }
    
    public Integer getMaxK(){
        return this.maxK;
    }
    
    public Integer getMinK(){
        return this.minK;
    }
    
    public Integer getStep(){
        return this.getStep();
    }
    
    public boolean getMovingAverage(){
        return this.movingAverage;
    }
    
    public Integer getWindowSize(){
        return this.windowSize;
    }
    
    public Double getMinPercVariation(){
        return this.minPercVariation;
    }
}
