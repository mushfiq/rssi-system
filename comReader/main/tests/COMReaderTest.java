package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.Application;

import com.sun.corba.se.pept.transport.ReaderThread;

import data.COMPortDataReader;
import data.DataProcessor;
import data.DatabaseDataWriter;
import data.SerialComm;

public class COMReaderTest {

	
	/**
	 * Instantiates a new reading from com port.
	 */
	private COMReaderTest() {

	}

	static SerialComm s;
    static ReaderThread rdt;
	
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
		 * room, using one of the algorithms. 
		 * 
		 * 
		 */

		COMPortDataReader reader = new COMPortDataReader();
		Thread thread = new Thread(reader);
		 thread.start();

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
		
		DatabaseDataWriter writer = new DatabaseDataWriter();
		writer.writeData();
	
	}
}
