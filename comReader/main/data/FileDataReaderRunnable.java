package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import main.Application;
import utilities.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class FileReaderRunnable.
 *
 * @author Danilo
 */
public class FileDataReaderRunnable implements Runnable {

	/** The file. */
	private File file;
	
	/** The num of millis to sleep after reading. */
	private static int NUM_OF_MILLIS_TO_SLEEP_AFTER_READING = 20; // milliseconds
	
	/** The num of lines to read before sleeping. */
	private static int NUM_OF_LINES_TO_READ_BEFORE_SLEEPING = 20;
	
	/** The sampling rate. */
	private static int SAMPLING_RATE = 100; // milliseconds
	
	/** The current batch. */
	private ArrayList<Reading> currentBatch;

	/**
	 * Instantiates a new file reader runnable.
	 *
	 * @param newFile the new file
	 */
	public FileDataReaderRunnable(File newFile) {
		
		this.file = newFile;
		currentBatch = new ArrayList<Reading>();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

		 BufferedReader br = new BufferedReader(fileReader);
		 String line = null;
		 int numberOfLinesRead = 0;
		 long startTime = System.currentTimeMillis();
		 
		 try {
			while ((line = br.readLine()) != null) {
				
				if (line.startsWith("REP")) {
					Reading reading = Utilities.createReading(line);
					currentBatch.add(reading);
				}

				long currentTime = System.currentTimeMillis();
				
				if (currentTime - startTime >= SAMPLING_RATE) {
					HashMap<Integer, HashMap<Integer, Double>> batchSignal = Utilities.calculateBatchSignalAverages(currentBatch);
					Application.getApplication().getController().addBatchSignalToQueue(batchSignal);
					currentBatch.clear();
					startTime = currentTime;
				}
				
				numberOfLinesRead++;
				if (numberOfLinesRead >= NUM_OF_LINES_TO_READ_BEFORE_SLEEPING) {
					try {
						numberOfLinesRead = 0;
						Thread.sleep(NUM_OF_MILLIS_TO_SLEEP_AFTER_READING);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			 }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		 
		
	}

}
