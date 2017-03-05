
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       9-1-2016
 * Last Modified 
 */

package m4mParameters;

import TETStructures.FeatureList;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class M4MArffGenerationParameters {
    
    private ArrayList<String>       names;
    private String                  relacao;
    private boolean                 classe;
    private ArrayList<String>       infClasses;
    private int                     numFiles;
    private int                     numTerms;
    private File                    dirOut;
    private ArrayList<File>         filesIn;
    private ArrayList<FeatureList>  atributos;
    private boolean                 binary;
    private boolean                 TF;
    private boolean                 TFIDF;
    private HashMap<String,String>  classes;
    private boolean                 sparse;
    private Object[]                orderedFiles;
    private HashMap<String,Integer> termDF;
    private boolean                 replaceStem;
    private HashMap<String,String>  stem_palProc;
    private HashMap<String,String>  palProc_pal;

    public static M4MArffGenerationParameters getArffGenerationParameters() {
        M4MArffGenerationParameters result = new M4MArffGenerationParameters();
        
        result.names        = null;
        result.relacao      = null;
        result.classe       = false;
        result.infClasses   = null;
        result.numFiles     = -1;
        result.numTerms     = -1;
        result.dirOut       = null;
        result.filesIn      = null;
        result.atributos    = null;
        result.binary       = false;
        result.TF           = false;
        result.TFIDF        = false;
        result.classes      = null;
        result.sparse       = false;
        result.orderedFiles = null;
        result.termDF       = null;
        result.replaceStem  = false;
        result.stem_palProc = null;
        result.palProc_pal  = null;

        return result;
    }

    @Override
    public String toString() {
        return "ArffGenerationParameters\n\t{" + "names=" + names + ", relacao=" + relacao + ", classe=" + classe + ", infClasses=" + infClasses + ", numFiles=" + numFiles + ", numTerms=" + numTerms + ", dirOut=" + dirOut + ", filesIn=" + filesIn + ", atributos=" + atributos + ", binary=" + binary + ", TF=" + TF + ", TFIDF=" + TFIDF + ", classes=" + classes + ", sparse=" + sparse + ", orderedFiles=" + orderedFiles + ", termDF=" + termDF + ", replaceStem=" + replaceStem + ", stem_palProc=" + stem_palProc + ", palProc_pal=" + palProc_pal + '}';
    }
    
    
    
    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public String getRelacao() {
        return relacao;
    }

    public void setRelacao(String relacao) {
        this.relacao = relacao;
    }

    public boolean isClasse() {
        return classe;
    }

    public void setClasse(boolean classe) {
        this.classe = classe;
    }

    public ArrayList<String> getInfClasses() {
        return infClasses;
    }

    public void setInfClasses(ArrayList<String> infClasses) {
        this.infClasses = infClasses;
    }

    public int getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }

    public File getDirOut() {
        return dirOut;
    }

    public void setDirOut(File dirOut) {
        this.dirOut = dirOut;
    }

    public ArrayList<File> getFilesIn() {
        return filesIn;
    }

    public void setFilesIn(ArrayList<File> filesIn) {
        this.filesIn = filesIn;
    }

    public ArrayList<FeatureList> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<FeatureList> atributos) {
        this.atributos = atributos;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public boolean isTF() {
        return TF;
    }

    public void setTF(boolean TF) {
        this.TF = TF;
    }

    public boolean isTFIDF() {
        return TFIDF;
    }

    public void setTFIDF(boolean TFIDF) {
        this.TFIDF = TFIDF;
    }

    public HashMap<String, String> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, String> classes) {
        this.classes = classes;
    }

    public boolean isSparse() {
        return sparse;
    }

    public void setSparse(boolean sparse) {
        this.sparse = sparse;
    }

    public Object[] getOrderedFiles() {
        return orderedFiles;
    }

    public void setOrderedFiles(Object[] orderedFiles) {
        this.orderedFiles = orderedFiles;
    }

    public HashMap<String, Integer> getTermDF() {
        return termDF;
    }

    public void setTermDF(HashMap<String, Integer> termDF) {
        this.termDF = termDF;
    }

    public boolean isReplaceStem() {
        return replaceStem;
    }

    public void setReplaceStem(boolean replaceStem) {
        this.replaceStem = replaceStem;
    }

    public HashMap<String, String> getStem_palProc() {
        return stem_palProc;
    }

    public void setStem_palProc(HashMap<String, String> stem_palProc) {
        this.stem_palProc = stem_palProc;
    }

    public HashMap<String, String> getPalProc_pal() {
        return palProc_pal;
    }

    public void setPalProc_pal(HashMap<String, String> palProc_pal) {
        this.palProc_pal = palProc_pal;
    }

    
    
}
