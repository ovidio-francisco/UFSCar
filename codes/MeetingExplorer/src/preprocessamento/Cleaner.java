/**
 * 
 */
package preprocessamento;

import java.util.ArrayList;

/**
 * @author ovidiojf
 */

public class Cleaner {
    private static final char   MAX_CHAR = 65535; /*65535 para suportar char UTF-8*/
	private static final char[] ALLOWED_LOW         = {'a', 'e', 'i', 'o', 'u', 'a', 'e', 'i', 'o', 'u', 'y', 'a', 'e', 'i', 'o', 'u', 'a', 'e', 'i', 'o', 'u', 'a', 'o', 'c', 'ñ', 'Ñ'};          
	private static final char[] ALLOWED_UP          = {'A', 'E', 'I', 'O', 'U', 'A', 'E', 'I', 'O', 'U', 'Y', 'A', 'E', 'I', 'O', 'U', 'A', 'E', 'I', 'O', 'U', 'A', 'O', 'C', 'N', 'N'};
	private static final char[] NOT_ALLOWED_LOW     = {'â', 'ê', 'î', 'ô', 'û', 'á', 'é', 'í', 'ó', 'ú', 'ý', 'à', 'è', 'ì', 'ò', 'ù', 'ä', 'ë', 'ï', 'ö', 'ü', 'ã', 'õ', 'ç', 'ñ', 'Ñ'};       
	private static final char[] NOT_ALLOWED_UP      = {'Â', 'Ê', 'Î', 'Ô', 'Û', 'Á', 'É', 'Í', 'Ó', 'Ú', 'Ý', 'À', 'È', 'Ì', 'Ò', 'Ù', 'Ä', 'Ë', 'Ï', 'Ö', 'Ü', 'Ã', 'Õ', 'Ç', 'ñ', 'Ñ'};       
	private static final char[] UP_LETTERS	        = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W', 'Z'}; 
	private static final char[] LOW_LETTERS	        = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'w', 'z'};
	private static final char[] NUMBERS             = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] PUNCTUATION         = {'.', ',', ';', '?', '/', '\\', '!', '@', '#', '$', '%', '&', '*', '(', ')', '[', ']', '{', '}', '-', '_', '+', '=', '"', '\'', '<', '>', ':' };
	private static final char[] TABLE = new char[MAX_CHAR];
	
	
	static {
		makeTable();
	}
	
	private static void makeTable() {
		for (int i=0; i<MAX_CHAR; i++) {
			TABLE[i] = ' ';
		}
		
		/* Insere cada letra minúscula na sua respectiva posição */
		for (int i=0; i<LOW_LETTERS.length; i++) {
			TABLE[LOW_LETTERS[i]] = LOW_LETTERS[i];
		}
		/* Insere cada letra maiúscula na sua respectiva posição */
		for (int i=0; i<UP_LETTERS.length; i++) {
			TABLE[UP_LETTERS[i]] = UP_LETTERS[i];
		}

		/* Insere cada caracter permitido na posição de seu respecitvo não permitido*/
		for (int i=0; i<NOT_ALLOWED_LOW.length; i++){
			TABLE[NOT_ALLOWED_LOW[i]] = ALLOWED_LOW[i];
		}
		/* Insere cada caracter permitido na posição de seu respecitvo não permitido*/
		for (int i=0; i<NOT_ALLOWED_UP.length; i++){
			TABLE[NOT_ALLOWED_UP[i]] = ALLOWED_UP[i];
		}
		
	}
	
	public static void setNumbersAllowed() {
		for (int i=0; i<NUMBERS.length; i++) {
			TABLE[NUMBERS[i]] = NUMBERS[i];
		}
	}

	public static void setPunctuationAllowed() {
		for (int i=0; i<PUNCTUATION.length; i++) {
			TABLE[PUNCTUATION[i]] = PUNCTUATION[i];
		}
	}
	
	public static void setAllowedChars(boolean allowNumbers, boolean allowPunctuation) {
		makeTable();
		if(allowNumbers)     setNumbersAllowed();
		if(allowPunctuation) setPunctuationAllowed();
	}

    public static String clean(String str) {
    	char[] result = str.toCharArray();
    	
    	for (int i=0; i<result.length; i++) {
    		result[i] = TABLE[result[i]];
    	}
    	
    	return new String(result);
    }

    public static String removeDoubleSpaces(String str) {
	    String result = str;
    	int aux = 1;
	    while(aux != 0){ 
	    	int tam_1 = result.length();
	    	result = result.replace("  "," ");
	    	int tam_2 = result.length();
	    	aux = tam_1 - tam_2;
	    }  
	       
    	return result;
    }
    
    public static String removeChars(String token, char[] remove) {
    	StringBuilder result = new StringBuilder();
    	
    	ArrayList<Character> notAllowed = new ArrayList<>();
    	
    	for(char c : remove) {
    		notAllowed.add(c);
    	}
    	
    	for(char c : token.toCharArray()) {
    		if(!notAllowed.contains(c))
    			result.append(c);
    	}
    	
    	return result.toString();
    }
    
    public static String removePunctuation(String token) {
    	return removeChars(token, PUNCTUATION);
    }
    
}
