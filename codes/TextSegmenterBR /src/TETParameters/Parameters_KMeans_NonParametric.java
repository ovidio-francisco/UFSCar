/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TETParameters;

import java.io.Serializable;

/**
 *
 * @author Gaia
 */
public class Parameters_KMeans_NonParametric extends Parameters_KMeans_Parametric implements Serializable{
    
    private int minValueK;
    private int maxValueK;
    private int stepSize;
    
    private boolean movingAverage;
    private int windowSize;
    private double minimumPercVariation;
    
    public Parameters_KMeans_NonParametric(){
        setNumMaxIterations(100);
        setPercChange(1.0);
        setCentroidLabel(true);
        
        setMinValueK(10);
        setMaxValueK(200);
        setStepSize(5);
        
        setMovingAverage(true);
        setWindowSize(5);
        setMinimumPercVariation(1.0);
    }
    
    public boolean isMovingAverage(){
        return this.movingAverage;
    }
    
    public double minimumPercVariation(){
        return this.minimumPercVariation;
    }
    
    public Double getMinPercVariation(){
        return this.minimumPercVariation;
    }
    
    public Integer getWindowSize(){
        return this.windowSize;
    }
    
    public Integer getMinValueK(){
        return this.minValueK;
    }
    
    public Integer getMaxValueK(){
        return this.maxValueK;
    }
    
    public Integer getStepSize(){
        return this.stepSize;
    }
    
    public void setMinValueK(int minValueK){
        this.minValueK = minValueK;
    }
    
    public void setMaxValueK(int maxValueK){
        this.maxValueK = maxValueK;
    }
    
    public void setStepSize(int stepSize){
        this.stepSize = stepSize;
    }
    
    public void setMovingAverage(boolean movingAverage){
        this.movingAverage = movingAverage;
    }
    
    public void setWindowSize(int windowSize){
        this.windowSize = windowSize;
    }
    
    public void setMinimumPercVariation(double minimumPercVariation){
        this.minimumPercVariation = minimumPercVariation;
    }
}
