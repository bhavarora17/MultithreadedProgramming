package framework.resource;


import framework.Clock;
import framework.Cook;
import framework.Logger;
import framework.Order;

public class Machine implements Resource {
	
	public int timeRequired;
	public boolean occupied;
	private String name;
	private Logger logger;
	
	public Machine(String name) {
		this.name = name;
		occupied = false;
		logger  = Logger.getInstance();
	}
	
	public String getName() {
		return name;
	}

	public void cook(Order order, Cook cook) {
		int timeForCooking = timeRequired;
		switch(name) {
		case "BurgerMachine":
			timeForCooking = order.numBurgers * timeRequired;
			break;
		case "FriesMachine":
			timeForCooking = order.numFries * timeRequired;
			break;
		case "SodaMachine":
			timeForCooking = order.numCokes * timeRequired;
			break;
		}
		int startTime = Clock.getInstance().getTime();
		switch(name) {
		case "BurgerMachine":
			cook.timeBurgerMacihineWasUsed = startTime;
			break;
		case "FriesMachine":
			cook.timeFriesMachineWasUsed = startTime;
			break;
		case "SodaMachine":
			cook.timeSodaMachineWasUsed = startTime;
			break;
		}
		logger.log(Thread.currentThread().getName() + " : Cooking on " + name + " for time " + timeForCooking);
		while(Clock.getInstance().getTime() < startTime + timeForCooking) {
			//cooking
			try {
				synchronized(Clock.getInstance()) {
					Clock.getInstance().wait();
				}
			} catch(InterruptedException ie) {}
		}
		this.occupied = false;
		switch(name) {
		case "BurgerMachine":
			order.burgersReady = true;
			break;
		case "FriesMachine":
			order.friesReady = true;
			break;
		case "SodaMachine":
			order.cokeReady = true;
			break;
		}
		logger.log(Thread.currentThread().getName() + " : Done cooking and leaving " + name);
	}
	
	synchronized public void occupy() {
		occupied = true;
		logger.log(Thread.currentThread().getName() + " : " + name + " occupied");
	}
	
	synchronized public boolean isOccupied() {
		return occupied;
	}
}
