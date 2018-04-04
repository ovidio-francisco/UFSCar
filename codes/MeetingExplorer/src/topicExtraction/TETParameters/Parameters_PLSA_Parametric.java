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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("=== PLSA Parametric === \n");
	    sb.append(String.format("   Maximum number of iterations:        %d\n"    , numMaxIterations));
	    sb.append(String.format("   Minimum difference of log liklihood: %3.3f\n" , minDifference));
		
		return sb.toString();
	}
    
    
    
}
