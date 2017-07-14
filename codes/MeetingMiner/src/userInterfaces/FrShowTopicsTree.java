package userInterfaces;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import meetingMiner.MeetingMiner;
import utils.Files;
import utils.ShowStatus;

public class FrShowTopicsTree extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTree view = new JTree();
	JScrollPane spTopics = new JScrollPane(view);

	public FrShowTopicsTree() {
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Meeting Miner");
		
		JToolBar toolBar = new JToolBar();
		JButton btSave = new JButton("Salvar");
		toolBar.setFloatable(false);
		toolBar.addSeparator();
		toolBar.add(btSave);
		
		setLayout(new BorderLayout());
		
		add(toolBar , BorderLayout.NORTH);
		add(spTopics, BorderLayout.CENTER);
		
		
		btSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveTopics();
			}
		});
		
	}

	@Override
	public void setVisible(boolean value) {
	    super.setVisible(value);
	    spTopics.requestFocusInWindow();
	}
	
	public void setTreeRoot(DefaultMutableTreeNode root) {
		DefaultTreeModel model = new DefaultTreeModel(root);
		view.setModel(model);
	}
	
	
	
	public void saveTopics() {
		String filename = MeetingMiner.getOutFolder().getPath()+"/topics.txt";

		if (MeetingMiner.getTopicExtractionconfiguration().isPLSA())            { filename = filename + " - PLSA"; }
		if (MeetingMiner.getTopicExtractionconfiguration().isKMeans()         ) { filename = filename + " - kMeans"; }
		if (MeetingMiner.getTopicExtractionconfiguration().isBisectingKMeans()) { filename = filename + " - BisectingkMeans"; }
		if (MeetingMiner.getTopicExtractionconfiguration().isLDAGibbs())        { filename = filename + " - LDA Gibbs"; }
		if (MeetingMiner.getTopicExtractionconfiguration().isAutoNumTopics())   { filename = filename + " - Non Parametric"; }

		File file = new File(filename);
		
		Files.saveTxtFile(getTreeText(view.getModel(), view.getModel().getRoot(), ""), file);

		
		int resp = JOptionPane.showConfirmDialog(null, "O arquivo foi salvo em: " + file+ ". Deseja visualizá-lo?", "Visualizar?", JOptionPane.YES_NO_OPTION);
		
		if(resp == JOptionPane.YES_OPTION) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		
		ShowStatus.setMessage("Saving topics to "+filename);
	}
	
	private static String getTreeText(TreeModel model, Object object, String indent) {
	    String myRow = indent + object + "\n";
	    for (int i = 0; i < model.getChildCount(object); i++) {
	        myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
	    }
	    return myRow;
	}

	
}




