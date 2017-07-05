/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETParameters;

import java.io.Serializable;

/**
 *
 * @author Gaia
 */
public class Parameters_KMeans_Parametric implements Serializable{
    
    int numMaxIterations;
    double percChange; 
    boolean centroidLabel;
    
    public Parameters_KMeans_Parametric(){
        setNumMaxIterations(100);
        setPercChange(1.0);
        setCentroidLabel(true);
    }
    
    public void setCentroidLabel(boolean label){
        this.centroidLabel = label;
    }
    
    public void setPercChange(double pChange){
        this.percChange = pChange;
    }
    
    public void setNumMaxIterations(int numMaxIt){
        this.numMaxIterations = numMaxIt;
    }
    
    public boolean isCentroidLabel(){
        return this.centroidLabel;
    }
    
    public Double getPercChange(){
        return this.percChange;
    } 
    
    public Integer getNumMaxIterations(){
        return this.numMaxIterations;
    }
    
}
