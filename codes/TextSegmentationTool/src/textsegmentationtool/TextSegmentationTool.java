package textsegmentationtool;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import textsegmentationtool.mainFrame.MainFrame;
import textsegmentationtool.mainFrame.State;

/**
 * @author ovidiojf
 */




public class TextSegmentationTool {

    private static MainFrame mainFrame;
    
            /*
    
Decisao
    nada
    Discussao
    Orientacao
    solicitacao
Informe
    Nada
    Discussao
    Orientacao
    solicitacao
irrelevante
    
    */
    
    private static final String[][] TAGS = {
                                                {"decisão"     , "informe", "irrelevante"},
                                                {"discussão"   , "orientação", "solicitação"}
                                           };
    
    private static File CSV_FOLDER = new File("./CSV/");
    private static final File stateFile = new File("./savestate.dat");
    
    private static boolean enableOther = true;

    public static String[][] getTags(){ return TAGS;}

    public static File getCSV_FOLDER() {
        return CSV_FOLDER;
    }

    public static void setCSV_FOLDER(File CSV_FOLDER) {
        TextSegmentationTool.CSV_FOLDER = CSV_FOLDER;
    }
    
    public static Font getTextAreaFont() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    }
        
    public static void main(String[] args) {
        mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setTitle("TextSegmentationTool");
        mainFrame.setExtendedState( mainFrame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        mainFrame.setVisible(true);
        
        
        if(mainFrame.allDone()) {
            mainFrame.thanks();
            return;
        }
        
        if (stateFile.exists() && JOptionPane.showConfirmDialog(mainFrame, "Restaurar trabalho anterior?") == JOptionPane.YES_OPTION) {
            mainFrame.restore();
        }
        else {
            mainFrame.carregarDocumento();
        }
            
        
    }

    public static boolean isEnableOther() {
        return enableOther;
    }

    public static void setEnableOther(boolean enableOther) {
        TextSegmentationTool.enableOther = enableOther;
    }
    
    public static void saveState(ArrayList<SegmentPanel> segPanels, String editingText, File doc) {
        try {
            FileOutputStream fileOut = new FileOutputStream(stateFile);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            
            objOut.writeObject(new State(segPanels, editingText, doc));
            objOut.flush();
            objOut.close();
            
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
    
    public static State loadState( ) {
         
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(stateFile);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            State state = (State)objIn.readObject();
            
            fileIn.close();
            
            return state;
            
        } catch (FileNotFoundException ex) {
        } catch (IOException | ClassNotFoundException ex) {
        } 
        
        return null;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
    
    
    
}
