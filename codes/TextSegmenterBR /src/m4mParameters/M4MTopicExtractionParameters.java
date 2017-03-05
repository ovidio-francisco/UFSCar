
/**
 * @author       ovidiojf
 * @email        ovidiojf@gmail.com
 * Created       
 * Last Modified 
 */

package m4mParameters;

import TETConfigurations.TopicExtractionParameters;
import mining4meetings.Mining4Meetings;


public class M4MTopicExtractionParameters {

    public static TopicExtractionParameters getDefaultTopicExtractionParameters() {
        
        TopicExtractionParameters result = new TopicExtractionParameters();  
        
        if (Mining4Meetings.getDocFolder() != null)
            result.setDirEntrada(Mining4Meetings.getTxtFolder().getAbsolutePath());
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
