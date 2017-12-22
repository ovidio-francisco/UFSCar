package segmenters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import segmenters.algorithms.TextTilingBR;

public class TesteCurvaSimilaridade {

	private static DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	 

	
	public static void main(String[] args) {

		System.out.println("Testando a curva de similaridade no TextTiling\n");
		
		
		TextTilingBR tt = new TextTilingBR();
		
//		File textFile = new File("legiao.txt");
//		File textFile = new File("simpleDoc2.txt");
//		File textFile = new File("Ata 32.txt");
		File textFile = new File("Ata 32 Real.txt");
		
		
//		int c = 1;
//		for(String s : Files.loadTxtFileToList(textFile)) {
//			System.out.println(c++ + " -- " + s + "\n");
//		}
		
//		System.out.println(
				tt.segmentsToString(textFile)
//				)
				;


		float[] sim_score = tt.getSim_score();
		int[]   site_loc  = tt.getSite_loc();
		float[] depth_scr = tt.getDepth_score();
		Vector<Integer> segmentation = tt.getSegmentation();
//		ArrayList<Integer> bounds = tt.getBoundariesIndexes();
		
//		System.out.println("score -->" + score.size());
		System.out.println("sim_score     --> " + sim_score.length);
		System.out.println("site_loc      --> " + site_loc.length);
		System.out.println("depth_score   --> " + depth_scr.length);
		System.out.println("segmentation  --> " + segmentation.size());
		System.out.println("win size      --> " + tt.getWindowSize());
		System.out.println("Bounds Count  --> " + tt.getBoundariesCount());
		
//		System.out.println("\n\nsim_score\tdepth_score");
//		for(int i=0; i < sim_score.length; i++) {
//			System.out.println(String.format("%f\t%f", sim_score[i],depth_scr[i]));
//		}

		System.out.print("\nsim_score    ");
		for(float s : sim_score) {
			System.out.print(String.format("%f ", s));
		}
//		System.out.print("\nBounds ");
//		for(int i=0;i<sim_score.length;i++) {
//			System.out.print(String.format("%f ", bounds.contains(i) ? 0.1 : 0));
//		}
		System.out.print("\ndepth_score  ");
		for(float s : depth_scr) {
			System.out.print(String.format("%f ", s));
		}
		System.out.print("\nsite_loc     ");
		for(int s : site_loc) {
			System.out.print(String.format("%d ", s));
		}
		System.out.print("\nsegmentation ");
		for(int s : segmentation) {
			System.out.print(String.format("%d ", s));
		}
//		System.out.print("\nBounds ");
//		for(int s : bounds) {
//			System.out.print(String.format("%d ", s));
//		}

		int qtdPalavras = tt.getCollection().text.size();
		System.out.println("\n\nPalavras = " + qtdPalavras);
		
		
		ArrayList<Integer> indices       = new ArrayList<>();
		ArrayList<Float>   similaridades = new ArrayList<>();
		ArrayList<Float>   depthScores   = new ArrayList<>();
		ArrayList<Float>   sim_no_smooth = new ArrayList<>();
		ArrayList<Float>   limites       = new ArrayList<>();
		
		
		for(int i=0;i<qtdPalavras;i++) indices.add(i);
		for(int i=0;i<qtdPalavras;i++) similaridades.add(0f);
		for(int i=0;i<qtdPalavras;i++) depthScores.add(0f);
		for(int i=0;i<qtdPalavras;i++) sim_no_smooth.add(0f);
		for(int i=0;i<qtdPalavras;i++) limites.add(0f);
		


		for(int i=0; i<site_loc.length; i++) {
			similaridades.set(site_loc[i], sim_score[i]);
			depthScores  .set(site_loc[i], depth_scr[i]);
			sim_no_smooth.set(site_loc[i], tt.getSim_no_smooth().get(i));
		}
		
		
		for(int i=0; i<segmentation.size(); i++) {

			try {
				
				limites.set(segmentation.get(i), 0.1f);
			} catch (Exception e) {
//				System.err.println(String.format("[%d]", segmentation.get(i)));
				
			}
		}

		System.out.println("\n\n ============== // ===============");
		
		System.out.print("\nIndices, ");
		for(int i : indices) System.out.print(String.format("%d,", i));
		System.out.print("\nSimilaridades, ");
		for(float s : similaridades) System.out.print(String.format("%f,", s));
		System.out.print("\nDepth Scores, ");
		for(float s : depthScores) System.out.print(String.format("%f,", s));
		System.out.print("\nScores no Smoothing, ");
		for(float s : sim_no_smooth) System.out.print(String.format("%f,", s));
		System.out.print("\nLimites, ");
		for(float s : limites)       System.out.print(String.format("%f,", s));
		
		

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
				sim_no_smooth.remove(j);
			}
			else {
				j++;
			}
			
			
		}

		
		System.out.print("\nIndices, ");
		for(int i : indices) System.out.print(String.format("%d,", i));

		System.out.print("\nSimilaridades, ");
		for(float s : similaridades) System.out.print(String.format("%f,", s));
		
		System.out.print("\nLimites, ");
		for(float s : limites)       System.out.print(String.format("%f,", s));

		
		System.out.print("\nDepth Scores, ");
		for(float s : depthScores) System.out.print(String.format("%f,", s));
		
		System.out.print("\nScores no Smoothing, ");
		for(float s : sim_no_smooth) System.out.print(String.format("%f,", s));
		

		
		
		
		
		
		System.out.println("\n\nCriando Gráfico");
		for(int i=0; i<indices.size(); i++) {
			dataset.addValue(similaridades.get(i), "Coesão Léxica", indices.get(i));
		}

		
		
		
		
		
