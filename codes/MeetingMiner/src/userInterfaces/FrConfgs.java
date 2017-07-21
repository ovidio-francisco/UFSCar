package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import meetingMiner.MeetingMiner;
import topicExtraction.TETConfigurations.TopicExtractionConfiguration;
import topicExtraction.TETParameters.Parameters_BisectingKMeans_NonParametric;
import topicExtraction.TETParameters.Parameters_KMeans_NonParametric;
import topicExtraction.TETParameters.Parameters_KMeans_Parametric;
import topicExtraction.TETParameters.Parameters_PLSA_NonParametric;
import topicExtraction.TETParameters.Parameters_PLSA_Parametric;
import utils.ShowStatus;


public class FrConfgs extends JFrame {

	private static final long serialVersionUID = -6203645257431028798L;

	private TopicExtractionConfiguration cfg = null;
	
	private Dimension panelAlgDim     = new Dimension(300, 530);
	private Dimension fieldsDim       = new Dimension(220, 30);
	private Dimension panelsDim       = new Dimension(260, 100);

	private JRadioButton rbPlsa       = new JRadioButton("PLSA", true);
	private JRadioButton rbLda        = new JRadioButton("LDA Gibbs");
	private JRadioButton rbKmeans     = new JRadioButton("k-Means");
	private JRadioButton rbBisKmeans  = new JRadioButton("Bisecting k-Means");
	private ButtonGroup  bgAlgs       = new ButtonGroup();
	
	private JPanel pnNumTopics        = new JPanel();
	
	private JPanel pnPlsa             = new JPanel();
	private JPanel pnKmeans           = new JPanel();
	private JPanel pnLda              = new JPanel();
	private JPanel pnBisectingKmeans  = new JPanel();

	
	private JPanel pnKmeansStopCriteria                    = new JPanel();
	private JPanel pnKmeansClusterLabeling                 = new JPanel();
	private JPanel pnKmeansKValueVariation                 = new JPanel();
	private JPanel pnKmeansStopCriteriaCluesterAnalysis    = new JPanel();

	private JPanel pnBisKmeansStopCriteria                 = new JPanel();
	private JPanel pnBisKmeansClusterLabeling              = new JPanel();
	private JPanel pnBisKmeansKValueVariation              = new JPanel();
	private JPanel pnBisKmeansStopCriteriaCluesterAnalysis = new JPanel();
	
	private JPanel pnPlsaParameters                        = new JPanel();
	private JPanel pnPlsaSelectionParameters               = new JPanel();
	
	private JRadioButton rbParametric    = new JRadioButton("Paramétrico");
	private JRadioButton rbNonParametric = new JRadioButton("Não Paramétrico", true);
	private ButtonGroup  btgr            = new ButtonGroup();
	
	private JComboBox<Integer> cbNumTopics = new JComboBox<>(new Integer[] {10, 50, 100, 150, 200});
	
	private JTextField tfPlsa_maxIterations         = new JTextField("100", 3);
	private JTextField tfPlsa_minDiffLoglikelihood  = new JTextField("1.0", 3);
	private JTextField tfPlsa_maxTopics             = new JTextField("200", 3);
	private JTextField tfPlsa_probThreshold         = new JTextField("1.0", 3);
	
	
	private JTextField   tfKmeans_maxIterations    = new JTextField("100", 3);
	private JTextField   tfKmeans_minPercOfChanges = new JTextField("1.0", 3);
	private JRadioButton rbKmeans_Centroid         = new JRadioButton("Centroid", true);
	private JRadioButton rbKmeans_F1               = new JRadioButton("F1 Measure");
	private ButtonGroup  bgKmeans_CluseringLab     = new ButtonGroup();
	private JTextField   tfKmeans_MaxK             = new JTextField("200", 3);
	private JTextField   tfKmeans_MinK             = new JTextField("10" , 3);
	private JTextField   tfKmeans_Step             = new JTextField("5"  , 3);
	private JCheckBox    cbKmeans_UsingMovAvg      = new JCheckBox("Using moving average");
	private JTextField   tfKmeans_WinSize          = new JTextField("5"  , 3);
	private JTextField   tfKmeans_MinVariation     = new JTextField("1.0", 3);
	
	
	private JTextField   tfBisKmeans_maxIterations    = new JTextField("100", 3);
	private JTextField   tfBisKmeans_minPercOfChanges = new JTextField("1.0", 3);
	private JRadioButton rbBisKmeans_Centroid         = new JRadioButton("Bis Centroid", true);
	private JRadioButton rbBisKmeans_F1               = new JRadioButton("F1 Measure");
	private ButtonGroup  bgBisKmeans_CluseringLab     = new ButtonGroup();
	private JTextField   tfBisKmeans_MaxK             = new JTextField("200", 3);
	private JTextField   tfBisKmeans_MinVariation     = new JTextField("0.01", 3);
	
