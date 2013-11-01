package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import utilities.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class FileReaderRunnable.
 *
 * @author Danilo
 */
public class FileReaderRunnable implements Runnable {

	/** The file. */
	private File file;
	
	/** The num of millis to sleep after reading. */
	private static int NUM_OF_MILLIS_TO_SLEEP_AFTER_READING = 200; // milliseconds
	
	/** The num of lines to read before sleeping. */
	private static int NUM_OF_LINES_TO_READ_BEFORE_SLEEPING = 20;
	
	/** The sampling rate. */
	private static int SAMPLING_RATE = 200; // milliseconds
	
	/** The Constant RADIX. */
	private static final int RADIX = 16;
	
	/** The current batch. */
	private ArrayList<Reading> currentBatch;

	/**
	 * Instantiates a new file reader runnable.
	 *
	 * @param newFile the new file
	 */
	public FileReaderRunnable(File newFile) {
		
		this.file = newFile;
		currentBatch = new ArrayList<Reading>();
	}

	/**
	 * Creates the reading.
	 *
	 * @param line the line
	 * @return the reading
	 */
	private Reading createReading(String line) {
		
		StringTokenizer tokenizer = new StringTokenizer(line);
			
			//REP
			tokenizer.nextToken();
			//2
			tokenizer.nextToken();
			//3
			tokenizer.nextToken();
			//4
			tokenizer.nextToken();
			//5
			tokenizer.nextToken();
			//6
			tokenizer.nextToken();
			//7
			tokenizer.nextToken();
			//8
			int receiverId = Integer.parseInt(tokenizer.nextToken());
			//9
			tokenizer.nextToken();
			//10
			tokenizer.nextToken();
			//11
			tokenizer.nextToken();
			//12
			tokenizer.nextToken();
			//13
			tokenizer.nextToken();
			//14
			int signalStrength1 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			//15
			int signalStrength2 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			//16
			int signalStrength3 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			//17
			int signalStrength4 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			//18
			tokenizer.nextToken();
			//19
			tokenizer.nextToken();
			
			ArrayList<Integer> signalStrengths = new ArrayList<Integer>();
			signalStrengths.add(signalStrength1);
			signalStrengths.add(signalStrength2);
			signalStrengths.add(signalStrength3);
			signalStrengths.add(signalStrength4);
			
			Reading reading = new Reading(receiverId, 0, signalStrengths);
			
		return reading;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		long overallStartTime = System.currentTimeMillis();
		
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
					Reading reading = createReading(line);
					System.out.println(reading);
					int average = Utilities.calculateReadingAverage(reading);
					reading.setAverageStrengthValue(average);
					int rssiDbm = Utilities.convertRSSIDecToDbm(average);
					reading.setRssiDbm(rssiDbm);
					currentBatch.add(reading);
					//Controller.getController().addReadingToQueue(reading);
				}

				long currentTime = System.currentTimeMillis();
				
				if (currentTime - startTime >= SAMPLING_RATE) {
					Controller.getController().addBatchToQueue(currentBatch);
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
			System.out.println("Total number of batches after reading is: " + Controller.getController().getBatchQueue().size());
			System.out.println("Total time: " + (System.currentTimeMillis() - overallStartTime));
		}
		 
		
	}

}
