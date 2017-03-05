/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TETAlgorithms;

import TETStructures.IndexValue;
import TETStructures.Neighbor;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public abstract class TopicExtractorOld {
    
    private double[][] docTopic; // Matriz que contém os pesos dos documentos para os tópicos
    private double[][] termTopic; // Matriz que contém os pesos dos termos para os topicos;
    private Integer numTopics; // Número de tópicos
    private Integer numDocs;
    private Integer numTerms;
    
    public TopicExtractorOld(int numDocs, int numTerms, int numTopics){
        setNumTopics(numTopics);
        setNumDocs(numDocs);
        setNumTerms(numTerms);
        
        docTopic = new double[numDocs][numTopics];
        termTopic = new double[numTerms][numTopics];
        
        initializeDocTopic(docTopic);
        initializeTermTopic(termTopic);
        
    }
    
    public abstract void buildTopics(Instances instances);
    
    public void initializeDocTopic(double[][] docTopic){
        /*for(int doc=0;doc<getNumDocs();doc++){
            for(int topic=0;topic<getNumTopics();topic++){
                docTopic[doc][topic] = 0.1;
            }
        }*/
        for(int doc=0;doc<getNumDocs();doc++){
            for(int topic=0;topic<getNumTopics();topic++){
                docTopic[doc][topic] = Math.random();
            }
            double soma=0;
            for(int topic=0;topic<getNumTopics();topic++){
                soma += docTopic[doc][topic];
            }
            for(int topic=0;topic<getNumTopics();topic++){
                docTopic[doc][topic] = docTopic[doc][topic] / soma;
            }
        }
    }
    
    public void initializeTermTopic(double[][] termTopic){
        /*for(int term=0;term<getNumTerms();term++){
            for(int topic=0;topic<getNumTopics();topic++){
                termTopic[term][topic] = 0.1;
            }
        }*/
        for(int term=0;term<getNumTerms();term++){
            for(int topic=0;topic<getNumTopics();topic++){
                termTopic[term][topic] = Math.random();
            }
            double soma=0;
            for(int topic=0;topic<getNumTopics();topic++){
                soma += termTopic[term][topic];
            }
            for(int topic=0;topic<getNumTopics();topic++){
                termTopic[term][topic] = termTopic[term][topic] / soma;
            }
        }
    }
    
    public void setNumTopics(int numTopics){
        this.numTopics = numTopics;
    }
    
    public void setNumDocs(int numDocs){
        this.numDocs = numDocs;
    }
    
    public void setNumTerms(int numTerms){
        this.numTerms = numTerms;
    }
    
    public double[][] getDocTopicMatrix(){
        return this.docTopic;
    }
    
    public double[][] getTermTopicMatrix(){
        return this.termTopic;
    }
    
    public Integer getNumTopics(){
        return this.numTopics;
    }
    
    public Integer getNumDocs(){
        return this.numDocs;
    }
    
    public Integer getNumTerms(){
        return this.numTerms;
    }
    
    public Neighbor[] getAdjListDocTerm(Instances data){
        Neighbor[] adjListDocTerm = new Neighbor[getNumDocs()];
        
        for(int inst=0;inst<getNumDocs();inst++){
            adjListDocTerm[inst] = new Neighbor();
        }
        
        for(int inst=0;inst<getNumDocs();inst++){
            for(int term=1;term<numTerms;term++){
                if(data.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = data.instance(inst).value(term);
                    adjListDocTerm[inst].AddNeighbor(indVal);
                }    
            }
        }
        
        return adjListDocTerm;
    }
    
    public Neighbor[] getAdjListTermDoc(Instances data){
        Neighbor[] adjListTermDoc = new Neighbor[numTerms];
        
        for(int term=0;term<numTerms;term++){
            adjListTermDoc[term] = new Neighbor();
        }
        
        for(int inst=0;inst<getNumDocs();inst++){
            for(int term=1;term<numTerms;term++){
                if(data.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = inst;
                    indVal.value = data.instance(inst).value(term);
                    adjListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        
        return adjListTermDoc;
    }
    
}
