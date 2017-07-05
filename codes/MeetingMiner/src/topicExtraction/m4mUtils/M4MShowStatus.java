
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       8-1-2016
 * Last Modified 
 */

package topicExtraction.m4mUtils;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;


public class M4MShowStatus {

    public static void setMessage_old(String message) {
        
//        if(!enable) {
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            return;
//        }
        
        if(label != null) label.setText(message);
        
        if (textArea != null) {
            textArea.append(message + "\n");
        }
        
        System.out.println(message);
    }

    public static void setProgress(int value) {
        if (progressBar != null) progressBar.setValue(value);
    }
    
    public static JLabel getLabel() {
        return label;
    }

    public static void setLabel(JLabel label) {
        M4MShowStatus.label = label;
    }

    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(JProgressBar progressBar) {
        M4MShowStatus.progressBar = progressBar;
    }

    public static JTextArea getTextArea() {
        return textArea;
    }

    public static void setTextArea(JTextArea textArea) {
        M4MShowStatus.textArea = textArea;
    }
    
    
    
    private static JLabel label = null;
    private static JProgressBar progressBar = null;
    private static JTextArea textArea = null;

}
