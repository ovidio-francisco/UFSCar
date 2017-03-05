
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       
 * Last Modified 
 */

package m4mParameters;

import java.io.File;
import mining4meetings.Mining4Meetings;


public class M4MRepresentationParameters {
    
    private File    dirIn;
    private File    dirOut;
    private String  relationName;
    private boolean dirClass;
    private String  language;
    private boolean stemming;
    private boolean removeStopWords;
    private boolean binary;
    private boolean TF;
    private boolean TFIDF;
    private boolean sparse;
    private boolean cutDF;
    private int     dfMin;
    private boolean topRanking;
    private int     topRanked;
    private boolean replaceStem;
        
    public static M4MRepresentationParameters getDefaultRepresentationParameters() {
        M4MRepresentationParameters result = new M4MRepresentationParameters();

        result.dirIn           = Mining4Meetings.getTxtFolder();
        result.dirOut          = Mining4Meetings.getArfFolder();
        result.relationName    = "representacao"; //arame
        result.dirClass        = false;
        result.language        = "port";
        result.stemming        = true;
        result.removeStopWords = true;
        result.binary          = false;
        result.TF              = true;
        result.TFIDF           = false;
        result.sparse          = true;
        result.cutDF           = true;
        result.dfMin           = 2;
        result.topRanked       = 5000;
        result.replaceStem     = true;
        
        return result;
    }

    public File getDirIn() {
        return dirIn;
    }

    public void setDirIn(File dirIn) {
        this.dirIn = dirIn;
    }

    public File getDirOut() {
        return dirOut;
    }

    public void setDirOut(File dirOut) {
        this.dirOut = dirOut;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public boolean isDirClass() {
        return dirClass;
    }

    public void setDirClass(boolean dirClass) {
        this.dirClass = dirClass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isStemming() {
        return stemming;
    }

    public void setStemming(boolean stemming) {
        this.stemming = stemming;
    }

    public boolean isRemoveStopWords() {
        return removeStopWords;
    }

    public void setRemoveStopWords(boolean removeStopWords) {
        this.removeStopWords = removeStopWords;
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

    public boolean isSparse() {
        return sparse;
    }

    public void setSparse(boolean sparse) {
        this.sparse = sparse;
    }

    public boolean isCutDF() {
        return cutDF;
    }

    public void setCutDF(boolean cutDF) {
        this.cutDF = cutDF;
    }

    public int getDfMin() {
        return dfMin;
    }

    public void setDfMin(int dfMin) {
        this.dfMin = dfMin;
    }

    public boolean isTopRanking() {
        return topRanking;
    }

    public void setTopRanking(boolean topRanking) {
        this.topRanking = topRanking;
    }

    public int getTopRanked() {
        return topRanked;
    }

    public void setTopRanked(int topRanked) {
        this.topRanked = topRanked;
    }

    public boolean isReplaceStem() {
        return replaceStem;
    }

    public void setReplaceStem(boolean replaceStem) {
        this.replaceStem = replaceStem;
    }
    
    
}
