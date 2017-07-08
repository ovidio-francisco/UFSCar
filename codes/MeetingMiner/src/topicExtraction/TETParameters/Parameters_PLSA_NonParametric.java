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
@SuppressWarnings({ "serial" })
public class Parameters_PLSA_NonParametric extends Parameters_PLSA_Parametric implements Serializable{
    
    int maxTopics;
    double minThreshold;
    
    public Parameters_PLSA_NonParametric(){
        setNumMaxIterations(100);
        setMinDifference(1.0);
        setMaxTopics(200);
        setMinThreshold(0.1);
    }
    
    public Double getMinThreshold(){
        return this.minThreshold;
    }
    
    public Integer getMaxTopics(){
        return this.maxTopics;
    }
    
    public void setMaxTopics(int maxTopics){
        this.maxTopics = maxTopics;
    }
    
    public void setMinThreshold(double minThershold){
        this.minThreshold = minThershold;
    }
}
