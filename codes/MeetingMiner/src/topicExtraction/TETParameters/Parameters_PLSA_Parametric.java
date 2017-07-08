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
@SuppressWarnings("serial")
public class Parameters_PLSA_Parametric  implements Serializable{
    
    int numMaxIterations;
    double minDifference;
    
    public Parameters_PLSA_Parametric(){
        setNumMaxIterations(100);
        setMinDifference(1.0);
    }
    
    public Parameters_PLSA_Parametric(int numIt, double minDiff){
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
