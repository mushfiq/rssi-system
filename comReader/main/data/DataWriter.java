package data;

/**
 * The Interface DataWriter. Contains a thread that writes <code>WatchPositionData</code> objects to the database.
 * 
 * @author Danilo
 * @see data.WatchPositionData
 */
public interface DataWriter {

	/**
	 * Writes data.
	 */
	void writeData();

}
