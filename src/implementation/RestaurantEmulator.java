package implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import framework.*;
import framework.resource.Tables;

public class RestaurantEmulator {
	
	private Diners diners;
	
	private Cooks cooks;
	
	private Tables tables;
	
	private Clock clock;

	private Output output;
	
	private Logger logger;
	
	RestaurantEmulator(int d, int c, int t) {
		logger = Logger.getInstance();
		output = Output.getInstance();
		output.init(d);
		clock = Clock.getInstance();
		tables = Tables.getInstance();
		tables.init(t);
		diners = Diners.getInstance();
		diners.init(d);
		cooks = Cooks.getInstance();
		cooks.init(c);
	}
	
	private void startExecution() {	
		// Time started
		while(clock.getTime() <=120 || Diners.getInstance().getNumCurrentDiners() > 0) {
			diners.startDinersArrivedNow();
			clock.increment(); 
			synchronized(clock) {
				clock.notifyAll();
			}
		}
	}
	
	private void writeOutput() {
		String str;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("output.txt"));
		} catch (IOException e) {
			logger.log("Failed to open file for output");
		} 
		
		str = "Diner    \t" +
				"Arrival \t" + 
				"Time of Seating \t" +
				"      Table ID\t\t" +
				"Cook ID\t" +
				"Food Served\t\t" + 
				"Time of Leaving";
		try {
			System.out.println(str);
			writer.write(str);
			writer.newLine();
		} catch (IOException e) {
			logger.log("Failed to write to the file.");
		}
		
		
		DinerRecord[] records = output.getOutData();
		for(int i=0; i<records.length; i++) {
			str = 	i + "        \t" + 
					records[i].timeOfArrival + "               \t" +
					records[i].timeSeated + "               \t" + 
					records[i].tableId + "       \t" + 
					records[i].cookId + "      \t" + 
					records[i].foodServedTime + "               \t" +
					records[i].timeOfLeaving;
			try {
				System.out.println(str);
				writer.write(str);
				writer.newLine();
			} catch (IOException e) {
				logger.log("Failed to write to the file.");
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			logger.log("Error closing output file writer.");
		}
	}
	
	public static void main(String args[]) {
		
		if (args.length < 1) {
			System.out.println("Incorrect use\nCorrect Usage : " +
					"java RestaurantEmulator <File name>");
			System.exit(0);
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			
			String line = reader.readLine();
			int numberOfDiners = Integer.parseInt(line.trim());
			
			line = reader.readLine();
			int numberOfTables = Integer.parseInt(line.trim());
			
			line = reader.readLine();
			int numberOfCooks = Integer.parseInt(line.trim());
			
			RestaurantEmulator restaurant = new RestaurantEmulator(
					numberOfDiners, numberOfCooks, numberOfTables);
			
			int i = 0;
			while (((line = reader.readLine()) != null) && (i < numberOfDiners)) {
				String in[] = line.split("\\s+");
				int ts = Integer.parseInt(in[0].trim());
				Order o = new Order(Integer.parseInt(in[1].trim()), 
						Integer.parseInt(in[2].trim()), Integer.parseInt(in[3].trim()));
				restaurant.diners.set(i, new Diner(ts,o,i));
				i++;
			}
			
			reader.close();
			
			restaurant.startExecution();
			
			restaurant.writeOutput();
		}
		catch (FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
			System.exit(0);
		}
		catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			System.exit(0);
		}
	}
}


/*
//records[i].burgerMachineUsedTime + "                \t" + 
//records[i].friesMachineUsedTime + "                \t" + 
//records[i].sodaMachineUsedTime + "               \t" + 
*/