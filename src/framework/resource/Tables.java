package framework.resource;

import framework.Diner;
import framework.Diners;
import framework.Logger;

public class Tables {
	private Table[] tables;
	private static Tables instance = null;
	private Logger logger;
	
	private Tables() {
		logger = Logger.getInstance();
	}
	
	public void init(int numTables) {
		if(tables == null) {
			tables = new Table[numTables];
			for(int i=0; i<tables.length; i++) {
				tables[i] = new Table(i);
			}
		}
	}
		
	public static Tables getInstance() {
		if (instance == null)
		{
			synchronized(Tables.class) {
				if(instance == null)
					instance = new Tables();
		    }
		}
		return instance;
	}
	
	synchronized public Table getTableForCook() {
		int index = -1;
		while(index == -1) {
			for(int i=0; i<tables.length; i++) {
				if(tables[i].isOccupied && !tables[i].cookAssigned) {
					index = i;
				}
			}
			//logger.log(Thread.currentThread().getName() + " : waiting on tables for a table");
			if(index == -1) {
				if(Diners.getInstance().getNumCurrentDiners() == 0)
					break;
				logger.log(Thread.currentThread().getName() + " : waiting on tables for a table");
				logger.log("Number of diners in the restaurant : " + Diners.getInstance().getNumCurrentDiners());
				try {
					wait();
				} catch(InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
			}
		}
		if(index == -1)
			return null;
		tables[index].cookAssigned = true;
		notifyAll();
		return tables[index]; 
	}
	
	synchronized public Table getTableForDiner(Diner d) {
		int index = -1;
		while(index == -1) {
			if(Diners.getInstance().isEarliest(d.getId())) {
				for(int i=0; i<tables.length; i++) {
					if(!tables[i].isOccupied) {
						index = i;
						break;
					}
				}
			}
			try {
				logger.log(Thread.currentThread().getName() + " : waiting on tables for a table");
				if(index == -1)
					wait();
			} catch(InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
		}
		tables[index].isOccupied = true;
		tables[index].diner = d;
		notifyAll();
		return tables[index]; 
	}
	
	synchronized public void releaseTable(int index) {
		tables[index].release();
	}
}