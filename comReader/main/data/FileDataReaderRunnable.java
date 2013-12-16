/*
 * 
 * 
 */
package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;

/**
 * Thread that reads the data from a file. This class is used for testing purposes. It reads several lines from the
 * file, determined by NUM_OF_LINES_TO_READ_BEFORE_SLEEPING, and then sleeps for NUM_OF_MILLIS_TO_SLEEP_AFTER_READING
 * milliseconds. After reading a batch of signals, it calls Controler's addBatchSignalToQueue method to add the batch to
 * the queue for further processing.
 * 
 * @author Danilo
 */
public class FileDataReaderRunnable implements Runnable {

	/** The number of lines to read before sleeping. */
	private static final int SAMPLING_RATE = 15;

	/** <code>Logger</code> object. */
	private Logger logger;

	/** The file to read from. */
	private File file;

	/** The current batch. */
	private ArrayList<Reading> currentBatch;

	/**
	 * Instantiates a new <code>FileDataReaderRunnable</code>.
	 * 
	 * @param newFile
	 *            <code>File</code> to read from.
	 */
	public FileDataReaderRunnable(File newFile) {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.file = newFile;
		currentBatch = new ArrayList<Reading>();
	}

	/*
	 * (non-Javadoc)
	 * 
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

		try {
			while ((line = br.readLine()) != null) {

				if (line.startsWith("REP")) {
					Reading reading = Utilities.createReading(line);
					currentBatch.add(reading);
				}
				numberOfLinesRead++;

				if (numberOfLinesRead >= SAMPLING_RATE) {
					HashMap<Integer, HashMap<Integer, Double>> batchSignal = Utilities
							.calculateBatchSignalAverages(currentBatch);
					Application.getApplication().getController().addBatchSignalToQueue(batchSignal);
					currentBatch.clear();
					numberOfLinesRead = 0;
				}
			}
		} catch (IOException e) {
			logger.warning("There was an error while trying to read data from file. " + e.getMessage());
		}
	}

}
