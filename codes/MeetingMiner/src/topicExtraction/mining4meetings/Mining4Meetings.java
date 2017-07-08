package topicExtraction.mining4meetings;

import topicExtraction.MMTopic;
import topicExtraction.TET.TopicExtraction;
import topicExtraction.m4mParameters.M4MArffGenerationParameters;
import topicExtraction.m4mParameters.M4MRepresentationParameters;
import topicExtraction.m4mParameters.M4MTopicExtractionParameters;
import topicExtraction.TETConfigurations.TopicExtractionConfiguration;
import topicExtraction.TETConfigurations.TopicExtractionParameters;
import topicExtraction.TETPreprocessing.TextRepresentation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import topicExtraction.m4mUtils.M4MFiles;
import topicExtraction.m4mUtils.M4MShowStatus;
import utils.Files;
import utils.ShowStatus;

/**
 *
 * @author ovidiojf
 */
public class Mining4Meetings {

    public static void prepareFolders() {
//        docFolder = new File(M4MUtils.searchFolder());
        
//    	arfFolder = new File(docFolder.getAbsolutePath() + "/arff");
//      txtFolder = new File(docFolder.getAbsolutePath() + "/txt");
//      outFolder = new File(docFolder.getAbsoluteFile() + "/out");
    
    	arfFolder = new File(Files.getSegmentedDocs().getAbsolutePath() + "/arff");
//      txtFolder = new File(docFolder.getAbsolutePath() + "/txt");
    	outFolder = new File(Files.getSegmentedDocs().getAbsoluteFile() + "/out");

        
        foldersOK = true;

        
        getRepresentationParameters().setDirIn(Files.getSegmentedDocs());
        getRepresentationParameters().setDirOut(arfFolder);

        getExtractionParameters().setDirEntrada(arfFolder.getAbsolutePath());
        getExtractionParameters().setDirSaida(outFolder.getAbsolutePath());
        
    }
    
    public static void mining4Meetings() {
        long startTime = new Date().getTime();

        M4MShowStatus.setProgress(0);
        
        if(!arfFolder.exists()) arfFolder.mkdir();
//        if(!txtFolder.exists()) txtFolder.mkdir();
        if(!outFolder.exists()) outFolder.mkdir();
        
        M4MShowStatus.setProgress(2);        
        
        extractRawTxt();
        M4MShowStatus.setProgress(10);
        
        represent();
        M4MShowStatus.setProgress(30);
          
        extractTopics();
        M4MShowStatus.setProgress(40);
        
        ShowStatus.setMessage("Extração de tópicos concluída");
        Mining4Meetings.visualise();
        
        saveTopics();

        M4MShowStatus.setProgress(100);
        
//        cleanFolders();

        ShowStatus.setMessage("Concluído em " + new SimpleDateFormat("mm:ss").format(new Date().getTime() - startTime));
        
    }
    
    public static void extractRawTxt() {

//        M4MTextExtractor.extractTxtFromAllPdfs(docFolder, txtFolder);
//        M4MTextExtractor.extractTxtFromAllFiles(docFolder, txtFolder);

    }
    
    public static void represent() {
        
        if(getArffFile().exists()) {
            ShowStatus.setMessage("Arquivo '"+getArffFile() + "' encontrado");
            return;
        }
        
        TextRepresentation.Represent(Mining4Meetings.getRepresentationParameters());                                             
        
    }
    
    public static void extractTopics() {

        TopicExtraction.ExtractTopics(Mining4Meetings.getTopicExtractionconfiguration());      
    
    }
    
