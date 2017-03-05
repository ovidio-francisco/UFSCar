/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TET;

import TETAlgorithms.NonParametric.BisectingKMeans_NonParametric;
import TETAlgorithms.NonParametric.KMeans_NonParametric;
import TETAlgorithms.NonParametric.PLSA_NonParametric;
import TETAlgorithms.Parametric.Bisecting_KMeans_Parametric2;
import TETAlgorithms.Parametric.KMeans_Parametric;
import TETAlgorithms.Parametric.LDA_Gibbs_Parametric;
import TETAlgorithms.Parametric.PLSA2_Parametric;
import TETAlgorithms.TopicExtractor;
import TETConfigurations.TopicExtractionConfiguration;
import TETIO.ListFiles;
import TETParameters.Parameters_BisectingKMeans_NonParametric;
import TETParameters.Parameters_KMeans_NonParametric;
import TETParameters.Parameters_KMeans_Parametric;
import TETParameters.Parameters_PLSA_NonParametric;
import TETParameters.Parameters_PLSA_Parametric;
import TETPreprocessing.Encoding;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import m4mUtils.M4MFiles;
import m4mUtils.M4MShowStatus;
import mining4meetings.Mining4Meetings;
//import preprocessamento.Encoding;
import weka.core.Instances;

/**
 *
 * @author Gaia
 */
public class TopicExtraction {
    