//		dataset.addValue( 15 , "schools" , "1970" );
//		dataset.addValue( 30 , "schools" , "1980" );
//		dataset.addValue( 60 , "schools" ,  "1990" );
//		dataset.addValue( 120 , "schools" , "2000" );
//		dataset.addValue( 240 , "schools" , "2010" );
//		dataset.addValue( 300 , "schools" , "2014" );

		
//		createChart(); 
		createChartXY();
	}
	
	
   
   private static void createChart() {
	   
//      JFreeChart lineChart = ChartFactory.createLineChart( "Coesão Lexica", "Termo","Similaridade", dataset, PlotOrientation.VERTICAL, true,true,false);
      JFreeChart lineChart = ChartFactory.createLineChart( "Coesão Lexica", "Termo","Similaridade", dataset, PlotOrientation.VERTICAL, true,true,false);
	         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 1200 , 600 ) );
      

//      IntervalMarker im = new IntervalMarker(50, 50);
//      
//      XYPlot xy = (XYPlot) lineChart.getPlot();
//      xy.addDomainMarker(im);
      
      JFrame f = new JFrame();
      f.setContentPane( chartPanel );
      f.pack();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      RefineryUtilities.centerFrameOnScreen(f);
      f.setVisible(true);
   }
   
//   https://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
//   http://www.codejava.net/java-se/graphics/using-jfreechart-to-draw-xy-line-chart-with-xydataset

   private static XYDataset createXYDataset() {
	    XYSeriesCollection dataset = new XYSeriesCollection();
	    XYSeries series1 = new XYSeries("Object 1");
	    XYSeries series2 = new XYSeries("Object 2");
	    XYSeries series3 = new XYSeries("Object 3");
	 
	    series1.add(1.0, 2.0);
	    series1.add(2.0, 3.0);
	    series1.add(3.0, 2.5);
	    series1.add(3.5, 2.8);
	    series1.add(4.2, 6.0);
	 
	    series2.add(2.0, 1.0);
	    series2.add(2.5, 2.4);
	    series2.add(3.2, 1.2);
	    series2.add(3.9, 2.8);
	    series2.add(4.6, 3.0);
	 
	    series3.add(1.2, 4.0);
	    series3.add(2.5, 4.4);
	    series3.add(3.8, 4.2);
	    series3.add(4.3, 3.8);
	    series3.add(4.5, 4.0);
	 
	    dataset.addSeries(series1);
	    dataset.addSeries(series2);
	    dataset.addSeries(series3);
	 
	    return dataset;
	}
   private static void createChartXY() {
	   XYDataset xyDataset = createXYDataset(); 
	   
	   String chartTitle = "Objects Movement Chart";
	    String xAxisLabel = "X";
	    String yAxisLabel = "Y";
	 
	 
	    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
	            xAxisLabel, yAxisLabel, xyDataset);
	 
	    XYPlot xy = chart.getXYPlot();
	    
	    BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	    
	    ValueMarker vm = new ValueMarker(3.1, Color.RED, bs);
	    xy.addDomainMarker(vm);
	    
	    JPanel pn = new ChartPanel(chart);
	    
	    JFrame f = new JFrame("XY");
	    f.setContentPane(pn);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.pack();
	    f.setVisible(true);
   }
	
}
