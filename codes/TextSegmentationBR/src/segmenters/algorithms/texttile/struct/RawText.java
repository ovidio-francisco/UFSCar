package segmenters.algorithms.texttile.struct;

import java.util.*;

import segmenters.algorithms.TextTilingBR;

import java.io.*;


/**
 * A text collection for segmentation. Use resource file 2 to
 * convert a Brown corpus file into raw text which is used by
 * this class.
 * Creation date: (07/12/99 00:37:12)
 * @author: Freddy Choi
 */
public class RawText {
	/* Information from the source */
	public Vector<String> text = new Vector<String>(300,50); // Source text
	public Vector<Integer> boundaries = new Vector<Integer>(100,20); // Sentence boundaries

/**
 * Collection constructor comment.
 */
public RawText() {
	super();
}


/**
 * Load a collection from input stream
 * Creation date: (07/12/99 00:37:40)
 * @param file java.lang.String
 */
public RawText(InputStream in) {
	try {
		/* Open the file and attach stream tokenizer */
		Reader r = new BufferedReader(new InputStreamReader(in));
		parse(r);
		r.close();
	}
	catch (Exception e) {}
}


/**
 * Load a collection from disk
 * Creation date: (07/12/99 00:37:40)
 * @param file java.lang.String
 */
public RawText(File file) {
	try {
		/* Open the file and attach stream tokenizer */
		Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		parse(r);
		r.close();
	}
	catch (Exception e) {
		System.err.println(String.format("Arquivo não encontrado: %s", file));
	}
}


/**
 * Parse a formatted stream.
 * Creation date: (07/12/99 00:40:21)
 * @param reader java.io.Reader
 */
protected void parse(Reader reader) {

	/* 1. Setup syntax table for the tokeniser */
	StreamTokenizer tk = new StreamTokenizer(reader);
	tk.resetSyntax();
	tk.wordChars('\u0021', '\u00FF');
	tk.whitespaceChars('\u0000', '\u0020');
	tk.eolIsSignificant(true);
	tk.lowerCaseMode(false);                          //lembrar de verificar o lowercase ***************

	/* 2. Define variables */
	int word = 0;           // Absolute word count, first word is word 0
	int prev_word = -1;     // The location of the previous boundary
//	int sep;                // Position of the / separator.

	/* 3. Add the implicit boundary at the
	beginning of the collection */
 	boundaries.addElement(new Integer(word));
		
	/* 4. Parse stream */
	try {
		while (tk.nextToken() != StreamTokenizer.TT_EOF) {
			/* Case 1. End of sentence */
			if (tk.ttype == StreamTokenizer.TT_EOL && word != prev_word) { 
				boundaries.addElement(new Integer(word));
				prev_word = word; 
			}
			/* Case 2. Token */
			else if (tk.ttype == StreamTokenizer.TT_WORD) {
				text.addElement(tk.sval);
				word++;
			}
		}
	}
	catch (IOException e) {}
}








public RawText(String txt) {
		parse2(txt);
}

private void parse2(String txt) {

	String words[] = txt.split(" ");
	
	int word_id = 0;              // Absolute word count, first word is word 0
//	int prev_boudary_id = -1;     // The location of the previous boundary
	
	/* 3. Add the implicit boundary at the
	beginning of the collection */
 	boundaries.addElement(new Integer(word_id));
	
	
	for(String word : words) {

		word_id++;
		
		if (word.endsWith(TextTilingBR.EOS_MARK) /*&& word_id != prev_boudary_id*/) {
			boundaries.addElement(new Integer(word_id));
//			prev_boudary_id = word_id; 
			
			if(true) word = word.substring(0, word.length()-TextTilingBR.EOS_MARK.length());
			
		}
		
		text.addElement(word);
	}
	
	boundaries.addElement(new Integer(word_id));
}



}
