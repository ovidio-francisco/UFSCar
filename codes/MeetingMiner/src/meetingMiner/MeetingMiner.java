package meetingMiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.tree.DefaultMutableTreeNode;

import topicExtraction.TET.TopicExtraction;
import topicExtraction.TETConfigurations.TopicExtractionConfiguration;
import topicExtraction.TETConfigurations.TopicExtractionParameters;
import topicExtraction.TETPreprocessing.TextRepresentation;
import topicExtraction.mmParameters.M4MArffGenerationParameters;
import topicExtraction.mmParameters.M4MRepresentationParameters;
import topicExtraction.mmParameters.M4MTopicExtractionParameters;
import topicExtraction.mmUtils.M4MFiles;
import utils.Files;
import utils.ShowStatus;

/**
 * @author ovidiojf
 */
public class MeetingMiner {

	private static StringBuffer[] descTopics;
	private static StringBuffer[] docsPerTopics;
	private static int numTopics;
	private static int[] orderedTopics;
	
	
    public static void prepareFolders() {
    
    	arfFolder = new File(Files.getSegmentedDocs().getAbsolutePath() + "/arff");
    	outFolder = new File(Files.getSegmentedDocs().getAbsoluteFile() + "/out");
        
        foldersOK = true;
        
        getRepresentationParameters().setDirIn(Files.getSegmentedDocs());
        getRepresentationParameters().setDirOut(arfFolder);

        getExtractionParameters().setDirEntrada(arfFolder.getAbsolutePath());
        getExtractionParameters().setDirSaida(outFolder.getAbsolutePath());
        
        
        
    }
    
    public static void miningTheMeetings() {
        long startTime = new Date().getTime();

        if(!arfFolder.exists()) arfFolder.mkdir();
        if(!outFolder.exists()) outFolder.mkdir();
        
        represent();
        extractTopics();
        
        ShowStatus.setMessage("Extração de tópicos concluída");
        extractDescriptorsAndFiles();
        
//        saveTopics();


        ShowStatus.setMessage("Concluído em " + new SimpleDateFormat("mm:ss").format(new Date().getTime() - startTime));
    }
    
    
    public static void represent() {
        
        if(getArffFile().exists()) {
            ShowStatus.setMessage("Arquivo '"+getArffFile() + "' encontrado");
            return;
        }
        
        TextRepresentation.Represent(MeetingMiner.getRepresentationParameters());                                             
        
    }
    
    public static void extractTopics() {

        TopicExtraction.ExtractTopics(MeetingMiner.getTopicExtractionconfiguration());      
    
    }
    
    public static void extractDescriptorsAndFiles() { // antigo visualize -- altered by ojf
        try{
            ShowStatus.setMessage("Reading document-term matrix...");
                 
            BufferedReader fileDocTopic = M4MFiles.getBufferedReader(new File(outFolder + "/" + MeetingMiner.DOCUMENT_TOPIC_MATRIX_FILE_NAME));
            
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
            ShowStatus.setProgress(40);

            
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
            BufferedReader fileTermTopic = M4MFiles.getBufferedReader(new File(outFolder + "/" + MeetingMiner.TERM_TOPIC_MATRIX_FILE_NAME));
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
            ShowStatus.setProgress(60);

            
            ShowStatus.setMessage("Matrices loaded");
            ShowStatus.setMessage("Setting the documents and descriptors for the topics");
            
            StringBuffer[] descTopics    = ExtractTopicDescriptors(numTerms, numTopics, MeetingMiner.descriptorsByTopic);
            ShowStatus.setProgress(70);

            StringBuffer[] docsPerTopics = ExtractDocsPerTopics(numDocs, numTopics); 
            ShowStatus.setProgress(80);
            
            ShowStatus.setMessage("Descriptors extracted and documents assigned to topics");
            
            
            MeetingMiner.descTopics = descTopics;
            MeetingMiner.docsPerTopics = docsPerTopics;
            MeetingMiner.numTopics = numTopics;
            MeetingMiner.orderedTopics = orderedTopics;
            
           
//            if (view instanceof DefaultTreeModel) {
//
//            	DefaultMutableTreeNode root = GererateTree(descTopics, docsPerTopics, numTopics, orderedTopics);
//
//                ((DefaultTreeModel)view).setRoot(root);
//                ((DefaultTreeModel)view).reload(root);
//            }
            
        }catch(IOException | NumberFormatException e){
            System.err.println("Erro ao carregar as matrizes de tópicos");
            e.printStackTrace();
        }
    }

    
    public static DefaultMutableTreeNode createTree() {
//    	DefaultMutableTreeNode root = GererateTree(descTopics, docsPerTopics, numTopics, orderedTopics);
//    	JTree tree = new JTree();
//    	((DefaultTreeModel)tree.getModel()).setRoot(root);
//    	((DefaultTreeModel)tree.getModel()).reload();
//    	return tree;
        ShowStatus.setMessage("Generating the topic tree...");
        ShowStatus.setMessage("Tree generated");
    	return GererateTree(descTopics, docsPerTopics, numTopics, orderedTopics);
    }
    
/**
 *  ======================================================================
 *  Criação dos Objetos que encapsulam os descritores e os documentos       
 *  ======================================================================
 *                                                                       */
    public static ArrayList<MMTopic> getMMTopics() {
        ArrayList<MMTopic> result = new ArrayList<>();
		
        for(int i=0;i<numTopics;i++){
            
        	MMTopic topic = new MMTopic();
        	
            int indTopic = orderedTopics[i];
            
            for(String desc: descTopics[indTopic].toString().split(";")) {
            	if (!desc.trim().isEmpty())
//            		topic.descriptors.add(desc.trim());
            	    topic.addDescriptor(desc);
            }
            
            for(String doc : docsPerTopics[indTopic].toString().split(";")) {
//                topic.segmentsdoc.add(new File(doc));
                topic.addSegmentDoc(new File(doc));
            }

            result.add(topic);
        }
        
        return result;
    }
    
