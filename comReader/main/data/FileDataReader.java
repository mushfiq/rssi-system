package data;

import java.io.File;


/**
 * The Class FileDataReader.
 *
 * @author Danilo
 */
public class FileDataReader extends DataReader {

	/** The file. */
	private File file;
	
	/**
	 * Instantiates a new file data reader.
	 *
	 * @param newFile the new file
	 */
	public FileDataReader(File newFile) {
		// TODO Auto-generated constructor stub
		this.file = newFile;
	}
	
	/* (non-Javadoc)
	 * @see data.DataReader#readData()
	 */
	@Override
	public void readData() {
		
		Runnable runnable = new FileDataReaderRunnable(file);
		Thread thread = new Thread(runnable);
		thread.start();
	}

}
