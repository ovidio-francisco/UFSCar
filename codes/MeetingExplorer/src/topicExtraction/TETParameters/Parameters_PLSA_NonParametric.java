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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("=== PLSA Non-Parametric === \n");
	    sb.append(String.format("   Maximum number of iterations:        %d\n"    , numMaxIterations));
	    sb.append(String.format("   Minimum difference of log liklihood: %3.3f\n" , minDifference));	    
	    sb.append(String.format("   Maximum number of topics:            %d\n"    , maxTopics));
	    sb.append(String.format("   Probability threshold:               %3.3f\n" , minThreshold));
	    
	    
		
		return sb.toString();
	}
    
    
    
}
