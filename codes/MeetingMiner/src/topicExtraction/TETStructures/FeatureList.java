//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package topicExtraction.TETStructures;

import java.util.ArrayList;

public class FeatureList {
    private ArrayList<TermValue> features;

    public void FeatureList(){
        features = new ArrayList<TermValue>();
    }
    
    public void setFeatures(ArrayList<TermValue> features){
        this.features = features;
    }
    
    public ArrayList<TermValue> getFeatures(){
        return features;
    }
    
    public TermValue getFeature(int pos){
        return features.get(pos);
    }
}
