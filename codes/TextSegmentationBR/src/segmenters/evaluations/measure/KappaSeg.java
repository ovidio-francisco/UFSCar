package segmenters.evaluations.measure;

import java.time.Duration;
import java.time.Instant;

public class KappaSeg {

	private int[] rateA = null;
	private int[] rateB = null;
	
	private int A_Yes = 0;
	private int B_Yes = 0;
	private int A_No = 0;
	private int B_No = 0;

	private int AB_Yes = 0;
	private int AB_No  = 0;
	
	private int A_Yes_B__No = 0;
	private int A_No__B_Yes = 0;
	
	private boolean debug = false;
	
	public static void main(String[] args) {
		Instant start = Instant.now();
		System.out.println("MyKappa");
		
//		int[] rateA = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,     1,1,1,1,1,   0,0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//		int[] rateB = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,     0,0,0,0,0,   1,1,1,1,1,1,1,1,1,1,  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		int[] rateA = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,     1,1,1,    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] rateB = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,     0,0,0,    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		
		
		
		KappaSeg k = new KappaSeg(rateA, rateB);
		
		System.out.println(String.format("k = %f", k.findKappa()));

		
		//your code
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
	}

	public KappaSeg(int[] rateA, int[] rateB) {
		this.rateA = rateA;
		this.rateB = rateB;

		StringBuilder lineA = null;
		StringBuilder lineB = null;;
		if (debug) {
			lineA = new StringBuilder();
			lineB = new StringBuilder();
		}
		
		for(int i=0;i<this.rateA.length;i++) {
			int a = this.rateA[i];
			int b = this.rateB[i];
			
			if(a==1 && b==1) AB_Yes++;
			if(a!=1 && b!=1) AB_No++;
				
			if(a==1 && b!=1) A_Yes_B__No++;
			if(a!=1 && b==1) A_No__B_Yes++;
			
			if(debug) {
				lineA.append(String.format("%d ", a));
				lineB.append(String.format("%d ", b));
			}
		}
		if (debug)
			System.out.println(String.format("%s\n%s", lineA.toString(), lineB.toString()));
		

		if (debug){
			A_Yes = AB_Yes + A_Yes_B__No;
			A_No  = AB_No  + A_No__B_Yes;
			B_Yes = AB_Yes + A_No__B_Yes;
			B_No  = AB_No  + A_Yes_B__No;

			System.out.println(String.format("A_Yes=%d, A_No=%d, B_Yes=%d, B_No=%d", A_Yes, A_No, B_Yes, B_No));
			System.out.println(String.format("\nAB_Yes=%d \nA_Yes_B__No=%d \nA_No__B_Yes=%d \nAB_No=%d \n", AB_Yes, A_Yes_B__No, A_No__B_Yes, AB_No));
		}
	}
	
	public double findKappa() {
		double po = findPo();
		double pe = findPe();
		
		double result = (po - pe) / (1-pe);
		if (result < 0 ) result = result+1;      
		
		
		return result;
	}

	private double findPe() {
		double pe = 0;

		double a = AB_Yes;
		double b = A_Yes_B__No;
		double c = A_No__B_Yes;
		double d = AB_No;
		
		if (debug)
			System.out.println(String.format("a=%f, b=%f, c=%f, d=%f", a, b, c, d));
		
		double PeYes = (a+b)/(a+b+c+d) * (a+c)/(a+b+c+d);
		double PeNo  = (c+d)/(a+b+c+d) * (b+d)/(a+b+c+d);
		
		if (debug){
			System.out.println(String.format("PeYes = %f", PeYes));
			System.out.println(String.format("PeNo  = %f", PeNo));
		}
		
		pe = PeYes + PeNo;
		
		if (debug){
			System.out.println(String.format("Pe = %f", pe));
		}

		return pe;
	}

	private double findPo() {
		double a = AB_Yes;
		double b = A_Yes_B__No;
		double c = A_No__B_Yes;
		double d = AB_No;

		double po = (a+d)/(a+b+c+d);
		if (debug){
			System.out.println(String.format("Po = %f", po));
		}
		
		return po;
	}

	public int[] getRateA() {
		return rateA;
	}

	public int[] getRateB() {
		return rateB;
	}
}
