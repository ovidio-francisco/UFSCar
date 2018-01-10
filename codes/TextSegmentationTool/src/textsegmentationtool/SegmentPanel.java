
package textsegmentationtool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import preprocessamento.TextUtils;
import textsegmentationtool.mainFrame.MainFrame;

/**
 * @author ovidiojf
 */
public class SegmentPanel extends JPanel {
    
    private boolean finished = false;
    
    private final ArrayList<JRadioButton> tagRad = new ArrayList<>();
    private final ArrayList<JCheckBox> tagChecks = new ArrayList<>();
    private final JTextArea taSegText = new JTextArea();
    private JTextField tfAnotherTag;
    private JCheckBox ckAnotherTagContent;
    private JTextField tfAnotherTagContent;
    
    private JPanel pnUserControls = new JPanel();
    private JPanel pnLabels;
    
    private JPanel pnChecks;
    private JPanel pnRadios;
    private JPanel pnTopics;
    
    private JPanel pnCheksOkButton;
    private JPanel pnSegmentOkButton;
    private JPanel pnFinished;
    private JLabel lbEstrato = new JLabel("");
    
    private JPanel pnInstructionTags;
    private JPanel pnInstructionContentsTags;
    private JPanel pnInstructionTopics;

    private final ButtonGroup group =  new ButtonGroup();
    
    private final String instructTags = "Por favor, classifique esse segmento";
    private final String instructContentTags = "Esse trecho contém também os seguintes tipos de informação:";
    private final String instructTopics = "<html><center>Indique de 1 a 5 palavras que <br>melhor representam os tópicos do segmento";
    
    private JList listTopics;

    private final JSeparator sepRadiosCheks = new JSeparator();
    private final JSeparator sepCheksButton = new JSeparator();
    
    private final JButton btSelectTopic = new JButton(">>");
    private final JButton btRemoveTopic = new JButton("<<");
    private final JButton btAddTopic    = new JButton("Add");

    private ArrayList<String> words;
    private DefaultListModel topicModel;
    private DefaultListModel wordsModel;

    private final JTextField tfFilter = new JTextField();
    private final JList listWords = new JList();

    private final JRadioButton radOther = new JRadioButton("outro");
    
    private JButton btOkContet;
    private JButton btSegmentOk;

    private boolean tagging = false;

    private boolean irrelevante = false;
    
    private MainFrame mainFrame = null;
    
    
    private Border createBorder(String title) {
        Border       marginIn  = new EmptyBorder( 0, 10, 0, 10);
        Border       marginOut = new EmptyBorder(10, 10, 0, 10);
        TitledBorder border    = new TitledBorder(title);     
        border.setTitleJustification(TitledBorder.CENTER);


        return new CompoundBorder(marginOut, new CompoundBorder(border, marginIn));
    }
    
    private JPanel createInstructionLabel(String txt) {
        JLabel lb = new JLabel(txt);
        lb.setFont(new Font(lb.getFont().getName(), Font.PLAIN, 16));

        JPanel pn = new JPanel();
        pn.setBorder(new EmptyBorder(10, 0, 0, 0));
        pn.add(lb);
        pn.setVisible(false);

        return pn;        
    }

    private void createPnCheks() {
        pnChecks = new JPanel();
        pnChecks.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        pnChecks.setLayout(new BoxLayout(pnChecks, BoxLayout.X_AXIS));
//        pnChecks.setBorder(createBorder());
        for(String s : TextSegmentationTool.getTags()[1]) {
            JCheckBox ckb = new JCheckBox(s); 
            
            JPanel pn = new JPanel(new BorderLayout());
            pn.add(ckb, BorderLayout.WEST);
            
            tagChecks.add(ckb);
            pnChecks.add(pn);
        }

        pnInstructionContentsTags = createInstructionLabel(instructContentTags);
        pnLabels.add(pnInstructionContentsTags);
        pnLabels.add(pnChecks);
    }
    
    private void createPnRadios() {
        pnRadios = new JPanel();
        pnRadios.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        pnRadios.setLayout(new BoxLayout(pnRadios, BoxLayout.X_AXIS));
        
        for(String s : TextSegmentationTool.getTags()[0]) {
            JRadioButton rad = new JRadioButton(s); 

            JPanel pn = new JPanel(new BorderLayout());
            pn.add(rad);
            pnRadios.add(pn, BorderLayout.WEST);
            
            tagRad.add(rad);
            group.add(rad);
        }
        
        pnInstructionTags = createInstructionLabel(instructTags);
        pnInstructionTags.setVisible(true);
        pnLabels.add(pnInstructionTags);
        pnLabels.add(pnRadios);
    }