    public static void visualise() {
        try{
            ShowStatus.setMessage("Reading document-term matrix...");
                 
            BufferedReader fileDocTopic = M4MFiles.getBufferedReader(new File(outFolder + "/" + Mining4Meetings.DOCUMENT_TOPIC_MATRIX_FILE_NAME));
            
            String[] parts;
            String linha;
            
            linha = fileDocTopic.readLine();
            parts = linha.split(":");
            int numDocs = Integer.parseInt(parts[1]);
            linha = fileDocTopic.readLine();
            parts = linha.split(":");
            int numTopics = Integer.parseInt(parts[1]);
            
            listDocs = new String[numDocs];
            docTopic = new double[numDocs][numTopics];

            int countDoc = 0;
            while((linha = fileDocTopic.readLine()) != null){
                if(linha.trim().length() == 0){
                    break;
                }
                parts = linha.split(",");
                listDocs[countDoc] = parts[0];
                for(int part=1;part<parts.length;part++){
                    double valor = Double.parseDouble(parts[part]);
                    docTopic[countDoc][part-1] = valor;
                }
                countDoc++;
                
            }
            fileDocTopic.close();
            M4MShowStatus.setProgress(40);

            
//            double[] topicRelevance = new double[numTopics];
            double[] topicRelevanceCopy = new double[numTopics];
            
            for(int topic=0;topic<numTopics;topic++){
                double acmRelTopic = 0;
                for(int doc=0;doc<numDocs;doc++){
                    acmRelTopic = docTopic[doc][topic];
                }
//                topicRelevance[topic] = (double)acmRelTopic / (double)numDocs;
                topicRelevanceCopy[topic] = (double)acmRelTopic / (double)numDocs;
            }
            
            int[] orderedTopics = new int[numTopics];
            double maxValue = -1;
            int indMax = -1;
            for(int topic1=0;topic1<numTopics;topic1++){
                for(int topic2=0;topic2<numTopics;topic2++){
                    if(topicRelevanceCopy[topic2] > maxValue){
                        maxValue = topicRelevanceCopy[topic2];
                        indMax = topic2;
                    }
                }
                topicRelevanceCopy[indMax] = -1;
                orderedTopics[topic1] = indMax;
                maxValue = -1;
                indMax = -1;
            }
            
            ShowStatus.setMessage("Reading term-topic matrix...");
            
//          linha = "";
//          BufferedReader fileTermTopic = new BufferedReader(new InputStreamReader(new FileInputStream(outFolder + "/" + Mining4Meetings.TERM_TOPIC_MATRIX_FILE_NAME), "ISO-8859-1"));
            BufferedReader fileTermTopic = M4MFiles.getBufferedReader(new File(outFolder + "/" + Mining4Meetings.TERM_TOPIC_MATRIX_FILE_NAME));
            linha = fileTermTopic.readLine();
            parts = linha.split(":");
            int numTerms = Integer.parseInt(parts[1]);
            linha = fileTermTopic.readLine();
            parts = linha.split(":");
            numTopics = Integer.parseInt(parts[1]);
            
            listTerms = new String[numTerms];
            termTopic = new double[numTerms][numTopics];
            
            int countTerm = 0;
            while((linha = fileTermTopic.readLine()) != null){
                if(linha.trim().length() == 0){
                    break;
                }
                parts = linha.split(",");
                listTerms[countTerm] = parts[0];
                for(int part=1;part<parts.length;part++){
                    double valor = Double.parseDouble(parts[part]);
                    termTopic[countTerm][part-1] = valor;
                }
                countTerm++;
            }
            fileTermTopic.close();
            M4MShowStatus.setProgress(60);

            
            ShowStatus.setMessage("Matrices loaded");
            ShowStatus.setMessage("Setting the documents and descriptors for the topics");
            
            StringBuffer[] descTopics    = ExtractTopicDescriptors(numTerms, numTopics, Mining4Meetings.descriptorsByTopic);
            ShowStatus.setProgress(70);

            StringBuffer[] docsPerTopics = ExtractDocsPerTopics(numDocs, numTopics); 
            ShowStatus.setProgress(80);
            
            ShowStatus.setMessage("Descriptors extracted and documents assigned to topics");
            ShowStatus.setMessage("Generating the topic tree...");
            
           
            if (view instanceof DefaultTreeModel) {
                
                DefaultMutableTreeNode root = GererateTree(descTopics, docsPerTopics, numTopics, orderedTopics);

    //            model = (DefaultTreeModel)tView.getModel();
                ((DefaultTreeModel)view).setRoot(root);
                ((DefaultTreeModel)view).reload(root);
            }
            
            MMTopic topics = MMTopic.getTopics(descTopics, docsPerTopics, numTopics, orderedTopics);
            

            M4MShowStatus.setProgress(90);
            ShowStatus.setMessage("Tree generated");
            
        }catch(IOException | NumberFormatException e){
            System.err.println("Erro ao carregar as matrizes de tópicos");
            e.printStackTrace();
        }
        
    }
    
    public static void cleanFolders() {
        M4MFiles.deleteFolder(arfFolder);
//        M4MFiles.deleteFolder(txtFolder);
        M4MFiles.deleteFolder(outFolder);        
    } 