	private JButton      btExtract = new JButton("Extract");
	private JButton      btClose   = new JButton("Close");
	
	public FrConfgs(TopicExtractionConfiguration cfg) {
		setSize(new Dimension(1000, 750));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Meeting Miner");
		configAppearance();
		
		this.cfg = cfg;
		
		this.setLayout(new BorderLayout());
		add(createAlgorithmsPane(), BorderLayout.CENTER);
		add(createButtonsPane(), BorderLayout.SOUTH);
		
		if (cfg != null) {
			setParams(cfg);
		}
		else {
			ShowStatus.setMessage("Nulllll");
		}
		
		setListeners();
		setComponentsEnable();
	}
	
	private void setParams(TopicExtractionConfiguration cfg) {

		boolean isParametric = !cfg.isAutoNumTopics();
		
		rbParametric.setSelected(isParametric);
		rbPlsa.setSelected(cfg.isPLSA());
		rbLda.setSelected(cfg.isLDAGibbs());
		rbKmeans.setSelected(cfg.isKMeans());
		rbBisKmeans.setSelected(cfg.isBisectingKMeans());
        
        if (isParametric && cfg.getNumTopics().size() > 0) {
        	cbNumTopics.setSelectedItem(cfg.getNumTopic(0));
        }
		                                      
		if (cfg.isPLSA() && isParametric) {
			
			tfPlsa_maxIterations       .setText(cfg.getParametersPLSAParametric().getNumMaxIterations().toString());
			tfPlsa_minDiffLoglikelihood.setText(cfg.getParametersPLSAParametric().getMinDifference().toString());
		}
		                                                                                 
		if (cfg.isPLSA() && !isParametric) {
			
			tfPlsa_maxIterations       .setText(cfg.getParametersPLSANonParametric().getNumMaxIterations().toString());
			tfPlsa_minDiffLoglikelihood.setText(cfg.getParametersPLSANonParametric().getMinDifference().toString());
			tfPlsa_maxTopics           .setText(cfg.getParametersPLSANonParametric().getMaxTopics().toString());
			tfPlsa_probThreshold       .setText(cfg.getParametersPLSANonParametric().getMinThreshold().toString());
		}
		                                  
		if (cfg.isKMeans() && isParametric) {
			tfKmeans_maxIterations   .setText(cfg.getParametersKMeansParametric().getNumMaxIterations().toString());
			tfKmeans_minPercOfChanges.setText(cfg.getParametersKMeansParametric().getPercChange().toString());
			
			rbKmeans_Centroid        .setSelected( cfg.getParametersKMeansParametric().isCentroidLabel());
			rbKmeans_F1              .setSelected(!cfg.getParametersKMeansParametric().isCentroidLabel());
				
			
		}
		
		if (cfg.isKMeans() && !isParametric) {

			tfKmeans_maxIterations   .setText(cfg.getParametersKMeansNonParametric().getNumMaxIterations().toString());
			tfKmeans_minPercOfChanges.setText(cfg.getParametersKMeansNonParametric().getPercChange().toString());
			
			rbKmeans_Centroid        .setSelected( cfg.getParametersKMeansNonParametric().isCentroidLabel());
			rbKmeans_F1              .setSelected(!cfg.getParametersKMeansNonParametric().isCentroidLabel());
			
			tfKmeans_MaxK            .setText(cfg.getParametersKMeansNonParametric().getMaxValueK().toString());
			tfKmeans_MinK            .setText(cfg.getParametersKMeansNonParametric().getMinValueK().toString());
			tfKmeans_Step            .setText(cfg.getParametersKMeansNonParametric().getStepSize().toString());

			cbKmeans_UsingMovAvg     .setSelected(cfg.getParametersKMeansNonParametric().isMovingAverage());
			tfKmeans_WinSize         .setText(cfg.getParametersKMeansNonParametric().getWindowSize().toString());
			tfKmeans_MinVariation    .setText(cfg.getParametersKMeansNonParametric().getMinPercVariation().toString());
			
		}
		                                                                                 
		                                                                                 
		if (cfg.isBisectingKMeans() && isParametric) {
			tfBisKmeans_maxIterations    .setText(cfg.getParametersBisectingKMeansParametric().getNumMaxIterations().toString());
			tfBisKmeans_minPercOfChanges .setText(cfg.getParametersBisectingKMeansParametric().getPercChange().toString());
			
			rbBisKmeans_Centroid         .setSelected( cfg.getParametersBisectingKMeansParametric().isCentroidLabel());
			rbBisKmeans_F1               .setSelected(!cfg.getParametersBisectingKMeansParametric().isCentroidLabel());
		}

		if (cfg.isBisectingKMeans() && !isParametric) {
			tfBisKmeans_maxIterations    .setText(cfg.getParametersNonKMeansNonParametric().getNumMaxIterations().toString());
			tfBisKmeans_minPercOfChanges .setText(cfg.getParametersNonKMeansNonParametric().getPercChange().toString());

			rbBisKmeans_Centroid         .setSelected( cfg.getParametersNonKMeansNonParametric().isCentroidLabel());
			rbBisKmeans_F1               .setSelected(!cfg.getParametersNonKMeansNonParametric().isCentroidLabel());

			tfBisKmeans_MaxK             .setText(cfg.getParametersNonKMeansNonParametric().getMaxValueK().toString());
			tfBisKmeans_MinVariation     .setText(cfg.getParametersNonKMeansNonParametric().getMinPercVariation().toString());		
		}

	}

