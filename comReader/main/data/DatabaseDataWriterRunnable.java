package data;

import java.util.concurrent.BlockingQueue;

import main.Application;


public class DatabaseDataWriterRunnable implements Runnable {

	private static final int MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY = 10;
	
	public DatabaseDataWriterRunnable() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		BlockingQueue<WatchPositionData> calculatedPositionsQueue = Application.getApplication().getController().getCalculatedPositionsQueue();
		
		while (true) { //always
			
			if(calculatedPositionsQueue.isEmpty()) { 
				
				try {
					Thread.sleep(MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY); // try again in some time
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				
				// TODO write data into the database
				
			}
			
		}
	}

}