    private void createTopics() {
        pnTopics = new JPanel();
    
        JPanel pnTopicsCtrls = new JPanel();
        pnTopicsCtrls.setLayout(new BoxLayout((pnTopicsCtrls), BoxLayout.X_AXIS));
        
        
        pnTopics.setLayout(new BoxLayout(pnTopics, BoxLayout.Y_AXIS));
        pnTopics.setBorder(createBorder("Tópicos"));              
        
        pnInstructionTopics = createInstructionLabel(instructTopics);
        pnTopics.add(pnInstructionTopics);
        
        listWords.setFont(TextSegmentationTool.getTextAreaFont());
        listWords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listWords.setVisibleRowCount(-1);
        listWords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        
        JPanel pnList = new JPanel();
        pnList.setLayout(new BoxLayout(pnList, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listWords);
        scroll.setPreferredSize(new Dimension(300, 130));
        
        JPanel pnPalavras = new JPanel();
        JLabel lbPalavras = new JLabel("Palavras");
        
        
        pnPalavras.setPreferredSize(new Dimension(pnList.getWidth(), 20));
        pnPalavras.add(lbPalavras);
        pnList.add(pnPalavras);
        
        pnList.add(tfFilter);
        
        pnList.add(scroll);
        pnTopicsCtrls.add(pnList);
        
        
        JPanel pnButtons = new JPanel();
        pnButtons.setPreferredSize(new Dimension(100, pnTopicsCtrls.getHeight()));
        pnButtons.setBorder(new EmptyBorder(50, 0, 0, 0));

//        pnButtons.setLayout(new BoxLayout(pnButtons, BoxLayout.Y_AXIS));
        pnButtons.setLayout(new FlowLayout());
        
        

        Dimension sizeBt = new Dimension(70, 25);
        btSelectTopic.setPreferredSize(sizeBt);
        btRemoveTopic.setPreferredSize(sizeBt);
        btAddTopic.setPreferredSize(sizeBt);
        btAddTopic.setVisible(false);

        pnButtons.add(btSelectTopic);
        pnButtons.add(btRemoveTopic);
        pnButtons.add(btAddTopic);
        
        pnTopicsCtrls.add(pnButtons);
        
        JPanel pnWords = new JPanel();
        pnWords.setLayout(new BoxLayout(pnWords, BoxLayout.Y_AXIS));
        
        
        JPanel pnEdTopicos = new JPanel();
        JLabel lbTopicos = new JLabel("Tópicos");
        pnEdTopicos.setPreferredSize(new Dimension(pnTopicsCtrls.getWidth(), 20));
        pnEdTopicos.add(lbTopicos);
        pnWords.add(pnEdTopicos);
        
        listTopics = new JList();
        listTopics.setFont(TextSegmentationTool.getTextAreaFont());
        JScrollPane scrollTopics = new JScrollPane(listTopics);
        scrollTopics.setPreferredSize(new Dimension(100, 150));
        pnWords.add(scrollTopics);
        
        pnTopicsCtrls.add(pnWords);
        
        pnTopics.add(pnTopicsCtrls);
        pnUserControls.add(pnTopics);
        
        
        topicModel = new DefaultListModel();
        wordsModel = new DefaultListModel();
       
        listTopics.setModel(topicModel);
        listWords.setModel(wordsModel);
        
        
        words = TextUtils.getWords(taSegText.getText().toLowerCase()); //taSegText.getText().split("[\\ \\.,!?\\n]");
        for(String w : words) {
            wordsModel.addElement(w);
        }
        
        
        
        
    }
    
    private void createOthers() {
        if (TextSegmentationTool.isEnableOther()) {
            tfAnotherTag = new JTextField(10);
            tfAnotherTagContent = new JTextField(10);
            ckAnotherTagContent = new JCheckBox("outro");
            
            tfAnotherTagContent.setEnabled(false);
            
            JPanel pn1 = new JPanel();
            JPanel pn2 = new JPanel();
            pn1.setLayout(new BoxLayout(pn1, BoxLayout.X_AXIS));
            pn2.setLayout(new BoxLayout(pn2, BoxLayout.X_AXIS));
            
            group.add(radOther);
            
            pn1.add(radOther);
            pn1.add(tfAnotherTag);
            
            pn2.add(ckAnotherTagContent);
//            pn2.add(new JLabel("outro"));
            pn2.add(tfAnotherTagContent);
            
            pnRadios.add(pn1);
            pnChecks.add(pn2);
        }
    }
    
    private void createCheksOkButton() {
        pnCheksOkButton = new JPanel();
        pnCheksOkButton.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btOkContet = new JButton("Ok");
        
        
        pnCheksOkButton.add(btOkContet);
        pnLabels.add(pnCheksOkButton);
    }
    
    private void createSegmentOk() {
        pnSegmentOkButton = new JPanel();
        pnSegmentOkButton.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btSegmentOk = new JButton("Finalizar esse techo");
//        btSegmentOk.setFont(new Font(btSegmentOk.getFont().getName(), btSegmentOk.getFont().getStyle(), 24));
        btSegmentOk.setEnabled(false);
        
        
        pnSegmentOkButton.add(btSegmentOk);
        pnUserControls.add(pnSegmentOkButton);
        
    }
    
    private String getEstrato() {
        String estrato = "";
        Segment seg = getSegment();
        
        
        estrato += "<html><br><br>";
        
        estrato += "Classe:<b> <br>"; 
        for(String classe : seg.getTags())
            estrato += classe + ". ";
        estrato += "</b><br><br>";

        estrato += "<br>Também os seguintes tipos de informação:<b> <br>"; 
        for(String content : seg.getContents())
            estrato += " " + content + ". ";
        estrato += "</b><br><br>";
                
        estrato += "<br>Palavras que melhor descrevem o trecho:<b> <br>"; 
        for(String topic : seg.getTopics())
            estrato += " " + topic + ". ";
                
        
        estrato += "</b><br><br></html>";
        return estrato;
    }
    
    private void createPnFinished() {
        pnFinished = new JPanel();
        pnFinished.setBorder(new EmptyBorder(40, 0, 20, 0));
        pnFinished.setLayout(new BoxLayout(pnFinished, BoxLayout.Y_AXIS));
        
        lbEstrato.setFont(TextSegmentationTool.getTextAreaFont());
        
        JLabel lb = new JLabel("Trecho Finalizado");
//        lb.setFont(new Font(Font.SERIF, Font.ITALIC, 24));
        
        pnFinished.add(lb);
        pnFinished.add(lbEstrato);
        
        pnUserControls.add(pnFinished);
    }
    
    private void createPnLabels() {
        pnLabels = new JPanel();
        pnLabels.setLayout(new BoxLayout(pnLabels, BoxLayout.Y_AXIS));
        
        sepRadiosCheks.setVisible(false);
        sepCheksButton.setVisible(false);
 
        pnLabels.setBorder(createBorder("Anotações"));
        
        createPnRadios();
        pnLabels.add(sepRadiosCheks);
        createPnCheks();
        pnLabels.add(sepCheksButton);
//        createCheksOkButton();
        createOthers();
        
        pnUserControls.add(pnLabels);
    }
    
    private void createPnUserControls() {
        pnUserControls = new JPanel();
        pnUserControls.setLayout(new BoxLayout(pnUserControls, BoxLayout.Y_AXIS));

        createPnLabels();
        createCheksOkButton();
        createTopics();
        createPnFinished();
        createSegmentOk();
        
        pnChecks.setVisible(false);
        pnTopics.setVisible(false);
        pnCheksOkButton.setVisible(false);
        pnSegmentOkButton.setVisible(false);
        pnFinished.setVisible(false);
        
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        this.add(p, BorderLayout.CENTER);

        p.add(pnUserControls, BorderLayout.NORTH);
    }

    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public SegmentPanel(String txt) {
        this.setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(taSegText);

        taSegText.setText(txt);
        taSegText.setLineWrap(true);
        taSegText.setWrapStyleWord(true);
        taSegText.setEditable(false);
        taSegText.setFont(TextSegmentationTool.getTextAreaFont());
        scroll.setPreferredSize(new Dimension(getWidth(), 150));
        
               
        this.add(scroll, BorderLayout.NORTH);
        createPnUserControls();

        setListeners();
        setComponentsStatus();
        
    }
    
    public void setComponentsStatus() {
//        if(btOkContet != null)
//            btOkContet.setEnabled(verifyContentIsTagged());
    }

    public boolean verifyTopicsSelected() {
        return topicModel.getSize() >=1;
    }
    
    public void setListeners() {
        btSelectTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (topicModel.getSize() >= 5) {
                    return;
                }
                
                if(listWords.getSelectedIndex() >= 0) {
                    words.remove(listWords.getSelectedIndex());
                    Object remove = wordsModel.remove(listWords.getSelectedIndex());
                    if(!topicModel.contains(remove)) {
                        topicModel.addElement(remove);
                    }
                    tfFilter.setText("");
                    tfFilter.getKeyListeners()[0].keyReleased(null);
                }
                
                btSegmentOk.setEnabled(verifyTopicsSelected());
            }
        });

        btRemoveTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listTopics.getSelectedIndex() >= 0) {
                    Object remove = topicModel.remove(listTopics.getSelectedIndex());
                    if(wordsModel.contains(remove)) {
                        wordsModel.addElement(remove);
                        words.add((String)remove);
                    }
                }
                
                btSegmentOk.setEnabled(verifyTopicsSelected());

            }
        });
        
        btAddTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog("Digite uma palavra");
                if(s != null)
                    topicModel.addElement(s);
            }
        });
        
        
        tfFilter.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
                utils.Utils.filterListModel( wordsModel, words, tfFilter.getText());
            }
        });
        
        
