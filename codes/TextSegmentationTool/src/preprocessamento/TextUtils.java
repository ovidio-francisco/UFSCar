package preprocessamento;

import java.util.ArrayList;

/**
 * @author ovidiojf
 */
public class TextUtils {
	private static final ArrayList<String> abrevs = new ArrayList<>();
	
	static {
		abrevs.add("Sr.");
		abrevs.add("Sra");
		abrevs.add("Prof.");
		abrevs.add("Profa.");
		
		for (int i=0; i<abrevs.size(); i++) {
			abrevs.set(i, abrevs.get(i).toLowerCase()); // tudo minúsculo
		}
		
	}
	
        public static ArrayList<String> getWords(String txt) {
            ArrayList<String> result = new ArrayList<>();
            txt = indentifyEndOfSentence(txt, " ");
            
            txt = Cleaner.clean(txt);
            
//            String regex = "[\"\')}]\\.|[\\!\\?]"; //          ["')}]\.|[\!\?]
            String regex = "[\n ]";
            String[] words = txt.split(regex);
            
            for(String w : words) {
                if (w.trim().length() < 3   ) continue;
                if (StopWords.isStopWord(w.trim().toLowerCase())) continue;
                
                if(!result.contains(w)){
                    result.add(w);
                }
            }
            
            return result;
        }
    
	public static String indentifyEndOfSentence(String txt, String eosMark) {

		txt = Cleaner.removeDoubleSpaces(txt);
		
//		[1] - ok - All ?! are EOS

//		[^ ]\?\ 
//		txt = txt.replaceAll("[^ ]\\?\\ ", "?"+eosMark);
		
		txt = txt.replace("!", "!"+eosMark); 

		String[] tokens = txt.split(" ");
		
		for(int i=0; i<tokens.length-1; i++) {
			
			if (tokens[i].length() == 1) continue;
			if (!( tokens[i].endsWith(".") || tokens[i].endsWith("?"))) continue;
			if (isAbreviation(tokens[i])) continue;

//			[5] If the token to which the period is attached is capitalized and is < 5 characters 
//			and the next token begins uppercase, it is not EOS
//			Contra exemplo : "Bla bla bla bla bla Ana. Bla bla bla bla bla."
/*
			if (isUppercase(tokens[i]) && (tokens[i].length() < 5) && isUppercase(tokens[i+1])) {
				System.out.println("5");
				continue;  
			}
*/
			
//			[6] If the token to which the period is attached has other periods, it is not EOS
//			Contra exemplo: "Bla bla bla bla bla no endereço ufscar.com.br. Bla bla bla bla bla."
/*
			if (tokens[i].substring(0, tokens[i].length()-1).contains(".")) {
				System.out.println(6);
				continue;
			}
 */
			
//			[7] If the token to which the period is attached has < 2 characters, it is not EOS
//			Contra exemplo : "Bla bla bla bla bla bloco A. Bla bla bla bla bla."
/*
			if (tokens[i].length()<3) { // 3 pq conto o '.' "s.".len = 2
//				System.out.println(7);
				continue;
			}
 */
			
//			[8] If the next token following whitespace begins with $({["' it is EOS 
			if ( "$({[\"'".contains(""+tokens[i+1].charAt(0))) {
				tokens[i] = tokens[i] + eosMark;
				continue;
			}
			
			
//			[9] If the token to which the period is attached begins with a lowercase letter 
//			and the next token following whitespace is uppercase, it is EOS
			if (/*isLowercase(tokens[i]) &&*/ isCapitalized(tokens[i+1])) {
				tokens[i] = tokens[i] + eosMark;
				continue;
			}
			
//			[2] - ok - If " or ' appears before period, it is EOS
//			[3] - ok - If )}] before period, it is EOS
			if ("\"')}]".contains(""+tokens[i].charAt(tokens[i].length()-2))) {
				tokens[i] = tokens[i] + eosMark;
//				continue;
			}
		}
		
		String result = "";

		for(String s : tokens) {
			result = result.concat(s).concat(" ");
		}
		
		return result;
	}

        private static boolean isCapitalized(String s) {
		return (s.charAt(0) >= 'A') && (s.charAt(0) <= 'Z'); 
	} 
	public static boolean isAbreviation(String token) {
		return abrevs.contains(token.toLowerCase()) || (token.length() <3 && isCapitalized(token));
	}


}