	public void setUserConfiguration() {

		boolean isParametric = rbParametric.isSelected();
		boolean isPlsa       = rbPlsa      .isSelected();
		boolean isLda        = rbLda       .isSelected();
		boolean iskMeans     = rbKmeans    .isSelected();
		boolean isBisKMeans  = rbBisKmeans .isSelected();
		
		cfg.setPLSA           (isPlsa);
		cfg.setLDAGibbs       (isLda);
		cfg.setKMeans         (iskMeans);
		cfg.setBisectingKMeans(isBisKMeans);

		cfg.getNumTopics().clear();
		cfg.addNumTopics(isParametric ? readUserInt(cbNumTopics) : -1);
		
		cfg.setAutoNumTopics(!isParametric);
		
		cfg.setParametersPLSAParametric   ((isPlsa &&  isParametric) ? getPlsaParametricUserParams()    : null);
		cfg.setParametersPLSANonParametric((isPlsa && !isParametric) ? getPlsaNonParametricUserParams() : null);
		
		cfg.setParametersKMeansParametric   ((iskMeans &&  isParametric) ? getKmeansParametricUserParams()    : null);
		cfg.setParametersKMeansNonParametric((iskMeans && !isParametric) ? getKmeansNonParametricUserParams() : null);
		
		cfg.setParametersBisectingKMeansParametric   ((isBisKMeans &&  isParametric) ? getBisKmeansParametricUserParams()    : null);
		cfg.setParametersBisectingKMeansNonParametric((isBisKMeans && !isParametric) ? getBisKmeansNonParametricUserParams() : null);
		
	}
	
	
	



	private Parameters_PLSA_Parametric getPlsaParametricUserParams() {
		Parameters_PLSA_Parametric result = new Parameters_PLSA_Parametric();
		
		int numIt      = readUserInt(tfPlsa_maxIterations);
		double minDiff = readUserDouble(tfPlsa_minDiffLoglikelihood);
		
		result.setNumMaxIterations(numIt);
		result.setMinDifference(minDiff);
		
		return result;
	}

