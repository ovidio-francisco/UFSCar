
package textsegmentationtool;

import java.util.ArrayList;

/**
 *
 * @author ovidiojf
 */
public class Segment {

    private String text = null;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> contents = new ArrayList<>();
    private ArrayList<String> topics = new ArrayList<>();

    public Segment(String text, ArrayList<String> tags, ArrayList<String> cotents, ArrayList<String> topics) {
        this.text = text;
        this.tags = tags;
        this.contents = cotents;
        this.topics = topics;
    }
    
    public String getText() {
        return text;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public ArrayList<String> getContents() {
        return contents;
    }
    
    
    
}
