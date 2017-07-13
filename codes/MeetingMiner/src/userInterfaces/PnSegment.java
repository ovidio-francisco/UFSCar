package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import meetingMiner.Segment;
import utils.Files;

public class PnSegment extends JPanel {

	private static final long serialVersionUID = 1L;
	private Segment segment;

	public PnSegment(Segment seg) {
		this.segment = seg;
		JTextArea taSeg = new JTextArea();
		JScrollPane spSeg = new JScrollPane(taSeg);
		
		taSeg.setText(seg.getText());
		taSeg.setLineWrap(true);
		taSeg.setMargin(new Insets(4, 4, 4, 4));
		
		this.setBorder(new EmptyBorder(10, 10, 5, 10));
		
		this.setLayout(new BorderLayout());
		this.add(spSeg, BorderLayout.CENTER);
		
		StringBuilder sb = new StringBuilder();
		for(String s : seg.getDescriptors()){
			sb.append(s+", ");
		}
		String descriptors = sb.toString().substring(0, sb.length() - 2);

		JLabel lbDescriptors = new JLabel(descriptors);
		
		JPanel pnBotton = new JPanel(new BorderLayout());
		
		JPanel pnFiles = new JPanel(new BorderLayout());
		DocLabel lbOriginalDoc = new DocLabel(Files.getOriginalDocs(), this.segment.getOriginalDocument());
		DocLabel lbSegmentDoc  = new DocLabel(Files.getSegmentedDocs(), this.segment.getSegmentDoc());
		pnFiles.add(lbSegmentDoc, BorderLayout.WEST);
		pnFiles.add(lbOriginalDoc, BorderLayout.EAST);
		
		pnBotton.add(lbDescriptors, BorderLayout.NORTH);
		pnBotton.add(pnFiles, BorderLayout.SOUTH);
		
		this.add(pnBotton, BorderLayout.SOUTH);
	}

	public Segment getSegment() {
		return segment;
	}
}
