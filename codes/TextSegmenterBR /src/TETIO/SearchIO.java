//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TETIO;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SearchIO {

    public static String SalvaXML(){
        String caminho = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(JFileChooser.FILES_ONLY);
        open.setDialogTitle("Topic Structure (SVM)");
        open.setDialogType(JFileChooser.SAVE_DIALOG);
        open.setFileFilter(new FileNameExtensionFilter("eXtensible Markup Language (*.xml)", "xml"));
        open.showSaveDialog(null);
        if(!(open.getSelectedFile()==null)){
            caminho = open.getSelectedFile().toString();
        }
        if(!caminho.endsWith(".xml")){
            caminho = caminho + ".xml";
        }
        return caminho;
    }
    
    public static String AbreCSV(String title){
        String caminho = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(JFileChooser.FILES_ONLY);
        open.setDialogTitle(title);
        open.setDialogType(JFileChooser.OPEN_DIALOG);
        open.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        open.showOpenDialog(null);
        if(!(open.getSelectedFile()==null)){
            caminho = open.getSelectedFile().toString();
        }
        return caminho;
    }
    
    public static String AbreDir(){
        String diretorio = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(open.DIRECTORIES_ONLY);
        open.setDialogTitle("Selecione o Diretório");
        open.setDialogType(open.OPEN_DIALOG);
        open.showOpenDialog(null);
        if(!(open.getSelectedFile()==null)){
            diretorio = open.getSelectedFile().toString();
        }
        return diretorio;
    }

    public static String AbreArq(){
        String arquivo = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(open.FILES_ONLY);
        open.setDialogTitle("Selecione o Arquivo");
        open.setDialogType(open.OPEN_DIALOG);
        open.showOpenDialog(null);
        if(!(open.getSelectedFile()==null)){
            arquivo = open.getSelectedFile().toString();
        }
        return arquivo;
    }
}
