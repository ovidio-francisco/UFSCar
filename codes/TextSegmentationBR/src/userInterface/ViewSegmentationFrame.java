package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import segmenters.Segmenter;
import segmenters.algorithms.c99br.C99BR;
import segmenters.algorithms.texttile.TextTilingBR;
import utils.TextExtractor;

public class ViewSegmentationFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea taOriginal  = new JTextArea();
	private JTextArea taSegmented = new JTextArea();
	private JTextArea taWindows   = new JTextArea();
	
	private JLabel lbStatus = new JLabel();

	private JCheckBox cbRemoveStopWords      = new JCheckBox("Remove StopWords");
	private JCheckBox cbShowPreprocess       = new JCheckBox("Show Preprocess");
	
	private File sourceFile = null;

//	private TextTilingBR textTiling = new TextTilingBR();

	
	
	
	public ViewSegmentationFrame() {
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("TextTilingBR");
		
		setLayout(new BorderLayout());
		
		
		/* Toll Bar*/
		JToolBar toolBar   = new JToolBar();
		JButton  btLoad    = new JButton("Load");
		JButton  btSegment = new JButton("Segment");
		JButton  btSaveToCSV = new JButton("Save");
		
		cbRemoveStopWords.setMaximumSize(new Dimension(170, 15));
		cbShowPreprocess .setMaximumSize(new Dimension(180, 15));
		
		cbRemoveStopWords.setSelected(true);
		cbShowPreprocess.setSelected(true);
		
		toolBar.setFloatable(false);
		
		toolBar.addSeparator();
		toolBar.add(btLoad);
		toolBar.addSeparator();
		toolBar.add(btSaveToCSV);
		toolBar.addSeparator();
		toolBar.add(btSegment);
		toolBar.addSeparator();
		toolBar.add(cbRemoveStopWords);
		toolBar.addSeparator();
//		toolBar.add(cbShowPreprocess);
		
		add(toolBar, BorderLayout.NORTH);
		
		
		/* Text Panels and Text Areas */
		JPanel pnTexts         = new JPanel();
		
		JTabbedPane tpOriginalText  = new JTabbedPane();
		JTabbedPane tpSegmentedtext = new JTabbedPane();
		JTabbedPane tpWindows       = new JTabbedPane();
		
		add(pnTexts, BorderLayout.CENTER);
		
		taOriginal .setLineWrap(true);
		taOriginal .setWrapStyleWord(true);
		
		taSegmented.setLineWrap(true);
		taSegmented.setWrapStyleWord(true);
		
		taWindows.setLineWrap(true);
		taWindows.setWrapStyleWord(true);
		
		
		JScrollPane spOriginal  = new JScrollPane(taOriginal);
		JScrollPane spWindows   = new JScrollPane(taWindows);
		JScrollPane spSegmented = new JScrollPane(taSegmented);
		
		pnTexts.setLayout(new GridLayout(1, 3));
		pnTexts.add(tpOriginalText );
		pnTexts.add(tpWindows);
		pnTexts.add(tpSegmentedtext);

		tpOriginalText .add("Original"  , spOriginal);
		tpSegmentedtext.add("Segments"  , spSegmented);
		tpWindows      .add("Preprocess", spWindows);
		
		int pad = 4;
		tpOriginalText .setBorder(new EmptyBorder(pad, pad  , 0, pad/2));
		tpWindows      .setBorder(new EmptyBorder(pad, pad/2, 0, pad/2));
		tpSegmentedtext.setBorder(new EmptyBorder(pad, pad/2, 0, pad  ));
		
		
		/* Status Bar */
		JPanel pnStatus = new JPanel(new BorderLayout());
		pnStatus.add(cbShowPreprocess, BorderLayout.WEST);
		pnStatus.add(lbStatus, BorderLayout.EAST);
		pnStatus.setPreferredSize(new Dimension(100, 22));
		lbStatus.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, lbStatus.getFont().getSize()));
		lbStatus.setBorder(new EmptyBorder(0, pad, 0, pad));
		add(pnStatus, BorderLayout.SOUTH);
		
		
		
		/* Listeners */
		btLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile();
			}
		});
		
		btSegment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				segmentFileTextTiling();
			}
		});
		
		btSaveToCSV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
//				File csvFile = new File(sourceFile.getName()+".csv");
				
//				textTiling.segmentToCsvFile(sourceFile, csvFile);
			}
		});
		
		cbShowPreprocess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pnTexts.removeAll();
				pnTexts.setVisible(false);
				
				if(cbShowPreprocess.isSelected()) {
					pnTexts.add(tpOriginalText);
					pnTexts.add(tpWindows);
					pnTexts.add(tpSegmentedtext);
				}
				else {
					pnTexts.add(tpOriginalText);
					pnTexts.add(tpSegmentedtext);
				}
				
				pnTexts.setVisible(true);
				repaint();
			}
		});
		
		cbShowPreprocess.getActionListeners()[0].actionPerformed(null);
		setVisible(true);
//		chooseFile();
		
	}
	
	private void chooseFile() {
		loadFile();
		segmentFileTextTiling();
//		segmentFileC99();
	}
	
	private void loadFile() {

		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.showOpenDialog(this);
		
		sourceFile = fc.getSelectedFile();
		
		if (sourceFile == null) {
			return;
		}
		
		String text = TextExtractor.docToString(sourceFile);
		
		taOriginal.setText(text);
		taOriginal.setCaretPosition(0);
		
	}

	private void segmentFileTextTiling() {
		

		Segmenter segmenter;
		
		String alg = "TextTiling$$$$$";
		
		if (alg.equals("TextTiling")) {

			System.out.println("TextTiling");

			TextTilingBR textTiling = new TextTilingBR();
			
			textTiling.setWindowSize(30);
			textTiling.setStep(5);
//			textTiling.setMinTokenSize(minToken);
			
			segmenter = textTiling;
			lbStatus.setText(String.format("<html>Headers Removed:  <b>%d</b> | Boundary candidates:  <b>%d</b> | Segments count:  <b>%d</b></html>",textTiling.getPreprocess().getHeaderOccurrence(), textTiling.getCollection().boundaries.size(), textTiling.getSegmentsCount())); 
		}
		else {

			C99BR c99 = new C99BR();
			
			c99.setnSegsRate(0.5);
			c99.setRakingSize(11);
			c99.setWeitght(true);
				
			System.out.println("C99");
			
			segmenter = c99;
		}
		

		segmenter.getPreprocess().setRemoveStopWord(true);
		segmenter.getPreprocess().setRemoveAccents(true);
		segmenter.getPreprocess().setRemoveNumbers(true);
		segmenter.getPreprocess().setRemovePunctuation(true);
		segmenter.getPreprocess().setRemoveStem(true);
		segmenter.getPreprocess().setRemoveShortThan(true);
		
		segmenter.getPreprocess().setRemoveHeaders(true);
		segmenter.getPreprocess().setIdentifyEOS(true);
		
		taSegmented.setText(segmenter.segmentsToString(sourceFile));
		taSegmented.setCaretPosition(0);
		
		taWindows.setText(segmenter.preprocessToString());
		taWindows.setCaretPosition(0);
				
	}

	
}