	private Parameters_PLSA_NonParametric getPlsaNonParametricUserParams() {
		Parameters_PLSA_NonParametric result = new Parameters_PLSA_NonParametric();
		
		int numIt      = readUserInt(tfPlsa_maxIterations);
		double minDiff = readUserDouble(tfPlsa_minDiffLoglikelihood);

		int maxTopics       = readUserInt(tfPlsa_maxTopics);
		double minThershold = readUserDouble(tfPlsa_probThreshold);

		result.setNumMaxIterations(numIt);
		result.setMinDifference(minDiff);

		result.setMaxTopics(maxTopics);
		result.setMinThreshold(minThershold);
		
		return result;
	}

	private Parameters_KMeans_Parametric getKmeansParametricUserParams() {
		Parameters_KMeans_Parametric result = new Parameters_KMeans_Parametric();
		
		int numMaxIt   = readUserInt(tfKmeans_maxIterations);
		double pChange = readUserDouble(tfKmeans_minPercOfChanges);
		boolean label  = readUserBool(rbKmeans_Centroid);

		
		result.setNumMaxIterations(numMaxIt);
		result.setPercChange(pChange);
		result.setCentroidLabel(label);
		
		return result;
	}

	private Parameters_KMeans_NonParametric getKmeansNonParametricUserParams() {
		Parameters_KMeans_NonParametric result = new Parameters_KMeans_NonParametric();

		int numMaxIt                = readUserInt   (tfKmeans_maxIterations);
		double pChange              = readUserDouble(tfKmeans_minPercOfChanges);
		boolean label               = readUserBool  (rbKmeans_Centroid);
		int maxValueK               = readUserInt   (tfKmeans_MaxK);
		int minValueK               = readUserInt   (tfKmeans_MinK);
		int stepSize                = readUserInt   (tfKmeans_Step);
		boolean movingAverage       = readUserBool  (cbKmeans_UsingMovAvg);
		int windowSize              = readUserInt   (tfKmeans_WinSize);
		double minimumPercVariation = readUserDouble(tfKmeans_MinVariation);
		
		
		result.setNumMaxIterations(numMaxIt);
		result.setPercChange(pChange);
		
		result.setCentroidLabel(label);

		result.setMaxValueK(maxValueK);
		result.setMinValueK(minValueK);
		result.setStepSize(stepSize);
		
		result.setMovingAverage(movingAverage);
		result.setWindowSize(windowSize);
		result.setMinimumPercVariation(minimumPercVariation);
		
		return result;
	}

	private Parameters_KMeans_Parametric getBisKmeansParametricUserParams() {
		Parameters_KMeans_Parametric result = new Parameters_KMeans_Parametric();

		int numMaxIt   = readUserInt   (tfBisKmeans_maxIterations);
		double pChange = readUserDouble(tfBisKmeans_minPercOfChanges);
		boolean label  = readUserBool  (rbBisKmeans_Centroid);

		result.setNumMaxIterations(numMaxIt);
		result.setPercChange(pChange);
		result.setCentroidLabel(label);
		
		return result;
	}

	private Parameters_BisectingKMeans_NonParametric getBisKmeansNonParametricUserParams() {
		Parameters_BisectingKMeans_NonParametric result = new Parameters_BisectingKMeans_NonParametric();
		
		int numMaxIt                = readUserInt   (tfBisKmeans_maxIterations);
		double pChange              = readUserDouble(tfBisKmeans_minPercOfChanges);
		boolean label               = readUserBool  (rbBisKmeans_Centroid);
		int maxValueK               = readUserInt   (tfBisKmeans_MaxK);
		double minimumPercVariation = readUserDouble(tfBisKmeans_MinVariation);
		
		result.setNumMaxIterations(numMaxIt);
		result.setPercChange(pChange);
		result.setCentroidLabel(label);
		result.setMaxValueK(maxValueK);
		result.setMinimumPercVariation(minimumPercVariation);
		
		return result;
	}
	
	private double readUserDouble(JTextComponent comp) {
		return comp.isEnabled() && comp.isVisible() ? Double.parseDouble(comp.getText()) : -1;
	}

	private int readUserInt(JTextComponent comp) {
		return comp.isEnabled() && comp.isVisible() ? Integer.parseInt(comp.getText()) : -1;
	}
	
