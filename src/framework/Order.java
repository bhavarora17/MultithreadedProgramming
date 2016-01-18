package framework;

public class Order {
	
	public int numBurgers;
	public int numFries;
	public int numCokes;
	public boolean burgersReady;
	public boolean friesReady;
	public boolean cokeReady;
	
	public Order(int b, int f, int c) {
		numBurgers = b;
		numFries = f;
		numCokes = c;
		
		burgersReady = false;
		
		if(numFries > 0)
			friesReady = false;
		else
			friesReady = true;
		
		if(numCokes > 0) 
			cokeReady = false;
		else
			cokeReady = true;
	}
	
	public boolean isComplete() {
		if(burgersReady && friesReady && cokeReady)
			return true;
		else
			return false;
	}
}
