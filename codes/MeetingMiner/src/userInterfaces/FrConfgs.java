package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import topicExtraction.TETConfigurations.TopicExtractionConfiguration;


public class FrConfgs extends JFrame {

	private static final long serialVersionUID = -6203645257431028798L;

	private Dimension panelAlgDim      = new Dimension(300, 530);
	private Dimension fieldsDim        = new Dimension(220, 30);
	private Dimension panelsDim        = new Dimension(260, 100);


	private JCheckBox cbPlsa            = new JCheckBox("PLSA", true);
	private JCheckBox cbLda             = new JCheckBox("LDA Gibbs");
	private JCheckBox cbKmeans          = new JCheckBox("k-Means");
	private JCheckBox cbBisectingKmeans = new JCheckBox("Bisecting k-Means");
	
	
	private JPanel pnNumTopics = new JPanel();
	
	private JPanel pnPlsa            = new JPanel();
	private JPanel pnKmeans          = new JPanel();
	private JPanel pnLda             = new JPanel();
	private JPanel pnBisectingKmeans = new JPanel();

	
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
	private JRadioButton rbBisKmeans_Centroid         = new JRadioButton("Centroid", true);
	private JRadioButton rbBisKmeans_F1               = new JRadioButton("F1 Measure");
	private ButtonGroup  bgBisKmeans_CluseringLab     = new ButtonGroup();
	private JTextField   tfBisKmeans_MaxK             = new JTextField("200", 3);
	private JTextField   tfBisKmeans_MinVariation     = new JTextField("0.01", 3);
	
	public FrConfgs() {
		setSize(new Dimension(1000, 800));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Meeting Miner");
		configAppearance();
		
		add(createAlgorithmsPane());
		
		setListeners();
		setComponentsEnable();
	}
	
	public TopicExtractionConfiguration getUserConfiguration() {
		TopicExtractionConfiguration config = new TopicExtractionConfiguration();
		
		return config;
	}
	
	private JPanel createAlgorthmsCheckPane() {
		JPanel algorithmsCheckPane = new JPanel(new BorderLayout());
		algorithmsCheckPane.setBorder(new EmptyBorder(0, 20, 0, 0));
		
		JPanel pnWest = new JPanel(new BorderLayout());
		JPanel pnEast = new JPanel(new BorderLayout());
		
		pnWest.add(cbPlsa, BorderLayout.NORTH);
		pnWest.add(cbLda, BorderLayout.SOUTH);
		pnEast.add(cbKmeans, BorderLayout.NORTH);
		pnEast.add(cbBisectingKmeans, BorderLayout.SOUTH);

		pnEast.setBorder(new EmptyBorder(0, 10, 0, 0));
		
		algorithmsCheckPane.add(pnWest, BorderLayout.WEST);
		algorithmsCheckPane.add(pnEast, BorderLayout.EAST);
		
		return algorithmsCheckPane;
	}
	
	private JPanel createAlgorithmsPane() {
		JPanel algorithmsPane = new JPanel(new BorderLayout());
		
		JPanel pnCheckAlgs = new JPanel();
		pnCheckAlgs.add(createParametricsPane());
		pnCheckAlgs.add(createAlgorthmsCheckPane());
		pnCheckAlgs.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		algorithmsPane.add(pnCheckAlgs, BorderLayout.NORTH);
		algorithmsPane.add(createAlgorithmsConfgPane(), BorderLayout.CENTER);
		
		return algorithmsPane;
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
		JPanel parametricsPane = new JPanel(new BorderLayout());
		parametricsPane.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(10, 10, 10, 10)) );
		
		btgr.add(rbNonParametric);
		btgr.add(rbParametric);
		
		JPanel pnRadios = new JPanel(new BorderLayout());
		
		
		pnRadios.add(rbParametric   , BorderLayout.NORTH);
		pnRadios.add(rbNonParametric, BorderLayout.SOUTH);
		
		
		pnNumTopics.setLayout(new BorderLayout());
		pnNumTopics.add(createLabeledField("Num Topics", cbNumTopics, new Dimension(150, 30)));
		pnNumTopics.setBorder(new EmptyBorder(5, 5, 0, 5));
		
		parametricsPane.add(pnRadios   , BorderLayout.NORTH);
		parametricsPane.add(pnNumTopics, BorderLayout.SOUTH);
		
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
	
	private void setComponentsEnable() {
		
		setEnable(pnNumTopics, rbParametric.isSelected());
		
		setEnable(pnPlsa                              , cbPlsa   .isSelected()                                 );
		setEnable(pnPlsaSelectionParameters           , cbPlsa   .isSelected() && rbNonParametric.isSelected() );
		
		setEnable(pnKmeans                            , cbKmeans .isSelected()                                 );
		setEnable(pnKmeansKValueVariation             , cbKmeans .isSelected() && rbNonParametric.isSelected() );
		setEnable(pnKmeansStopCriteriaCluesterAnalysis, cbKmeans .isSelected() && rbNonParametric.isSelected() );
		
		setEnable(pnBisectingKmeans                      , cbBisectingKmeans .isSelected()                                 );
		setEnable(pnBisectingKmeans                      , cbBisectingKmeans .isSelected()                                 );
		setEnable(pnBisKmeansKValueVariation             , cbBisectingKmeans .isSelected() && rbNonParametric.isSelected() );
		setEnable(pnBisKmeansStopCriteriaCluesterAnalysis, cbBisectingKmeans .isSelected() && rbNonParametric.isSelected() );
		
	}
	
	private void setListeners(){
		
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setComponentsEnable();
			}
		};
		
		cbPlsa           .addActionListener(l);
		cbKmeans         .addActionListener(l);
		cbBisectingKmeans.addActionListener(l);
		cbLda            .addActionListener(l);
		rbParametric     .addActionListener(l);
		rbNonParametric  .addActionListener(l);
		
	}
	
	
}















