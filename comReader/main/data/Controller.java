package data;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Controller contains queues for readings obtained from COM port, 
 * for averaged signal strength batches and for calculated watch positions.
 * This class transfers data from queues and to queues.
 * 
 */
public final class Controller {

	/** Readings from COM port. */
	private BlockingQueue<Reading> readings;
	
	/** Batches of signal that have to be passed to the algorithm. */
	private BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> batchSignalQueue;
	
	/** After the algorithm calculates the actual position, it puts the value in this queue. */
	private BlockingQueue<WatchPositionData> calculatedPositionsQueue;
	
	/** The logger. */
	private Logger logger;
	
	/**
	 * Instantiates a new controller.
	 */
	public Controller() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		readings = new LinkedBlockingQueue<Reading>();
        batchSignalQueue = new LinkedBlockingQueue<HashMap<Integer, HashMap<Integer, Double>>>();
        calculatedPositionsQueue = new LinkedBlockingQueue<WatchPositionData>();
	}
	
	
	/**
	 * Gets the data queue.
	 *
	 * @return the data queue - readings from COM port
	 */
	public BlockingQueue<Reading> getDataQueue() {
		return readings;
	}

	/**
	 * Sets the data queue.
	 *
	 * @param newDataQueue the new data queue
	 */
	public void setDataQueue(BlockingQueue<Reading> newDataQueue) {
		this.readings = newDataQueue;
	}
	
	/**
	 * Adds the reading to queue.
	 *
	 * @param reading the reading
	 * @throws InterruptedException the interrupted exception
	 */
	public void addReadingToQueue(Reading reading) throws InterruptedException {
		
		this.readings.put(reading);
	}

	/**
	 * Gets the batch signal queue.
	 *
	 * @return the batch signal queue - contains batches of signal that have to be passed
	 * to the algorithm
	 */
	public BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> getBatchSignalQueue() {
		return batchSignalQueue;
	}

	/**
	 * Sets the batch signal queue.
	 *
	 * @param batchSignalQueue the batch signal queue
	 */
	public void setBatchSignalQueue(
			BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> batchSignalQueue) {
		this.batchSignalQueue = batchSignalQueue;
	}
	
	/**
	 * Adds the batch signal to queue.
	 *
	 * @param batchSignal the batch signal
	 */
	public void addBatchSignalToQueue(HashMap<Integer, HashMap<Integer, Double>> batchSignal) {
		this.batchSignalQueue.add(batchSignal);
	}

	public BlockingQueue<WatchPositionData> getCalculatedPositionsQueue() {
		return calculatedPositionsQueue;
	}

	public void setCalculatedPositionsQueue(
			BlockingQueue<WatchPositionData> calculatedPositionsQueue) {
		this.calculatedPositionsQueue = calculatedPositionsQueue;
	}
	
	/**
	 * Adds the watch position to queue.
	 *
	 * @param watchPosition the watch position
	 */
	public void addWatchPositionToQueue(WatchPositionData watchPosition){
		
		this.calculatedPositionsQueue.add(watchPosition);
	}

	
	
	
}
