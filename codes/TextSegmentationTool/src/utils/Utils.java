
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.commons.csv.CSVFormat;
import textsegmentationtool.mainFrame.MainFrame;
import org.apache.commons.csv.CSVPrinter;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import textsegmentationtool.TextSegmentationTool;


/**
 * @author ovidiojf
 */

public class Utils {

    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";
    
    private static String MMDefautCharSet = ISO_8859_1;
    
    
    public static BufferedReader getBufferedReader(File file) {
        BufferedReader result = null;
        
        try {
            result = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), MMDefautCharSet));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao ler o arquivo"+file);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public static String getFileExtension (File file) {
        String fileName = file.getName();
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else 
            return "";
    }

    public static FileFilter getM4M_TxtFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || (getFileExtension(file).compareTo("txt")==0);
            }
        };
    }  
    
    
    public static void listTxts(File dirIn, ArrayList<File> filesIn){
        File[] files = dirIn.listFiles(getM4M_TxtFileFilter());
        for(int i=0;i<files.length;i++){
            if(!files[i].isDirectory()){
                filesIn.add(files[i]);
            }else{
                listTxts(files[i], filesIn);
            }
        }
    }
    
    
	public static String loadTxtFile(File file) {

        String line;
        StringBuilder buffer = new StringBuilder();

        try {
            BufferedReader text = getBufferedReader(file);  
            
            while((line = text.readLine()) != null){
                buffer.append(line + "\n");
            }
                        
        } catch (IOException ex) {
            System.err.println("Houve um erro ao abrir o arquivo " + file);
        }

        return buffer.toString();
    }
        
            public static void saveTxtFile(String txt, File txtFile) {
        BufferedWriter out = getBufferedWriter(txtFile); 

        try {
			out.write(txt);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.err.println("Erro ao salvar o arquivo " + txtFile.getName());
			e.printStackTrace();
		}
    }

        
    public static ArrayList<String> loadTxtFileToArray(File file) {

        String line;
        ArrayList<String> result = new ArrayList<>();

        try {
            BufferedReader text = getBufferedReader(file);  
            
            while((line = text.readLine()) != null){
                result.add(line);
            }
                        
        } catch (IOException ex) {
            System.err.println("Houve um erro ao abrir o arquivo " + file);
            ex.printStackTrace();
        }
        return result;
    }
        
        
    public static String loadDocumentFile(File file) {
        return docToString(file);
    }    
        
    public static BufferedWriter getBufferedWriter(File file) {
        //  BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + output),"ISO-8859-1"));
        BufferedWriter result = null;
        
        try {
            result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), MMDefautCharSet));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao escrever o arquivo: "+file);
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Erro ao escrever o arquivo"+file + ": Charset "+MMDefautCharSet+" não suportado");
        }
        
        return result;
    }
            
        
    public static CSVPrinter getCSVPrinter(File file) {
    	Appendable out = getBufferedWriter(file);
    	
    	try {
		return new CSVPrinter(out, CSVFormat.DEFAULT);
	} catch (IOException e) {
		System.err.println("Não foi possível criar o arquivo " + file);
		return null;
	}
    }
        
    public static void closeCSV(CSVPrinter csv) {
    	try {
    	csv.flush();
            csv.close();
	} catch (IOException e) {
            System.err.println("Não foi possível criar o arquivo csv");
	}
    }
    
    public static File getCSVFile(File file) {
        
        if (!TextSegmentationTool.getCSV_FOLDER().exists()) {
            TextSegmentationTool.getCSV_FOLDER().mkdir();
        }
        
        return new File(TextSegmentationTool.getCSV_FOLDER() + "/" + file.getName()+".csv");
    }
            
    
    public static boolean existsCSV(File file) {
        return getCSVFile(file).exists();
    }
    
    public static FileFilter getM4M_FileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (!existsCSV(file)) && (!file.isDirectory());
            }
        };
    }  
    
    
    public static javax.swing.filechooser.FileFilter getFileFilter() {
        return new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return !existsCSV(f);
            }

            @Override
            public String getDescription() {
                return "Arquivos ainda não segmentados";
            }
        };
            
    }
    
    public static String docToString(File docFile)  {
//      http://stackoverflow.com/questions/2709923/how-to-convert-doc-or-docx-files-to-txt

        try {
            
            return new Tika().parseToString(docFile);
            
        } catch (IOException | TikaException ex) {
            System.err.println("Erro ao ler " + docFile);
            return null;
        }
    }
    
    public static void filterListModel(DefaultListModel model, ArrayList<String> words, String filter) {
        for(String w : words) {
            if(!w.toLowerCase().startsWith(filter.toLowerCase())) {
                while(model.contains(w)){
                    model.removeElement(w);
                }
            }
            else {
                if(!model.contains(w)) {
                    model.addElement(w);
                }
            }
        }
    }
    
}
