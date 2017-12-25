package segmenters.tests;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import segmenters.algorithms.TextTilingBR;
import utils.Files;

public class TesteCurvaSimilaridade3 {

	
//	private static DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	 
	private static JFreeChart chart = null;
	private static StringBuilder sbOut = new StringBuilder();


	
	public static void main(String[] args) {

//		createFigure_CoesaoLexica(new File("./docs/Fabio/Ata 32 - 28a Reunião Odinária PPGCCS.txt"));
		createFigure_CoesaoLexica(new File("Generos-Musicais.txt"));
	}
	
	
	private static void createFigure_CoesaoLexica(File file) {
		TextTilingBR tt = new TextTilingBR();
		
//		tt.setWindowSize(40);
		tt.setStep(10);
		
		File textFile = file;
		
		
		System.out.println(
				tt.segmentsToString(textFile)
				)
				;


		float[] sim_score = tt.getSim_score();
		int[]   site_loc  = tt.getSite_loc();
		float[] depth_scr = tt.getDepth_score();
		Vector<Integer> segmentation = tt.getSegmentation();
		

		int qtdPalavras = tt.getCollection().text.size();
		System.out.println("\n\nPalavras = " + qtdPalavras);
		
		
		ArrayList<Integer> indices       = new ArrayList<>();
		ArrayList<Float>   similaridades = new ArrayList<>();
		ArrayList<Float>   depthScores   = new ArrayList<>();
		ArrayList<Float>   limites       = new ArrayList<>();
		
		
		for(int i=0;i<qtdPalavras;i++) indices.add(i);
		for(int i=0;i<qtdPalavras;i++) similaridades.add(0f);
		for(int i=0;i<qtdPalavras;i++) depthScores.add(0f);
		for(int i=0;i<qtdPalavras;i++) limites.add(0f);
		


		for(int i=0; i<site_loc.length; i++) {
			similaridades.set(site_loc[i], sim_score[i]);
			depthScores  .set(site_loc[i], depth_scr[i]);
		}
		
		
		for(int i=0; i<segmentation.size(); i++) {

			try {
				limites.set(segmentation.get(i), 0.1f);
			} catch (Exception e) {
//				System.err.println(String.format("[%d]", segmentation.get(i)));
			}
		}

		System.out.println("\n\nFiltrando");
		int j=0;
		while (j<similaridades.size()) {
			
			if (similaridades.get(j)==0 && limites.get(j)!=0 ) {
				try {
					similaridades.set(j, (similaridades.get(j-1)+similaridades.get(j+1))/2);
				} catch (Exception e) {
				}
			}
			
			if (similaridades.get(j)==0 && limites.get(j)==0 ) {
				indices.remove(j);
				similaridades.remove(j);
				limites.remove(j);
				
				depthScores.remove(j);
			}
			else {
				j++;
			}
		}

		
		System.out.println("\n\nCriando Gráfico");
		

		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries serie_coesao = new XYSeries("Coesão Léxica");
		XYSeries serie_deapthscore = new XYSeries("Depth score");

		for(int i=0; i<indices.size(); i++) {
			serie_coesao.add(indices.get(i), similaridades.get(i));
		}

		for(int i=0; i<indices.size(); i++) {
			serie_deapthscore.add(indices.get(i), depthScores.get(i));
		}
		

		dataset.addSeries(serie_coesao);
//		dataset.addSeries(serie_deapthscore);

		File filePNGImage = new File(textFile+".png");
		
		createChartXY(dataset, filePNGImage);
		
		
		HashSet<Integer> boundsHyp = new HashSet<>();
		for(int s : segmentation) {
			if(s<qtdPalavras) boundsHyp.add(s);
		}

		for(int s : boundsHyp) {
			addHypMarker(s);
		}
		

		
		
		HashSet<Integer> boundsRef = new HashSet<>();
		File fileRef = new File(textFile+".bounds");
		
		if (fileRef.exists()) {
			
			for(String s : Files.loadTxtFileToList(fileRef)) {
				boundsRef.add(Integer.parseInt(s));
			}
					
	
			for(int b : boundsRef) {
				addRefMarker(b); 
			}
		}
		
		
//		printTokens(tt);
		
		saveSummary(textFile, fileRef, boundsHyp, boundsRef, tt);
		
		
	}
	
	
	private static String lastWords(int lastOne, int lastWordsCount, TextTilingBR tt) {
		String result = "";
		
		for(int i=0; i<lastWordsCount; i++) {
			result = tt.getCollection().text.get(lastOne-i) + " " + result;
		}
		
		return result;
	}
	
