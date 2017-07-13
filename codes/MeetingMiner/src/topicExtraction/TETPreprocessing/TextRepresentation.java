//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to generate a document-term matrix in ARFF format.
//              (http://www.cs.waikato.ac.nz/ml/weka/arff.html)
//*****************************************************************************

package topicExtraction.TETPreprocessing;

import topicExtraction.TETStructures.TermValue;
import topicExtraction.TETStructures.FeatureList;
import topicExtraction.TETIO.ListFiles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import meetingMiner.MeetingMiner;
import topicExtraction.mmParameters.M4MArffGenerationParameters;
import topicExtraction.mmParameters.M4MRepresentationParameters;
import topicExtraction.mmUtils.M4MFiles;
import topicExtraction.mmUtils.M4MShowStatus;
import utils.ShowStatus;
import ptstemmer.Stemmer;
import ptstemmer.implementations.OrengoStemmer;

@SuppressWarnings("unused")
public class TextRepresentation {
    
//    public static void Represent(File dirIn, File dirOut, String relationName, boolean dirClass, String language, boolean stemming, boolean removeStopWords, boolean binary, boolean TF, boolean TFIDF, boolean sparse, boolean cutDF, int dfMin, int topRanked, boolean replaceStem){
    //Function to read a text collection and generate a document-term matrix
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void Represent(M4MRepresentationParameters cfg){
        
        ArrayList<String>       names        = new ArrayList<>();
        ArrayList<FeatureList>  terms        = new ArrayList<>(); // lista constendo os termos com as respectivas frequencias de cada documento da coleção
        HashMap<String,Integer> termDF       = new HashMap<>(); //armazena as DF dos atributos
        HashMap<String,String>  stem_palProc = new HashMap<>(); // dicionário do tipo stem - palavra preprocessada
        HashMap<String,String>  palProc_pal  = new HashMap<>(); // dicionário do tipo stem - palavra original - palavra pré-processada
        ArrayList<File>         filesIn      = new ArrayList<>();
        StopWords               sw           = new StopWords(cfg.getLanguage()); //Objeto para remoção das stopwords dos documentos
        Cleaner                 cln          = new Cleaner();
        Stemmer                 stemPt       = new OrengoStemmer(); //Objeto para a radicalização em português
        StemmerEn               stemEn       = new StemmerEn(); //Objeto para a radicalização em inglês
        
        
        ShowStatus.setMessage("Listing Files... Input Diretory: " + cfg.getDirIn());
        ListFiles.list(cfg.getDirIn(), filesIn); //Vetor para armazenar os documentos textuais
        
        Object[] orderedFiles = filesIn.toArray(); //Ordenando os arquivos pelo nome
        int numDocs = orderedFiles.length;
        Arrays.sort(orderedFiles);
        
        ShowStatus.setMessage("Extracting Terms...");
        for(int i =0;i<orderedFiles.length;i++){ // criando vetores contendo os atributos e suas frquências em cada documento da coleção
            File fileIn =  (File)orderedFiles[i];
            
            ShowStatus.setMessage(fileIn.getAbsoluteFile().getAbsolutePath());
            
            FeatureList features = new FeatureList(); // Armazena a lista de atributos-frequencia dos termos nos textos
                      
            // Pré-processa a lista de atributos de cada documento
            features.setFeatures(
                    Preprocessing.FeatureGeneration( fileIn          , cfg.getLanguage(), cfg.isRemoveStopWords(), 
                                                     cfg.isStemming(), palProc_pal      , stem_palProc           , 
                                                     names           , termDF           , sw                     , 
                                                     cln             , stemPt           , stemEn                 , 
                                                     cfg.isReplaceStem()  
                                                   )
            );
            
            terms.add(i, features);
        }
        
        if(cfg.isCutDF()){ // Realiza o corte por DF mínimo
            if(cfg.getDfMin() > 0){
                names = new ArrayList<>();
                Set<String> featureName = termDF.keySet();
                Iterator it = featureName.iterator();
                while(it.hasNext()){
                    String key = (String)it.next();
                    if(termDF.get(key) >= cfg.getDfMin()){
                        names.add(key);
                    }
                }
            }
        }else{ // realiza o corte rankendo os termos de maneira decrescente de acordo com o TFIDF e considera os maiores valores
            //Obtendo a soma total dos termos na coleção
            HashMap<String,Double> hashTF = new HashMap<>();
            for(int t1=0;t1<terms.size();t1++){
                ArrayList<TermValue> listTermsFreq = terms.get(t1).getFeatures();
                for(int t2=0;t2<listTermsFreq.size();t2++){
                    String key = listTermsFreq.get(t2).getFeature();
                    Double freq = (double)listTermsFreq.get(t2).getFrequency();
                    if(!hashTF.containsKey(key)){
                        hashTF.put(key, freq);
                    }else{
                        double currentFreq = hashTF.get(key);
                        double newFreq = currentFreq + freq;
                        hashTF.remove(key);
                        hashTF.put(key, newFreq);
                    }
                }
            }
            
            ArrayList<TermValue> sortDFs = new ArrayList<>(); // Armazenando os termos e seus valores de TFIDF para rankelos e selecitonar os X termos mais bem rankeados
            names = new ArrayList<>();
            Set<String> featureName = termDF.keySet();
            Iterator it = featureName.iterator();
            while(it.hasNext()){
                String key = (String)it.next();
                double scoreTFIDF = hashTF.get(key) * (Math.log10((double)numDocs / (double)(1 + termDF.get(key))));
                sortDFs.add(new TermValue(key,scoreTFIDF));
            }
            
            // Ordenando termos pela TFIDF
            Object[] orderedTFIDFs = sortDFs.toArray(); //Ordenando os arquivos pelo nome
            Arrays.sort(orderedTFIDFs, new Comparator() {
                @Override
                public int compare( Object term1, Object term2 ) {
                    TermValue obj1 = (TermValue)term1;
                    TermValue obj2 = (TermValue)term2;
                    if(obj1.getFrequency()>obj2.getFrequency()){
                        return -1;
                    }else{
                        if(obj1.getFrequency()<obj2.getFrequency()){
                            return 1;
                        }else{
                            return 0;
                        }         
                    }
                }
            });
            
            //Selecionados os top termos 
            for(int top=0;top < cfg.getTopRanked(); top++){
                TermValue item = (TermValue)orderedTFIDFs[top];
                names.add(item.getFeature());
            }
        }
        

        HashMap<String,String> classes = new HashMap<>();
        ArrayList<String> allClasses = new ArrayList<>();
        if(cfg.isDirClass()){
            classes = GetTextCollectionClasses(filesIn, allClasses);
        }

        int numTerms = (cfg.isDirClass()) ? names.size() + 2 : names.size() + 1;
        
        ShowStatus.setMessage("Generating an ARFF file");

        M4MArffGenerationParameters arffCfg = M4MArffGenerationParameters.getArffGenerationParameters();
        
        arffCfg.setNames        (names);
        arffCfg.setInfClasses   (allClasses);
        arffCfg.setNumFiles     (filesIn.size());
        arffCfg.setNumTerms     (numTerms);
        arffCfg.setFilesIn      (filesIn);
        arffCfg.setAtributos    (terms);
        arffCfg.setClasses      (classes);
        arffCfg.setOrderedFiles (orderedFiles);
        arffCfg.setTermDF       (termDF);
        arffCfg.setStem_palProc (stem_palProc);
        arffCfg.setPalProc_pal  (palProc_pal);
        arffCfg.setRelacao      (cfg.getRelationName());
        arffCfg.setClasse       (cfg.isDirClass());
        arffCfg.setDirOut       (cfg.getDirOut());
        arffCfg.setBinary       (cfg.isBinary());
        arffCfg.setTF           (cfg.isTF());
        arffCfg.setTFIDF        (cfg.isTFIDF());
        arffCfg.setSparse       (cfg.isSparse());
        arffCfg.setReplaceStem  (cfg.isReplaceStem());
        
        GenerateARFF(arffCfg);
        
        ShowStatus.setMessage("Document-term matrix was generated.");
    }
    
