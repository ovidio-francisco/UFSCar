
package m4mUtils;

/**
 *
 * @author ovidiojf
 */
public class M4MCleaner {

    private static final int MAXCHARS = 3000;
    private static final char BLANKCHAR = ' ';
	
    static final char[] ACCENTEDS_LOW      = {'â', 'ê', 'î', 'ô', 'û', 'á', 'é', 'í', 'ó', 'ú', 'ý', 'à', 'è', 'ì', 'ò', 'ù', 'ä', 'ë', 'ï', 'ö', 'ü', 'ã', 'õ', 'ç'};
    static final char[] ACCENTEDS_UP       = {'Â', 'Ê', 'Î', 'Ô', 'Û', 'Á', 'É', 'Í', 'Ó', 'Ú', 'Ý', 'À', 'È', 'Ì', 'Ò', 'Ù', 'Ä', 'Ë', 'Ï', 'Ö', 'Ü', 'Ã', 'Õ', 'Ç'};
    static final char[] NOACCENTEDS_LOW    = {'a', 'e', 'i', 'o', 'u', 'a', 'e', 'i', 'o', 'u', 'y', 'a', 'e', 'i', 'o', 'u', 'a', 'e', 'i', 'o', 'u', 'a', 'o', 'c'};

    static final char[] LETTERS_AZ_UP      = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W', 'Z', 'Ñ'};
    static final char[] LETTERS_AZ_LOW     = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'w', 'z', 'ñ'};
	
    static final char[] POINTS             = {' ' ,  '.', '@',   '+', '*',  '-' };
    static final char[] SPACES             = {' ' ,  ' ', ' ',   ' ', ' ',  ' ' };
    
    static final char[] ALL      = new char[MAXCHARS];
    static final char[] ALLOWEDS = new char[MAXCHARS];

    static {
    	for(int i=0; i<MAXCHARS; i++) {
    		ALLOWEDS[i] = BLANKCHAR;
    	}

    	replaceChars(ALLOWEDS, LETTERS_AZ_LOW, LETTERS_AZ_LOW);
    	replaceChars(ALLOWEDS, LETTERS_AZ_UP , LETTERS_AZ_LOW);
    	replaceChars(ALLOWEDS, ACCENTEDS_LOW , NOACCENTEDS_LOW);
    	replaceChars(ALLOWEDS, ACCENTEDS_UP  , NOACCENTEDS_LOW);
    	replaceChars(ALLOWEDS, POINTS        , SPACES);
    }
	
    private static void replaceChars(char[] target, char[] replaceds, char[] substitutes) {
    	for(int i=0; i<replaceds.length; i++) {
    		target[replaceds[i]] = substitutes[i];
    	}
    }
	
    private static char getAllowedChar(char c) {
    	return ALLOWEDS[c];
    }
    
    public static String clean(String str) {
        char[] chars = new char[str.length()];

        int len   = str.length();
        int i = 0;

        if(len==0) return str;

        while (getAllowedChar(str.charAt(i)) == BLANKCHAR) {
            i++;
            if (i >= len) return "";
        }

        int j=-1;

        while(true) {
            chars[++j] = getAllowedChar(str.charAt(i++));

            if (i >= len) break;

            while(chars[j] == BLANKCHAR && getAllowedChar(str.charAt(i)) == BLANKCHAR) {
                i++;
                if (i >= len) 
                    return new String(chars, 0, j);
            }
        }

        return new String(chars, 0, j+1);
    }
    
}
