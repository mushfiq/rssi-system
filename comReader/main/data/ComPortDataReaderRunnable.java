package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;

public class ComPortDataReaderRunnable implements Runnable{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@SuppressWarnings("unused")
	private volatile boolean running = true;
	
	/** The sampling rate. */
	private static int SAMPLING_RATE = 100; // milliseconds
	
	/** The current batch. */
	private ArrayList<Reading> currentBatch = new ArrayList<Reading>();
	final static int NMAX = 500;
    Thread treader;
    private SerialComm serialPortEventListener;
    private byte rdBuf[] = new byte[NMAX];
    private int rdPos = 0;
    int numberCounter = 0;
    long currentTime = 0;
    long startTime = System.currentTimeMillis();
	
	public ComPortDataReaderRunnable() {
		this.running = true; // otherwise it would be 'false' and the thread would terminate immediately after starting
	}

	@Override
	public void run() {
	
		serialPortEventListener = new SerialComm();
		serialPortEventListener.getPortList();
		boolean isOpen = serialPortEventListener.openSio(1, 115200);
		
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

	
	public void terminate() {
        running = false;
        logger.log(Level.INFO, "ComPortDataReaderRunnable has been terminated.");
    }
	
}