    public static void saveTopics() {
        
        DefaultTreeModel model = (DefaultTreeModel)view; 
        String filename = outFolder.getPath()+"/topics.txt";
        
        if (Mining4Meetings.getTopicExtractionconfiguration().isPLSA()) {
            filename = filename + " - PLSA";
        }
        if (Mining4Meetings.getTopicExtractionconfiguration().isKMeans()) {
            filename = filename + " - kMeans";
        }
        if (Mining4Meetings.getTopicExtractionconfiguration().isBisectingKMeans()) {
            filename = filename + " - BisectingkMeans";
        }
        if (Mining4Meetings.getTopicExtractionconfiguration().isLDAGibbs()) {
            filename = filename + " - LDA Gibbs";
        }
        
        if (Mining4Meetings.getTopicExtractionconfiguration().isAutoNumTopics()) {
            filename = filename + " - Non Parametric";
        }
        
        BufferedWriter out = M4MFiles.getBufferedWriter(new File(filename));

        try {

            for(int i=0; i < model.getChildCount(model.getRoot()); i++) {
                Object node = model.getChild(model.getRoot(), i);

                if(!((DefaultMutableTreeNode)node).isLeaf()) {
                    out.write("Topic : " + node);    
                    out.newLine();
                }
            }

            out.flush();
            out.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       
        ShowStatus.setMessage("Saving topics to "+filename);
    }
    

    private static DefaultMutableTreeNode GererateTree(StringBuffer[] descTopics, StringBuffer[] docsPerTopics, int numTopics, int[] orderedTopics){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tópicos");
        
        for(int topic1=0;topic1<numTopics;topic1++){
            
            int indTopic = orderedTopics[topic1];
            DefaultMutableTreeNode nodeDescTopic = new DefaultMutableTreeNode(descTopics[indTopic].toString());
            String[] parts  = docsPerTopics[indTopic].toString().split(";");
            
            
//            System.out.println(docsPerTopics[indTopic].toString()+ "========================&&&&&&&&&&&&&&&&&&&&&&&");
        
            for(int part=0;part<parts.length;part++){
                DefaultMutableTreeNode doc_Topic = new DefaultMutableTreeNode(parts[part]);
                nodeDescTopic.add(doc_Topic);
            }
            
            root.add(nodeDescTopic);
        }
        return root;
    }
    
    
    // Função para extrair os descritores dos tópicos
    private static StringBuffer[] ExtractTopicDescriptors(int numTerms, int numTopics, int numDesc){
        StringBuffer[] descriptors = new StringBuffer[numTopics];
        for(int topic=0;topic<numTopics;topic++){
            descriptors[topic] = new StringBuffer();
        }
        
        for(int topic=0;topic<numTopics;topic++){
            for(int desc=0;desc<numDesc;desc++){
                double max = -1;
                int indMax = -1;
                for(int term=0;term<numTerms;term++){
                    if(termTopic[term][topic] > max){
                        max = termTopic[term][topic];
                        indMax = term;
                    }
                }
                descriptors[topic].append(listTerms[indMax] + "; ");
                termTopic[indMax][topic] = -2;
            }
        }
        
        return descriptors;
    }

    // ver ojf   
    // Função para alocar os documentos aos tópicos
    private static StringBuffer[] ExtractDocsPerTopics(int numDocs, int numTopics){
        StringBuffer[] docsPerTopic = new StringBuffer[numTopics];
        for(int topic=0;topic<numTopics;topic++){
            docsPerTopic[topic] = new StringBuffer();
        }
        
        for(int doc=0;doc<numDocs;doc++){
            double max = -1;
            int indMax = -1;
            for(int topic=0;topic<numTopics;topic++){
                if(docTopic[doc][topic] > max){
                    max = docTopic[doc][topic];
                    indMax = topic;
                }
            }
            docsPerTopic[indMax].append(listDocs[doc] + ";");
            docTopic[doc][indMax] = -2;
        }
        
        return docsPerTopic;
    }
    
    public static final String DOCUMENT_TOPIC_MATRIX_FILE_NAME = "documentTopicMatrix.csv";
    public static final String TERM_TOPIC_MATRIX_FILE_NAME     = "termTopicMatrix.csv";    
//  public static final int    DESCRIPTORS_BY_TOPIC            = 10;

    public static  TopicExtractionConfiguration topicExtractionconfiguration = new TopicExtractionConfiguration();
    
    private static TopicExtractionParameters   extractionParameters     ;// = M4MTopicExtractionConfiguration.getDefaultTopicExtractionConf();
    private static M4MRepresentationParameters representationParameters ;// = M4MRepresentationConfiguration.getDefaultRepresentationConf();
    private static M4MArffGenerationParameters arffGenerationParameters ;// = M4MArffGenerationConfiguration.getArffGenerationConf();
    private static int descriptorsByTopic = 10;
    
//    private static File docFolder = null;
    private static File arfFolder = null;
//    private static File txtFolder = null;
    private static File outFolder = null;
    
    private static boolean foldersOK;
    private static boolean algorithmsOk;

    
    private static Object view = null;
    
    private static double[][] docTopic;
    private static double[][] termTopic;
    
    private static String[] listDocs;
    private static String[] listTerms;


    
    public static int getDescriptorsByTopic() {
        return descriptorsByTopic;
    }

    public static void setDescriptorsByTopic(int descriptorsByTopic) {
        Mining4Meetings.descriptorsByTopic = descriptorsByTopic;
    }
    
    public static TopicExtractionParameters getExtractionParameters() {
        if(extractionParameters == null) {
            extractionParameters = M4MTopicExtractionParameters.getDefaultTopicExtractionParameters();
        }
        
        return extractionParameters;
    }

    public static void setExtractionParameters(TopicExtractionParameters extractionParameters) {
        Mining4Meetings.extractionParameters = extractionParameters;
    }

    public static M4MRepresentationParameters getRepresentationParameters() {
        if(representationParameters == null) {
            representationParameters = M4MRepresentationParameters.getDefaultRepresentationParameters();
        }
        
        return representationParameters;
    }

    public static void setRepresentationParameters(M4MRepresentationParameters representationParameters) {
        Mining4Meetings.representationParameters = representationParameters;
    }

    public static M4MArffGenerationParameters getArrffGenerationParameters() {
        if(arffGenerationParameters == null) {
            arffGenerationParameters = M4MArffGenerationParameters.getArffGenerationParameters();
        }
        
        return arffGenerationParameters;
    }

    public static void setArrffGenerationParameters(M4MArffGenerationParameters arrffGenerationParameters) {
        Mining4Meetings.arffGenerationParameters = arrffGenerationParameters;
    }
    
    
//    public static File getDocFolder() {
//        return docFolder;
//    }
//
//    public static void setDocFolder(File docFolder) {
//        Mining4Meetings.docFolder = docFolder;
//    }

    public static File getArfFolder() {
        return arfFolder;
    }

    public static void setArfFolder(File arfFolder) {
        Mining4Meetings.arfFolder = arfFolder;
    }

//    public static File getTxtFolder() {
//        return txtFolder;
//    }

//    public static void setTxtFolder(File txtFolder) {
//        Mining4Meetings.txtFolder = txtFolder;
//    }

    public static File getOutFolder() {
        return outFolder;
    }

    public static void setOutFolder(File outFolder) {
        Mining4Meetings.outFolder = outFolder;
    }
    
    public static File getArffFile() {
//      File arffFile = new File(cfg.getDirOut() + "/" + cfg.getRelacao() + ".arff");
        return new File(arfFolder.getAbsolutePath() + "/representacao.arff");
    }

    public static Object getView() {
        return view;
    }

    public static void setView(Object view) {
        Mining4Meetings.view = view;
    }

    public static TopicExtractionConfiguration getTopicExtractionconfiguration() {
        return topicExtractionconfiguration;
    }
    
    public static void setTopicExtractionconfiguration(TopicExtractionConfiguration topicExtractionconfiguration) {
        Mining4Meetings.topicExtractionconfiguration = topicExtractionconfiguration;
    }

    public static boolean isFoldersOK() {
        return foldersOK;
    }

    public static void setFoldersOK(boolean foldersOK) {
        Mining4Meetings.foldersOK = foldersOK;
    }

    public static boolean isAlgorithmsOk() {
        return algorithmsOk;
    }

    public static void setAlgorithmsOk(boolean algorithmsok) {
        Mining4Meetings.algorithmsOk = algorithmsok;
    }
    
    
    
}
