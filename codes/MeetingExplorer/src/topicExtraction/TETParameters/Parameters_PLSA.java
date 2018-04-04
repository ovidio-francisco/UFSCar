/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETParameters;

/**
 *
 * @author Gaia
 */
public class Parameters_PLSA {
    
    int numMaxIterations;
    double minDifference;
    
    public Parameters_PLSA(){
        setNumMaxIterations(100);
        setMinDifference(1.0);
    }
    
    public Parameters_PLSA(int numIt, double minDiff){
        setNumMaxIterations(numIt);
        setMinDifference(minDiff);
    }
    
    public void setNumMaxIterations(int numIt){
        this.numMaxIterations = numIt;
    }
    
    public void setMinDifference(double minDiff){
        this.minDifference = minDiff;
    }
    
    public Integer getNumMaxIterations(){
        return this.numMaxIterations;
    }
    
    public Double getMinDifference(){
        return this.minDifference;
    }
    
}
