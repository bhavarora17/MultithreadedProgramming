package framework;

public class Clock {
	private static Clock clock = null;
	private int time;
	private Logger logger; 
	
	private Clock() {
		time = 0;
		logger = Logger.getInstance();
	}
	
	public int getTime() {
		return time;
	}
	
	public static Clock getInstance() {
		if(clock == null) {
			clock = new Clock();
		}
		return clock;
	}
	
	public void increment() {
		logger.log("Current Time : " + time);
		try {
			Thread.sleep(50);
		} catch(InterruptedException ie) {
			
		}
		time++;
	}
}
