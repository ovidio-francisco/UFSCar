//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: October 20, 2015
// Description: 
//*****************************************************************************  

package topicExtraction.TETStructures;

public class DocValue {
    private String doc;
    private Double value;
    
    public DocValue(){
        setPathDoc("");
        setValue(0d);
    }
    
    public DocValue(String doc, Double value){
        setPathDoc(doc);
        setValue(value);
    }
    
    public void setPathDoc(String doc){
        this.doc = doc;
    }
    
    public void setValue(Double value){
        this.value = value;
    }
    
    public String getPathDoc(){
        return doc;
    }
    
    public Double getValue(){
        return value;
    }
}
