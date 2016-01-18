package framework;

public class Cooks {
	private static Cooks instance = null;
	private Cook[] cooks;
	
	private Cooks() {
		
	}
	
	public void init(int numCooks) {
		cooks = new Cook[numCooks];
		for (int i = 0; i < cooks.length; i++) {
			cooks[i] = new Cook(i);
		}
	}
	
	public static Cooks getInstance() {
		if (instance == null)
		{
			synchronized(Cooks.class) {  //1
				if(instance == null)          //2
					instance = new Cooks();  //3
		    }
		}
		return instance;
	}

	public int getNumCooks() {
		return cooks.length;
	}
	
	public void set(int index, Cook c) {
		cooks[index] = c;
	}
}