//        =============================================

        for(final JRadioButton rad : tagRad) {
            
            rad.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {     
                    boolean visible = !rad.getText().toLowerCase().equals("irrelevante".toLowerCase());
                    irrelevante = rad.getText().toLowerCase().equals("irrelevante".toLowerCase());
                    pnChecks.setVisible(visible);
                    pnCheksOkButton.setVisible(visible);
                    pnInstructionContentsTags.setVisible(visible);
//                    pnInstructionTopics.setVisible(visible);
                    sepCheksButton.setVisible(visible);
                    sepRadiosCheks.setVisible(visible);
                    
//                    pnTopics.setVisible(visible);

                    pnSegmentOkButton.setVisible(!visible);
                    btSegmentOk.setEnabled(!visible);
                }
            });
        }

        radOther.addActionListener(tagRad.get(0).getActionListeners()[0]);


        for(JCheckBox ckb : tagChecks) {
            
            ckb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    btOkContet.setEnabled(verifyContentIsTagged());
                }
            });
        }
        
        tfAnotherTagContent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
//                 btOkContet.setEnabled(verifyContentIsTagged());
            }

        });
                



//=================================

        btOkContet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                tagging = !tagging;
                
                pnTopics.setVisible(tagging);
                pnInstructionTopics.setVisible(tagging);
                
                pnChecks.setVisible(!tagging);
                pnInstructionContentsTags.setVisible(!tagging);
                pnRadios.setVisible(!tagging);
                pnInstructionTags.setVisible(!tagging);
                sepCheksButton.setVisible(!tagging);
                sepRadiosCheks.setVisible(!tagging);
                
