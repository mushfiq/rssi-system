package data;

import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Implementation of DataWriter interface. This class has a thread that 
 * writes watch positions to the queue.
 */
public class DatabaseDataWriter implements DataWriter{

	/** The logger. */
	private Logger logger;
	
	private DatabaseDataWriterRunnable runnable;
	
	/**
	 * Instantiates a new database data writer.
	 */
	public DatabaseDataWriter() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see data.DataWriter#writeData()
	 */
	@Override
	public void writeData() {
		
		runnable = new DatabaseDataWriterRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void stopReading() {
		runnable.terminate();
	}

}
