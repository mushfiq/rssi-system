package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class ComPortDataReaderRunnable.
 */
public class ComPortDataReaderRunnable implements Runnable{
	
	/** The logger. */
	private Logger logger;
	
	/** The running. */
	private volatile boolean running = true;
	
	/** The sampling rate. */
	private static int SAMPLING_RATE = 100; // milliseconds
	
	/** The current batch. */
	private ArrayList<Reading> currentBatch = new ArrayList<Reading>();
	
	/** The Constant NMAX. */
	final static int NMAX = 500;
    
    /** The treader. */
    Thread treader;
    
    /** The serial port event listener. */
    private SerialComm serialPortEventListener;
    
    /** The rd buf. */
    private byte rdBuf[] = new byte[NMAX];
    
    /** The rd pos. */
    private int rdPos = 0;
    
    /** The number counter. */
    int numberCounter = 0;
    
    /** The current time. */
    long currentTime = 0;
    
    /** The start time. */
    long startTime = System.currentTimeMillis();
	
    /** The Constant COM_PORT_NUMBER. */
	private static final String COM_PORT_NUMBER = "com_port_number";
	
	/** The Constant BAUD_RATE. */
	private static final String BAUD_RATE = "baud_rate";
	
	/** The Constant DEFAULT_COM_PORT_NUMBER. */
	private static final int DEFAULT_COM_PORT_NUMBER = 1;
	
	/** The Constant DEFAULT_BAUD_RATE. */
	private static final int DEFAULT_BAUD_RATE = 115200;
    
	/**
	 * Instantiates a new com port data reader runnable.
	 */
	public ComPortDataReaderRunnable() {
		this.running = true; // otherwise it would be 'false' and the thread would terminate immediately after starting
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	
		serialPortEventListener = new SerialComm();
		serialPortEventListener.getPortList();
		/* we set the default values for com_port_number and baud_rate in case
		 reading from the config file fails */
		int com_port_number = DEFAULT_COM_PORT_NUMBER;
		int baud_rate = DEFAULT_BAUD_RATE;
		
		try { // read parameters from the configuration file
			
			com_port_number = Integer.parseInt(Application.getApplication().getConfigurationValue(COM_PORT_NUMBER));
			baud_rate = Integer.parseInt(Application.getApplication().getConfigurationValue(BAUD_RATE));
		
		} catch (NumberFormatException exception) { // reading has failed, use default values
			
			logger.info("Reading parameters from configuration file failed. "
					+ "Using default values for com_port_number and default_baud_rate instead.");
		}
		
		boolean isOpen = serialPortEventListener.openSio(com_port_number, baud_rate);
		
		while (running == true) {
            try {
            	Thread.sleep(10);
            }
            catch (InterruptedException e) {};
            // check for incoming data
            char character;
            int serializedIntegerValue;
            String line = "";
   		 
            try {
                while ((serializedIntegerValue = serialPortEventListener.readSio()) > -1) {
                 	character = (char) serializedIntegerValue; 
                	line = line + character;
                 	rdBuf[rdPos] = new Integer(serializedIntegerValue).byteValue();
                    if (rdPos < NMAX-1) {
                    	rdPos++;
                    }
                    numberCounter++;
              
                    if(serializedIntegerValue == 10) {
                	    if (line.startsWith("REP")) {
                		   Reading reading = Utilities.createReading(line);
                		   currentBatch.add(reading);
                	   }
                    	  
                       currentTime = System.currentTimeMillis();
          			   if (currentTime - startTime >= SAMPLING_RATE) {
          					HashMap<Integer, HashMap<Integer, Double>> batchSignal = Utilities.calculateBatchSignalAverages(currentBatch);
          					Application.getApplication().getController().addBatchSignalToQueue(batchSignal);
          					currentBatch.clear();
          					startTime = currentTime;
          				}
          				
          			   line = "";
                      }            
                  }
                } // try 
            catch (IOException e) {
                System.err.println("Exception: " + e.getMessage());
                } // ioexception
        } // while always
	} // end run

	
	/**
	 * Terminate.
	 */
	public void terminate() {
        running = false;
        logger.log(Level.INFO, "ComPortDataReaderRunnable has been terminated.");
    }
	
}
