/*
 * 
 * 
 */
package data;

import java.util.logging.Logger;

import utilities.Utilities;

/**
 * DataProcessor takes batches of signal strengths from the batchSignalQueue and performs algorithm calculations on
 * them.
 * 
 * @author Danilo
 */
public class DataProcessor {

	/** The logger. */
	@SuppressWarnings("unused")
	private Logger logger;

	/** The runnable. */
	private DataProcessorRunnable runnable;

	/**
	 * Instantiates a new <code>DataProcessor</code>.
	 */
	public DataProcessor() {
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/**
	 * Creates a new thread that does the processing.
	 * 
	 * @see data.DataProcessorRunnable
	 */
	public void processData() {
		runnable = new DataProcessorRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * Stop reading.
	 */
	public void stopReading() {
		runnable.terminate();
	}
}
