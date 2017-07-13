/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.mmUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;

/**
 *
 * @author ovidiojf
 */
public class M4MUtils {
    
    public static String searchFolder() {
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Selecione o diretório que contem os documentos");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setCurrentDirectory(new File("./docs"));
        
        fc.showOpenDialog(null);
        
//        if (fc.getSelectedFile() == null)
//            return null;

        return fc.getSelectedFile().toString();
    }

     /**
     * Convert a millisecond duration to a string format
     * 
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());
    }    
    
}
