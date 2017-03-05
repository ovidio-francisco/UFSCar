package preprocessamento;

/**
 * @author ovidiojf
 */
public class Cleaner {

        private static final char   MAX_CHAR = 65535; /*65535 para suportar char UTF-8*/
	private static final char[] ALLOWED         = {' ' , ' ', ' ', ' ', ' ' , ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};          
	private static final char[] NOT_ALLOWED     = {'\\', '/', '?', '!', '\n', '.', '(', ')', '[', ']', ';', ':', ',', '=', '-', '=', '@', '*'};       
//	private static final char[] UP_LETTERS	    = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W', 'Z'}; 
//	private static final char[] LOW_LETTERS	    = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'w', 'z'};
	private static final char[] TABLE = new char[MAX_CHAR];
	
	
	static {
		for (int i=0; i<MAX_CHAR; i++) {
			TABLE[i] = (char)i;
		}
		
		/* Insere cada letra minúscula na sua respectiva posição */
//		for (int i=0; i<LOW_LETTERS.length; i++) {
//			TABLE[LOW_LETTERS[i]] = LOW_LETTERS[i];
//		}
//		/* Insere cada letra minúscula na posição de sua respecitva maiúscula*/
//		for (int i=0; i<UP_LETTERS.length; i++) {
//			TABLE[UP_LETTERS[i]] = LOW_LETTERS[i];
//		}
		/* Insere cada caracter permitido na posição de seu respecitvo não permitido*/
		for (int i=0; i<NOT_ALLOWED.length; i++) {
			TABLE[NOT_ALLOWED[i]] = ALLOWED[i];
		}
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
    

    
}
