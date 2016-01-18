package framework.resource;

import framework.Clock;
import framework.Diners;
import framework.Logger;
import framework.Order;

public class Machines {
	private static Machines instance = null;
	private BurgerMachine bm;
	private FriesMachine fm;
	private SodaMachine sm;
	private Logger logger;
	
	private Machines() {
		bm = new BurgerMachine(5);
		fm = new FriesMachine(3);
		sm = new SodaMachine(1);
		logger = Logger.getInstance();
	}
	
	public static Machines getInstance() {
		if(instance == null) {
			instance = new Machines();
		}
		return instance;
	}
	
	/*synchronized*/ public Machine getMachine(Order order) {
		logger.log(Thread.currentThread().getName() + " : looking for machines");
		while(Clock.getInstance().getTime() <=120 || Diners.getInstance().getNumCurrentDiners() > 0) {
			if(!order.burgersReady) {
				if(!bm.isOccupied()) {
					synchronized(bm) {
						if(!bm.isOccupied()) {		// double check locking
							bm.occupy();
							return bm;
						}
					}
				}
			}
			if(!order.friesReady) {
				if(!fm.isOccupied()) {
					synchronized(fm) {
						if(!fm.isOccupied()) {	// double check locking
							fm.occupy();
							return fm;
						}
					}
				}
			}
			if(!order.cokeReady) {
				if(!sm.isOccupied()) {
					synchronized(fm) {
						if(!sm.isOccupied()) {	// double check locking
							sm.occupy();
							return sm;
						}
					}
				}
			}
		}	
		return null;
	}
}
