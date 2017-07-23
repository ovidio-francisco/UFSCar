/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms.Parametric;

import java.util.ArrayList;

import topicExtraction.TETAlgorithms.TopicExtractorOld;
import topicExtraction.TETStructures.IndexValue;
import topicExtraction.TETStructures.Neighbor;
import utils.ShowStatus;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class PLSA_Parametric extends TopicExtractorOld{
    
    private int numMaxInterations;
    
    public PLSA_Parametric(int numDocs, int numTerms, int numTopics, int numMaxIterations){
        super(numDocs, numTerms, numTopics);
        setNumMaxIterations(numMaxIterations);
    }
    
    public void buildTopics(Instances data){
        
        Neighbor[] adjListDocTerm = getAdjListDocTerm(data);
        Neighbor[] adjListTermDoc = getAdjListTermDoc(data);
        
        double[][] docTopics = getDocTopicMatrix();
        double[][] termTopics = getTermTopicMatrix();
        
        int numDocs = getNumDocs();
        int numTerms = getNumTerms();
        int numTopics = getNumTopics();
        
        double[][] docTopicsTemp = new double[numDocs][numTopics];
        //double[][] termTopicsTemp = new double[numDocs][numTopics];
        
        int numIt = 0; // armazena o número de iterações
        boolean sair = false;
        
        while(sair == false){
        	ShowStatus.setMessageVerbose("NumIt:" + numIt);
            // Definindo pesos dos documentos para os tópicos
        	ShowStatus.setMessageVerbose("Part1");
            for(int doc=0;doc<numDocs;doc++){
                for(int topic=0;topic<numTopics;topic++){
                    ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
                    double acm1 = 0;
                    for(int term=0;term<neighbors.size();term++){
                        double valor = neighbors.get(term).value * termTopics[neighbors.get(term).index][topic];
                        double acm2 = 0;
                        for(int topic2=0;topic2<numTopics;topic2++){
                            acm2 += docTopics[doc][topic2] * termTopics[neighbors.get(term).index][topic2];
                        }
                        if(acm2 == 0){
                            continue;
                        }
                        valor = valor / acm2;
                        acm1 += valor;
                    }
                    docTopicsTemp[doc][topic] = docTopics[doc][topic] * acm1;
                }
                // Normalizando docTopic
                double soma = 0;
                for(int topic=0;topic<numTopics;topic++){
                    soma += docTopicsTemp[doc][topic];
                }
                if(soma == 0){
                    continue;
                }
                for(int topic=0;topic<numTopics;topic++){
                    docTopicsTemp[doc][topic] = docTopicsTemp[doc][topic] / soma;
                }
            }
            
            // Copiando docTopicsTemp para docTopics e verificando a análise de convergência
            double acmDif = 0;
            for(int doc=0;doc<numDocs;doc++){
                for(int topic=0;topic<numTopics;topic++){
                    acmDif += Math.pow((docTopicsTemp[doc][topic] - docTopics[doc][topic]),2);
                    //acmDif += Math.abs((docTopicsTemp[doc][topic] - docTopics[doc][topic]));
                    docTopics[doc][topic] = docTopicsTemp[doc][topic];
                }
            }
            ShowStatus.setMessageVerbose("Difference:" + acmDif);

            // Definindo pesos dos termos para os tópicos
            ShowStatus.setMessageVerbose("Part2");
            
            for(int topic=0;topic<numTopics;topic++){
                for(int term=0;term<numTerms;term++){
                    ArrayList<IndexValue> neighbors = adjListTermDoc[term].getNeighbors();
                    double acm1 = 0;
                    for(int doc=0;doc<neighbors.size();doc++){
                        double valor = neighbors.get(doc).value * docTopics[neighbors.get(doc).index][topic];
                        double acm2=0;
                        for(int topic2=0;topic2<numTopics;topic2++){
                            acm2 =+ docTopics[neighbors.get(doc).index][topic2] * termTopics[term][topic2];
                        }
                        if(acm2 == 0){
                            continue;
                        }
                        valor = valor / acm2;
                        acm1 += valor;
                    }
                    termTopics[term][topic] = termTopics[term][topic] * acm1;
                }
                double soma = 0;
                for(int term=0;term<numTerms;term++){
                    soma += termTopics[term][topic];
                }
                if(soma == 0){
                    continue;
                }
                for(int term=0;term<numTerms;term++){
                    termTopics[term][topic] = termTopics[term][topic] / soma;
                }
            }
            
            //Análises de critério de parada;
            numIt++;
            if(numIt >= getNumMaxIterations()){
                sair = true;
            }
        
        }
    }
    
    public void setNumMaxIterations(int numMaxIterations){
        this.numMaxInterations = numMaxIterations;
    }
    
    public Integer getNumMaxIterations(){
        return this.numMaxInterations;
    }
    
}
