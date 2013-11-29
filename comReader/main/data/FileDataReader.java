package data;

import java.io.File;


/**
 * The Class FileDataReader.
 *
 * @author Danilo
 */
public class FileDataReader extends DataReader {

	private FileDataReaderRunnable runnable;
	
	/** The file. */
	private File file;
	
	/**
	 * Instantiates a new file data reader.
	 *
	 * @param newFile the new file
	 */
	public FileDataReader(File newFile) {
		
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
