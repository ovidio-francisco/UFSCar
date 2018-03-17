package tests;

import java.util.ArrayList;

public class TexArticle {
	
	private static final String articleHeader = 
			"\\documentclass{article} \n" + 
			"\\usepackage[landscape]{geometry} \n" + 
			"\\usepackage{longtable} \n" +
			"\\geometry{a4paper, left=10mm, top=10mm} \n" + 
	        "\\begin{document} \n";
	
	private static final String articleFooter = "\\end{document} \n ";
	
	ArrayList<TexTable> tables = new ArrayList<>();
	
	
	public void addTable(TexTable table) {
		tables.add(table);
	}
	
	
	public String createTexArticle() {
		String article = "";
		
		article += articleHeader;
		
		for(TexTable t : tables) {
			article += "\n";
			article += t.createTexTable();
			article += "\n";
		}
		
		article += articleFooter;
		
		return article;
	}
	

}
