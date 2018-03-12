/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETStructures;

import java.util.ArrayList;

public class DocsPerTopic {
    private ArrayList<DocValue> docs;
    
    public DocsPerTopic(){
        docs = new ArrayList<DocValue>();
    }
    
    public void AddDoc(DocValue docVal){
        docs.add(docVal);
    }
    
    public DocValue getDoc(int index){
        return docs.get(index);
    }
    
    public ArrayList<DocValue> getDocs(){
        return docs;
    }
}

