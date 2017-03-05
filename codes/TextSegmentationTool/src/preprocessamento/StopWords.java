/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessamento;

import java.io.File;
import java.util.ArrayList;
import static utils.Utils.loadTxtFileToArray;

/**
 * @author ovidiojf
 */

public class StopWords {
    
	private static ArrayList<String> lista = new ArrayList<>();

	static {
                File f = new File("./stopPort.txt");
                
                if(f.exists()) {
                    lista = loadTxtFileToArray(f);
                }
                else {
                    System.out.println("Lista de stopwords não encontrada!!!");
                }
                    
	}
	
	public static boolean isStopWord(String str) {
		return lista.contains(str);
	}
	
	public static String removeStopWords(String txt) {
		StringBuilder sb = new StringBuilder();
		
		for(String s : txt.split(" ")) {
			if (!isStopWord(s)) {
				sb.append(s+" ");
			}
		}
		
		return sb.toString();
	}
}
