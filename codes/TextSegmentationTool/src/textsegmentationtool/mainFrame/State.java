/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textsegmentationtool.mainFrame;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import textsegmentationtool.SegmentPanel;

/**
 *
 * @author ovidiojf
 */
public class State implements Serializable {
    File doc;
    ArrayList<SegmentPanel> segs;
    String editingText;

    public State(ArrayList<SegmentPanel> segs, String editingText, File doc) {
        this.doc = doc;
        this.segs = segs;
        this.editingText = editingText;
        
    }

    public ArrayList<SegmentPanel> getSegs() {
        return segs;
    }

    public String getEditingText() {
        return editingText;
    }
}
