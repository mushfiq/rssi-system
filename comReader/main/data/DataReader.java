package data;

import main.Application;


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
		controller = Application.getApplication().getController();
	}

	/**
	 * Read data.
	 */
	public abstract void readData();
}
