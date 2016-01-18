package framework;

public class Diners {
	private static Diners instance = null;
	private Diner[] diners;
	private int numCurrentDiners;
	
	private Diners() {
		numCurrentDiners = 0;
	}
	
	public static Diners getInstance() {
		if(instance == null) {
			instance = new Diners();
		}
		return instance;
	}
	
	public int getNumCurrentDiners() {
		return numCurrentDiners;
	}

	public void init(int numDiners) {
		diners = new Diner[numDiners];
	}

	public int getNumDiners() {
		return diners.length;
	}
	
	public Diner get(int index) {
		if(index < diners.length)
			return diners[index];
		else
			return null;
	}
	
	public void set(int index, Diner d) {
		diners[index] = d;
	}
	
	public void startDinersArrivedNow() {
		for(int i=0; i<diners.length; i++) {
			if(diners[i].getTimeOfArrival() == Clock.getInstance().getTime()) {
				diners[i].dinerArrive();
				numCurrentDiners++;
			}
		}
	}
	
	public void leaveRestaurant() {
		this.numCurrentDiners--;
	}
	
	public boolean isEarliest(int id) {
		for(int i=0; i<diners.length; i++) {
			if(diners[i].isInRestaurant() && diners[i].getTimeOfSeating() == -1) {
				if(id == i)
					return true;
				else
					return false;
			}
		}
		return false;
	}
}