	private int readUserInt(JComboBox<Integer> comp) {
		
		if (comp.isEnabled() && comp.isVisible()) {
			Integer result = comp.getItemAt(comp.getSelectedIndex());
			return result.intValue();
		}
		else {
			return -1;
		}
	}


	private boolean readUserBool(JToggleButton comp) {
		if (comp.isEnabled() && comp.isVisible()) {

			return comp.isSelected();
		}
		else {
			System.out.println(String.format("Warning: %s is disable or invisible and cannot be read. False returned.", comp.getName()));
			return false;
		}
		
	}
	
	private JPanel createAlgorthmsCheckPane() {
		JPanel algorithmsCheckPane = new JPanel(new BorderLayout());
		algorithmsCheckPane.setBorder(new EmptyBorder(0, 20, 0, 0));
		
		JPanel pnWest = new JPanel(new BorderLayout());
		JPanel pnEast = new JPanel(new BorderLayout());
		
		bgAlgs.add(rbPlsa);
		bgAlgs.add(rbLda);
		bgAlgs.add(rbKmeans);
		bgAlgs.add(rbBisKmeans);
		
		pnWest.add(rbPlsa, BorderLayout.NORTH);
		pnWest.add(rbLda, BorderLayout.SOUTH);
		pnEast.add(rbKmeans, BorderLayout.NORTH);
		pnEast.add(rbBisKmeans, BorderLayout.SOUTH);

		pnEast.setBorder(new EmptyBorder(0, 10, 0, 0));
		
		algorithmsCheckPane.add(pnWest, BorderLayout.WEST);
		algorithmsCheckPane.add(pnEast, BorderLayout.EAST);
		
		return algorithmsCheckPane;
	}
	
	private JPanel createAlgorithmsPane() {
		JPanel algorithmsPane = new JPanel(new BorderLayout());
		
		JPanel pnCheckAlgs = new JPanel();
		JPanel pnAlgorthmsCheckPane = createAlgorthmsCheckPane();
		JPanel pnParametricsPane = createParametricsPane();
		
		pnAlgorthmsCheckPane.setBorder(new EmptyBorder(10, 10, 10, 10) );
		
		pnCheckAlgs.add(pnAlgorthmsCheckPane);
		pnCheckAlgs.add(pnParametricsPane);
		pnCheckAlgs.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		algorithmsPane.add(pnCheckAlgs, BorderLayout.NORTH);
		algorithmsPane.add(createAlgorithmsConfgPane(), BorderLayout.CENTER);
		
		return algorithmsPane;
	}
	
	private JPanel createButtonsPane() {
		JPanel buttonsPane = new JPanel();
		
		buttonsPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		buttonsPane.add(btExtract);
		buttonsPane.add(btClose);
		
		return buttonsPane;
	}
	
	private JPanel createAlgorithmsConfgPane() {
		JPanel algorithmsConfgPane = new JPanel();
		algorithmsConfgPane.setBorder(new EmptyBorder(20, 0, 0, 0));
		

		pnPlsa                  .setPreferredSize(panelAlgDim);
		pnKmeans                .setPreferredSize(panelAlgDim);
		pnBisectingKmeans       .setPreferredSize(panelAlgDim);
		
		pnPlsa           .setBorder(new TitledBorder("PLSA"));
		pnLda            .setBorder(new TitledBorder("LDA Gibbs"));
		pnKmeans         .setBorder(new TitledBorder("k-Means"));
		pnBisectingKmeans.setBorder(new TitledBorder("Bisecting k-Means"));
		
		createPLSAPane();
		createKMeansPane();
		createBisectingKMeansPane();

		algorithmsConfgPane.add(pnPlsa);
		algorithmsConfgPane.add(pnKmeans);
		algorithmsConfgPane.add(pnBisectingKmeans);
		
		return algorithmsConfgPane;
	}
	
