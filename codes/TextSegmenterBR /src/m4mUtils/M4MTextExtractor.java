
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       
 * Last Modified 
 */

package m4mUtils;

//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
//import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
//import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;


public class M4MTextExtractor {
//
//    public static void pdfToText(File pdfFile, File txtFile)  {
//    //http://stackoverflow.com/questions/8821107/pdf-text-extraction-using-itext    
//        try {
//            PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
//            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
////          PrintWriter out = new PrintWriter(new FileOutputStream(txtFile.getAbsolutePath()));
//            BufferedWriter out = M4MFiles.getBufferedWriter(txtFile); //new PrintWriter(new FileOutputStream(txtFile.getAbsolutePath()));
//            TextExtractionStrategy strategy;
//            
//            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
//                out.write(strategy.getResultantText());
//                out.newLine();
//            }
//            
//            out.flush();
//            out.close();
//        } 
//        catch (NoClassDefFoundError | Exception e) {
////            Logger.getLogger(TextExtractor.class.getName()).log(Level.SEVERE, null, e);            
//            System.err.println("Error reading " + pdfFile + "  --  " + e.toString());
//        }
//    }
//    
//    public static void extractTxtFromAllPdfs(File docFolder, File txtFolder) {
////        pdfToText(pdfFile, new File(pdfFile.getAbsoluteFile() + ".txt"));
//        
//        M4MShowStatus.setMessage("Extractin raw text to ...");
//
//        ArrayList<File> files = new ArrayList<>();
//        
//        M4MFiles.listDocs(docFolder, files);
//        
//        for(File f : files) {
//            if (M4MFiles.isPdfFile(f)) {
//                
//                try {
//                    File out = new File(txtFolder.getPath() + "/" + f.getName() + ".txt");
//                    M4MShowStatus.setMessage("  '" + out +"'");
//                    pdfToText(f, out);
//                    
//                } catch (Exception e) {
//                    System.err.println("Erro ao ler " + f);
//                    e.printStackTrace();
//                }
//            }
//        }
//        
//        M4MShowStatus.setMessage("Done!");
//
//    }
//    
    
    public static void docToText(File docFile, File txtFile)  {
//      http://stackoverflow.com/questions/2709923/how-to-convert-doc-or-docx-files-to-txt
        Tika tika = new Tika();

        BufferedWriter out = M4MFiles.getBufferedWriter(txtFile); //new PrintWriter(new FileOutputStream(txtFile.getAbsolutePath()));
                
        try {
            
            String txt = tika.parseToString(docFile);
            out.write(txt);
            out.flush();
            out.close();

            
        } catch (IOException | TikaException ex) {
                    System.err.println("Erro ao ler " + docFile);
                    ex.printStackTrace();
        }
    }
    
    public static void extractTxtFromAllFiles(File docFolder, File txtFolder) {
        M4MShowStatus.setMessage("Extractin raw text to ...");

        ArrayList<File> files = new ArrayList<>();
        
        M4MFiles.listDocs(docFolder, files);
        
        for(File f : files) {
            File out = new File(txtFolder.getPath() + "/" + f.getName() + ".txt");
            
            if (out.exists()) {
                continue;
            }
            
            M4MShowStatus.setMessage("  '" + out +"'");
            docToText(f, out);
        }

    }

    
    
}
