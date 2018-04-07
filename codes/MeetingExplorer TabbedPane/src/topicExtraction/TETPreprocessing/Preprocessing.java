//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 26, 2015
// Description: Steps to generate a document-term matrix from a text collections
//*****************************************************************************

package topicExtraction.TETPreprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ptstemmer.Stemmer;
import topicExtraction.TETStructures.TermValue;
import utils.Files;
import utils.ShowStatus;

public class Preprocessing {
    
    /* Function to extract features from a textual document
       file - path of textual document
       lang [port|engl] - laguage a textual document (only portugues and english
       remStopWords - remove stopwords
       stemming - replace the word by their stem
       stemTerm - mapping from stemmed temrs to original words
       terms - list of generated terms
       termDF - document frequency of terms
       sw - instance of the StopWords class
       cln - instance of the Cleaner class
       stemPt - instance of the Stemmer class
       stemEn - instance of the StemmerEn class
    */ 
    
    public static ArrayList<TermValue> FeatureGeneration(
        File file                            , String lang                     , boolean remStopWords    , boolean stemming, 
        HashMap<String,String> termProc_term , HashMap<String,String> stemTerm , ArrayList<String> terms , 
        HashMap<String,Integer> termDF       , StopWords sw                    , Cleaner cln             , 
        Stemmer stemPt                       , StemmerEn stemEn                , boolean replaceStem) {
        
        
        ArrayList<TermValue> atributos = new ArrayList<>();
        
//        StringBuilder txt = new StringBuilder();
          String txt="";	
        
        try{
            if (!file.exists()) {
                ShowStatus.setMessage("File not found: " +file.getAbsolutePath());
            }
//            RandomAccessFile txtFile = new RandomAccessFile(file, "r");
//            String line;
//            while( (line = txtFile.readLine()) != null ) { txt.append(line + " "); } // Leitura do fileuivo texto e armazenamento na variável txt
//            
//            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + txt.toString());
//            
//            txtFile.close();
            
              txt = Files.loadTxtFile(file);
            
        }catch(Exception e){
            System.err.println("Error when reading the file " + file.getAbsolutePath() + ".");
            System.exit(0);
        }

        String cleanText;// = cln.clean(txt.toString()); //Text cleaning

//        if (false) {
////            cleanText = m4mUtils.M4MCleaner.clean(txt.toString());
//        }
//        else {
//            cleanText = cln.clean(txt.toString()); //Text cleaning
            cleanText = cln.clean(txt); //Text cleaning

            
//        }

        cleanText = LimparAcentos.Limpar(cleanText, termProc_term);
        
        if(remStopWords==true){
            cleanText = sw.removeStopWords(cleanText); //Stopwords removal
        }
        else {
        }
            

        ArrayList<String> words = new ArrayList<>();
        HashMap<String, Double> hashTermFreq = new HashMap<>();
        String[] allWords = cleanText.split(" "); //Stores the words of a text in a vector
        
        if(stemming == true){
            if(lang.equals("port")){ //Word stemming
                for(int i = 0;i<allWords.length;i++){
                    String term = allWords[i];
                    String stem = stemPt.wordStemming(term);
                    
//                    M4MShowStatus.setMessage(term + " ||||||||||||||||||||||||||||||||| " + stem);
                    
                    stemTerm.put(stem, term);
                    if(hashTermFreq.containsKey(stem)){
                        Double freq = hashTermFreq.get(stem);
                        hashTermFreq.put(stem, freq + 1);
                    }else{
                        if(stem.length()>1){
                            hashTermFreq.put(stem, 1.0);
                            if(!terms.contains(stem)){
                                terms.add(stem);
                            }
                            if(!words.contains(stem)){
                                words.add(stem);
                                if(termDF.containsKey(stem)){
                                    int value = termDF.get(stem);
                                    value++;
                                    termDF.put(stem, value);
                                }else{
                                    termDF.put(stem, 1);
                                }
                            }
                        }
                    }
                    
                }
            }else{
                for(int i = 0;i<allWords.length;i++){
                    String term = allWords[i];
                    term = term.trim();
                    String stem = StemmerEn.get(term);
                    if(!stemTerm.containsKey(stem)){
                        stemTerm.put(stem, term);
                    }
                    

                    if(hashTermFreq.containsKey(stem)){
                        Double freq = hashTermFreq.get(stem);
                        hashTermFreq.put(stem, freq + 1);
                    }else{
                        if(stem.length()>1){
                            hashTermFreq.put(stem, 1.0);
                            if(!terms.contains(stem)){
                                terms.add(stem);
                            }
                            if(!words.contains(stem)){
                                words.add(stem);
                                if(termDF.containsKey(stem)){
                                    int value = termDF.get(stem);
                                    value++;
                                    termDF.put(stem, value);
                                }else{
                                    termDF.put(stem, 1);
                                }
                            }
                        }
                        
                    }
                }
            }
        }else{
                for(int i = 0;i<allWords.length;i++){
                    String term = allWords[i];
                    term = term.trim();
                    if(!stemTerm.containsKey(term)){
                        stemTerm.put(term, term);
                    }
                    
                    if(hashTermFreq.containsKey(term)){
                        Double freq = hashTermFreq.get(term);
                        hashTermFreq.put(term, freq + 1);
                    }else{
                        if(term.length()>1){
                            hashTermFreq.put(term, 1.0);
                            if(!terms.contains(term)){
                                terms.add(term);
                            }
                            if(!words.contains(term)){
                                words.add(term);
                                if(termDF.containsKey(term)){
                                    int value = termDF.get(term);
                                    value++;
                                    termDF.put(term, value);
                                }else{
                                    termDF.put(term, 1);
                                }
                            }
                        }
                    }
                }
        }

        Set<String> termList =  hashTermFreq.keySet();
        Object[] termArray = termList.toArray();
        for(int i=0;i<termArray.length;i++){
            String key = termArray[i].toString();
            atributos.add(new TermValue(key,hashTermFreq.get(key)));
        }
        return atributos;
        
        /*Set<String> termList =  hashTermFreq.keySet();
        Object[] termArray = termList.toArray();
        for(int i=0;i<termArray.length;i++){
            String key = termArray[i].toString();
            if(replaceStem){
                atributos.add(new TermFreq(termProc_term.get(termStem.get(key)),(double)hashTermFreq.get(key)));
            }else{
                atributos.add(new TermFreq(key,(double)hashTermFreq.get(key)));
            }
            
        }
        return atributos;*/
    }


}
