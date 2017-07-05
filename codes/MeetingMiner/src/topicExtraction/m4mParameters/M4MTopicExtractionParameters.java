
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       
 * Last Modified 
 */

package topicExtraction.m4mParameters;

import topicExtraction.TETConfigurations.TopicExtractionParameters;
import topicExtraction.mining4meetings.Mining4Meetings;
import utils.Files;


public class M4MTopicExtractionParameters {

    public static TopicExtractionParameters getDefaultTopicExtractionParameters() {
        
        TopicExtractionParameters result = new TopicExtractionParameters();  
        
        if (Files.getSegmentedDocs() != null)
            result.setDirEntrada(Files.getSegmentedDocs().getAbsolutePath());
        else
            result.setDirEntrada("");

        if (Mining4Meetings.getOutFolder() != null)
            result.setDirSaida(Mining4Meetings.getOutFolder().getAbsolutePath());
        else
            result.setDirSaida("");
        
        result.setNumTopics(10);
        result.setPLSA(true);
        
        return result;
    }
}
