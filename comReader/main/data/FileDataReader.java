package data;

import java.io.File;
import java.util.logging.Logger;

import utilities.Utilities;


/**
 * The Class FileDataReader.
 *
 * @author Danilo
 */
public class FileDataReader extends DataReader {

	/** The logger. */
	private Logger logger;
	
	private FileDataReaderRunnable runnable;
	
	/** The file. */
	private File file;
	
	/**
	 * Instantiates a new file data reader.
	 *
	 * @param newFile the new file
	 */
	public FileDataReader(File newFile) {
		
		logger = Utilities.initializeLogger(this.getClass().getName());

		this.file = newFile;
	}
	
	/* (non-Javadoc)
	 * @see data.DataReader#readData()
	 */
	@Override
	public void readData() {
		
		runnable = new FileDataReaderRunnable(file);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public void stopReading() {
		runnable.terminate();
	}
}
