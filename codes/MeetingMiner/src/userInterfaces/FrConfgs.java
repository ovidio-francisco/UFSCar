package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;


public class FrConfgs extends JFrame {

	private static final long serialVersionUID = -6203645257431028798L;

	
	private JRadioButton parametric = new JRadioButton("Paramétrico");
	private JRadioButton nonParametric = new JRadioButton("Não Paramétrico");
	private ButtonGroup  btgr = new ButtonGroup();
	
	
	public FrConfgs() {
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Meeting Miner");
		
		add(createAlgorithmsPane());
		
	}
	
	private JPanel createAlgorithmsPane() {
		JPanel algorithmsPane = new JPanel(new BorderLayout());
		
		algorithmsPane.add(createParametricsPane(), BorderLayout.NORTH);
		algorithmsPane.add(createAlgorithmsMethodsPane(), BorderLayout.CENTER);
		
		return algorithmsPane;
	}
	
	private JPanel createAlgorithmsMethodsPane() {
		JPanel methodsPane = new JPanel();
		
		methodsPane.add(createPLSAPane());
		methodsPane.add(createLDAPane());
		methodsPane.add(createKMeansPane());
		methodsPane.add(createBisectingKMeansPane());
		
		return methodsPane;
	}
	
	private JPanel createParametricsPane() {
		JPanel parametricsPane = new JPanel(new GridLayout(1, 2));
		
		parametricsPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		btgr.add(nonParametric);
		btgr.add(parametric);
		
		parametricsPane.add(nonParametric);
		parametricsPane.add(parametric);
		
		
		return parametricsPane;
	}

	private JPanel createPLSAPane() {
		JPanel plsaPane = new JPanel();
		plsaPane.setBorder(new TitledBorder("PLSA"));
		plsaPane.setPreferredSize(new Dimension(200, 200));
		
		return plsaPane;
	}
	
	private JPanel createKMeansPane() {
		JPanel kmeansPane = new JPanel();
		kmeansPane.setBorder(new TitledBorder("k-Means"));
		kmeansPane.setPreferredSize(new Dimension(200, 200));
		
		return kmeansPane;
	}
	
	private JPanel createLDAPane() {
		JPanel ldaPane = new JPanel();
		ldaPane.setBorder(new TitledBorder("LDA"));
		ldaPane.setPreferredSize(new Dimension(200, 200));
		
		return ldaPane;
	}
	
	private JPanel createBisectingKMeansPane() {
		JPanel bisectingKmeansPane = new JPanel();
		bisectingKmeansPane.setBorder(new TitledBorder("Bisecting k-Means"));
		bisectingKmeansPane.setPreferredSize(new Dimension(200, 200));
		
		return bisectingKmeansPane;
	}

}
