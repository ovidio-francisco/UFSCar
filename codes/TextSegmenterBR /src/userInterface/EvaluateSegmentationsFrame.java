package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import segmenter.AbstractSegmenter;
import segmenter.Segmenter;
import segmenter.Segmenter.SegmenterAlgorithms;
import segmenter.algorithms.c99br.C99BR;
import segmenter.algorithms.texttile.TextTilingBR;
import segmenter.evaluations.Evaluation;
import segmenter.evaluations.EvaluationData;
import segmenter.tests.Tests;
import segmenter.tests.Tests2;
import utils.Files;

public class EvaluateSegmentationsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextArea taReal = new JTextArea();
	private JTextArea taTest = new JTextArea();
	private JTextArea taOut  = new JTextArea();
	private JButton   btLoadFile   = new JButton("Load file");
	private JButton   btLoadFolder = new JButton("Load folder");
	private JButton   btTests      = new JButton("Tests");
	private JButton   btTests2     = new JButton("Tests 2");

	private JTabbedPane tpReal  = new JTabbedPane();
	private JTabbedPane tpTest  = new JTabbedPane();
	private String[] algs = {"TextTiling", "C99"};
	private JComboBox<String> cbAlg = new JComboBox<>(algs);
	
	
	private static JSpinner spWinSize      = new JSpinner(new SpinnerNumberModel(30,  1, 999, 1));
	private static JSpinner spStep         = new JSpinner(new SpinnerNumberModel(5 ,  1, 999, 1));

	private static JSpinner  spNSegs       = new JSpinner(new SpinnerNumberModel(0.5, -1, 1, 0.1));
	private static JSpinner  spRankingsize = new JSpinner(new SpinnerNumberModel(11,  1, 999, 2));
	private static JCheckBox cbWeight      = new JCheckBox("Weight context vector with term frequencies", true);
	
	private static JCheckBox cbStopWords   = new JCheckBox("Remove StopWords", false);
	private static JCheckBox cbStems        = new JCheckBox("Remove Stems", false);

	
	public EvaluateSegmentationsFrame() {
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("TextTilingBR");
		
		setLayout(new BorderLayout());	
		
		cbAlg         .setMaximumSize(new Dimension(190, 25));
		spWinSize     .setMaximumSize(new Dimension(40 , 25));
		spStep        .setMaximumSize(new Dimension(40 , 25));
		spNSegs       .setMaximumSize(new Dimension(40 , 25));
		spRankingsize .setMaximumSize(new Dimension(40 , 25));
		cbWeight      .setMaximumSize(new Dimension(400, 15));
		
		cbAlg.setSelectedItem(0);
		
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(btTests);
		toolBar.addSeparator();
		toolBar.add(btTests2);
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add(btLoadFile);
		toolBar.addSeparator();
		toolBar.add(btLoadFolder);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Algorithm: "));
		toolBar.add(cbAlg);
		toolBar.addSeparator();
		toolBar.add(cbStopWords);
		toolBar.add(cbStems);
		
		
		JToolBar textTilingToolBar = new JToolBar();
		textTilingToolBar.setFloatable(false);
		textTilingToolBar.add(new JLabel("TextTiling Config → "));
		textTilingToolBar.addSeparator();
		textTilingToolBar.add(new JLabel("Win Size:"));
		textTilingToolBar.add(spWinSize);
		textTilingToolBar.addSeparator();
		textTilingToolBar.add(new JLabel("Step:"));
		textTilingToolBar.add(spStep);
		textTilingToolBar.addSeparator();

		
		JToolBar c99ToolBar = new JToolBar();
		c99ToolBar.setFloatable(false);
		c99ToolBar.add(new JLabel("C99 Config → "));
		c99ToolBar.addSeparator();
		c99ToolBar.add(new JLabel("Number of segments (default automatic = -1) : "));
		c99ToolBar.add(spNSegs);
		c99ToolBar.addSeparator();
		c99ToolBar.add(new JLabel("Size of ranking mask (>= 1 and an odd number) : "));
		c99ToolBar.add(spRankingsize);
		c99ToolBar.addSeparator();
		c99ToolBar.add(cbWeight);
		
		JPanel pnToolBars = new JPanel();
		pnToolBars.setLayout(new BorderLayout());

		
		pnToolBars.add(toolBar, BorderLayout.NORTH);
		pnToolBars.add(textTilingToolBar, BorderLayout.CENTER);
		pnToolBars.add(c99ToolBar, BorderLayout.SOUTH);
		
		add(pnToolBars, BorderLayout.NORTH);
		
//		taReal.setLineWrap(true);
//		taReal .setWrapStyleWord(true);

//		taTest.setLineWrap(true);
//		taTest.setWrapStyleWord(true);

		
		JScrollPane spReal = new JScrollPane(taReal);
		JScrollPane spTest = new JScrollPane(taTest);
		JScrollPane spOut  = new JScrollPane(taOut);
		
		JPanel pnTexts         = new JPanel();
		JPanel pnOut		   = new JPanel();
		
		
		tpReal.add("Real", spReal);
		tpTest.add("Test", spTest);
		
		int pad = 4;
		tpReal.setBorder(new EmptyBorder(pad, pad  , 0, pad/2));
		tpTest.setBorder(new EmptyBorder(pad, pad/2, 0, pad  ));
		pnOut .setBorder(new EmptyBorder(pad, pad, pad, pad  ));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnTexts, pnOut);
		splitPane.setDividerLocation(100);
		splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		add(splitPane);
		
		taOut.setFont(new Font(Font.MONOSPACED, Font.PLAIN, taOut.getFont().getSize()));
		
		spOut.setPreferredSize(new Dimension(100, 100));
		
		pnTexts.setLayout(new GridLayout(1, 2));
		pnOut  .setLayout(new BorderLayout());
		pnTexts.add(tpReal);
		pnTexts.add(tpTest);
		pnOut.add(spOut, BorderLayout.CENTER);
		
		
		btLoadFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Loading a file");

				
				File doc=null, real=null;
				
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./docs"));
				fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
					@Override
					public String getDescription() {
						return "Text files";
					}
					
					@Override
					public boolean accept(File arg0) {
						return Files.getFileExtension(arg0).equals("txt") || arg0.isDirectory();
					}
				});
				fc.showOpenDialog(null);

				doc = fc.getSelectedFile();
				
				if(doc==null) return;
				
				real = new File(doc.getAbsoluteFile()+".csv");
				
				
				load(doc, real, getAlgorithm());
			}
		});
		
		btLoadFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				loadall();
				ArrayList<EvaluationData> evaluations = loadall();
				showTests(evaluations);
				
			}
		});
		
		btTests.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<ArrayList<EvaluationData>> tests = Tests.doTestes(new File("./docs"));
				
				
				showTests1(tests);
				
			}
		});
		
		btTests2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Tests2.doTests();
			}
			
		});
		
		setVisible(true);
	}
	
	private void load(File doc, File real, Segmenter alg) {
		
		
		tpReal.setTitleAt(0, real.getName());
		tpTest.setTitleAt(0, doc.getName());
		
//		taReal.setText(arrayToString(Evaluation.getTokens(real, alg)));
//		taTest.setText(arrayToString(Evaluation.getTokens(doc, alg)));

		taReal.setText(arrayToString(Evaluation.getSentences(real, alg)));
		taTest.setText(arrayToString(Evaluation.getSentences(doc, alg)));
		
		evaluate(doc, real, alg);
	}
	
	private void evaluate(File doc, File real, Segmenter alg) {
		EvaluationData evaluationData = Evaluation.evalueate(real, doc, alg);
		
		taOut.setText(evaluationData.toString());
	}
	
