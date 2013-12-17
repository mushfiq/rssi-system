package data;

import java.util.logging.Logger;

import utilities.Utilities;

/**
 * DataProcessor takes batches of signal strengths from the batchSignalQueue and performs 
 * algorithm calculations on them. 
 */
public class DataProcessor {

	/** The logger. */
	private Logger logger;
	
	private DataProcessorRunnable runnable;
	
	/**
	 * Instantiates a new data processor.
	 */
	public DataProcessor() {
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/**
	 * Process data.
	 */
	public void processData() {
		
		runnable = new DataProcessorRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void stopReading() {
		runnable.terminate();
	}
}
