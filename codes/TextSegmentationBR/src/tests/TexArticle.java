package tests;

import java.io.File;
import java.util.ArrayList;

import utils.Files;

public class TexArticle {
	
//	"\\usepackage{geometry} \n" + 
	private static final String articleHeader = 
			"\\documentclass{article} \n" + 
			"\\usepackage[landscape]{geometry} \n" + 
			"\\usepackage{longtable} \n" +
			"\\usepackage[table]{xcolor}" +
			"\\geometry{a4paper, left=10mm, top=10mm} \n" + 
	        "\\begin{document} \n";
	
	private static final String articleFooter = "\\end{document} \n ";
	
	private String article = "";
	
	private ArrayList<TexTable> tables = new ArrayList<>();
	
	
	public void addTable(TexTable table) {
		tables.add(table);
	}
	
	
	public String createTexArticle() {
		article = "";
		
		article += articleHeader;
		
		for(TexTable t : tables) {
			article += "\n";
			article += t.createTexTable();
			article += "\\newpage";
		}
		
		article += articleFooter;
		
		return article;
	}
	
	public void save(File file) {
		Files.saveTxtFile(article, file);
	}
	

}
