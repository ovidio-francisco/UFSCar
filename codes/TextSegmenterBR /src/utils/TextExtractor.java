package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;


public class TextExtractor {
	
//	private String getStringFromFile(File file) {
//		String result = null;
//		
//		String extention = Files.getFileExtension(file);
//		
//		
//		return result;		
//	}

	public static String docToString(File docFile) {
//      http://stackoverflow.com/questions/2709923/how-to-convert-doc-or-docx-files-to-txt
        Tika tika = new Tika();

        try {            
            return tika.parseToString(docFile);
            
        } catch (IOException | TikaException ex) {
            System.err.println("Erro ao ler " + docFile);
            ex.printStackTrace();
        }

        return null;
	}
	
    public static void docToText(File docFile, File txtFile)  {
//      http://stackoverflow.com/questions/2709923/how-to-convert-doc-or-docx-files-to-txt
        Tika tika = new Tika();

        try {            
            String txt = tika.parseToString(docFile);
            Files.saveTxtFile(txt, txtFile);
            
        } catch (IOException | TikaException ex) {
            System.err.println("Erro ao ler " + docFile);
            ex.printStackTrace();
        }
    }
    
    public static void extractTxtFromAllFiles(File docFolder, File txtFolder) {
    	
    	if (!txtFolder.exists()) {
    		ShowStatus.setMessage("Criando diretório: " + txtFolder);
    		txtFolder.mkdir();
    	}
    	
        ArrayList<File> files = new ArrayList<>();
        Files.listDocs(docFolder, files);
        
        ShowStatus.setMessage("Extraindo texto para ...");

        int count = 0;
        
        for(File f : files) {
            File out = new File(txtFolder.getPath() + "/" + f.getName() + ".txt");
            
            if (out.exists()) {
                continue;
            }
            
            ShowStatus.setMessage("  '" + out +"'");
            docToText(f, out);
            count++;
        }

        ShowStatus.setMessage(files.size() + " documentos encontrados");
        ShowStatus.setMessage(count + " documentos texto criados");
    }

	
	
}
