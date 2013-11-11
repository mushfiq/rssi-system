package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.Application;
import utilities.Utilities;

public class COMPortDataReader implements Runnable {
	/** The num of millis to sleep after reading. */
	private static int NUM_OF_MILLIS_TO_SLEEP_AFTER_READING = 20; // milliseconds
	
	/** The num of lines to read before sleeping. */
	private static int NUM_OF_LINES_TO_READ_BEFORE_SLEEPING = 20;
	
	/** The sampling rate. */
	private static int SAMPLING_RATE = 100; // milliseconds
	
	/** The current batch. */
	private ArrayList<Reading> currentBatch = new ArrayList<Reading>();
	final static int NMAX = 500;
    Thread treader;
    private SerialComm s;
    private byte rdBuf[] = new byte[NMAX];
    private int rdPos = 0;
    int numberCounter = 0;
    long currentTime = 0;
    long startTime = System.currentTimeMillis();
    
	
	public COMPortDataReader() {
		super();
		
	}

	@Override
	public void run() {
		
		
		s = new SerialComm();
		  s.getPortList();
		boolean isOpen = s.openSio(1, 115200);
		
		
		// TODO Auto-generated method stub
		while (true) {
            try {Thread.sleep(10);}
            catch (InterruptedException e) {};
                // check for incoming data
            char cc;
            int c;
            String line;line="";
            
   		 int numberOfLinesRead = 0;
   		 
   		   
            try {
           
                    while ((c = s.readSio()) > -1) 
                          {
                     	 cc = (char) c;
                     	 
                    	line = line +cc;
                     	 rdBuf[rdPos] = new Integer(c).byteValue();
                              if (rdPos < NMAX-1) rdPos++;
  
                              numberCounter++;
                          
                              if(c == 10) {
                            	  
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
                  				
                  				numberOfLinesRead++;
                  				if (numberOfLinesRead >= NUM_OF_LINES_TO_READ_BEFORE_SLEEPING) {
                  					try {
                  						numberOfLinesRead = 0;
                  						Thread.sleep(NUM_OF_MILLIS_TO_SLEEP_AFTER_READING);
                  					} catch (InterruptedException e) {
                  						// TODO Auto-generated catch block
                  						e.printStackTrace();
                  					}
                  				}
                  				 line = "";
                              }            
                             
                          }
                    } // try 
                catch (IOException e) {
                    System.err.println("Exception: " + e.getMessage());
                    } // ioexception
            } // while always
        }

        public void ReaderThread(SerialComm aSC) {
            s = aSC;
            System.out.println("Reader Thread started\n");
            treader = new Thread(this, "treader");
            treader.start();
        }

        
}
		
	 
	
 
//catch (IOException e) {
	// TODO Auto-generated catch block
//	e.printStackTrace();


 
	

	//public void readData() {
		// TODO Auto-generated method stub
		
	//}

	