//	private void evaluate(File doc, Segmenter alg) {
//		EvaluationData evaluationData = Evaluation.evalueate(new File(doc+".csv"), doc, alg);
//		
//		taOut.setText(taOut.getText()+evaluationData.toString());
//		taOut.setCaretPosition(0);
//	}
	
	private ArrayList<EvaluationData> loadall() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./docs"));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(null);

		File folder = fc.getSelectedFile();
		
		return testAll(folder);
	}
	
	private ArrayList<EvaluationData> testAll(File folder) {
		ArrayList<EvaluationData> evaluations = new ArrayList<>();
		
		File files[] = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return Files.getFileExtension(pathname).equals("txt");
			}
		});
		
		for(File txt : files) {
			File csv = new File(txt+".csv");
			EvaluationData evaluation = Evaluation.evalueate(csv, txt, getAlgorithm()); 
			evaluations.add(evaluation);
			
		}
		
		return evaluations;
	}

	private void showTests(ArrayList<EvaluationData> evaluations) {
		
//		taOut.setText("");
		
		for(EvaluationData e : evaluations) {
			if(e==null) {
				taOut.setText(taOut.getText() + "Erro\n\n");
			}
			else {
				taOut.setText(taOut.getText()+e.toString());
			}
		}

		taOut.setText(taOut.getText()+String.format("\n%d files avaliated\n", evaluations.size()));
		taOut.setCaretPosition(0);
	}
	
	private void showTests1(ArrayList<ArrayList<EvaluationData>> evaluations) {
		for(ArrayList<EvaluationData> ev : evaluations) {
			showTests(ev);
		}
	}
	
	public String arrayToString(ArrayList<String> stringArray) {
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		
		for (String s : stringArray) sb.append(String.format("%3d - %s\n", i++, s));
		
		return sb.toString();
	}
	
	private Segmenter getAlgorithm() {
		Segmenter result = cbAlg.getSelectedItem().toString().equals(algs[0]) ? 
			 	              getTextTilingSegmenter() : 
					          getC99BrSegmenter(); 
		
		((AbstractSegmenter)result).getPreprocess().setRemoveStopWord(cbStopWords.isSelected());
		((AbstractSegmenter)result).getPreprocess().setRemoveStem(cbStems.isSelected());	 	              
		
		return result;
	}
	
	private static TextTilingBR getTextTilingSegmenter() {
		System.out.println(SegmenterAlgorithms.TEXT_TILING);
		
		int winSize  = (Integer)spWinSize.getValue();
		int step     = (Integer)spStep.getValue();

		
		TextTilingBR textTiling = new TextTilingBR();
		
		textTiling.setWindowSize(winSize);
		textTiling.setStep(step);
//		textTiling.setStopwords(null);
//		textTiling.setStemmer(TextTilingBR.StemmingAlgorithms.NONE);
//		textTiling.setMinTokenSize(1);
//		textTiling.setRemoveHeader(false);
		textTiling.getPreprocess().setRemoveHeaders(false);
		
		return textTiling;
		
	}
	
	private static C99BR getC99BrSegmenter() {
		System.out.println(SegmenterAlgorithms.C99);
		
		
		double  n = (Double)spNSegs.getValue();
		int     s = (Integer)spRankingsize.getValue();
		boolean w = cbWeight.isSelected();

		C99BR c99 = new C99BR();
//		c99.setnSegs(n);
		c99.setnSegsRate(n); 
		c99.setRakingSize(s);
		c99.setWeitght(w);
		
		return c99;
	}
		
}