	private JPanel createParametricsPane() {
		JPanel parametricsPane = new JPanel();
		
		btgr.add(rbNonParametric);
		btgr.add(rbParametric);
		
		JPanel pnRadios = new JPanel(new BorderLayout());
		pnRadios.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(10, 10, 10, 10)) );
		
		pnRadios.add(rbParametric   , BorderLayout.NORTH);
		pnRadios.add(rbNonParametric, BorderLayout.SOUTH);
		
		pnNumTopics.setLayout(new BorderLayout());
		pnNumTopics.add(createLabeledField("Num Topics", cbNumTopics, new Dimension(150, 30)));
		pnNumTopics.setBorder(new EmptyBorder(5, 5, 0, 5));
		
		parametricsPane.add(pnRadios   );
		parametricsPane.add(pnNumTopics);
		
		return parametricsPane;
	}

	private void createPLSAPane() {
		pnPlsaParameters.add(createLabeledField("Max. iterations:"          , tfPlsa_maxIterations       , fieldsDim));
		pnPlsaParameters.add(createLabeledField("Min. dif. log likelihood:" , tfPlsa_minDiffLoglikelihood, fieldsDim));
		
		pnPlsaSelectionParameters.add(createLabeledField("Number of Topics:"      , tfPlsa_maxTopics    , fieldsDim));
		pnPlsaSelectionParameters.add(createLabeledField("Probability threshold:" , tfPlsa_probThreshold, fieldsDim));

		pnPlsa.add(pnPlsaParameters);
		pnPlsa.add(pnPlsaSelectionParameters);
		
	}
	
	private void createKMeansPane() {
		
		pnKmeansStopCriteria.add(createLabeledField("Maximum Iterations:"  , tfKmeans_maxIterations   , fieldsDim));
		pnKmeansStopCriteria.add(createLabeledField("Minimum % of Changes:", tfKmeans_minPercOfChanges, fieldsDim));
		
		pnKmeansClusterLabeling.setLayout(new BorderLayout());
		pnKmeansClusterLabeling.add(rbKmeans_Centroid , BorderLayout.NORTH);
		pnKmeansClusterLabeling.add(rbKmeans_F1       , BorderLayout.SOUTH);
		bgKmeans_CluseringLab.add(rbKmeans_Centroid);
		bgKmeans_CluseringLab.add(rbKmeans_F1);
		
		pnKmeansKValueVariation.add(createLabeledField("Maximum Value of k:", tfKmeans_MaxK, fieldsDim));
		pnKmeansKValueVariation.add(createLabeledField("Minimum Value of k:", tfKmeans_MinK, fieldsDim));
		pnKmeansKValueVariation.add(createLabeledField("Step size:"         , tfKmeans_Step, fieldsDim));
		
		pnKmeansStopCriteriaCluesterAnalysis.add(cbKmeans_UsingMovAvg);
		cbKmeans_UsingMovAvg.setPreferredSize(fieldsDim);
		pnKmeansStopCriteriaCluesterAnalysis.add(createLabeledField("Windown Size:"       , tfKmeans_WinSize     , fieldsDim));
		pnKmeansStopCriteriaCluesterAnalysis.add(createLabeledField("% Minimum Variation:", tfKmeans_MinVariation, fieldsDim));
		
		
		pnKmeans.add(pnKmeansStopCriteria);
		pnKmeans.add(pnKmeansClusterLabeling);
		pnKmeans.add(pnKmeansKValueVariation);
		pnKmeans.add(pnKmeansStopCriteriaCluesterAnalysis);
	}
	
	private void createBisectingKMeansPane() {
		
		pnBisKmeansStopCriteria.add(createLabeledField("Maximum Iterations:"  , tfBisKmeans_maxIterations   , fieldsDim));
		pnBisKmeansStopCriteria.add(createLabeledField("Minimum % of Changes:", tfBisKmeans_minPercOfChanges, fieldsDim));
		
		pnBisKmeansClusterLabeling.setLayout(new BorderLayout());
		pnBisKmeansClusterLabeling.add(rbBisKmeans_Centroid , BorderLayout.NORTH);
		pnBisKmeansClusterLabeling.add(rbBisKmeans_F1       , BorderLayout.SOUTH);
		bgBisKmeans_CluseringLab.add(rbBisKmeans_Centroid);
		bgBisKmeans_CluseringLab.add(rbBisKmeans_F1);

		pnBisKmeansKValueVariation.add(createLabeledField("Maximum Value of k:", tfBisKmeans_MaxK, fieldsDim));
		
		pnBisKmeansStopCriteriaCluesterAnalysis.add(createLabeledField("% Minimum Variation:", tfBisKmeans_MinVariation, fieldsDim));

		
		pnBisectingKmeans.add(pnBisKmeansStopCriteria);
		pnBisectingKmeans.add(pnBisKmeansClusterLabeling);
		pnBisectingKmeans.add(pnBisKmeansKValueVariation);
		pnBisectingKmeans.add(pnBisKmeansStopCriteriaCluesterAnalysis);
	}

	private JPanel createLabeledField(String label, Component component, Dimension dim) {
		
		JPanel pnField = new JPanel();
		pnField.setLayout(new BorderLayout());
		pnField.setPreferredSize(dim);
		
		JPanel pnlb = new JPanel(new BorderLayout());
		JPanel pncp = new JPanel();
		pnlb.setBorder(new EmptyBorder(2, 0, 0, 0));
		pnlb.add(new JLabel(label), BorderLayout.EAST);
		pncp.add(component);
		
		pnField.add(pnlb, BorderLayout.WEST);
		pnField.add(pncp, BorderLayout.EAST);
		
		return pnField;
	}

	@SuppressWarnings("unused")
	private JPanel createCheckPanel(JCheckBox cb) {
		Dimension panelCheckBoxdim = new Dimension(panelsDim.width, 20);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(panelCheckBoxdim);
		panel.add(cb, BorderLayout.WEST);
		
		return panel;
	}
	
	private void configAppearance() {
		
		pnPlsaParameters                       .setPreferredSize(panelsDim);
		pnPlsaSelectionParameters              .setPreferredSize(panelsDim);
		
		pnKmeansStopCriteria                   .setPreferredSize(panelsDim);
		pnKmeansClusterLabeling                .setPreferredSize(new Dimension(panelsDim.width, 80));
		pnKmeansKValueVariation                .setPreferredSize(new Dimension(panelsDim.width, 140));
		pnKmeansStopCriteriaCluesterAnalysis   .setPreferredSize(new Dimension(panelsDim.width, 150));
		
		pnBisKmeansStopCriteria                .setPreferredSize(panelsDim);
		pnBisKmeansClusterLabeling             .setPreferredSize(new Dimension(panelsDim.width, 80));
		pnBisKmeansKValueVariation             .setPreferredSize(new Dimension(panelsDim.width, 140));
		pnBisKmeansStopCriteriaCluesterAnalysis.setPreferredSize(new Dimension(panelsDim.width, 150));

		pnPlsaParameters                       .setBorder(new TitledBorder("Parâmetros"));
		pnPlsaSelectionParameters              .setBorder(new TitledBorder("Parâmentros de seleção"));
		
		pnKmeansStopCriteria                   .setBorder(new TitledBorder("Stoping Criteria"));
		pnKmeansClusterLabeling                .setBorder(new CompoundBorder(new TitledBorder("Cluster Labeling"), new EmptyBorder(05, 10, 5, 10)));
		pnKmeansKValueVariation                .setBorder(new TitledBorder("k Value Variation"));
		pnKmeansStopCriteriaCluesterAnalysis   .setBorder(new TitledBorder("<html>Stoping Criterea <br/> # Cluster Analysis</html>"));
		
		pnBisKmeansStopCriteria                .setBorder(new TitledBorder("Stoping Criteria"));
		pnBisKmeansClusterLabeling             .setBorder(new CompoundBorder(new TitledBorder("Cluster Labeling"), new EmptyBorder(05, 10, 5, 10)));
		pnBisKmeansKValueVariation             .setBorder(new TitledBorder("k Value Variation"));
		pnBisKmeansStopCriteriaCluesterAnalysis.setBorder(new TitledBorder("<html>Stoping Criterea <br/> # Cluster Analysis</html>"));
		
		Insets insets = new Insets(2, 2, 2, 2);
		tfPlsa_maxIterations       .setMargin(insets);
		tfPlsa_maxTopics           .setMargin(insets);
		tfPlsa_minDiffLoglikelihood.setMargin(insets);
		tfPlsa_probThreshold       .setMargin(insets);
		
		tfKmeans_maxIterations     .setMargin(insets);
		tfKmeans_MaxK              .setMargin(insets);
		tfKmeans_MinK              .setMargin(insets);
		tfKmeans_MinVariation      .setMargin(insets);
		tfKmeans_WinSize           .setMargin(insets);
		tfKmeans_minPercOfChanges  .setMargin(insets);
		tfKmeans_Step              .setMargin(insets);

		tfBisKmeans_maxIterations     .setMargin(insets);
		tfBisKmeans_MaxK              .setMargin(insets);
		tfBisKmeans_MinVariation      .setMargin(insets);
		tfBisKmeans_minPercOfChanges  .setMargin(insets);
	}

	
	private void setEnable(JPanel p, boolean enable) {
		
		p.setEnabled(enable);
		
		for(Component c : p.getComponents()) {
			if( c instanceof JPanel ) {
				setEnable((JPanel)c, enable);
			}
			else {
				c.setEnabled(enable);
			}
				
				
		}
		
	}
	
	private void setVisible(JPanel p, boolean enable) {
		
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
	
	
	private void setComponentsEnable() {
		
		setEnable(pnNumTopics, rbParametric.isSelected());
		
		setEnable(pnPlsa                              , rbPlsa   .isSelected()                                 );
		setEnable(pnPlsaSelectionParameters           , rbPlsa   .isSelected() && rbNonParametric.isSelected() );
		
		setEnable(pnKmeans                            , rbKmeans .isSelected()                                 );
		setEnable(pnKmeansKValueVariation             , rbKmeans .isSelected() && rbNonParametric.isSelected() );
		setEnable(pnKmeansStopCriteriaCluesterAnalysis, rbKmeans .isSelected() && rbNonParametric.isSelected() );
		
		setEnable(pnBisectingKmeans                      , rbBisKmeans .isSelected()                                 );
		setEnable(pnBisectingKmeans                      , rbBisKmeans .isSelected()                                 );
		setEnable(pnBisKmeansKValueVariation             , rbBisKmeans .isSelected() && rbNonParametric.isSelected() );
		setEnable(pnBisKmeansStopCriteriaCluesterAnalysis, rbBisKmeans .isSelected() && rbNonParametric.isSelected() );
		
	}
	
	@SuppressWarnings("unused")
	private void setComponentsVisible() {
		
		setVisible(pnNumTopics, rbParametric.isSelected());
		
		setVisible(pnPlsa                              , rbPlsa   .isSelected()                                 );
		setVisible(pnPlsaSelectionParameters           , rbPlsa   .isSelected() && rbNonParametric.isSelected() );
		
		setVisible(pnKmeans                            , rbKmeans .isSelected()                                 );
		setVisible(pnKmeansKValueVariation             , rbKmeans .isSelected() && rbNonParametric.isSelected() );
		setVisible(pnKmeansStopCriteriaCluesterAnalysis, rbKmeans .isSelected() && rbNonParametric.isSelected() );
		
		setVisible(pnBisectingKmeans                      , rbBisKmeans .isSelected()                                 );
		setVisible(pnBisectingKmeans                      , rbBisKmeans .isSelected()                                 );
		setVisible(pnBisKmeansKValueVariation             , rbBisKmeans .isSelected() && rbNonParametric.isSelected() );
		setVisible(pnBisKmeansStopCriteriaCluesterAnalysis, rbBisKmeans .isSelected() && rbNonParametric.isSelected() );
		
	}

	private void goodBye() {
		setUserConfiguration();
		ShowStatus.setMessage(cfg.toString());
		this.setVisible(false);
		this.dispose();
	}
	
	private void setListeners(){
		
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setComponentsEnable();
			}
		};
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				goodBye();
			}
		});
		
		btClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goodBye();
			}
		});
		
		btExtract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
			        @Override
			        public void run(){
			    		setUserConfiguration();
			    		ShowStatus.setMessage(cfg.toString());

			        	
			    		MeetingMiner.miningTheMeetings();
			        }
				}.start();
			}
		});
		
		
		rbPlsa           .addActionListener(l);
		rbKmeans         .addActionListener(l);
		rbBisKmeans.addActionListener(l);
		rbLda            .addActionListener(l);
		rbParametric     .addActionListener(l);
		rbNonParametric  .addActionListener(l);
		
	}
	
	
}















