package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import TETConfigurations.TopicExtractionConfiguration;
import m4mUtils.M4MShowStatus;
import mining4meetings.Mining4Meetings;
import segmenter.Segmenter;
import segmenter.algorithms.c99br.C99BR;
import utils.Files;
import utils.TextExtractor;

public class TopicsFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
//	private JTextArea taText = new JTextArea();
	private JTree view = new JTree();
	private JPanel pnSegments = new JPanel();


	public TopicsFrame() {

		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Topics");
		

		JToolBar toolBar = new JToolBar();
		
		
		JButton btLoadFile = new JButton("Load File");
		JButton btExtractTopics = new JButton("Extract Topics");
		JLabel  lbStatus = new JLabel("Status");
		
		toolBar.setFloatable(false);
		toolBar.addSeparator();
		toolBar.add(btLoadFile);
		toolBar.addSeparator();
		toolBar.add(btExtractTopics);
		toolBar.addSeparator();
		toolBar.add(lbStatus);
		toolBar.addSeparator();

		
		
		JPanel pnToolBar = new JPanel();
		pnToolBar.setLayout(new BorderLayout());
		pnToolBar.add(toolBar, BorderLayout.NORTH);
		
		
		JTabbedPane tpText = new JTabbedPane();
//		JScrollPane spText = new JScrollPane(taText);
		JScrollPane spText = new JScrollPane(view);
		tpText.add("segments", spText);
		
		pnSegments.setLayout(new BoxLayout(pnSegments, BoxLayout.Y_AXIS));
		JScrollPane spSegments = new JScrollPane(pnSegments);
		spSegments.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(5, 5, 5, 5) ));
		
		JPanel pnResults = new JPanel();
		pnResults.setLayout(new BorderLayout());
		pnResults.setBorder(new EmptyBorder(5, 5, 5, 5));
		JLabel lbSegments = new JLabel("Segments", SwingConstants.CENTER);
		lbSegments.setBorder(new EmptyBorder(5, 0, 5, 0));

		pnResults.add(lbSegments, BorderLayout.NORTH);
		pnResults.add(spSegments, BorderLayout.CENTER);
		
		
		
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new GridLayout(1, 2));
		pnCenter.add(tpText);
		pnCenter.add(pnResults);
		
		setLayout(new BorderLayout());	
		add(pnToolBar, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		

		
		M4MShowStatus.setLabel(lbStatus);
		
		
		
		
		btLoadFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File doc = null;
				
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./docs"));
				fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
					@Override
					public String getDescription() {
						return "Text files";
					}
					
					@Override
					public boolean accept(File arg0) {
						return "doc docx pdf".contains(Files.getFileExtension(arg0));
					}
				});
				fc.showOpenDialog(null);

				doc = fc.getSelectedFile();
				if(doc==null) return;
				
				ArrayList<String> segments = getSegments(doc);
				
//				taText.setText("");
				for(String seg : segments) {
//					taText.setText(taText.getText() + seg + "\n\n============================\n\n");
					addSegmentPanel(seg);
				}
				
				
			}
		});
		
		btExtractTopics.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
		        Mining4Meetings.prepareFolders();

				
				defineConfiguration();
				
				Mining4Meetings.setView(view.getModel());
				
				Mining4Meetings.mining4Meetings();
			}
		});
		
		
		setVisible(true);
	}
	
	private void addSegmentPanel(String seg) {
		JPanel pnSeg = new JPanel();
		JTextArea taSeg = new JTextArea();
		JScrollPane spSeg = new JScrollPane(taSeg);
		taSeg.setText(seg);

		int w = pnSegments.getWidth()-20;
		int h = 150;
		
		pnSeg.setLayout(new BorderLayout());
		pnSeg.add(spSeg, BorderLayout.CENTER);
		pnSeg.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 5, 10) ,BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		pnSeg.setPreferredSize(new Dimension(w, h));
		pnSeg.setMaximumSize(new Dimension(w, h));
		
		JLabel lbTopics = new JLabel("[topics]");
		pnSeg.add(lbTopics, BorderLayout.SOUTH);
		
		
		pnSegments.add(pnSeg);
		
		pnSegments.revalidate();
		pnSegments.repaint();
		
	}
	
	
	private Segmenter createSegmenter() {
		C99BR c99 = new C99BR();
		c99.setnSegsRate(0.85);
		c99.setRakingSize(11);
		c99.setWeitght(true);
		
		c99.setRemovePageNumbers(true);
		c99.setRemoveHeader(true);
		c99.setRemoveExtraSpaces(true);		
		
		return c99;
	}
	
	private ArrayList<String> getSegments(File doc) {
		
		String text = TextExtractor.docToString(doc);
		
		Segmenter segmenter = createSegmenter();
		
		return segmenter.getSegments(text);
	}
	
	
    private void defineConfiguration(){
        
        TopicExtractionConfiguration configuration = new TopicExtractionConfiguration();

        configuration.setDirEntrada(Mining4Meetings.getArfFolder().getAbsolutePath());
        configuration.setDirSaida  (Mining4Meetings.getOutFolder().getAbsolutePath());
        
//        configuration.setKMeans(true); 
        configuration.setPLSA(true);
//        configuration.setBisectingKMeans(true);

        if (true) {

            configuration.setAutoNumTopics(false);

            ArrayList<Integer> numTopics = new ArrayList<>();
            
            numTopics.add(10);
//            numTopics.add(50);
            
            
//            ListModel model = lNumTopics.getModel();
//            for(int item = 0;item<model.getSize();item++){
//                numTopics.add(Integer.parseInt(model.getElementAt(item).toString()));
//            }

            configuration.setNumTopics(numTopics);
        }
        
        Mining4Meetings.setTopicExtractionconfiguration(configuration);
    }
    

	
}
