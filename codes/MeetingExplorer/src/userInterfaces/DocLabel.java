package userInterfaces;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

public class DocLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private File folder = null;
	private File doc = null;
	
	public DocLabel(File folder, File doc) {
		this.folder = folder;
		this.doc = doc;
		
		this.setText(doc.getName());
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, this.getFont().getSize()));

		addEvents();
	}

	private void addEvents() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setForeground(Color.blue);
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setForeground(Color.black);
				setCursor(java.awt.Cursor.getDefaultCursor());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File(folder.getAbsolutePath()+"/"+doc.getName()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public File getFolder() {
		return folder;
	}
	public File getDoc() {
		return doc;
	}
}