	private static String nextWords(int nextOne, int nextWordsCount, TextTilingBR tt) {
		String result = "";
		
		for(int i=0; i<nextWordsCount; i++) {
			result = result + " " + tt.getCollection().text.get(nextOne+i+1);
		}
		
		return result;
	}
	
	private static void saveSummary(File fileOriginal, File fileRef, HashSet<Integer> segHyp, HashSet<Integer> segRef, TextTilingBR tt) {
		
		TreeSet<Integer> ref = new TreeSet<>(segRef);
		TreeSet<Integer> hyp = new TreeSet<>(segHyp);
		
		sbOut.append("Coesão léxica em e segmentações automática e manual ao longo do arquivo '" + fileOriginal + "'\n");
		if(fileRef.exists()) {
			sbOut.append("Arquivo de Referêcia: '" + fileRef+"'\n\n");
		}
		else {
			sbOut.append("Arquivo de Referência: '"+ fileRef + "' não encontrado\n\n");
		}
			
		
		sbOut.append("Palvras = " + tt.getCollection().text.size());
		sbOut.append("\n\n"+tt.getConfigurationLabel());
		sbOut.append("\nSegmentos Hyp = " + tt.getSegmentsCount());
		sbOut.append("\nSegmentos Ref = " + (ref.size()+1));

		int lastWordsCount = 4;
		
		
        sbOut.append("\n\nFinais dos segmentos automáticos:\n");
        for(int s : hyp) {
        	sbOut.append(String.format("  ... %s | [%d] | %s ...\n", lastWords(s-1, lastWordsCount, tt), s, nextWords(s-1, lastWordsCount, tt)));
        }
        sbOut.append("\n\nFinais dos segmentos referência:\n");
        for(int s : ref) {
        	sbOut.append(String.format("  ... %s | [%d] | %s ...\n", lastWords(s, lastWordsCount, tt), s, nextWords(s, lastWordsCount, tt)));
        }
		
        
        Files.saveTxtFile(sbOut.toString(), new File(fileOriginal+".out"));
        
		System.out.println("\n\n"+sbOut);
	}
	
   
	private static void createChartXY(XYDataset xydataset, File file ) {
//	    String chartTitle = "Coesão Lexica";
	    String chartTitle = "";
	    String xAxisLabel = "Termos";
	    String yAxisLabel = "Similaridade";
	 
	    chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, xydataset);
	 
	    ChartPanel pn = new ChartPanel(chart);
	    pn.setPreferredSize( new java.awt.Dimension( 1400, 300 ) );
	    
	    chart.getPlot().setBackgroundPaint(Color.white);
	    XYItemRenderer xyrenderer = chart.getXYPlot().getRenderer();
	    xyrenderer.setSeriesPaint(0, Color.black);

	    
//	    try {
//			ChartUtilities.saveChartAsPNG(file, chart, 1400, 300);
//			sbOut.append("Arquivo de imagem salvo como: " + file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	    
	    JFrame f = new JFrame("Coesão Lexica - " + file.getName());
	    f.setContentPane(pn);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.pack();
	    RefineryUtilities.centerFrameOnScreen(f);
	    f.setVisible(true);
   }
   
   private static void addHypMarker(float value) {
	   BasicStroke bs = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 100f, new float[]{9}, 0);
	   ValueMarker vm = new ValueMarker(value, Color.red, bs);
	   chart.getXYPlot().addDomainMarker(vm);
   }
   
   private static void addRefMarker(float value) {
	   BasicStroke bs = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 100f, new float[]{9}, 0);
	   ValueMarker vm = new ValueMarker(value, Color.blue, bs);
	   chart.getXYPlot().addDomainMarker(vm);
   }
   
	@SuppressWarnings("unused")
	private static void printTokens(TextTilingBR tt) {
		System.out.println(tt);
		
		for(int i=0; i<tt.getCollection().text.size();i++) {
			System.out.println(String.format("[[%s]] {%d}", tt.getCollection().text.get(i), i));
		}
		
	}
   
}


//https://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
//http://www.codejava.net/java-se/graphics/using-jfreechart-to-draw-xy-line-chart-with-xydataset
//https://stackoverflow.com/questions/21989082/drawing-dashed-line-in-java
//http://www.java2s.com/Code/Java/Chart/JFreeChartMarkerDemo1.htm
//http://www.jfree.org/forum/viewtopic.php?f=3&t=8686
