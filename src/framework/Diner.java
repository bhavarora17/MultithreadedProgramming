package framework;

import framework.resource.Table;
import framework.resource.Tables;

public class Diner implements Runnable{
	
	private int id;
	
	private int timeOfArrival;		// time of arrival of the dinner at the framework
	
	private int timeOfSeating;
	
	private int timeEatingStarted;
	
	private Table tableSeatedIn;	// 
	
	private Order order;			// 
	
	private Cook cook;
	
	private boolean inRestaurant;

	private Thread t;			// diner thread

	private Logger logger;
	
	public Diner(int a, Order o, int i) {
		id = i;
		timeOfArrival = a;
		timeOfSeating = -1;
		order = o;
		inRestaurant = false;
		t = new Thread(this, "Diner-"+id);
		logger = Logger.getInstance();
	}
	
	public int getId() {
		return id;
	}

	public int getTimeOfArrival() {
		return timeOfArrival;
	}

	public int getTimeOfSeating() {
		return timeOfSeating;
	}

	public Order getOrder() {
		return order;
	}
	
	public boolean isInRestaurant() {
		return inRestaurant;
	}
	
	public void dinerArrive() {
		inRestaurant = true;
		t.start();
	}
	
	public void run() {
		Output output = Output.getInstance();
		DinerRecord record = output.getOutData()[id];
		tableSeatedIn = Tables.getInstance().getTableForDiner(this);
		timeOfSeating = Clock.getInstance().getTime();
		logger.log(Thread.currentThread().getName() + " : seated on Table-" + tableSeatedIn.id);
		tableSeatedIn.setOrder(this.order);
		tableSeatedIn.waitOnCookAssigned();
		cook = tableSeatedIn.cook;
		record.cookId = cook.getId();
		tableSeatedIn.waitOnFoodServed();
		
		record.timeOfArrival = timeOfArrival;
		record.timeSeated = timeOfSeating;
		record.tableId = tableSeatedIn.id;
		
		timeEatingStarted = Clock.getInstance().getTime();
		logger.log(Thread.currentThread().getName() + " : Started Eating");
		while (Clock.getInstance().getTime() < timeEatingStarted + 30) {
			// eating
			try {
				synchronized(Clock.getInstance()) {
					Clock.getInstance().wait();
				}
			} catch(InterruptedException ie) {}
		}

		record.burgerMachineUsedTime = tableSeatedIn.timeBurgerMacihineWasUsed;
		record.friesMachineUsedTime = tableSeatedIn.timeFriesMachineWasUsed;
		record.sodaMachineUsedTime = tableSeatedIn.timeSodaMachineWasUsed;
		record.foodServedTime = tableSeatedIn.timeFoodBroughtToTable;
		
		Tables.getInstance().releaseTable(tableSeatedIn.id);
		record.timeOfLeaving = Clock.getInstance().getTime();
		this.leave();		
	}
	
	public void leave() {
		inRestaurant = false;
		synchronized(Diners.getInstance()) {
			Diners.getInstance().leaveRestaurant();}
		synchronized(Tables.getInstance()) {
			Tables.getInstance().notifyAll(); }
	}
}