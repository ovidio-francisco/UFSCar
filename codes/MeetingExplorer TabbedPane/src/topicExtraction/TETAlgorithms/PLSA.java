/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETAlgorithms;

import java.util.ArrayList;

import topicExtraction.TETStructures.IndexValue;
import topicExtraction.TETStructures.Neighbor;
import utils.ShowStatus;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class PLSA extends TopicExtractorOld{
    
    private int numMaxInteracoes;
    
    public PLSA(int numDocs, int numTerms, int numTopics, int numMaxIteracoes){
        super(numDocs, numTerms, numTopics);
        setNumMaxIteracoes(numMaxIteracoes);
    }
    
    public void buildTopics(Instances data){
        
        Neighbor[] adjListDocTerm = getAdjListDocTerm(data);
        Neighbor[] adjListTermDoc = getAdjListTermDoc(data);
        
        //Padronizando listas (colocar isso como opção de geração)
        /*for(int doc=0;doc<getNumDocs();doc++){
            double soma = 0;
            ArrayList<IndexValue> neighbors = adjListDocTerm[doc].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                soma += neighbors.get(term).value;
            }
            for(int term=0;term<neighbors.size();term++){
                neighbors.get(term).value = neighbors.get(term).value/soma;
            }
        }
        
        for(int term=0;term<getNumTerms();term++){
            double soma=0;
            ArrayList<IndexValue> neighbors = adjListTermDoc[term].getNeighbors();
            for(int doc=0;doc<neighbors.size();doc++){
                soma += neighbors.get(doc).value;
            }
            for(int doc=0;doc<neighbors.size();doc++){
                neighbors.get(doc).value = neighbors.get(doc).value / soma;
            }
        }*/
        // Fim da padronização
        
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
            if(numIt >= getNumMaxIteracoes()){
                sair = true;
            }
        
        }
    }
    
    public void setNumMaxIteracoes(int numMaxIteracoes){
        this.numMaxInteracoes = numMaxIteracoes;
    }
    
    public Integer getNumMaxIteracoes(){
        return this.numMaxInteracoes;
    }
    
}