    //Function to return the labels of text documents. Directories are treated as labels.
    public static HashMap<String,String> GetTextCollectionClasses(ArrayList<File> filesIn, ArrayList<String> allClasses){
        HashMap<String,String> classes = new HashMap<>();
        
        for(int i=0;i<filesIn.size();i++){
            String arquivo = filesIn.get(i).getAbsolutePath();
            arquivo = arquivo.replace("\\", "/");
            arquivo = arquivo.substring(0,arquivo.lastIndexOf("/"));
            arquivo = arquivo.substring(arquivo.lastIndexOf("/") + 1, arquivo.length());
            classes.put(filesIn.get(i).getAbsolutePath(), arquivo);
            if(!allClasses.contains(arquivo)){
                allClasses.add(arquivo);
            }
        }
        return classes;
    }
    
    
//    public static boolean GenerateARFF(ArrayList<String> names, String relacao, boolean classe, ArrayList<String> infClasses, int numFiles, int numTerms, File dirOut, ArrayList<File> filesIn, ArrayList<FeatureList> atributos, boolean binary, boolean TF, boolean TFIDF, HashMap<String,String> classes, boolean sparse, Object[] orderedFiles, HashMap<String,Integer> termDF, boolean replaceStem, HashMap<String,String> stem_palProc, HashMap<String,String> palProc_pal){

    
    public static boolean GenerateARFF(M4MArffGenerationParameters cfg){
        
        try{
            ShowStatus.setMessage("Saving ARFF file...");
    
            if (! cfg.getDirOut().exists()) {
                ShowStatus.setMessage("Creating output directory " + cfg.getDirOut());
                cfg.getDirOut().mkdir();
            }
            
//          BufferedWriter outputArff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cfg.getDirOut() + "/" + cfg.getRelacao() + ".arff"), charCode));
//          File arffFile = new File(cfg.getDirOut() + "/" + cfg.getRelacao() + ".arff");
            BufferedWriter outputArff = M4MFiles.getBufferedWriter(MeetingMiner.getArffFile());

            //Saving Header
            
            outputArff.write("@RELATION " + cfg.getRelacao());
            outputArff.newLine();
            outputArff.write("");
            outputArff.newLine();
            
            outputArff.write("@ATTRIBUTE name_file STRING\n");
            
            if(cfg.isReplaceStem() == true){
                for(int i=0; i < cfg.getNames().size(); i++){
                    outputArff.write("@ATTRIBUTE " + cfg.getPalProc_pal().get(cfg.getStem_palProc().get(cfg.getNames().get(i))) + " REAL\n");
//                    M4MShowStatus.setMessage(cfg.getNames().get(i) + " ========================= " + cfg.getPalProc_pal().get(cfg.getStem_palProc().get(cfg.getNames().get(i))));
                }
            }else{
                for(int i=0; i < cfg.getNames().size(); i++){
                    
                    
                    outputArff.write("@ATTRIBUTE " + cfg.getNames().get(i) + " REAL\n");
                }
            }
            
                
            if(cfg.isClasse()){
                outputArff.write("@ATTRIBUTE class_atr {");
                StringBuilder lineClasse = new StringBuilder();
                for(int j=0;j<cfg.getInfClasses().size();j++){
                    lineClasse.append(cfg.getInfClasses().get(j)+",");
                }
                String todasClasses = lineClasse.toString().substring(0,lineClasse.toString().length()-1);
                outputArff.write(todasClasses + "}\n");
            }
            outputArff.flush();
            
            outputArff.write("");
            outputArff.newLine();
            outputArff.write("@DATA");
            outputArff.newLine();

            
            double[] data = new double[cfg.getNumTerms()];
            
            if(cfg.isClasse()){
                cfg.setNumTerms(cfg.getNumTerms() - 1);
            }
            
            int numDocs = cfg.getOrderedFiles().length;
            
            for(int i=0;i < cfg.getNumFiles();i++){
                for(int j=0;j < cfg.getNumTerms();j++){
                    data[j] = 0;
                }
                FeatureList atrbs = cfg.getAtributos().get(i);
                for(int j=0;j<atrbs.getFeatures().size();j++){
                    TermValue item = atrbs.getFeature(j);
                    int pos = cfg.getNames().indexOf(item.getFeature());
                    // Verica as opções de peso do usuário
                    if(cfg.isTF()){ // TF
                        data[pos + 1] = item.getFrequency();
                    }else{
                        if(cfg.isTFIDF()){ // TFIDF
                            data[pos + 1] = item.getFrequency() * (Math.log10((double)numDocs/(double)(1 + cfg.getTermDF().get(item.getFeature()))));
                        }else{
                            if(item.getFrequency() > 0){ //Binary
                                data[pos + 1] = 1;
                            }else{
                                data[pos + 1] = 0;
                            }    
                        }
                    }
                    
                }
                StringBuffer line = new StringBuffer();
                File file = (File)cfg.getOrderedFiles()[i];
                
                if(cfg.isSparse()){
                    line.append("0" + " \"" + file.getAbsoluteFile().toString().replace("\\","/") + "\",");
                    for(int j=1;j<cfg.getNumTerms();j++){
                        if(data[j] > 0){
                            if(cfg.isTFIDF()){
                                line.append((j) + " " + String.format(Locale.ENGLISH, "%.4f", data[j]) + ",");
                            }else{
                                line.append((j) + " " + data[j] + ",");
                            }    
                        }
                    }
                }else{
                    line.append(file.getAbsoluteFile() + ",");
                    for(int j=1;j < cfg.getNumTerms();j++){
                        if(cfg.isTFIDF()){
                            line.append(String.format(Locale.ENGLISH, "%.4f", data[j]) + ",");
                        }else{
                            line.append(data[j] + ",");
                        }
                    }
                }
                
                if(cfg.isSparse()){
                    if(cfg.isClasse()){
                        line = line.append((cfg.getNumTerms()) + " " + "\"" + cfg.getClasses().get(cfg.getFilesIn().get(i).getAbsolutePath()) + "\",");
                    }
                }else{
                    if(cfg.isClasse()){
                        line = line.append("\"" + cfg.getClasses().get(cfg.getFilesIn().get(i).getAbsolutePath()) + "\",");
                    }    
                }
                
                
                String output = line.substring(0,line.length()-1);
                if(cfg.isSparse()){
                    outputArff.write("{" + output + "}" + "\n");
                }else{
                    outputArff.write(output + "\n");
                }
                

            }

            outputArff.flush();
            outputArff.close();

        }

        catch(IOException ex){
            System.err.println("Error when saving document-term matrix.");
//            ex.printStackTrace();
            System.exit(0);
        }
        ShowStatus.setMessage("ARFF file was generated");
        return true;
    }
    
}

    