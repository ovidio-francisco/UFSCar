package utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.JPanel;

public class UIUtils {
	
	public static void setEnable(Container p, boolean enable) {
		
		p.setEnabled(enable);
		
		for(Component c : p.getComponents()) {
			if( c instanceof Container ) {
				setEnable((Container)c, enable);
			}
			else {
				c.setEnabled(enable);
			}
		}
	}
	
	public static void setVisible(JPanel p, boolean enable) {
		
		p.setVisible(enable);
		
		for(Component c : p.getComponents()) {
			if( c instanceof JPanel ) {
				setEnable((JPanel)c, enable);
			}
			else {
				c.setVisible(enable);
			}
		}
	}

	public static void setWaiting(Container c, boolean waiting) {
		setEnable(c, !waiting);
		c.setCursor(Cursor.getPredefinedCursor(waiting ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
	}
	
	public static void setWaiting(Container[] c, boolean waiting) {
		for(Container d : c) {
			setWaiting(d, waiting);
                        System.out.println("--->> utils.UIUtils.setWaiting()");
		}
	}
	
	
}