    public static void ExtractTopics(TopicExtractionConfiguration configuration){
        
        ArrayList<File> filesIn = new ArrayList<File>();
        ListFiles.list(new File(configuration.getDirEntrada()), filesIn); //Vetor para armazenar os documentos textuais
        for(int i =0;i<filesIn.size();i++){ 

            if(!filesIn.get(i).getAbsolutePath().endsWith(".arff")){
                continue;
            }
            M4MShowStatus.setMessage(filesIn.get(i).getAbsolutePath());
            M4MShowStatus.setMessage("Loading ARFF file...");
            try{
                
                String charset = Encoding.getCharSet(filesIn.get(i));
                BufferedReader arffReader = M4MFiles.getBufferedReader(filesIn.get(i)); //new BufferedReader(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), charset));
                Instances data = new Instances(arffReader);
                arffReader.close();
            
                //DataSource trainSource = new DataSource(new InputStreamReader(new InputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1")); //Carregando arquivo de Dados
                //DataSource trainSource = new DataSource(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1")); //Carregando arquivo de Dados
                //DataSource trainSource = new DataSource(filesIn.get(i).getAbsolutePath().toString()); //Carregando arquivo de Dados
                //Instances data = trainSource.getDataSet();
                M4MShowStatus.setMessage("Arff loaded");
                
                int numDocs = data.numInstances();
                int numTerms = data.numAttributes();
                
                if(configuration.isAutoNumTopics() == false){ 
                    for(int topic=0;topic<configuration.getNumTopics().size();topic++){
                        int numTopics = configuration.getNumTopic(topic);
                        
                        M4MShowStatus.setMessage("Extraction topics...");
                        M4MShowStatus.setMessage("Number of topics: " + numTopics);

                            // Disparando algoritmos de extração de tópicos paramétricos, isto é, metodos nos quais é necessário informar o número de tópicos
                            // Disparando o LDA
                            if(configuration.isPLSA()){
                                Parameters_PLSA_Parametric parametersPLSA = configuration.getParametersPLSAParametric();
                                M4MShowStatus.setMessage("Algorithm: PLSA - Parametric");
                                PLSA2_Parametric plsa = new PLSA2_Parametric(numDocs, numTerms, numTopics, parametersPLSA.getNumMaxIterations(), parametersPLSA.getMinDifference());
                                String output = "_PLSA_" + parametersPLSA.getNumMaxIterations() + "iterations_" + parametersPLSA.getMinDifference() + "mindiff_";
                                //Extract(configuration, plsa, data, output);
                                Extract(configuration, plsa, data);
                                M4MShowStatus.setMessage("Topics Extracted!");
                            }

                            // Disparando o PLSA
                            if(configuration.isLDAGibbs()){
                                int numMaxIterations = 2000;
                                int burn = 1000;
                                double alpha = 5;
                                double beta = 0.1;
                                double minDiff = 1.0;
                                M4MShowStatus.setMessage("Algorithm: LDA - Gibbs Sampling - Parametric");
                                LDA_Gibbs_Parametric lda = new LDA_Gibbs_Parametric(numDocs, numTerms, numTopics, numMaxIterations, burn, alpha, beta);
                                String output = "_PLSA_" + numMaxIterations + "iterations_" + minDiff + "mindiff_";
                                Extract(configuration, lda, data);
                                M4MShowStatus.setMessage("Topics Extracted!");
                            }
                            if(configuration.isKMeans()){
                                Parameters_KMeans_Parametric parameters = configuration.getParametersKMeansParametric();
                                int numMaxIterations = parameters.getNumMaxIterations();
                                double minDiff = parameters.getPercChange();
                                boolean centroidLabeling = parameters.isCentroidLabel();
                                M4MShowStatus.setMessage("Algorithm: KMeans - Parametric");
                                
                                KMeans_Parametric kMeans = new KMeans_Parametric(numDocs, numTerms, numTopics, numMaxIterations, minDiff, centroidLabeling);
                                String output = "_kMeans_" + numMaxIterations + "iterations_" + minDiff + "mindiff_";
                                if(centroidLabeling == true){
                                    M4MShowStatus.setMessage("Cluster labeling: Centroid");
                                    output =  output.concat("CentroidLabeling_");
                                }else{
                                    M4MShowStatus.setMessage("Cluster labeling: F1-Measure");
                                    output = output.concat("F1Labeling_");
                                }
                                Extract(configuration, kMeans, data);
                                M4MShowStatus.setMessage("Topics Extracted!");
                            }
                            if(configuration.isBisectingKMeans()){
                                Parameters_KMeans_Parametric parameters = configuration.getParametersBisectingKMeansParametric();
                                int numMaxIterations = parameters.getNumMaxIterations();
                                double minDiff = parameters.getPercChange();
                                boolean centroidLabeling = parameters.isCentroidLabel();
                                M4MShowStatus.setMessage("Algorithm: Bisecting-k-Means - Parametric");
                                //Bisecting_KMeans_Parametric bisectingKMeans = new Bisecting_KMeans_Parametric(numDocs, numTerms, numTopics, numMaxIterations, minDiff, centroidLabeling);
                                Bisecting_KMeans_Parametric2 bisectingKMeans = new Bisecting_KMeans_Parametric2(numDocs, numTerms, numTopics, numMaxIterations, minDiff, centroidLabeling);
                                String output = "_Bisecting-k-Means_" + numMaxIterations + "iterations_" + minDiff + "mindiff_";
                                if(centroidLabeling == true){
                                    M4MShowStatus.setMessage("Cluster labeling: Centroid");
                                    output = output.concat("CentroidLabeling_");
                                }else{
                                    M4MShowStatus.setMessage("Cluster labeling: F1-Measure");
                                    output = output.concat("F1Labeling_");
                                }
                                Extract(configuration, bisectingKMeans, data);
                                M4MShowStatus.setMessage("Topics Extracted!");
                            }
                        }
                    
                }else{ // Disparando algoritmos de extração de tópicos não-paramétricos, isto é, metodos nos quais não é necessário informar o número de tópicos
                    if(configuration.isPLSA()){
                        Parameters_PLSA_NonParametric parametersPLSA = configuration.getParametersPLSANonParametric();
                        M4MShowStatus.setMessage("Algorithm: PLSA - Non Parametric");
                        PLSA_NonParametric plsa = new PLSA_NonParametric(numDocs, numTerms, parametersPLSA.getMaxTopics(), parametersPLSA.getNumMaxIterations(), parametersPLSA.getMinDifference(), parametersPLSA.getMaxTopics(), parametersPLSA.getMinThreshold());
                        String output = "_PLSA_" + parametersPLSA.getNumMaxIterations() + "iterations_" + parametersPLSA.getMinDifference() + "mindiff_";
                        Extract(configuration, plsa, data);
                        M4MShowStatus.setMessage("Topics Extracted!");
                    }
                    if(configuration.isKMeans()){
                        Parameters_KMeans_NonParametric parameters = configuration.getParametersKMeansNonParametric();
                        int numMaxIterations = parameters.getNumMaxIterations();
                        double minDiff = parameters.getPercChange();
                        boolean centroidLabeling = parameters.isCentroidLabel();
                        int minValueK = parameters.getMinValueK();
                        int maxValueK = parameters.getMaxValueK();
                        int stepSize = parameters.getStepSize();
                        boolean movingAverage = parameters.isMovingAverage();
                        int windowSize = parameters.getWindowSize();
                        double minPercVariation = parameters.getMinPercVariation();
                        M4MShowStatus.setMessage("Algorithm: KMeans - Non Parametric");
                        String output = "_kMeans_" + numMaxIterations + "iterations_" + minDiff + "mindiff_";
                        if(centroidLabeling == true){
                            M4MShowStatus.setMessage("Cluster labeling: Centroid");
                            output = output.concat("CentroidLabeling_");
                        }else{
                            M4MShowStatus.setMessage("Cluster labeling: F1-Measure");
                            output = output.concat("F1Labeling_");
                        }
                        KMeans_NonParametric kMeansNonParametric = new KMeans_NonParametric(numDocs, numTerms, 0, numMaxIterations, minDiff, centroidLabeling, minValueK, maxValueK, stepSize, movingAverage, windowSize, minPercVariation);
                        kMeansNonParametric.buildTopics(data);
                        int bestK = kMeansNonParametric.getBestValueK();
                        KMeans_Parametric kMeansParametric = new KMeans_Parametric(numDocs, numTerms, bestK, numMaxIterations, minDiff, centroidLabeling);
                        Extract(configuration, kMeansParametric, data);
                        M4MShowStatus.setMessage("Topics Extracted!");
                    }
                    if(configuration.isBisectingKMeans()){
                        Parameters_BisectingKMeans_NonParametric parameters = configuration.getParametersNonKMeansNonParametric();
                        int numMaxIterations = parameters.getNumMaxIterations();
                        double minDiff = parameters.getPercChange();
                        boolean centroidLabeling = parameters.isCentroidLabel();
                        double minPercVariation = parameters.getMinPercVariation();
                        int maxValueK = parameters.getMaxValueK();
                        M4MShowStatus.setMessage("Algorithm: Bisecting-K-Means - Non Parametric");
                        String output = "_BisectingKMeans_" + numMaxIterations + "iterations_" + minDiff + "mindiff_";
                        if(centroidLabeling == true){
                            M4MShowStatus.setMessage("Cluster labeling: Centroid");
                            output = output.concat("CentroidLabeling_");
                        }else{
                            M4MShowStatus.setMessage("Cluster labeling: F1-Measure");
                            output = output.concat("F1Labeling_");
                        }
                        BisectingKMeans_NonParametric bisectingKMeansNonParametric = new BisectingKMeans_NonParametric(numDocs, numTerms, 0, numMaxIterations, minDiff, centroidLabeling, maxValueK, minPercVariation);
                        //bisectingKMeansNonParametric.buildTopics(data);
                        Extract(configuration, bisectingKMeansNonParametric, data);
                        M4MShowStatus.setMessage("Topics Extracted!");
                    }
                }
            }catch(Exception e){
                System.err.println("Ocorreu um erro ao gerar os tópicos");
                e.printStackTrace();
            }
        }    
        M4MShowStatus.setMessage("Process concluded successfully");
    }
    
    
    public static void Extract(TopicExtractionConfiguration configuration, TopicExtractor algorithm, Instances data){
        
        algorithm.buildTopics(data);
        
        printDocTopic(data, algorithm.getDocTopicMatrix(), algorithm.getNumTopics(), configuration.getDirSaida(), Mining4Meetings.DOCUMENT_TOPIC_MATRIX_FILE_NAME);
        printTermTopic(data, algorithm.getTermTopicMatrix(), algorithm.getNumTopics(), configuration.getDirSaida(), Mining4Meetings.TERM_TOPIC_MATRIX_FILE_NAME);
        
    }
    
    public static void printDocTopic(Instances data, double[][] docTopic, int numTopics, String dirOut, String output){
        try{
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + data.relationName() + "_" + numTopics + "topics" + output + "DocTopic.csv"),"UTF-8"));
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
//          BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + data.relationName() + "_" + numTopics + "topics" + output + "TermTopic.csv"),"UTF-8"));
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
