package data;

import java.util.logging.Logger;
import utilities.Utilities;

/**
 * Contains the thread that reads and parses data from COM port.
 */
public abstract class DataReader {

	/** The logger. */
	private Logger logger;
	
	/**
	 * Instantiates a new data reader.
	 */
	public DataReader() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/**
	 * Read data.
	 */
	public abstract void readData();
}
