package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import meetingMiner.MMTopic;
import meetingMiner.MeetingMiner;
import meetingMiner.Segment;
import preprocessamento.Preprocess;
import segmenters.Segmenter;
import topicExtraction.TETConfigurations.TopicExtractionConfiguration;
import utils.Files;
import utils.ShowStatus;
import utils.UIUtils;

public class TopicsFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JPanel  pnSegments          = new JPanel();
	private JPanel  pnToolBar           = new JPanel();
	private JPanel  pnCenter            = new JPanel();
	private JPanel  pnStatusBar         = null;
	private JPanel  pnSearch             = null;
	
	private JButton btAddToTheBase      = new JButton("Adicionar documentos");
	private JButton btConfg             = new JButton("Configurações");
	private JButton btExtractTopics     = new JButton("Extrair Tópicos");
	private JButton btShowTree          = new JButton("Exibir tópicos");
	private JButton btShowSegments      = new JButton("Exibir Segmentos");
	
	private JLabel lbSegmentsCount      = new JLabel();
	private JLabel lbDocsCount          = new JLabel();
	private JLabel lbTopicsExtracted    = new JLabel();
	private JLabel lbAlgorithm          = new JLabel();
	private JLabel lbDescriptorsByTopic = new JLabel("Descritores por tópico: ");
			
	private JTextField tfSearchDescriptors = new JTextField(30);
	private JButton btSearch = new JButton("Localizar");
	
	private JComboBox<Integer> cbDescriptorsByTopic = new JComboBox<>(new Integer[] {5, 10, 15, 20, 25});

	ArrayList<PnSegment> pnSegs = new ArrayList<>();

	private JTextArea taStatus = new JTextArea("");


	private JScrollPane spSegments = null;

	
	private boolean isBasesFolderFound = false;
	private boolean isOrignalDocsFound = false;
	private boolean isArffFileFound = false;
	private boolean isMatricesFound = false;
	private int     originalDocsCount = 0;
	
	public TopicsFrame() {

		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Meeting Miner");
		
		pnToolBar = createPnToolBar();
		
		
		pnSearch               = createPnSearchSegments(); 
		pnSegments             = createPnSegments();
		JPanel pnResultsStatus = createPnResultsStatus();
		
		JPanel pnResults = new JPanel();
		pnResults.setLayout(new BorderLayout());


		
		pnResults.add(pnSearch, BorderLayout.NORTH);
		pnResults.add(spSegments, BorderLayout.CENTER);
		pnResults.add(pnResultsStatus, BorderLayout.SOUTH);
		
		
		JPanel pnBottom = createPnBottom();
		
		pnStatusBar = createPnStatus();
		pnBottom.add(pnStatusBar, BorderLayout.SOUTH);
		
		
		pnCenter.setLayout(new GridLayout(1, 2));
		pnCenter.add(pnResults);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnCenter, pnBottom);
		splitPane.setBorder(new EmptyBorder(0, 5, 0, 5));
		splitPane.setDividerLocation(500);                          // try to put on de very botton position
		pnCenter.setMinimumSize(new Dimension(100, 150));
		pnBottom.setMinimumSize(new Dimension(100,  30));

		setLayout(new BorderLayout());	
		add(pnToolBar, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);

		
		/** Creating a default Topic Extraction Configuration */
		MeetingMiner.prepareFolders();
		defaultConfiguration();

		
		cbDescriptorsByTopic.setMaximumSize(new Dimension(50, 25));
		cbDescriptorsByTopic.setSelectedItem(MeetingMiner.getDescriptorsByTopic());
		cbDescriptorsByTopic.setEditable(true);
		
		verifyStatus();
		
		ShowStatus.setTextArea(taStatus);
		addListeners();
		setVisible(true);
	}
	
	@Override
	public void setVisible(boolean value) {
	    super.setVisible(value);
	    tfSearchDescriptors.requestFocusInWindow();
	}
	
	private void verifyStatus() {
		
		isBasesFolderFound = Files.getBasesFolder().exists();
		isOrignalDocsFound = Files.getOriginalDocs().exists();
		if (isOrignalDocsFound) originalDocsCount = Files.getOriginalDocs().list().length;
		isArffFileFound    = MeetingMiner.getArffFile().exists();
		isMatricesFound    = MeetingMiner.getDocument_TopicMatrixFile().exists() && MeetingMiner.getTerm_TopicMatrixFile().exists();
		
		ShowStatus.setMessage(isBasesFolderFound ? "Diretório base econtrado" : "Diretório base não econtrado");
		ShowStatus.setMessage(originalDocsCount > 0 ? String.format("%d documentos encontrados", originalDocsCount) : "Nenhum documento encontrado");
		ShowStatus.setMessage(isArffFileFound ? "Arquivo arff encontrado" : "Arquivo arff não encontrado");
		ShowStatus.setMessage(isMatricesFound ? "Matrizes encontradas" : "Matrizes não encontradas");
		
		if(isMatricesFound) {
			MeetingMiner.extractDescriptorsAndFiles();
		}
		
		if (isMatricesFound) {
			lbTopicsExtracted.setText(String.format("%d topics extracted", MeetingMiner.getNumTopics()));
			
			Properties topicExtractionConfigSaveFile = new Properties();
			try {
				topicExtractionConfigSaveFile.load(new FileInputStream(MeetingMiner.getConfigSaveFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			lbAlgorithm.setText(topicExtractionConfigSaveFile.getProperty("Algorithm"));
		}
		else {
			lbTopicsExtracted.setText(String.format("%d topics extracted", MeetingMiner.getNumTopics()));
			
			if (MeetingMiner.getConfigSaveFile().exists())
				lbAlgorithm.setText("Please, extract the topics");
			
		}
		
		lbDocsCount.setText(String.format("%d documentos na base de dados", originalDocsCount));
		
		
		setControlsEnable();
	}
	
	private void extractTopics() {
		
//		MeetingMiner.prepareFolders();
//		defaultConfiguration();
		MeetingMiner.miningTheMeetings();
	}
	
	private void showTree() {
		
		MeetingMiner.prepareFolders();
		MeetingMiner.extractDescriptorsAndFiles();
		
		FrShowTopicsTree f = new FrShowTopicsTree();
		f.setTreeRoot(MeetingMiner.createTree());
		f.setVisible(true);
	}
	
	private int addToTheBase() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./.."));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		
	 	int option =  fc.showOpenDialog(null);
	 	if (option != JFileChooser.APPROVE_OPTION) return 0;
	 	
		File folder = fc.getSelectedFile();
		
//		=========================================================================
		
		Files.prepareBaseFolders();
		int filesAdded = Files.addToTheBase(folder);
		Files.extractTextToTheBase();
		
//		Segmenter segmenter = new TextTilingBR();
                
                Segmenter segmenter = MeetingMiner.getBestSegmenterConfiguration().buildSegmenter();
		
		int segmentsExtracteds = Files.extractSegmentsToTheBase(segmenter);
		
		ShowStatus.setMessage("Concluído!");
		ShowStatus.setMessage(filesAdded + " arquivos adicionados a base");
		ShowStatus.setMessage(segmentsExtracteds + " segmentos extratídos");
		
		return filesAdded;
	}
	
	
	private void addListeners() {
		
		btExtractTopics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
			        @Override
			        public void run(){
			        	setWainting(true);
			        	extractTopics();
			    		setWainting(false);
			        }
				}.start();  
			}
		});
		
		btShowTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTree();
			}
		});
		
		btAddToTheBase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
			        @Override
			        public void run(){
			        	setWainting(true);
			        	if (addToTheBase() > 0 ) {
			        		MeetingMiner.represent(true);
			        		MeetingMiner.removePreviousMatrices();
			        	}
			    		setWainting(false);
			        }
				}.start();
			}
		});
		
		btConfg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrConfigExtractor f = new FrConfigExtractor(MeetingMiner.getTopicExtractionconfiguration());
				f.setVisible(true);
			}
		});
		
		btShowSegments.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
			        @Override
			        public void run(){
			        	setWainting(true);
			        	showSegments(false);
			    		setWainting(false);
			        }
				}.start();
			}
		});
		
		btSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
			        @Override
			        public void run(){
			        	setWainting(true);
			        	showSegments(true);
			    		setWainting(false);
			        }
				}.start();
			}
		});
		
		cbDescriptorsByTopic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MeetingMiner.setDescriptorsByTopic(getUserDescriptorsByTopic());
			}
		});
		
		tfSearchDescriptors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btSearch.getActionListeners()[0].actionPerformed(null);
			}
		});
	}
	
	
	private ArrayList<MMTopic> filterTopicsByDescriptor(ArrayList<MMTopic> topics, Set<String> descs) {
		ArrayList<MMTopic> result = new ArrayList<>();
		
		for(MMTopic t : topics) {
//			ArrayList<String> descriptorsIntersections = t.descriptorsIntersection(descs);
			Set<String> descriptorsIntersections = t.descriptorsStemedIntersection(descs);
			if(descriptorsIntersections.size() > 0) {
				result.add(t);
			}
		}
		
		return result;
	}
	
	private void showSegments(boolean filter) {
		MeetingMiner.prepareFolders();	
		
		MeetingMiner.extractDescriptorsAndFiles();
		ArrayList<MMTopic> topics = MeetingMiner.getMMTopics();
		
		ArrayList<Segment> segments = null;
		
		if (filter) {
			
			HashMap<String, String> userDescsStems = new HashMap<>();
			for(String s : tfSearchDescriptors.getText().split(" ")) {
				if (!Preprocess.getStopWords().isStopWord(s)) {
					userDescsStems.put(s, Preprocess.getStemmer().wordStemming(s));
				}
			}

			/** Filtra os tópicos que contém algum descritor informado pelo usuário */
			ShowStatus.setMessage("\nFiltering topics by: " + userDescsStems.values());
			topics = filterTopicsByDescriptor(topics, userDescsStems.keySet());

			/** Carrega os (objetos) segmentos que contém Documentos(segmento de uma ata) associados com os tópicos filtrados */
			/** Para cada tópico associado ao segmento, associa os decritores dos tópico ao segmento */
			segments = Segment.getAllSegments(topics);
			
			for(Segment seg : segments) {
				seg.matchUserDescriptors(userDescsStems.keySet());
			}
			Segment.sortSegmentsByMatcheCount(segments);
			
			ShowStatus.setMessage(String.format("%d tópicos selecionados %d segmentos encontrados", topics.size(), segments.size()));
		}
		else {

			/** Carrega todos os segmentos que contém Documentos associados com todos os tópicos */
			segments = Segment.getAllSegments(topics);
		}
			
		
		clearSegments();
		lbSegmentsCount.setText("Gerando visualização");
		ShowStatus.setMessage  ("Gerando visualização");
		int count = 0;
		for(Segment seg : segments) {
			addSegmentPanel(seg);
			count++;
		}
		lbSegmentsCount.setText(count+" trechos relacionados");
		ShowStatus.setMessage  (count+" trechos relacionados");

	}
	
	
	private void setWainting(boolean waiting) {
		UIUtils.setWaiting(new Container[] {pnCenter, pnToolBar}, waiting);
		
		if (!waiting){
			verifyStatus();
		}
	}
	
	private void setControlsEnable() {
		btExtractTopics     .setEnabled(isArffFileFound);
		btConfg             .setEnabled(isArffFileFound);
		lbDescriptorsByTopic.setEnabled(isArffFileFound);
		cbDescriptorsByTopic.setEnabled(isArffFileFound);
		
		boolean wasTopicsExtactionExecuted = isMatricesFound;
		
		btShowSegments.setEnabled(wasTopicsExtactionExecuted);
		btShowTree    .setEnabled(wasTopicsExtactionExecuted);
		
		UIUtils.setEnable(pnSearch, wasTopicsExtactionExecuted);
	}
	

	private void addSegmentPanel(Segment seg) {
		PnSegment pnSeg = new PnSegment(seg); 
		
		pnSegs.add(pnSeg);
		
//		int w = pnSegments.getWidth()-20;
		int w = 800;
		int h = 170;		
		pnSeg.setPreferredSize(new Dimension(w, h));
		pnSeg.setMaximumSize(new Dimension(w, h));
	
		pnSegments.add(pnSeg);
		pnSegments.revalidate();
		pnSegments.repaint();
	}
	
	private void clearSegments() {
		pnSegments.removeAll();
		pnSegments.revalidate();
		pnSegments.repaint();
	}
	
	
	private JPanel createPnSearchSegments() {
		JPanel pnSearch = new JPanel();
		pnSearch.setLayout(new BorderLayout());
		
		JPanel pnComponents = new JPanel();
		JLabel lbDescriptor = new JLabel("Digite um assunto: ");
		pnComponents.add(lbDescriptor);
		pnComponents.add(tfSearchDescriptors);
		pnComponents.add(btSearch);
		
		tfSearchDescriptors.setMargin(new Insets(3, 4, 4, 3));
		
		pnSearch.setBorder(new EmptyBorder(15, 0, 15, 0));
		
		pnSearch.add(pnComponents);
		return pnSearch;
	}
	
	private JPanel createPnSegments() {
		JPanel pnSegments = new JPanel();

		pnSegments.setLayout(new BoxLayout(pnSegments, BoxLayout.Y_AXIS));

		spSegments = new JScrollPane(pnSegments);

		spSegments.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(5, 5, 5, 5) ));
		spSegments.getVerticalScrollBar().setUnitIncrement(12);

		return pnSegments;
	}
	
	private JPanel createPnResultsStatus() {
		JPanel pnResultsStatus = new JPanel(new BorderLayout());
		lbSegmentsCount.setBorder(new EmptyBorder(0, 0, 0, 2));
		pnResultsStatus.add(lbSegmentsCount, BorderLayout.EAST);

		return pnResultsStatus;
	}
	
	private JPanel createPnToolBar() {
		JPanel pnToolBar = new JPanel();

		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(6, 0, 4, 0));
		
		toolBar.setFloatable(false);
		toolBar.addSeparator();
		toolBar.add(btAddToTheBase);
		toolBar.addSeparator();
		toolBar.add(btConfg);
		toolBar.addSeparator();
		toolBar.add(btExtractTopics);
		toolBar.addSeparator();
		toolBar.add(lbDescriptorsByTopic);
		toolBar.add(cbDescriptorsByTopic);
		toolBar.addSeparator();
		toolBar.add(btShowTree);
		toolBar.addSeparator();
		toolBar.add(btShowSegments);
		toolBar.addSeparator();

		
		pnToolBar.setLayout(new BorderLayout());
		pnToolBar.add(toolBar, BorderLayout.NORTH);
		
		return pnToolBar;
	}
	
	private JPanel createPnBottom() {
		JPanel pnBottom = new JPanel();

		pnBottom.setLayout(new BorderLayout());
		pnBottom.setPreferredSize(new Dimension(50, 50));
		pnBottom.setBorder(new EmptyBorder(5, 0, 5, 0));
		taStatus.setFont(new Font(Font.MONOSPACED, taStatus.getFont().getStyle(), taStatus.getFont().getSize()));
		JScrollPane spStatus = new JScrollPane(taStatus);
		pnBottom.add(spStatus);
		
		return pnBottom;
	}
	
	private JPanel createPnStatus() {
		JPanel pnStatusBar = new JPanel();
		
		pnStatusBar.setLayout(new BoxLayout(pnStatusBar, BoxLayout.LINE_AXIS));
		JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
		JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
		sep1.setMaximumSize(new Dimension(1, 30));
		sep2.setMaximumSize(new Dimension(1, 30));

		pnStatusBar.add(lbDocsCount);
		pnStatusBar.add(Box.createHorizontalStrut(5));
		pnStatusBar.add(sep1);
		pnStatusBar.add(Box.createHorizontalStrut(5));
		pnStatusBar.add(lbTopicsExtracted);
		pnStatusBar.add(Box.createHorizontalStrut(5));
		pnStatusBar.add(sep2);
		pnStatusBar.add(Box.createHorizontalStrut(5));
		pnStatusBar.add(lbAlgorithm);

		lbTopicsExtracted.setFont(lbTopicsExtracted.getFont().deriveFont(Font.PLAIN));
		lbDocsCount      .setFont(lbDocsCount      .getFont().deriveFont(Font.PLAIN));
		lbAlgorithm      .setFont(lbAlgorithm      .getFont().deriveFont(Font.PLAIN));
		
		pnStatusBar.setBorder(new EmptyBorder(2, 0, 2, 2));

		return pnStatusBar;
	}
	
    private void defaultConfiguration(){
        
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
    
    private int getUserDescriptorsByTopic() {
    	return Integer.parseInt(cbDescriptorsByTopic.getSelectedItem().toString());
    }
    
 
	
}
