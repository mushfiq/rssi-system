package data;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import main.Application;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 *  Thread that is run from DatabaseDataWriter object. It reads 
 *  calculated watch positions queue and writes them into the database one
 *  by one. If the queue is empty, It sleeps MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY milliseconds.  
 */
public class DatabaseDataWriterRunnable implements Runnable {

	private Logger logger;
	
	private volatile boolean running = true;
	
	/** The Constant MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY. */
	private static final int MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY = 10;
	
	/** The mongo. */
	private Mongo mongo;
	
	/** The database. */
	private DB db;
	
	/** The sample data. */
	private DBCollection sampleData;
	
	/**
	 * Instantiates a new database data writer runnable.
	 */
	public DatabaseDataWriterRunnable() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		this.running = true;
		try {
			mongo = new Mongo("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        db = mongo.getDB("javaTest");
        sampleData = db.getCollection("sampleData");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		BlockingQueue<WatchPositionData> calculatedPositionsQueue = Application.getApplication().getController().getCalculatedPositionsQueue();
		
		while (running) { 
			
			if(calculatedPositionsQueue.isEmpty()) { 
				
				try {
					Thread.sleep(MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY); // try again in some time
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else { // queue is not empty, take WatchPositionData object and write it into the database
				
				// we take watchPositionData object from the queue by calling method 'poll()' on the queue
				WatchPositionData watchPositionData = calculatedPositionsQueue.poll();
				
				System.out.println(watchPositionData);
				
			    try {
		        	DBObject documentDetail = new BasicDBObject();
		        	documentDetail.put("x", watchPositionData.getPosition().getX());
		        	documentDetail.put("y", watchPositionData.getPosition().getY());
		        	documentDetail.put("insertedAt", watchPositionData.getTime());
		        	documentDetail.put("mapId", 1);
		        	documentDetail.put("watchId", Integer.toString(watchPositionData.getWatchId()));
		        	sampleData.insert(documentDetail);

		        } catch (Exception e) {
		        	System.err.print(e);
		        }	
			}
		}
	} // end run
	
	public void terminate() {
        running = false;
        logger.log(Level.INFO, "ComPortDataReaderRunnable has been terminated.");
    }

}
