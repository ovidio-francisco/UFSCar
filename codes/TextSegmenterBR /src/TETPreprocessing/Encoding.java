/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TETPreprocessing;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Gaia
 */
public class Encoding {
    
    public static String getCharSet(File file) throws java.io.IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        CharsetDetector cd = new CharsetDetector();
        cd.setText(bis);
        CharsetMatch cm = cd.detect();
        String charset = "";
        if (cm != null) {
           charset = cm.getName();
        }
        return charset;
    }
}
