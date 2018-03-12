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

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		sb.append("=== k-Means Non-Parametric === \n");
	    sb.append(String.format("   Maximum number of iterations:        %d\n"    , numMaxIterations));
	    sb.append(String.format("   Minimum %% of Changes:                %3.3f\n", percChange));
	    sb.append(String.format("   Cluster Labeling:                    %s\n"    , centroidLabel ? "Centroid" : "F1-Measure"));
	    
	    sb.append(String.format("   Maximum value of k:                  %d\n"    , maxValueK));

	    sb.append(String.format("   %% Minimum Variation:                 %3.3f\n", minimumPercVariation));
	    
		
		return sb.toString();
	}
    
    
    
}
