package vsm.tests;

import java.util.ArrayList;

import vsm.Document;
import vsm.VSMQuery;

public class TestVSM_Descriptors {
	
	
	public static void main(String[] args) {
		System.out.println("VSM - Descriptors");
		
		String q = "júnior; confeccionado; marystela;";
		
		String d01 = "marques; apontados; prazo; promovendo; apresente; ";
		String d02 = "persianas; pudessem; richard; mídias; saiam; ";
		String d03 = "cabível; avaliação; mestrado; cientista; prograd; ";
		String d04 = "min; definitiva; retirar; fuzzy; treinamento; ";
		String d05 = "júnior; confeccionado; marystela; modificado; favor; ";
		String d06 = "substituto; conferidas; risco; mongodb; dal; ";
		String d07 = "seminários; metodologia; alíneas; coffee; reclamações; ";
		String d08 = "questiona; atenção; autonomia; pen; básicas; ";
		String d09 = "cientista; prosseguimento; delibera; corte; lizandra; ";
		String d10 = "acordo; worshop; sorteada; chave; presença; ";

		ArrayList<Document> documents = new ArrayList<>();
		
		documents.add(new Document(d01));
		documents.add(new Document(d02));
		documents.add(new Document(d03));
		documents.add(new Document(d04));
		documents.add(new Document(d05));
		documents.add(new Document(d06));
		documents.add(new Document(d07));
		documents.add(new Document(d08));
		documents.add(new Document(d09));
		documents.add(new Document(d10));
		
		VSMQuery vsm = new VSMQuery(documents);
		
		System.out.println(String.format("%f", vsm.getSimilarity(q, new Document(d05))));
		
	}

}
