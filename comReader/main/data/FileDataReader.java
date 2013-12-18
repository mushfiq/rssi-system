/*
 * 
 * 
 */
package data;

import java.io.File;
import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Implementation of <code>DataReader</code>. It uses a file as data source input. It contains a
 * <code>FileDataReaderRunnable</code> that does the actual reading. This class is used for testing purposes. For
 * example, when actual hardware (receivers and main hub) is not available, when we want to test the functionality on
 * the exact same data sample (it is difficult to achieve the exact same input twice when using actual hardware) or when
 * we want to test another functionality, rather then reading.
 * 
 * @author Danilo
 * @see data.FileDataReaderRunnable
 */
public class FileDataReader implements DataReader {

	/** <code>Logger</code> object. */
	@SuppressWarnings("unused")
	private Logger logger;

	/** The runnable. */
	private FileDataReaderRunnable runnable;

	/** The file to read from. */
	private File file;

	/**
	 * Instantiates a new <code>FileDataReader</code>.
	 * 
	 * @param newFile
	 *            <code>File</code> object.
	 */
	public FileDataReader(File newFile) {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.file = newFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataReader#readData()
	 */
	@Override
	public void readData() {
		
		runnable = new FileDataReaderRunnable(file);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	@Override
	public void stopReading() {
		// stopping reading from file is not needed at the moment 
	}
}
