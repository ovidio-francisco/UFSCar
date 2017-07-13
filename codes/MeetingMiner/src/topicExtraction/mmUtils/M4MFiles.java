/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.mmUtils;

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

import utils.ShowStatus;

/**
 *
 * @author ovidiojf
 */
public class M4MFiles {
    
    static {
        docExtentitions = new ArrayList<>();
        addDocExtention("pdf");
        addDocExtention("doc");
        addDocExtention("docx");
    }
        
    public static String loadTxtFile(File file) {

        String line;
        StringBuilder buffer = new StringBuilder();

        try {
            BufferedReader text = M4MFiles.getBufferedReader(file);  
            
            while((line = text.readLine()) != null){
                buffer.append(line + "\n");
            }
                        
        } catch (IOException ex) {
            System.err.println("Houve um erro ao abrir o arquivo " + file);
            ex.printStackTrace();
        }
        
        return buffer.toString();

    }
    
    public static FileFilter getM4MFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || docExtentitions.contains(getFileExtension(file));
            }
        };
    }  
    
    public static void listDocs(File dirIn, ArrayList<File> filesIn){
        File[] files = dirIn.listFiles(getM4MFileFilter());
        for(int i=0;i<files.length;i++){
            if(!files[i].isDirectory()){
                filesIn.add(files[i]);
            }else{
                listDocs(files[i], filesIn);
            }
        }
    }
    
    public static boolean deleteFolder(File folder) {
        
        if (!folder.isDirectory()) return false;
        
        File[] files = folder.listFiles();
        
        for (File f : files) {
            if (!f.isDirectory()) {
                f.delete();
            }
            else {
                // do nothing;
            }
        }
        
        boolean result = folder.delete();
        
        if (result) 
            ShowStatus.setMessage("Diretório " + folder + " deletado");
            
        return result;
    }
    
    public static String getFileExtension (File file) {
        String fileName = file.getName();
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else 
            return "";
    }
    
    public static boolean isPdfFile(File file) {
        return getFileExtension(file).compareToIgnoreCase("pdf") == 0;
    }
    
    public static BufferedReader getBufferedReader(File file) {
        //   = new BufferedReader(new InputStreamReader(new FileInputStream(filesIn.get(i).getAbsolutePath().toString()), "ISO-8859-1"));
        BufferedReader result = null;
        
        try {
            result = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), M4MDefautCharSet));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao ler o arquivo"+file);
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Erro ao ler o arquivo"+file + ": Charset "+M4MDefautCharSet+" não suportado");
            ex.printStackTrace();
        }
        
        return result;
    }

    public static BufferedWriter getBufferedWriter(File file) {
        //  BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirOut + "/" + output),"ISO-8859-1"));
        BufferedWriter result = null;
        
        try {
            result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), M4MDefautCharSet));
        } catch (FileNotFoundException ex) {
            System.err.println("Erro ao escrever o arquivo"+file);
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Erro ao escrever o arquivo"+file + ": Charset "+M4MDefautCharSet+" não suportado");
            ex.printStackTrace();
        }
        
        return result;
    }

    

    public static ArrayList<String> getDocExtentitions() {
        return docExtentitions;
    }

    public static void setDocExtentitions(ArrayList<String> docExtentitions) {
        M4MFiles.docExtentitions = docExtentitions;
    }
    
    public static void addDocExtention(String fileExtention) {
        M4MFiles.docExtentitions.add(fileExtention);
    }


    public static String getM4MDefautCharSet() {
        return M4MDefautCharSet;
    }

    public static void setM4MDefautCharSet(String M4MDefautCharSet) {
        M4MFiles.M4MDefautCharSet = M4MDefautCharSet;
    }

    
    private static ArrayList<String> docExtentitions;
    
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";
    
    private static String M4MDefautCharSet = ISO_8859_1;
    
}
