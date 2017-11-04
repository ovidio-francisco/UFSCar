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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


public class Files {
	
    private static ArrayList<String> docExtentitions;

    static {
        docExtentitions = new ArrayList<>();
        addDocExtention("pdf");
        addDocExtention("doc");
        addDocExtention("docx");
    }

    public static void addDocExtention(String fileExtention) {
        Files.docExtentitions.add(fileExtention);
    }

	
	public static String loadTxtFile(File file) {

        String line;
        StringBuilder buffer = new StringBuilder();

        try {
            BufferedReader text = Files.getBufferedReader(file);  
            
            while((line = text.readLine()) != null){
                buffer.append(line + "\n");
            }
                        
        } catch (IOException ex) {
            System.err.println("Houve um erro ao abrir o arquivo " + file);
            ex.printStackTrace();
        }
        return buffer.toString();
    }
	
	public static List<String> loadTxtFileToList(File file) {
		ArrayList<String> result = new ArrayList<>();
		
        String line;
        try {
            BufferedReader text = Files.getBufferedReader(file);  
            
            while((line = text.readLine()) != null){
                result.add(line);
            }
                        
        } catch (IOException ex) {
            System.err.println("Houve um erro ao abrir o arquivo " + file);
            ex.printStackTrace();
        }
		
		
		return result;
	}

	
    public static BufferedReader getBufferedReader(File file) {
        //   = new BufferedReader(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1"));
        BufferedReader result = null;
        
        try {
            result = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao ler o arquivo"+file);
            ex.printStackTrace();
        }
        
        return result;
    }
    
    
    
    
    
    public static void saveTxtFile(String txt, File txtFile) {
        BufferedWriter out = Files.getBufferedWriter(txtFile); 

        try {
			out.write(txt);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.err.println("Erro ao salvar o arquivo " + txtFile.getName());
			e.printStackTrace();
		}
    }

    public static void saveLinesToTxtFile(String[] lines, File txtFile) {
        BufferedWriter out = Files.getBufferedWriter(txtFile); 

        try {
        	for(String line: lines) {
        		out.write(line);
        	}
			out.flush();
			out.close();
		} catch (IOException e) {
			System.err.println("Erro ao salvar o arquivo " + txtFile.getName());
			e.printStackTrace();
		}
    }

    public static BufferedWriter getBufferedWriter(File file) {
        //  BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + output),"ISO-8859-1"));
        BufferedWriter result = null;
        
        try {
            result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao escrever o arquivo: "+file);
            ex.printStackTrace();
        }
        
        return result;
    }
    
    
    public static void listDocs(File dirIn, ArrayList<File> filesIn){
        File[] files = dirIn.listFiles(getM4M_DocFileFilter());
        for(int i=0;i<files.length;i++){
            if(!files[i].isDirectory()){
                filesIn.add(files[i]);
            }else{
                listDocs(files[i], filesIn);
            }
        }
    }

    public static FileFilter getM4M_DocFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || docExtentitions.contains(getFileExtension(file));
            }
        };
    }  

    public static String getFileExtension (File file) {
        String fileName = file.getName();
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else 
            return "";
    }
    
    public static CSVPrinter getCSVPrinter(File file) {
    	Appendable out = getBufferedWriter(file);
    	
    	try {
			return new CSVPrinter(out, CSVFormat.DEFAULT);
		} catch (IOException e) {
			System.err.println("Não foi possível criar o arquivo " + file);
			e.printStackTrace();
			return null;
		}
    }


    public static void closeCSV(CSVPrinter csv) {
    	try {
    		csv.flush();
    		csv.close();
    	} catch (IOException e) {
    		System.err.println("Não foi possível criar o arquivo csv");
    		e.printStackTrace();
    	}
    }
    

}