    public static void cleanFolders() {
        M4MFiles.deleteFolder(arfFolder);
//        M4MFiles.deleteFolder(txtFolder);
        M4MFiles.deleteFolder(outFolder);        
    } 

//    public static void saveTopics() {
//        
//        DefaultTreeModel model = (DefaultTreeModel)view; 
//        String filename = outFolder.getPath()+"/topics.txt";
//        
//        if (MeetingMiner.getTopicExtractionconfiguration().isPLSA()) {
//            filename = filename + " - PLSA";
//        }
//        if (MeetingMiner.getTopicExtractionconfiguration().isKMeans()) {
//            filename = filename + " - kMeans";
//        }
//        if (MeetingMiner.getTopicExtractionconfiguration().isBisectingKMeans()) {
//            filename = filename + " - BisectingkMeans";
//        }
//        if (MeetingMiner.getTopicExtractionconfiguration().isLDAGibbs()) {
//            filename = filename + " - LDA Gibbs";
//        }
//        
//        if (MeetingMiner.getTopicExtractionconfiguration().isAutoNumTopics()) {
//            filename = filename + " - Non Parametric";
//        }
//        
//        BufferedWriter out = M4MFiles.getBufferedWriter(new File(filename));
//
//        try {
//
//            for(int i=0; i < model.getChildCount(model.getRoot()); i++) {
//                Object node = model.getChild(model.getRoot(), i);
//
//                if(!((DefaultMutableTreeNode)node).isLeaf()) {
//                    out.write("Topic : " + node);    
//                    out.newLine();
//                }
//            }
//
//            out.flush();
//            out.close();
//            
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//       
//        ShowStatus.setMessage("Saving topics to "+filename);
//    }
    

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

    
//    private static Object view = null;
    
    private static double[][] docTopic;
    private static double[][] termTopic;
    
    private static String[] listDocs;
    private static String[] listTerms;


    
    public static int getDescriptorsByTopic() {
        return descriptorsByTopic;
    }

    public static void setDescriptorsByTopic(int descriptorsByTopic) {
        MeetingMiner.descriptorsByTopic = descriptorsByTopic;
    }
    
    public static TopicExtractionParameters getExtractionParameters() {
        if(extractionParameters == null) {
            extractionParameters = M4MTopicExtractionParameters.getDefaultTopicExtractionParameters();
        }
        
        return extractionParameters;
    }

    public static void setExtractionParameters(TopicExtractionParameters extractionParameters) {
        MeetingMiner.extractionParameters = extractionParameters;
    }

    public static M4MRepresentationParameters getRepresentationParameters() {
        if(representationParameters == null) {
            representationParameters = M4MRepresentationParameters.getDefaultRepresentationParameters();
        }
        
        return representationParameters;
    }

    public static void setRepresentationParameters(M4MRepresentationParameters representationParameters) {
        MeetingMiner.representationParameters = representationParameters;
    }

    public static M4MArffGenerationParameters getArrffGenerationParameters() {
        if(arffGenerationParameters == null) {
            arffGenerationParameters = M4MArffGenerationParameters.getArffGenerationParameters();
        }
        
        return arffGenerationParameters;
    }

    public static void setArrffGenerationParameters(M4MArffGenerationParameters arrffGenerationParameters) {
        MeetingMiner.arffGenerationParameters = arrffGenerationParameters;
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
        MeetingMiner.arfFolder = arfFolder;
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
        MeetingMiner.outFolder = outFolder;
    }
    
    public static File getArffFile() {
//      File arffFile = new File(cfg.getDirOut() + "/" + cfg.getRelacao() + ".arff");
        return new File(arfFolder.getAbsolutePath() + "/representacao.arff");
    }

//    public static Object getView() {
//        return view;
//    }
//
//    public static void setView(Object view) {
//        MeetingMiner.view = view;
//    }

    public static TopicExtractionConfiguration getTopicExtractionconfiguration() {
        return topicExtractionconfiguration;
    }
    
    public static void setTopicExtractionconfiguration(TopicExtractionConfiguration topicExtractionconfiguration) {
        MeetingMiner.topicExtractionconfiguration = topicExtractionconfiguration;
    }

    public static boolean isFoldersOK() {
        return foldersOK;
    }

    public static void setFoldersOK(boolean foldersOK) {
        MeetingMiner.foldersOK = foldersOK;
    }

    public static boolean isAlgorithmsOk() {
        return algorithmsOk;
    }

    public static void setAlgorithmsOk(boolean algorithmsok) {
        MeetingMiner.algorithmsOk = algorithmsok;
    }
    
    
    
}
