package tests;

import java.io.File;

import main.Application;
import data.DataProcessor;
import data.DataReader;
import data.FileDataReader;


/**
 *  For ease of testing, application may read from a file instead of from COM port.
 *  This class performs a test using a file.
 */
public final class ReadingFromFileTest {

	/**
	 * Instantiates a new reading from file test.
	 */
	private ReadingFromFileTest() {

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		/*
		 * We instantiate the application simply by calling its "getApplication"
		 * method, since it is a singleton class.
		 */

		@SuppressWarnings("unused")
		Application application = Application.getApplication();

		/*
		 * DataReader class reads data from COM port and converts it to usable
		 * signal strengths. Then, it puts those values into the signals queue,
		 * so that DataProcessor can calculate actual watch position in the
		 * room, using one of the algorithms. For testing purposes, we are
		 * reading sample data from a file. To read data from COM port, the
		 * following line of code should be changed to:
		 * 
		 * DataReader reader = new COMPortReader();
		 */

		/*DataReader reader = new FileDataReader(new File("comReader"
				+ File.separator + "main" + File.separator + "resources"
				+ File.separator + "data.txt"));*/
		
		DataReader reader = new FileDataReader(new File("comReader"
				+ File.separator + "main" + File.separator + "resources"
				+ File.separator + "data.txt"));
		reader.readData();

		/*
		 * Controller class is used to change the algorithm. Default algorithm
		 * is ProbabilityBasedAlgorithm. To change the algorithm, "setAlgorithm"
		 * method of Controller class should be called:
		 * 
		 * Application.getApplication().getController().setAlgorithm(new
		 * MyNewAlgorithm(roomMap, receivers));
		 */

		/*
		 * DataProcessor class takes the signal strengths from the queue,
		 * calculates the actual position using one of the algorithms and puts
		 * the result (actual position in form of (X,Y)) into the positions
		 * queue. After that, actual positions from this queue are sent to the
		 * server.
		 */
		DataProcessor processor = new DataProcessor();
		processor.processData();
		
	}

}
