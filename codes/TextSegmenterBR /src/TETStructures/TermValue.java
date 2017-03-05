//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TETStructures;

public class TermValue {
    private String term;
    private Double frequency;
    
    public TermValue(){
        setTerm("");
        setFrequency(0d);
    }
    
    public TermValue(String term, Double freq){
        setTerm(term);
        setFrequency(freq);
    }
    
    public void setTerm(String term){
        this.term = term;
    }
    
    public void setFrequency(Double frequency){
        this.frequency = frequency;
    }
    
    public String getFeature(){
        return term;
    }
    
    public Double getFrequency(){
        return frequency;
    }
}
