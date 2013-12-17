/*
 * 
 * 
 */
package data;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import utilities.Utilities;
import main.Application;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * Thread that is run from <code>DatabaseDataWriter</code> object. It reads calculated watch positions queue and writes
 * them into the database one by one. If the queue is empty, It sleeps <code>MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY</code>
 * milliseconds.
 * 
 * @author Danilo
 */
public class DatabaseDataWriterRunnable implements Runnable {

	/** <code>Logger</code> object. */
	private Logger logger;

	/** Flag that denotes if the thread is running. */
	private volatile boolean running = true;

	/** The Constant MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY. */
	private static final int MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY = 10;

	/** The <code>Mongo</code> object. */
	private Mongo mongo;

	/** The database. */
	private DB database;

	/** The sample data. */
	private DBCollection sampleData;

	/**
	 * Instantiates a new <code>DatabaseDataWriterRunnable</code>.
	 */
	public DatabaseDataWriterRunnable() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.running = true;

		try {
			mongo = new Mongo("127.0.0.1");
		} catch (UnknownHostException e) {
			logger.severe("Connecting to mongo database failed. " + e.getMessage());
		}

		database = mongo.getDB("javaTest");
		sampleData = database.getCollection("sampleData");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		BlockingQueue<WatchPositionData> calculatedPositionsQueue = Application.getApplication().getController()
				.getCalculatedPositionsQueue();

		while (running) {

			if (calculatedPositionsQueue.isEmpty()) {
				try {
					Thread.sleep(MILLIS_TO_SLEEP_IF_QUEUE_IS_EMPTY); // try again in some time
				} catch (InterruptedException e) {
					logger.warning("Thread sleeping failed. " + e.getMessage());
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
					logger.severe("Writing to mongo database failed. " + e.getMessage());
				}
			}
		}
	} // end run

	/**
	 * Stops the writing to the database. This call actually terminates the thread. <br>
	 * <br>
	 * 
	 * To restart writing to database, a new instance of <code>DatabaseDataWriter</code> should be created. This is
	 * achieved by calling <code>data.DatabaseDataWriter.writeData()</code> method.
	 * 
	 * @see data.DatabaseDataWriter
	 */
	public void terminate() {
		running = false;
	}

}
