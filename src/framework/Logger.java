package framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Singleton class for Logging
 */
public class Logger {
	private static BufferedWriter logwriter;
	private static Logger loggerInstance;
	
	private Logger() {
		File file = new File("log.txt");
		file.delete();
		try{
			logwriter = new BufferedWriter(new FileWriter("log.txt", true));
			logwriter.newLine();
		} catch(IOException ioe) {
			System.out.println("Unable to open log file.");
			System.out.println("Log will not be written.");
		}
	}
	
	public synchronized static Logger getInstance() {
		if(loggerInstance == null) {
			loggerInstance = new Logger();
		}
		return loggerInstance;
	}
	
	public synchronized void log(String message) {
		try {
			System.out.println("\n" + getDateTime() + "  " + message);
			logwriter.newLine();
			logwriter.write(getDateTime() + "  " + message);
			logwriter.flush();
		} catch(IOException ioe) {
			System.out.println("Unable to log the message: " + message);
		}
	}
	
	public String getDateTime() {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	     Date date = new Date();
	     return dateFormat.format(date);
	}
	
	public void flush() {
		try {
			logwriter.flush();
		} catch(IOException ioe) {
			System.out.println("ERROR: Could not flush the logger.");
		}
	}
	
	public void close() {
		try {
			logwriter.close();
		} catch(IOException ioe) {
			System.out.println("ERROR: Could not close the logger.");
		}
	}
}
