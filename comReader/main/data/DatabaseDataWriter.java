/*
 * 
 * 
 */
package data;

import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Implementation of <code>DataWriter</code> interface. This class has a thread that does the actual writing of
 * <code>WatchPositionData</code> to the database.
 * 
 * @see data.DataWriter
 * @see data.WatchPositionData
 * @author Danilo
 */
public class DatabaseDataWriter implements DataWriter {

	/** The logger. */
	private Logger logger;

	/** The runnable. */
	private DatabaseDataWriterRunnable runnable;

	/**
	 * Instantiates a <code>DatabaseDataWriter</code> object.
	 */
	public DatabaseDataWriter() {

		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataWriter#writeData()
	 */
	@Override
	public void writeData() {

		runnable = new DatabaseDataWriterRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
		logger.info("Writing data to database has been started.");
	}

	/**
	 * Stops the writing to the database. 
	 */
	@Override
	public void stopWriting() {
		runnable.terminate();
		logger.log(Level.INFO, "Writing to database has been stopped.");
	}

}
