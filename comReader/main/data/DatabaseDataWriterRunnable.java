/*
 * 
 * 
 */
package data;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;

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
	
	private static final String DATABASE_ADDRESS = "database_address";
	
	private static final String DEFAULT_DATABASE_ADDRESS = "127.0.0.1";
	
	
	/**
	 * Instantiates a new <code>DatabaseDataWriterRunnable</code>.
	 */
	public DatabaseDataWriterRunnable() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		this.running = true;

		String databaseAddress = DEFAULT_DATABASE_ADDRESS;
		
		try { // read parameters from the configuration file
			databaseAddress = Utilities.getConfigurationValue(DATABASE_ADDRESS);
			
		} catch (NumberFormatException exception) { // reading has failed, use default values
			logger.info("Reading parameters from configuration file failed. "
					+ "Using default values for database_address instead.");
		}
		
		try {
			mongo = new Mongo(databaseAddress);
		} catch (UnknownHostException e1) {
			logger.severe("Connecting to database failed. Please check your internet connection and database parameters." + e1.getMessage());
		}
		
		database = mongo.getDB("rssiSystem");
		sampleData = database.getCollection("watch_records");
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
				try {
					DBObject documentDetail = new BasicDBObject();

					documentDetail.put("_cls", "watchRecords"); // for mongoEngine ORM users

					documentDetail.put("x", watchPositionData.getPosition().getX());
					documentDetail.put("y", watchPositionData.getPosition().getY());

					long time = watchPositionData.getTime();
					SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					String strDate = simpledateformat.format(new Date(time));
					documentDetail.put("insertedAt", strDate);

					documentDetail.put("mapId", watchPositionData.getMapId());
					documentDetail.put("watchId", Integer.toString(watchPositionData.getWatchId()));

					sampleData.insert(documentDetail);

				} catch (Exception e) {
					logger.warning("Couldn't send watch position data to the Mongo database");;
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