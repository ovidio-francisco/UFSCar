//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package topicExtraction.TETPreprocessing;

import java.io.RandomAccessFile;
import java.util.HashSet;

import javax.swing.JOptionPane;

import utils.ShowStatus;

public class StopWords {
//    static ArrayList<String> list = new ArrayList<String>();
    static HashSet<String> list = new HashSet<>();

    public StopWords(String language){
//        JOptionPane.showMessageDialog(null, "Criando StopWord, language =  "+language);
        if(language.equals("port")){
            try{
                RandomAccessFile arqStop = new RandomAccessFile("stopPort.txt", "r");
                ShowStatus.setMessage("Aquivo '"+arqStop.toString()+"'carregado");
//                JOptionPane.showMessageDialog(null, "Aquivo '"+arqStop.toString()+"'carregado - Finalmente!!");
                String line = "";
                while((line = arqStop.readLine())!=null){
                    if(line.length()>0){
                        String elem = new String(line);
                        list.add(elem);
                    }
                }
                arqStop.close();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error when reading stopwords file (stopPort.txt).", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(0);
            }   

            /** Lista com Stopwords extras - by ojf */
            try{
                RandomAccessFile arqStop = new RandomAccessFile("stopPort2.txt", "r");
                ShowStatus.setMessage("Aquivo '"+arqStop.toString()+"'carregado");
                String line = "";
                while((line = arqStop.readLine())!=null){
                    if(line.length()>0){
                        String elem = new String(line);
                        list.add(elem);
                    }
                }
                arqStop.close();
            }catch(Exception e){
            	ShowStatus.setMessage("Error when reading stopwords file (stopPort2.txt).");
                e.printStackTrace();
                System.exit(0);
            }   
        }else{
            try{
                RandomAccessFile arqStop = new RandomAccessFile("stopIngl.txt", "r");
                String line = "";
                while((line = arqStop.readLine())!=null){
                    if(line.length()>0){
                        String elem = new String(line);
                        list.add(elem);
                    }
                }
                arqStop.close();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error when reading stopwords file (stopIngl.txt)", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(0);
            }
        }
        
    }
    
    public boolean isStopWord(String str){

        if(list.contains(str)){
            return true;
        }else{
            return false;
        }
    }
    
    public String removeStopWords(String str){
        String[] terms = str.split(" ");
        String new_str = "";

        for(int i=0; i < terms.length; i++){
            String termo = terms[i];
            if(termo.startsWith("\n")){
                new_str = new_str.concat("\n");
            }
            termo = termo.trim();
            boolean quebra = false;
            if(termo.contains("\n")){
                quebra = true;
            }
            String[] terms2 = termo.split("\n");
            for(int j=0; j < terms2.length; j++){
                String termo2 = terms2[j].trim();
                if(j == terms2.length - 1){
                    quebra = false;
                }
//                System.out.println(String.format("isStopWord '%s' --> %b", termo2, isStopWord(termo2)));
                if(!isStopWord(termo2)){
                        if(termo2.equals(".")){
                            new_str=new_str.concat(" . ");
                            if(quebra == true){new_str=new_str.concat("\n");}
                            continue;
                        }
                        if(!(termo2.length()<=2)){
                            new_str=new_str.concat(termo2+" ");
                            if(quebra == true){new_str=new_str.concat("\n");}
                            continue;
                        }   

                }else{
                    new_str = new_str + " @ ";
                    if(quebra == true){new_str=new_str.concat("\n");}
                }    
            }
        }
        return  new_str.trim();
        
    }
}
