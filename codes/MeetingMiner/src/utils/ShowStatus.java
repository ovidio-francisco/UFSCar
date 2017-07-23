package utils;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class ShowStatus {
	
	private static JLabel label = null;
	private static JProgressBar progressBar = null;
	private static JTextArea textArea = null;
	private static boolean verbose = false;

	public static void setMessage(String message) {
        
		System.out.println(message);

		if(label != null) label.setText(message);
        
        if (textArea != null) {
            textArea.append(message + "\n");
            try {
				textArea.setCaretPosition(textArea.getLineStartOffset(textArea.getLineCount() - 1));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
        }
    }
	
	
	public static void setMessageVerbose(String message) {
		if (verbose) {
			setMessage(message);
		}
	}

	
    public static void setProgress(int value) {
        if (progressBar != null) progressBar.setValue(value);
    }
    
    public static JLabel getLabel() {
        return label;
    }

    public static void setLabel(JLabel label) {
        ShowStatus.label = label;
    }

    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(JProgressBar progressBar) {
        ShowStatus.progressBar = progressBar;
    }

    public static JTextArea getTextArea() {
        return textArea;
    }

    public static void setTextArea(JTextArea textArea) {
        ShowStatus.textArea = textArea;
    }
    
    

}
