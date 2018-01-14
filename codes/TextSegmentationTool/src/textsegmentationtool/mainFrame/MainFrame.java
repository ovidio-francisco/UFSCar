
package textsegmentationtool.mainFrame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.undo.UndoManager;
import org.apache.commons.csv.CSVPrinter;
import textsegmentationtool.Segment;
import textsegmentationtool.SegmentPanel;
import textsegmentationtool.TextSegmentationTool;
import utils.Utils;

/**
 * @author ovidiojf
 */
public class MainFrame extends javax.swing.JFrame implements Serializable {

    private ArrayList<SegmentPanel> segPanels = new ArrayList<>();
    private static File doc;
    private File docsFolder = new File("./docs");

    private final UndoManager undoManeger = new UndoManager();
    
    public enum Status  {LOAD, SELECT, CUT, LABEL, SALVE};
    
    private int fontSize = 12;
    
    
    public void restore() {
        State state = TextSegmentationTool.loadState();
        segPanels = state.getSegs();

        for(SegmentPanel seg : segPanels) {
            String tabText = "seg " + (tabbedPane1Segmentos.getTabCount() +1);
            tabbedPane1Segmentos.add(tabText, seg);        
            
            seg.setListeners();
            seg.setComponentsStatus();
        }
        
        tabbedPane1Segmentos.setSelectedIndex(tabbedPane1Segmentos.getTabCount()-1);   
    
        taTexto.setText(state.getEditingText());
        
        doc = state.doc;
        
        tabbedPanelDocumento.setTitleAt(0, doc.getName());

        setFontSizeForAll(this.getContentPane());
        
    }
    
    public void goToNextDoc() {
        boolean b = true;
        
        for(SegmentPanel s : segPanels) {
            b = b && s.isFinished();
        }
        
        if (b && (!(taTexto.getText().trim().length() > 0))){
            gerarCSV();
            
            JOptionPane.showMessageDialog(this, "Um novo documento será carregado");
            
            carregarDocumento();
        }
    }
    
    public boolean allDone() {
        assertFolders();
        return docsFolder.listFiles(utils.Utils.getM4M_FileFilter()).length == 0; 
    }
    
    public void thanks() {
        String msg = "Não há mais arquivos a segmentar.\n Os arquivos resultantes estão salvos na pasta "+
                "\""+TextSegmentationTool.getCSV_FOLDER().getName() +"\""+
                ". Por favor envie essa pasta para ovidiojf@gmail.com\n Obrigado!";
        JOptionPane.showMessageDialog(this, msg);
    }
    
    public void assertFolders() {
        if(!docsFolder.exists()) {
            JOptionPane.showMessageDialog(this, "O diretório \""+docsFolder+"\" não foi encontrado. Por favor indique a pasta que contém os arquivos a serem rotulados");
            JFileChooser fc = new JFileChooser(new File("."));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = fc.showOpenDialog(this);
            
            if(res == JOptionPane.YES_OPTION)
                docsFolder = fc.getSelectedFile();
        }       
    }
    
    public void carregarDocumento() {
        clearSementPanels();
        
        assertFolders();
                
        File[] files = docsFolder.listFiles(utils.Utils.getM4M_FileFilter()); 
        
        Utils.sortFiles(files);

        if (files!= null && files.length > 0) {
            doc = files[0];
        }
        else {
            thanks();
            return;
        }
            
        if (doc == null) {
            JOptionPane.showMessageDialog(this, "Não há mais arquivos");
            return;            
        }
        
        System.out.println(doc.getName());
        
        taTexto.setText(Utils.loadDocumentFile(doc));
        taTexto.setCaretPosition(0);
        
        tabbedPanelDocumento.setTitleAt(0, doc.getName());
        
        setStatus(Status.SELECT);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MainFrame() {
        initComponents();
        setSize(1000, 600);
        jPanel1.requestFocus();
        
        taTexto.getDocument().addUndoableEditListener(undoManeger);

        
        btUndo.setPreferredSize(new Dimension(btUndo.getWidth(), btSave.getHeight()));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
        
                splitPanel.setDividerLocation(getWidth()/2);
                                 
                
            }
            
        });
        
        btCut.setEnabled(false);
        btUndo.setEnabled(false);
