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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import preprocessamento.Cleaner;
import segmenter.algorithms.texttile.TextTilingBR;
import segmenter.algorithms.texttile.TextTilingBR.StemmingAlgorithms;
import utils.TextExtractor;

public class ViewSegmentationFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea taOriginal  = new JTextArea();
	private JTextArea taSegmented = new JTextArea();
	private JTextArea taWindows   = new JTextArea();
	
	private JLabel lbStatus = new JLabel();

	private JSpinner spWinSize      = new JSpinner(new SpinnerNumberModel(30, 1, 999, 1));
	private JSpinner spStep         = new JSpinner(new SpinnerNumberModel(5 , 1, 999, 1));
	private JSpinner spMinTokenSize = new JSpinner(new SpinnerNumberModel(2 , 1, 999, 1));
	
	private String stemmingAlgs[]            = {"None", "Porter", "Orengo"};
	private String allowedChars[]            = {"Only Letters", "Letters and Numbers", "Letters, numbers and punctuaction"};
	private JCheckBox cbRemoveStopWords      = new JCheckBox("Remove StopWords");
	private JComboBox<String> cbStemming     = new JComboBox<>(stemmingAlgs);
	private JComboBox<String> cbAllowedChars = new JComboBox<>(allowedChars);
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
		
		cbStemming       .setMaximumSize(new Dimension(100, 25));
		cbAllowedChars   .setMaximumSize(new Dimension(190, 25));
		spWinSize        .setMaximumSize(new Dimension(40 , 25));
		spStep           .setMaximumSize(new Dimension(40 , 25));
		spMinTokenSize   .setMaximumSize(new Dimension(40 , 25));
		cbRemoveStopWords.setMaximumSize(new Dimension(170, 15));
		cbShowPreprocess .setMaximumSize(new Dimension(180, 15));
		
		cbStemming.setSelectedItem(stemmingAlgs[2]);
		cbAllowedChars.setSelectedItem(allowedChars[0]);
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
		toolBar.add(new JLabel("Win Size:"));
		toolBar.add(spWinSize);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Step:"));
		toolBar.add(spStep);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Min Token Size:"));
		toolBar.add(spMinTokenSize);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Stemmer: "));
		toolBar.add(cbStemming);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Allowed:"));
		toolBar.add(cbAllowedChars);
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
		int winSize  = (Integer)spWinSize.getValue();
		int step     = (Integer)spStep.getValue();
		int minToken = (Integer)spMinTokenSize.getValue();
		boolean removeStopWords = cbRemoveStopWords.isSelected();
		
		StemmingAlgorithms stem = null;
		String selectedStem = cbStemming.getSelectedItem().toString();
		String selectedAllowedChars = cbAllowedChars.getSelectedItem().toString();
		File stopWordFile = removeStopWords ? new File("stopPort.txt") : null;
		
		if (selectedStem.equals(stemmingAlgs[0])) stem = StemmingAlgorithms.NONE;
		if (selectedStem.equals(stemmingAlgs[1])) stem = StemmingAlgorithms.PORTER;
		if (selectedStem.equals(stemmingAlgs[2])) stem = StemmingAlgorithms.ORENGO;
		
		boolean allowNumbers     = selectedAllowedChars.equals(allowedChars[1]);
		boolean allowPunctuation = false;
		
		if (selectedAllowedChars.equals(allowedChars[2])) {
			allowNumbers     = true;
			allowPunctuation = true;
		}
		
		Cleaner.setAllowedChars(allowNumbers, allowPunctuation);
		
		
		TextTilingBR textTiling = new TextTilingBR();
		
		textTiling.setWindowSize(winSize);
		textTiling.setStep(step);
		textTiling.setStopwords(stopWordFile);
		textTiling.setStemmer(stem);
		textTiling.setMinTokenSize(minToken);
		
		taSegmented.setText(textTiling.segmentsToString(sourceFile));
		taSegmented.setCaretPosition(0);
		
		taWindows.setText(textTiling.preprocessToString());
		taWindows.setCaretPosition(0);
				
		lbStatus.setText(String.format("<html>Headers Removed:  <b>%d</b> | Boundary candidates:  <b>%d</b> | Segments count:  <b>%d</b></html>",textTiling.getHeaderOccurrence(), textTiling.getCollection().boundaries.size(), textTiling.getSegmentsCount()));
	}
//	
//	private void segmentFileC99() {
//		C99BR c99 = new C99BR();
//		
//		taSegmented.setText(c99.segmentsToString(sourceFile));
//	}
//	
//	private TextTilingBR getTextTilingSegmenter() {
//		return null;
//		
//	}
	
}
