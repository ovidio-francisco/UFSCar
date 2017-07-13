package userInterfaces;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PnSearchSegments extends JPanel {

	private static final long serialVersionUID = 1L;

	public PnSearchSegments() {
		setLayout(new BorderLayout());
		
		JLabel lbSegments = new JLabel("Segmentos", SwingConstants.CENTER);
		lbSegments.setBorder(new EmptyBorder(0, 0, 5, 0));
		this.add(lbSegments, BorderLayout.NORTH);
		
		
		JPanel pnComponents = new JPanel(new BorderLayout());
		JTextField tfDescriptors = new JTextField(30);
		pnComponents.add(tfDescriptors);
		
		this.add(pnComponents);
	}
}


