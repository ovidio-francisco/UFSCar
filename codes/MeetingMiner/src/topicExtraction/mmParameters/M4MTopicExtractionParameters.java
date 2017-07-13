
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       
 * Last Modified 
 */

package topicExtraction.mmParameters;

import meetingMiner.MeetingMiner;
import topicExtraction.TETConfigurations.TopicExtractionParameters;
import utils.Files;


public class M4MTopicExtractionParameters {

    public static TopicExtractionParameters getDefaultTopicExtractionParameters() {
        
        TopicExtractionParameters result = new TopicExtractionParameters();  
        
        if (Files.getSegmentedDocs() != null)
            result.setDirEntrada(Files.getSegmentedDocs().getAbsolutePath());
        else
            result.setDirEntrada("");

        if (MeetingMiner.getOutFolder() != null)
            result.setDirSaida(MeetingMiner.getOutFolder().getAbsolutePath());
        else
            result.setDirSaida("");
        
        result.setNumTopics(10);
        result.setPLSA(true);
        
        return result;
    }
}
