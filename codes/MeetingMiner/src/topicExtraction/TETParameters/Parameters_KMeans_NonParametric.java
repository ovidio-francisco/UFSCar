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

	@Override
	public String toString() {
//	    private int minValueK;
//	    private int maxValueK;
//	    private int stepSize;
//	    
//	    private boolean movingAverage;
//	    private int windowSize;
//	    private double minimumPercVariation;

		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("=== k-Means Non-Parametric === \n");
	    sb.append(String.format("   Maximum number of iterations:        %d\n"    , numMaxIterations));
	    sb.append(String.format("   Minimum %% of Changes:                %3.3f\n", percChange));
	    sb.append(String.format("   Cluster Labeling:                    %s\n"    , centroidLabel ? "Centroid" : "F1-Measure"));
	    
	    sb.append(String.format("   Maximum value of k:                  %d\n"    , maxValueK));
	    sb.append(String.format("   Minimum Value of k:                  %d\n"    , minValueK));
	    sb.append(String.format("   Step size:                           %d\n"    , stepSize));

	    sb.append(String.format("   Using moving average:                %b\n"    , movingAverage));
	    sb.append(String.format("   Window Size:                         %d\n"    , windowSize));
	    sb.append(String.format("   %% Minimum Variation:                 %3.3f\n", minimumPercVariation));
	    
		
		return sb.toString();
	}
    
    
    
}
