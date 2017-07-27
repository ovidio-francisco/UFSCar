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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import segmenter.Segmenter;

public class Files {
	
    private static ArrayList<String> docExtentitions;
    
    private static File basesFolder   = new File("bases");
    private static File originalDocs  = new File(basesFolder+"/originalDocs");
    private static File textDocs      = new File(basesFolder+"/textDocs");
    private static File segmentedDocs = new File(basesFolder+"/segmentedDocs"); 
    

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
    
    public static boolean deleteFile(File file) {
    	
    	
    	if (file.exists()) {
    		ShowStatus.setMessage(String.format("Deleting file '%s'", file));
    		file.delete();
    	}
    	
    	
    	return false;
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


	public static boolean createDir(File folder) {
		
		if( !folder.exists()) {
			boolean result = folder.mkdir(); 
			
			if(result) {
				ShowStatus.setMessage(String.format("Criado diretório: %s", folder));
			}
			
			return result;
		}
		
		return false;
	}


	public static void copy(File f, File originalDocs) {
		File newfile = new File(originalDocs+"/"+f.getName());
		
		if (newfile.exists()) {
			ShowStatus.setMessage(String.format("O arquivo %s já existe", newfile));
			return;
		}
		
		try {
			java.nio.file.Files.copy(f.toPath(), newfile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			ShowStatus.setMessage("Arquivo copiado para a base: " + newfile);
		} catch (IOException e) {
			ShowStatus.setMessage("Erro ao copiar o arquivo " + newfile);
		}
		
	}
    
	public static void prepareBaseFolders() {
		Files.createDir(basesFolder);
		Files.createDir(originalDocs);
		Files.createDir(textDocs);
		Files.createDir(segmentedDocs);		
	}
	
	
	public static int addToTheBase(File folder) {
		ArrayList<File> files = new ArrayList<>();
		listDocs(folder, files);
		
		for(File f : files) {
			Files.copy(f, originalDocs);
		}
		
		return files.size();
	}

	public static void extractTextToTheBase() {
		TextExtractor.extractTxtFromAllFiles(originalDocs, textDocs);
	}


	public static int extractSegmentsToTheBase(Segmenter segmenter) {
		File[] files = textDocs.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return getFileExtension(file).equals("txt");
			}
		});
		
		int count = 0;
		
		for(File f : files) {
			count  += segmenter.segmentToFiles(f, segmentedDocs);
		}
		
		return count;
	}


	public static File getBasesFolder() {
		return basesFolder;
	}


	public static File getOriginalDocs() {
		return originalDocs;
	}


	public static File getTextDocs() {
		return textDocs;
	}


	public static File getSegmentedDocs() {
		return segmentedDocs;
	}
	
	
	
	
	
}
