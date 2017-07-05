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
public class Parameters_BisectingKMeans_NonParametric extends Parameters_KMeans_Parametric implements Serializable{
    
    private int maxValueK;
    private double minimumPercVariation;
    
    public Parameters_BisectingKMeans_NonParametric(){
        setNumMaxIterations(100);
        setPercChange(1.0);
        setCentroidLabel(true);
        
        setMaxValueK(200);
        setMinimumPercVariation(0.01);
    }
    
    public Double getMinPercVariation(){
        return this.minimumPercVariation;
    }
    
    public Integer getMaxValueK(){
        return this.maxValueK;
    }
    
    public void setMaxValueK(int maxValueK){
        this.maxValueK = maxValueK;
    }
    
    public void setMinimumPercVariation(double minimumPercVariation){
        this.minimumPercVariation = minimumPercVariation;
    }
    
}
