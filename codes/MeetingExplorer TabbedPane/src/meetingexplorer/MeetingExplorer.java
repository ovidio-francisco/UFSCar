
package meetingexplorer;

import javax.swing.JFrame;

/**
 * @author ovidiojf
 */
public class MeetingExplorer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(">> Meeting Explorer <<");
        
        MainForm f = new MainForm();
        
        f.setState(JFrame.MAXIMIZED_BOTH);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        f.setVisible(true);
        
        
    }
    
}
