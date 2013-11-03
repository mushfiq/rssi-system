package data;

// TODO: Auto-generated Javadoc
/**
 * DataProcessor takes batches of signal strengths from the batchSignalQueue and performs 
 * algorithm calculations on them. 
 */
public class DataProcessor {

	/**
	 * Instantiates a new data processor.
	 */
	public DataProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Process data.
	 */
	public void processData() {
		
		DataProcessorRunnable runnable = new DataProcessorRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
