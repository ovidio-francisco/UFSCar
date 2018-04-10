package vsm.tests;

import java.util.ArrayList;

import vsm.Document;
import vsm.VSMQuery;

public class TestVSM_Descriptors2 {
	
	public static void main(String[] args) {
		System.out.println("VSM - Descriptors");
		String q = "júnior; confeccionado; marystela; cientista; prograd;";
		
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

		ArrayList<String> strings = new ArrayList<>();
		
		strings.add(d01);
		strings.add(d02);
		strings.add(d03);
		strings.add(d04);
		strings.add(d05);
		strings.add(d06);
		strings.add(d07);
		strings.add(d08);
		strings.add(d09);
		strings.add(d10);
		
		String[] a = {};
		
		VSMQuery vsm = new VSMQuery(strings.toArray(a));
		System.out.println(String.format("%f", vsm.getSimilarity(q, d05)));
		
		Document first = vsm.getSimilarest(q, 1);
		
		
		System.out.println(String.format("\nFirst == %s", first.getTxt()));
		
	}

}
