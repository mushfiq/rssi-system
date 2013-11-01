package data;


/**
 * The Class DataReader.
 */
public abstract class DataReader {

	/** The controller. */
	private Controller controller;
	
	/**
	 * Instantiates a new data reader.
	 */
	public DataReader() {
		// TODO Auto-generated constructor stub
		controller = Controller.getController();
	}

	/**
	 * Read data.
	 */
	public abstract void readData();
}
