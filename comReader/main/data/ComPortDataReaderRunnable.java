package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;

/**
 * Implementation of <code>Runnable</code> interface. This class does the actual reading from COM port. <br>
 * <br>
 * 
 * Data that comes from COM port is serialized and is read byte by byte. After reaching "end of the line", denoted by
 * <code>LINE_END_VALUE</code> constant, line is parsed and added to the batch. Batch size is determined by
 * <code>SAMPLING_RATE</code> constant (after every <code>SAMPLING_RATE</code> milliseconds, batch is sent to
 * <code>Controller</code> for further processing. <br>
 * <br>
 * 
 * By increasing the <code>SAMPLING_RATE</code> (and thus the batch size), less batches will be sent to
 * <code>Controller</code>. And vice versa.
 * 
 * @author Danilo
 * @see data.Controller
 */
public class ComPortDataReaderRunnable implements Runnable {

	/** Sampling rate. The amount of data read from data source is determined by this parameter. */
	private static final int SAMPLING_RATE = 100; // milliseconds

	/** The Constant COM_PORT_NUMBER. */
	private static final String COM_PORT_NUMBER = "com_port_number";
	
	private static final int TIME_TO_SLEEP = 10; // milliseconds

	/** The Constant BAUD_RATE. */
	private static final String BAUD_RATE = "baud_rate";

	/** The Constant DEFAULT_COM_PORT_NUMBER. */
	private static final int DEFAULT_COM_PORT_NUMBER = 1;

	/** The Constant DEFAULT_BAUD_RATE. */
	private static final int DEFAULT_BAUD_RATE = 115200;

	private static final int LINE_END_VALUE = 10;

	private static final int SLEEP_TIME = 10; // milliseconds

	/** The Constant NMAX. */
	private static final int NMAX = 500;

	/** <code>Logger</code> object. */
	private Logger logger;

	/** Flag that denotes if the thread is running. */
	private volatile boolean running = true;

	/** Current batch of readings. It contains readings from all watches. */
	private ArrayList<Reading> currentBatch = new ArrayList<Reading>();

	/** The serial port event listener. */
	private SerialComm serialPortEventListener;

	/** The rd buf. */
	private byte[] rdBuf = new byte[NMAX];

	/** The rd pos. */
	private int rdPos = 0;

	/** Current time in milliseconds. */
	private long currentTime = 0;

	/** Start time. */
	private long startTime = System.currentTimeMillis();

	/**
	 * Instantiates a new <code>ComPortDataReaderRunnable</code>.
	 */
	public ComPortDataReaderRunnable() {
		this.running = true; // otherwise it would be 'false' and the thread would terminate immediately after starting
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		serialPortEventListener = new SerialComm();
		serialPortEventListener.getPortList();
		/*
		 * We set the default values for comPortNumber and baudRate in case reading from config.ini file fails.
		 */
		int comPortNumber = DEFAULT_COM_PORT_NUMBER;
		int baudRate = DEFAULT_BAUD_RATE;

		try { // read parameters from the configuration file
			comPortNumber = Integer.parseInt(Utilities.getConfigurationValue(COM_PORT_NUMBER));
			baudRate = Integer.parseInt(Utilities.getConfigurationValue(BAUD_RATE));
		} catch (NumberFormatException exception) { // reading has failed, use default values
			logger.info("Reading parameters from configuration file failed. "
					+ "Using default values for com_port_number and default_baud_rate instead.");
		}

		serialPortEventListener.openSio(comPortNumber, baudRate);

		while (running) {
			// FIXME this code may need to be changed
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				logger.warning("Thread failed to sleep.");
			}

			char character;
			int serializedIntegerValue;
			String line = "";

			try {
				while ((serializedIntegerValue = serialPortEventListener.readSio()) > -1) {
					
					character = (char) serializedIntegerValue;
					line = line + character;
					// FIXME this code may not be needed at all, doesn't seem like it has a function
					// after testing the reading without this code checking, remove code if necessary
					rdBuf[rdPos] = new Integer(serializedIntegerValue).byteValue();
					if (rdPos < NMAX - 1) {
						rdPos++;
					}
					// end of potentially unnecessary code

					if (serializedIntegerValue == LINE_END_VALUE) {
						/* messages from receievers start with 'REP' and messages from watch start with 'CHRON' */
						if (line.startsWith("REP")) {
							Reading reading = Utilities.createReading(line);
							currentBatch.add(reading);
						}

						currentTime = System.currentTimeMillis();
						/*
						 * After every SAMPLING_RATE number of milliseconds, calculate averages of signals from all
						 * watches and call controller's method to add batch to the queue for further processing.
						 */
						if (currentTime - startTime >= SAMPLING_RATE) {
							HashMap<Integer, HashMap<Integer, Double>> batchSignal = Utilities
									.calculateBatchSignalAverages(currentBatch);
							Application.getApplication().getController().addBatchSignalToQueue(batchSignal);
							currentBatch.clear();
							startTime = currentTime;
						}
						line = "";
					}
				}
			} catch (IOException e) {
				logger.warning("An error occured while reading from COM port: " + e.getMessage());
			} 
		} // while always
	} // end run

	/**
	 * Stops the reading from COM port. This call actually terminates the thread. <br>
	 * <br>
	 * 
	 * To restart reading from COM port, a new instance of <code>ComPortDataReaderRunnable</code> should be created.
	 * This is achieved by calling <code>data.COMPortDataReader.readData()</code> method.
	 * 
	 * @see data.COMPortDataReader
	 */
	public void terminate() {
		running = false;
		serialPortEventListener.closeSio();
	}

}