//              pnSegmentOkButton.setVisible(!visible);
                pnSegmentOkButton.setVisible(tagging);
                
                btOkContet.setText(!tagging ? "Ok" : "Exibir Anotações");
                    
                
            }
        });

        btSegmentOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finished = !finished;
                 
                
                if(true) {
//                pnUserControls.setVisible(false);
                    pnLabels.setVisible(!finished);
                    pnTopics.setVisible(!finished);
                    pnFinished.setVisible(finished);
                }
                else {
                    pnLabels.setEnabled(!finished);
                    pnTopics.setEnabled(!finished);
                    pnFinished.setEnabled(finished);
                }
                    
                
                if(finished) {
                    TextSegmentationTool.getMainFrame().goToNextDoc();
                    mainFrame.setStatus(MainFrame.Status.SELECT);
                    mainFrame.enableDocPanel();
                    lbEstrato.setText(getEstrato()); 
               }

                btSegmentOk.setText(!finished ? "Finalizar esse segmento" : "Editar esse segmento");
                
               
                
            }
            
        });
        
        ckAnotherTagContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfAnotherTagContent.setEnabled(ckAnotherTagContent.isSelected());
            }
        });

        
        listWords.addMouseListener(new MouseAdapter() {
                    @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() >1 ) {
                    btSelectTopic.getActionListeners()[0].actionPerformed(null);
                }
            }
        });
        
    }
    
    public Segment getSegment() {
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();
        ArrayList<String> topics = new ArrayList<>();
        
        for(JRadioButton rad : tagRad) {
            if(rad.isSelected())
                tags.add(rad.getText());
        }
        
        if(!irrelevante) {
            
        
            if(tfAnotherTag.getText().trim().length()>0)
                tags.add(tfAnotherTag.getText().trim());



            for(JCheckBox ckb : tagChecks) {
                if(ckb.isSelected())
                    contents.add(ckb.getText());
            }
            if(tfAnotherTagContent.getText().trim().length() > 0)
                contents.add(tfAnotherTagContent.getText());


            if (true) {
                for(int i=0; i< topicModel.getSize();i++) {
                    topics.add((String)topicModel.getElementAt(i));
                }
            }
        
        }
        return new Segment(taSegText.getText(), tags, contents, topics);
    }

    public boolean isFinished() {
        return finished;
    }
    
    private boolean verifyContentIsTagged() {
        boolean result = false;
        for(JCheckBox ckb : tagChecks) {
            result = result || ckb.isSelected() || (!tfAnotherTagContent.getText().isEmpty());
        }
        
        return result;
        
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
}