//        btSavlarSegmentos.setEnabled(false);
        
        btSave.setVisible(false);
        
        taTexto.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {

                boolean hasSelectedText = taTexto.getSelectedText() != null && taTexto.getSelectedText().trim().length() > 0;
                
                btCut.setEnabled(hasSelectedText);
                
                setStatus(hasSelectedText ? Status.CUT : Status.SELECT);
            }
        });
        
//        pnStatus.setSize(getWidth(), 17);
        setStatus(Status.LOAD);

//        taTexto.setFont(TextSegmentationTool.getTextAreaFont());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                btClose.getActionListeners()[0].actionPerformed(null);
            }
        });
        
        setFontSizeForAll(this.getContentPane());
        
        
    }

    private Font increaseComponenteFontSize(Component c) {
        String name = c.getFont().getName();
        int style =   c.getFont().getStyle();
//        int size =    c.getFont().getSize() + 2;
        int size =    fontSize;
        
        return new Font(name, style, size);
    }

    private void increaseFontSizeForAll(Container container) {
        Component[] components = container.getComponents();
        
        for(Component c : components) {
            c.setFont(increaseComponenteFontSize(c));
            if (c instanceof Container) {
                increaseFontSizeForAll((Container)c);
            }
        }
        
    }
    
    private void setFontSizeForAll(Container container) {
        Component[] components = container.getComponents();
        
        for(Component c : components) {
            c.setFont(increaseComponenteFontSize(c));
            if (c instanceof Container) {
                setFontSizeForAll((Container)c);
            }
        }        
        
        setUIFont(new FontUIResource(getFont().getName(), getFont().getStyle(), fontSize));

    }
     
    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }    
    
    public void setStatus(Status status) {
        switch (status) {
            case LOAD: {
                lbStatus.setText("Aguarde carregar o documento");//Clique em 'Iniciar' para carregar um documento");
                break;
            }
            case SELECT: {
                lbStatus.setText("Use a função arrastar do mouse e selecione um trecho do documento");                
                break;
            }
            case CUT: {
                lbStatus.setText("Clique em 'Rotular' para classificar o trecho selecionado");                
                break;
            }
            case LABEL: {
                lbStatus.setText("Use os controles abaixo do trecho para selecionar as opções.");                
                break;
            }
            case SALVE: {
                lbStatus.setText("Quando terminar, clique em salvar para gravras as rotulações");
                break;
            }
            default: {
                
            }
        }
    }

    private String cutSegment() {
        String seg = taTexto.getSelectedText();
        
        if (seg != null) {
            taTexto.setEditable(true);
            taTexto.cut();
            taTexto.setEditable(false);
            return seg;
        }
        
        return null;
    }
    
    private JPanel newSegmentPanel(String txt) {
        SegmentPanel newseg = new SegmentPanel(txt);
        segPanels.add(newseg);
        
        newseg.setMainFrame(this);
        
        return newseg;
    }
    
    private void addTab() {
        
        String newText = cutSegment();
        
        if (newText != null) {

            String tabText = "seg " + (tabbedPane1Segmentos.getTabCount() +1);
            tabbedPane1Segmentos.add(tabText, newSegmentPanel(newText));        
            
            tabbedPane1Segmentos.setSelectedIndex(tabbedPane1Segmentos.getTabCount()-1);
            
        }
        
        setFontSizeForAll(this.getContentPane());
        
        
    }
    
    private void clearSementPanels() {
        segPanels.clear();
        tabbedPane1Segmentos.removeAll();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        splitPanel = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        tabbedPanelDocumento = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        taTexto = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        tabbedPane1Segmentos = new javax.swing.JTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        btSave = new javax.swing.JButton();
        btIncreaseFont = new javax.swing.JButton();
        btDecFontSize = new javax.swing.JButton();
        btUndo = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btCut = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lbStatus = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        btClose2 = new javax.swing.JButton();
        btClose = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));

        jScrollPane2.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                onShow(evt);
            }
        });

        splitPanel.setBorder(null);
        splitPanel.setDividerLocation(500);
        splitPanel.setDividerSize(5);
        splitPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Documento"));

        tabbedPanelDocumento.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        taTexto.setEditable(false);
        taTexto.setColumns(20);
        taTexto.setLineWrap(true);
        taTexto.setRows(5);
        taTexto.setWrapStyleWord(true);
        taTexto.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jScrollPane1.setViewportView(taTexto);

        tabbedPanelDocumento.addTab("<Vazio>", jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPanelDocumento)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPanelDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPanel.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Segmentos"));

        tabbedPane1Segmentos.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane1Segmentos)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane1Segmentos, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPanel.setRightComponent(jPanel2);

        jToolBar1.setRollover(true);

        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/diskette.png"))); // NOI18N
        btSave.setText("Salvar");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btSave);

        btIncreaseFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/incFont2.png.png"))); // NOI18N
        btIncreaseFont.setFocusable(false);
        btIncreaseFont.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btIncreaseFont.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btIncreaseFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btIncreaseFontActionPerformed(evt);
            }
        });
        jToolBar1.add(btIncreaseFont);

        btDecFontSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/decFont2.png"))); // NOI18N
        btDecFontSize.setFocusable(false);
        btDecFontSize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDecFontSize.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDecFontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDecFontSizeActionPerformed(evt);
            }
        });
        jToolBar1.add(btDecFontSize);

        btUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/undo-arrow24.png"))); // NOI18N
        btUndo.setText("Desfazer");
        btUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUndoActionPerformed(evt);
            }
        });
        jToolBar1.add(btUndo);
        jToolBar1.add(jSeparator2);

        btCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/price-ticket.png"))); // NOI18N
        btCut.setText("Rotular");
        btCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCutActionPerformed(evt);
            }
        });
        jToolBar1.add(btCut);
        jToolBar1.add(filler3);

        lbStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(51, 51, 255));
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbStatus.setText("jLabel1");
        lbStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToolBar1.add(lbStatus);
        jToolBar1.add(filler1);
        jToolBar1.add(filler2);

        btClose2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/question-mark24.png"))); // NOI18N
        btClose2.setFocusable(false);
        btClose2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btClose2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btClose2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClose2ActionPerformed(evt);
            }
        });
        jToolBar1.add(btClose2);

        btClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-door-symbol-24.png"))); // NOI18N
        btClose.setText("Fechar");
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });
        jToolBar1.add(btClose);
        jToolBar1.add(filler5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPanel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCutActionPerformed

        addTab();
        
        btUndo.setEnabled(true);
        
        if( !(taTexto.getText().trim().length() > 0) ){
            
//            btSavlarSegmentos.setEnabled(true);
            setStatus(Status.SALVE);
            return;
        }
        
        desableDocPanel();
        setStatus(Status.LABEL);
        
    }//GEN-LAST:event_btCutActionPerformed

    public void desableDocPanel() {
        jPanel1.setEnabled(false);
        tabbedPanelDocumento.setEnabled(false);
        taTexto.setEnabled(false);
    }

    public void enableDocPanel() {
        jPanel1.setEnabled(true);
        tabbedPanelDocumento.setEnabled(true);
        taTexto.setEnabled(true);
    }
    
    private void gerarCSV() {
        

        if(taTexto.getText().trim().length() > 0 ) {
            JOptionPane.showMessageDialog(this, "Ainda há texto não segmentado no documento \n \"" + doc.getName() + "\"");
            return;
        }
        
        ArrayList<Segment> segs = new ArrayList<>();
        
        for(SegmentPanel sp : segPanels) {
            segs.add(sp.getSegment());
        }
        
        
        CSVPrinter csv = Utils.getCSVPrinter(Utils.getCSVFile(doc));
        
        for(Segment s: segs) {
            try {
                csv.print(s.getText());
                
                String tags = "";
                for(String tag : s.getTags()) {
                    tags = tags + tag;
                }
                csv.print(tags);
                
                String contents = "";
                for(String cont : s.getContents()) {
                    contents = contents + cont + ";";
                }
                csv.print(contents);
                
                
                String tops = "";
                for(String top : s.getTopics()) {
                    tops = tops + top + ";";
                }
                csv.print(tops);
                
                csv.println();
                
            } catch (IOException ex) {
                System.err.println("Falha ao escrever arquivo csv");
            }
 
        }
        
        Utils.closeCSV(csv);
        
        JOptionPane.showMessageDialog(this, "Arquivo "+Utils.getCSVFile(doc)+" criado");
        
        File log = new File("./log.txt");
        StringBuilder sbLog = new StringBuilder();

        try {
            if (log.exists()) {
                String strLog = Utils.loadTxtFile(log);
                sbLog.append(strLog);            
            }
            sbLog.append(String.format("Arquivo CSV gerado: \"%s\" em [%s]", Utils.getCSVFile(doc), new Date()));
            Utils.saveTxtFile(sbLog.toString(), log);
            
        } catch (Exception e) {
                System.err.println("Falha ao escrever arquivo log");
        }
    }
    
    private void btUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUndoActionPerformed
        if (undoManeger.canUndo() && segPanels.size() > 0) {
            undoManeger.undo();
            
            tabbedPane1Segmentos.remove(tabbedPane1Segmentos.getTabCount()-1);
            segPanels.remove(segPanels.size()-1);
            
            btUndo.setEnabled(undoManeger.canUndo());
            
//            btSavlarSegmentos.setEnabled(false);

            enableDocPanel();
        }
    }//GEN-LAST:event_btUndoActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
       // TextSegmentationTool.saveState(segPanels, taTexto.getText(), doc);
        
        JOptionPane.showMessageDialog(this, "Edição salva! Você pode contiuar desse ponto na próxima execução da ferramenta!");
    }//GEN-LAST:event_btSaveActionPerformed

    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed

        if(allDone()) {
            dispose();
            return;
        }
        
        
        if (false) {
            
        Object[] buttons = {"Salvar e sair", "Sair sem salvar", "Cancelar"};
        int resp = JOptionPane.showOptionDialog(this, "Salvar esse trabalho antes de sair?", "Sair?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null );
        if(resp == JOptionPane.YES_OPTION) {
            dispose();
            TextSegmentationTool.saveState(segPanels, taTexto.getText(), doc);
        }

        resp = JOptionPane.NO_OPTION;
        
        if(resp == JOptionPane.NO_OPTION) {
            dispose();
        }
        }
        
        
        dispose();
        
    }//GEN-LAST:event_btCloseActionPerformed

    private void onShow(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onShow
        if (!TextSegmentationTool.getCSV_FOLDER().exists()) {
//        if (true) {
            IntroduceFrame f = new IntroduceFrame();
//            JOptionPane.showMessageDialog(null, f.getContentPane(), "Olá", JOptionPane.PLAIN_MESSAGE);
        f.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setTitle("Bem vindo!");
        f.setVisible(true);
        }
    }//GEN-LAST:event_onShow

    
