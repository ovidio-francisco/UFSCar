/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TET;

import topicExtraction.TETAlgorithms.PLSA2;
import topicExtraction.TETAlgorithms.TopicExtractorOld;
import topicExtraction.TETConfigurations.TopicExtractionParameters;
import topicExtraction.TETIO.ListFiles;
import topicExtraction.TETParameters.Parameters_PLSA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import topicExtraction.m4mUtils.M4MFiles;
import topicExtraction.m4mUtils.M4MShowStatus;
import topicExtraction.mining4meetings.Mining4Meetings;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class TopicExtraction_Old {
    
    public static void ExtractTopics(TopicExtractionParameters configuration){
        
        ArrayList<File> filesIn = new ArrayList<File>();
        ListFiles.list(new File(configuration.getDirEntrada()), filesIn); //Vetor para armazenar os documentos textuais
    
        for(int i =0;i<filesIn.size();i++){ 

            if(!filesIn.get(i).getAbsolutePath().endsWith(".arff")){
                continue;
            }
            M4MShowStatus.setMessage("Loading ARFF file...");
            M4MShowStatus.setMessage(filesIn.get(i).getAbsolutePath());
            try{

                
//              BufferedReader arffReader = new BufferedReader(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1"));
                BufferedReader arffReader = M4MFiles.getBufferedReader(filesIn.get(i)); 
                Instances data = new Instances(arffReader);
                arffReader.close();
            
                //DataSource trainSource = new DataSource(new InputStreamReader(new InputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1")); //Carregando arquivo de Dados
                //DataSource trainSource = new DataSource(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1")); //Carregando arquivo de Dados
                //DataSource trainSource = new DataSource(filesIn.get(i).getAbsolutePath().toString()); //Carregando arquivo de Dados
                //Instances data = trainSource.getDataSet();
                M4MShowStatus.setMessage("Arff loaded");
                
                int numDocs = data.numInstances();
                int numTerms = data.numAttributes();
                int numTopics = configuration.getNumTopics();
                
                M4MShowStatus.setMessage("Extraction topics...");
                if(configuration.isPLSA()){
                    Parameters_PLSA parametersPLSA = configuration.getParametersPLSA();
                    M4MShowStatus.setMessage("Algorithm: PLSA");
                    PLSA2 plsa = new PLSA2(numDocs, numTerms, numTopics, parametersPLSA.getNumMaxIterations(), parametersPLSA.getMinDifference());
//                    String output = "_PLSA_" + parametersPLSA.getNumMaxIterations() + "iterations_" + parametersPLSA.getMinDifference() + "mindiff_";
                    //Extract(configuration, plsa, data, output);
                    Extract(configuration, plsa, data);
                    M4MShowStatus.setMessage("Topics Extracted!");
                }
                
            }catch(Exception e){
                System.err.println("Ocorreu um erro ao gerar os tópicos");
                e.printStackTrace();
            }
        }    
        M4MShowStatus.setMessage("Process concluded successfully");
    }
    
    
    public static void Extract(TopicExtractionParameters configuration, TopicExtractorOld algorithm, Instances data){
        JOptionPane.showMessageDialog(null, "Extracting ERRADO");
        
        algorithm.buildTopics(data);
        
        printDocTopic(data, algorithm.getDocTopicMatrix(), algorithm.getNumTopics(), configuration.getDirSaida(), Mining4Meetings.DOCUMENT_TOPIC_MATRIX_FILE_NAME);
        printTermTopic(data, algorithm.getTermTopicMatrix(), algorithm.getNumTopics(), configuration.getDirSaida(), Mining4Meetings.TERM_TOPIC_MATRIX_FILE_NAME);
        
    }
    
    public static void printDocTopic(Instances data, double[][] docTopic, int numTopics, String dirOut, String output){
        try{
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + data.relationName() + "_" + numTopics + "topics" + output + "_DocTopic.csv"),"ISO-8859-1"));
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + output),"ISO-8859-1"));
            BufferedWriter fileOut = M4MFiles.getBufferedWriter(new File(dirOut + "/" + output));
            
            int numDocs = data.numInstances();
            
            fileOut.write("#Docs:"+numDocs+"\n");
            fileOut.write("#Topics:"+numTopics+"\n");
            
            for(int doc=0;doc<numDocs;doc++){
                fileOut.write(data.attribute(0).value((int)data.instance(doc).value(0)) + ",");
                for(int topic=0;topic<numTopics;topic++){
                    fileOut.write(docTopic[doc][topic] + ",");
                }
                fileOut.write("\n");
            }
            
            fileOut.close();
            
        }catch(Exception e){
            System.err.println("Houve um erro ao gravar a matriz Documento-Topico");
            e.printStackTrace();
        }
    }
    
    public static void printTermTopic(Instances data, double[][] termTopic, int numTopics, String dirOut, String output){
        try{
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + data.relationName() + "_" + numTopics + "topics" + output + "topics_TermTopic.csv"),"ISO-8859-1"));
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + output),"ISO-8859-1"));
            BufferedWriter fileOut = M4MFiles.getBufferedWriter(new File(dirOut + "/" + output));
            
            int numTerms = data.numAttributes();
            
            fileOut.write("#Terms:"+numTerms+"\n");
            fileOut.write("#Topics:"+numTopics+"\n");
            
            for(int term=1;term<numTerms;term++){
                fileOut.write(data.attribute(term).name() + ",");
                for(int topic=0;topic<numTopics;topic++){
                    fileOut.write(termTopic[term][topic] + ",");
                }
                fileOut.write("\n");
            }
            
            fileOut.close();
            
        }catch(Exception e){
            System.err.println("Houve um erro ao gravar a matriz Termo-Topico");
            e.printStackTrace();
        }
    }
}
