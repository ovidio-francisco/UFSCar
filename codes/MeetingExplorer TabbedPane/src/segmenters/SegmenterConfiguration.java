
package segmenters;

import edu.mit.nlp.segmenter.SegmenterParams;
import preprocessamento.Preprocess;
import segmenters.Segmenter.SegmenterAlgorithms;
import segmenters.algorithms.C99BR;
import segmenters.algorithms.DPSeg;
import segmenters.algorithms.MinCutSeg;
import segmenters.algorithms.SentencesSegmenter;
import segmenters.algorithms.TextTilingBR;
import segmenters.algorithms.UISeg;

/**
 * @author ovidiojf
 */
public class SegmenterConfiguration {

    private SegmenterAlgorithms algorithm = null;
    
    private boolean doPreprocess = true;
    
    private Preprocess preprocess = null;
    
    private int TT_winSize = 30;
    private int TT_wtep    = 50;

    private double C99_segRate = 0.5;
    private int C99_rankSize = 3;
    private boolean C99_weight = true;
    
    private double MC_segRate = 0.4;
    private int MC_lenCutOff = 15;
    
    private boolean BS_segsKnow = false;
    private double BS_prior = 0.08;
    private double BS_dispertion = 0.5;
    
    private boolean TS_segKnow = false;
    private double TS_segRate = 0.2;

    
    
    public SegmenterConfiguration(SegmenterAlgorithms algorithm, boolean doPreprocess) {
        this.algorithm = algorithm;
        this.doPreprocess = doPreprocess;
    }
    
    
    public Segmenter buildSegmenter() {
        Segmenter result = null;
        
        switch (algorithm) {
            case TEXT_TILING:
                TextTilingBR tt = new TextTilingBR();
                tt.setWindowSize(TT_winSize);
                tt.setStep(TT_wtep);
                result = tt;
                break;
                
            case C99:
                C99BR c99 = new C99BR();
                c99.setnSegsRate(C99_segRate);
                c99.setRakingSize(C99_rankSize);
                c99.setWeitght(C99_weight);
                result = c99;
                break;
                
            case MINCUT:
                MinCutSeg mc = new MinCutSeg();
                mc.setnSegsRate(MC_segRate);
                SegmenterParams params = new SegmenterParams();
                params.setSegLenCutoff(MC_lenCutOff);
                params.setSegLenCutoffEnabled(true);
                mc.setSegmenterParams(params);
                result = mc;
                break;
                
            case TEXT_SEG:
                UISeg ui = new UISeg();
                ui.setnSegsRate(TS_segRate);
                result = ui;
                break;
                
            case BAYESSEG:
                DPSeg dp = new DPSeg();
                dp.setNumSegsKnown(BS_segsKnow);
                dp.setnSegsRate(MC_segRate);
                result = dp;
                break;
                
            case SENTENCES:
                result = new SentencesSegmenter();
                break;            
        }
        
        result.setPreprocess(getPreprocessDoAnything(doPreprocess));
        return result;
    }
    
    
    private static Preprocess getPreprocessDoAnything(boolean doanything) {
            Preprocess preprocess = new Preprocess();

            if(doanything) {
                    preprocess.setIdentifyEOS       (true);
                    preprocess.setRemoveAccents     (true);
                    preprocess.setRemoveHeaders     (true);
                    preprocess.setRemoveNumbers     (true);
                    preprocess.setRemovePunctuation (true);
                    preprocess.setRemoveShortThan   (true);
                    preprocess.setRemoveStem        (true);
                    preprocess.setRemoveStopWord    (true);
                    preprocess.setToLowCase         (true);			
            } else {
                    preprocess.setIdentifyEOS       (true);
                    preprocess.setRemoveAccents     (false);
                    preprocess.setRemoveHeaders     (false);
                    preprocess.setRemoveNumbers     (false);
                    preprocess.setRemovePunctuation (false);
                    preprocess.setRemoveShortThan   (false);
                    preprocess.setRemoveStem        (false);
                    preprocess.setRemoveStopWord    (false);
                    preprocess.setToLowCase         (false);						
            }

            return preprocess;
    }    

    public SegmenterAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SegmenterAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public boolean isDoPreprocess() {
        return doPreprocess;
    }

    public void setDoPreprocess(boolean doPreprocess) {
        this.doPreprocess = doPreprocess;
    }

    public Preprocess getPreprocess() {
        return preprocess;
    }

    public void setPreprocess(Preprocess preprocess) {
        this.preprocess = preprocess;
    }

    public int getTT_winSize() {
        return TT_winSize;
    }

    public void setTT_winSize(int TT_winSize) {
        this.TT_winSize = TT_winSize;
    }

    public int getTT_wtep() {
        return TT_wtep;
    }

    public void setTT_wtep(int TT_wtep) {
        this.TT_wtep = TT_wtep;
    }

    public double getC99_segRate() {
        return C99_segRate;
    }

    public void setC99_segRate(double C99_segRate) {
        this.C99_segRate = C99_segRate;
    }

    public int getC99_rankSize() {
        return C99_rankSize;
    }

    public void setC99_rankSize(int C99_rankSize) {
        this.C99_rankSize = C99_rankSize;
    }

    public boolean isC99_weight() {
        return C99_weight;
    }

    public void setC99_weight(boolean C99_weight) {
        this.C99_weight = C99_weight;
    }

    public double getMC_segRate() {
        return MC_segRate;
    }

    public void setMC_segRate(double MC_segRate) {
        this.MC_segRate = MC_segRate;
    }

    public int getMC_lenCutOff() {
        return MC_lenCutOff;
    }

    public void setMC_lenCutOff(int MC_lenCutOff) {
        this.MC_lenCutOff = MC_lenCutOff;
    }

    public boolean isBS_segsKnow() {
        return BS_segsKnow;
    }

    public void setBS_segsKnow(boolean BS_segsKnow) {
        this.BS_segsKnow = BS_segsKnow;
    }

    public double getBS_prior() {
        return BS_prior;
    }

    public void setBS_prior(double BS_prior) {
        this.BS_prior = BS_prior;
    }

    public double getBS_dispertion() {
        return BS_dispertion;
    }

    public void setBS_dispertion(double BS_dispertion) {
        this.BS_dispertion = BS_dispertion;
    }

    public boolean isTS_segKnow() {
        return TS_segKnow;
    }

    public void setTS_segKnow(boolean TS_segKnow) {
        this.TS_segKnow = TS_segKnow;
    }

    public double getTS_segRate() {
        return TS_segRate;
    }

    public void setTS_segRate(double TS_segRate) {
        this.TS_segRate = TS_segRate;
    }
    
    
    
    
}