//    private void listComponents(Container container) {
//
//        Component[] components = container.getComponents();
//        
////        System.out.println("==== " + components.length);
//        
//        for(Component c : components) {
//            if (c instanceof Container) {
////                System.out.println("*********> "+c.getClass().getName());
//                c.setFont(increaseComponenteFontSize(c));
//                listComponents((Container)c);
//            }
//            else {
//                System.out.println("--> "+c.getClass().getName());
//            }
//                
//        }
//        
//    }
    
    private void btIncreaseFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIncreaseFontActionPerformed

        fontSize +=2;
        
        setFontSizeForAll(this.getContentPane());
        
    }//GEN-LAST:event_btIncreaseFontActionPerformed

    private void btDecFontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDecFontSizeActionPerformed
        fontSize -=2;
        
        setFontSizeForAll(this.getContentPane());
    }//GEN-LAST:event_btDecFontSizeActionPerformed

    private void btClose2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClose2ActionPerformed
        
        IntroduceFrame f = new IntroduceFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setTitle("Instruções");
        f.setButtonLabel("Ok");
        f.setVisible(true);
        

    }//GEN-LAST:event_btClose2ActionPerformed


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClose;
    private javax.swing.JButton btClose2;
    private javax.swing.JButton btCut;
    private javax.swing.JButton btDecFontSize;
    private javax.swing.JButton btIncreaseFont;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btUndo;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JSplitPane splitPanel;
    private javax.swing.JTextArea taTexto;
    private javax.swing.JTabbedPane tabbedPane1Segmentos;
    private javax.swing.JTabbedPane tabbedPanelDocumento;
    // End of variables declaration//GEN-END:variables
}
