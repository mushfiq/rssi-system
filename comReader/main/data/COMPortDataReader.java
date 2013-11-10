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
	private ArrayList<Reading> currentBatch;
	final static int NMAX = 500;
    Thread treader;
    private SerialComm s;
    private byte rdBuf[] = new byte[NMAX];
    private int rdPos = 0;
    int numberCounter = 0;
    long currentTime = System.currentTimeMillis();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            try {Thread.sleep(10);}
            catch (InterruptedException e) {};
                // check for incoming data
            char cc;
            int c;
            String s1;s1="";
            try {
                    while ((c = s.readSio()) > -1) 
                          {
                     	 cc = (char) c;
                     	 
                    	s1 = s1 +cc;
                     	 rdBuf[rdPos] = new Integer(c).byteValue();
                              if (rdPos < NMAX-1) rdPos++;
  
                              numberCounter++;
                          
                              if(c == 10) {
                            	  String[]  splits=s1.split(" ");
                            	  for (int index=0;index<splits.length;index++){
                            		  if (splits[index].contains("REP")){
                            			  String receiverID=splits[index+7];
                            			  String strength1=splits[index+13];
                            			  String strength2=splits[index+14];
                            			  String strength3=splits[index+15];
                            			  String strength4=splits[index+16];
                            			  System.out.println(receiverID+": "+strength1+" "+strength2+" "+strength3+" "+strength4); 
                                	  }
                            	  }
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
        {
       
		long startTime = 0;
		if (currentTime - startTime >= SAMPLING_RATE)
		
		{
			HashMap<Integer, HashMap<Integer, Double>> batchSignal = Utilities.calculateBatchSignalAverages(currentBatch);
			Application.getApplication().getController().addBatchSignalToQueue(batchSignal);
			currentBatch.clear();
			startTime = currentTime;
		}
		
		int numberOfLinesRead = 0;
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
		}}
		
	 
	
 
//catch (IOException e) {
	// TODO Auto-generated catch block
//	e.printStackTrace();


 
	

	//public void readData() {
		// TODO Auto-generated method stub
		
	//}

	

