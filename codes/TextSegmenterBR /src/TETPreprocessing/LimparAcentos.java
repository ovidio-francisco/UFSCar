/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TETPreprocessing;

import java.util.HashMap;

/**
 *
 * @author Gaia
 */
public class LimparAcentos {
    
    public static String Limpar(String txt, HashMap<String,String> termProc_term){
        String[] parts = txt.split(" ");
        for(int part=0;part<parts.length;part++){
            if(parts[part].trim().length() != 0){
                String palavraOriginal = parts[part];
                String palavraProcessada = ProcessarTermo(palavraOriginal);
                if(!termProc_term.containsKey(palavraProcessada)){
                    termProc_term.put(palavraProcessada, palavraOriginal);
                }
            }
        }
        txt = ProcessarTermo(txt);
        return txt;
    }    
    public static String ProcessarTermo(String termo){
                
        termo = termo.replace("á", "a");
        termo = termo.replace("à", "a");
        termo = termo.replace("ã", "a");
        termo = termo.replace("â", "a");
        termo = termo.replace("é", "e");
        termo = termo.replace("ê", "e");
        termo = termo.replace("í", "i");
        termo = termo.replace("ó", "o");
        termo = termo.replace("õ", "o");
        termo = termo.replace("ô", "o");
        termo = termo.replace("ú", "u");
        termo = termo.replace("ü", "u");
        termo = termo.replace("ç", "c");
        
        return termo;
    }
}
