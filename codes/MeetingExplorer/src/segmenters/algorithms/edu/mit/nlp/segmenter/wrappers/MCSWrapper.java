package segmenters.algorithms.edu.mit.nlp.segmenter.wrappers;

import java.util.List;

import edu.mit.nlp.segmenter.MinCutSeg;
import edu.mit.nlp.segmenter.SegmenterParams;
import segmenters.algorithms.edu.mit.nlp.MyTextWrapper;
import segmenters.algorithms.edu.mit.nlp.segmenter.Segmenter;

public class MCSWrapper implements Segmenter {
    public MCSWrapper () {
        mcs = new MinCutSeg();
    }
    public void initialize(String config_filename){
        mcs.setConfigFile(config_filename);
        mcs.globalInit();
        mcs.loadSegmenterParams(); //eh?
    }
    
    /* by OJF */
    public void configure(SegmenterParams params) {
    	mcs.setParams(params);
//    	mcs.loadSegmenterParams();
    }

    public List[] segmentTexts(MyTextWrapper[] texts, int[] num_segs){ 
        List[] output = new List[texts.length];
        for (int i = 0; i < texts.length; i++){
            output[i] = mcs.segmentText(texts[i],num_segs[i]);
        }
        return output;
    }

    public void setDebug(boolean debug){ this.debug = debug; }
    boolean debug = false;
    MinCutSeg mcs;
}