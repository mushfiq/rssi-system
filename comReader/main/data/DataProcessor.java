package data;

/**
 * DataProcessor takes batches of signal strengths from the batchSignalQueue and performs 
 * algorithm calculations on them. 
 */
public class DataProcessor {

	private DataProcessorRunnable runnable;
	
	/**
	 * Instantiates a new data processor.
	 */
	public DataProcessor() {
		
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
