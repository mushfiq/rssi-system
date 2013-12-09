package data;

import java.util.logging.Logger;

import utilities.Utilities;


public class COMPortDataReader extends DataReader {

	/** The logger. */
	private Logger logger;
	
	private ComPortDataReaderRunnable runnable;
	
	public COMPortDataReader() {
		super();
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	@Override
	public void readData() {
		
		ComPortDataReaderRunnable runnable = new ComPortDataReaderRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void stopReading() {
		runnable.terminate();
	}
      
}
		

	

