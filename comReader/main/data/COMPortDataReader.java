/*
 * 
 * 
 */
package data;

import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Implementation of <code>data.DataReader</code> interface. This class has a thread
 * <code>COMPortDataReaderRunnable</code> that does the actual reading. This class controls starting and stopping of
 * reading from COM port.
 * 
 * @author Danilo
 * @see data.DataReader
 */
public class COMPortDataReader implements DataReader {

	/** <code>Logger</code> object. */
	private Logger logger;

	/** <code>Runnable</code> that does the actual reading. */
	private ComPortDataReaderRunnable runnable;

	/**
	 * Instantiates a new <code>COMPortDataReader</code>.
	 */
	public COMPortDataReader() {
		super();
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataReader#readData()
	 */
	@Override
	public void readData() {

		runnable = new ComPortDataReaderRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
		logger.info("Reading data from COM port started.");
	}

	/**
	 * Stops reading from COM port.
	 */
	@Override
	public void stopReading() {
		runnable.terminate();
		logger.info("Reading data from COM port stopped.");
	}

}
