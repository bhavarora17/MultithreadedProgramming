package framework.resource;

import framework.Clock;
import framework.Cook;
import framework.Diner;
import framework.Logger;
import framework.Order;

public class Table implements Resource {

	public int id;		// id of the table
	public boolean isOccupied;
	public boolean cookAssigned;
	public boolean foodServed;
	private Order order;
	private Logger logger;
	public Cook cook;
	public Diner diner;
	public int timeBurgerMacihineWasUsed;
	public int timeFriesMachineWasUsed;
	public int timeSodaMachineWasUsed;
	public int timeFoodBroughtToTable;
	
	public Table(int id) {
		this.id = id;
		isOccupied = false;
		cookAssigned = false;
		foodServed = false;
		order = null;
		logger = Logger.getInstance();
	}

	public void release() {
		isOccupied = false;
		cookAssigned = false;
		foodServed = false;
		order = null;
		logger.log(Thread.currentThread().getName() + " : released Table-" + id);
	}


	public Order getOrder() {
		return order;
	}

	public synchronized void setOrder(Order order) {
		this.order = order;
		logger.log(Thread.currentThread().getName() + " : placed order");
		notifyAll();
	}
	
	public synchronized void waitOnCookAssigned() {
		try {
			logger.log(Thread.currentThread().getName() + " : waiting on table for cook");
			while(!cookAssigned)
				wait();
		} catch(InterruptedException ie) {}
	}
	
	public synchronized void assignCook(Cook cook) {
		cookAssigned = true;
		this.cook = cook;
		logger.log(Thread.currentThread().getName() + " : assigned to Table-" + id);
		notifyAll();
	}
	
	public synchronized void waitOnOrder() {
		try {
			logger.log(Thread.currentThread().getName() + " : waiting for diner to place order");
			while(order == null)
				wait();
		} catch(InterruptedException ie) {}
	}
	
	public synchronized void waitOnFoodServed() {
		try {
			logger.log(Thread.currentThread().getName() + " : waiting on table for food");
			while(!foodServed)
				wait();
		} catch(InterruptedException ie) {}
	}
	
	public synchronized void serveFood() {
		logger.log("Food served for " + Thread.currentThread().getName());
		foodServed = true;
		timeFoodBroughtToTable = Clock.getInstance().getTime();
		notifyAll();
	}
}
