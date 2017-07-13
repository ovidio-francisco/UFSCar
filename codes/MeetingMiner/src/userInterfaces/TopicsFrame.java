package userInterfaces;

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

import meetingMiner.MMTopic;
import meetingMiner.MeetingMiner;
import meetingMiner.Segment;
import topicExtraction.TETConfigurations.TopicExtractionConfiguration;
import segmenter.Segmenter;
import segmenter.algorithms.texttile.TextTilingBR;
import utils.Files;
import utils.ShowStatus;

public class TopicsFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JTree view = new JTree();
	private JPanel pnSegments = new JPanel();


	public TopicsFrame() {

		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Meeting Miner");
		

		JToolBar toolBar = new JToolBar();
		
		JButton btLoadFile = new JButton("Load File");
		JButton btAddToTheBase = new JButton("Adicionar documentos");
		JButton btExtractTopics = new JButton("Extract Topics");
		JButton btShowSegments = new JButton("Exibir Segmentos");
		
		toolBar.setFloatable(false);
		toolBar.addSeparator();
		toolBar.add(btLoadFile);
		toolBar.addSeparator();
		toolBar.add(btAddToTheBase);
		toolBar.addSeparator();
		toolBar.add(btExtractTopics);
		toolBar.addSeparator();
		toolBar.add(btShowSegments);
		toolBar.addSeparator();

		
		JPanel pnToolBar = new JPanel();
		pnToolBar.setLayout(new BorderLayout());
		pnToolBar.add(toolBar, BorderLayout.NORTH);
		
		
		JTabbedPane tpTopics = new JTabbedPane();
		JScrollPane spTopics = new JScrollPane(view);
		tpTopics.add("Tópicos", spTopics);
		tpTopics.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		pnSegments.setLayout(new BoxLayout(pnSegments, BoxLayout.Y_AXIS));
		JScrollPane spSegments = new JScrollPane(pnSegments);
		spSegments.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(5, 5, 5, 5) ));
		
		JPanel pnResults = new JPanel();
		pnResults.setLayout(new BorderLayout());
		pnResults.setBorder(new EmptyBorder(5, 5, 5, 5));
		JLabel lbSegments = new JLabel("Segments", SwingConstants.CENTER);
		lbSegments.setBorder(new EmptyBorder(5, 0, 0, 0));

		pnResults.add(lbSegments, BorderLayout.NORTH);
		pnResults.add(spSegments, BorderLayout.CENTER);
		
		
		
		JPanel pnBottom = new JPanel();
		pnBottom.setLayout(new BorderLayout());
		pnBottom.setPreferredSize(new Dimension(100, 100));
		pnBottom.setBorder(new EmptyBorder(5, 5, 5, 5));
		JTextArea taStatus = new JTextArea("Status");
		JScrollPane spStatus = new JScrollPane(taStatus);
		pnBottom.add(spStatus);
		
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new GridLayout(1, 2));
		pnCenter.add(tpTopics);
		pnCenter.add(pnResults);
		
		setLayout(new BorderLayout());	
		add(pnToolBar, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		add(pnBottom, BorderLayout.SOUTH);
		

		ShowStatus.setTextArea(taStatus);

		
		
		
//		btLoadFile.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				File doc = null;
//				
//				JFileChooser fc = new JFileChooser();
//				fc.setCurrentDirectory(new File("./.."));
//				fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
//					@Override
//					public String getDescription() {
//						return "Text files";
//					}
//					
//					@Override
//					public boolean accept(File arg0) {
//						return "doc docx pdf".contains(Files.getFileExtension(arg0));
//					}
//				});
//				fc.showOpenDialog(null);
//
//				doc = fc.getSelectedFile();
//				if(doc==null) return;
//				
//				ArrayList<String> segments = getSegments(doc);
//				
//
//				for(String seg : segments) {
//					addSegmentPanel(seg);
//				}
//			}
//		});
		
		btExtractTopics.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
		        MeetingMiner.prepareFolders();

				
				defineConfiguration();
				
				MeetingMiner.setView(view.getModel());
				
				MeetingMiner.miningTheMeetings();
			}
		});
		
		
		
		btAddToTheBase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./.."));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				
			 	int option =  fc.showOpenDialog(null);
			 	if (option != JFileChooser.APPROVE_OPTION) return;
			 	
				File folder = fc.getSelectedFile();
				
//				=========================================================================
				
				Files.prepareBaseFolders();
				Files.addToTheBase(folder);
				Files.extractTextToTheBase();
				
				Segmenter segmenter = new TextTilingBR();
				
				Files.extractSegmentsToTheBase(segmenter);
				
			}
		});
		
		
		btShowSegments.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				MeetingMiner.prepareFolders();	
					
				ArrayList<MMTopic> topics = MeetingMiner.extractDescriptorsAndFiles();
				ArrayList<Segment> segments = Segment.getAllSegments(topics);
				
				for(Segment seg : segments) {
					addSegmentPanel(seg);
				}
			
	            				
			}
		});
		
		
		setVisible(true);
	}
	
	private void addSegmentPanel(Segment seg) {
		JPanel pnSeg = new JPanel();
		JTextArea taSeg = new JTextArea();
		JScrollPane spSeg = new JScrollPane(taSeg);
		taSeg.setText(seg.getText());

		int w = pnSegments.getWidth()-20;
		int h = 150;
		
		taSeg.setLineWrap(true);
		
		pnSeg.setLayout(new BorderLayout());
		pnSeg.add(spSeg, BorderLayout.CENTER);
		pnSeg.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 5, 10), BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		pnSeg.setPreferredSize(new Dimension(w, h));
		pnSeg.setMaximumSize(new Dimension(w, h));
		
		
		StringBuilder sb = new StringBuilder();
		for(String s : seg.getDescriptors()){
			sb.append(s+", ");
		}
		String descriptors = sb.toString().substring(0, sb.length() - 2);
			
		JLabel lbDescriptors = new JLabel(descriptors);
		pnSeg.add(lbDescriptors, BorderLayout.SOUTH);
		
		
		pnSegments.add(pnSeg);
		
		pnSegments.revalidate();
		pnSegments.repaint();
		
	}
	
	
//	private Segmenter createSegmenter() {
//		C99BR c99 = new C99BR();
//		c99.setnSegsRate(0.85);
//		c99.setRakingSize(11);
//		c99.setWeitght(true);
//		
//		c99.getPreprocess().setRemovePageNumbers(true);
//		c99.getPreprocess().setRemoveHeaders(true);
//		c99.getPreprocess().setRemoveExtraSpaces(true);		
//		
//		return c99;
//	}
	
//	private ArrayList<String> getSegments(File doc) {
//		
//		String text = TextExtractor.docToString(doc);
//		
//		Segmenter segmenter = createSegmenter();
//		
//		return segmenter.getSegments(text);
//	}
//	
	
    private void defineConfiguration(){
        
        TopicExtractionConfiguration configuration = new TopicExtractionConfiguration();

        configuration.setDirEntrada(MeetingMiner.getArfFolder().getAbsolutePath());
        configuration.setDirSaida  (MeetingMiner.getOutFolder().getAbsolutePath());
        
//      configuration.setKMeans(true); 
        configuration.setPLSA(true);
//      configuration.setBisectingKMeans(true);

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
        
        MeetingMiner.setTopicExtractionconfiguration(configuration);
    }
    

	
}
