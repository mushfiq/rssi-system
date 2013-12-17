/*
 * 
 * 
 */
package data;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Controller contains queues for <code>Reading</code>s obtained from COM port, for averaged signal strength batches and
 * for calculated watch positions. This class transfers data from queues and to queues.
 * 
 * @author Danilo
 * @see data.Reading
 */
public final class Controller {

	/** Readings from COM port. */
	private BlockingQueue<Reading> readings;

	/** Batches of signal that have to be passed to the algorithm. */
	private BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> batchSignalQueue;

	/** After the algorithm calculates the actual position, it puts the value in this queue. */
	private BlockingQueue<WatchPositionData> calculatedPositionsQueue;

	/** <code>Logger</code> object. */
	@SuppressWarnings("unused")
	private Logger logger;

	/**
	 * Instantiates a new <code>Controller</code>.
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
	 * @return <code>BlockingQueue</code> with <code>Reading</code>s from COM port
	 * @see <code>Reading</code>
	 */
	public BlockingQueue<Reading> getDataQueue() {
		return readings;
	}

	/**
	 * Sets the data queue.
	 * 
	 * @param newDataQueue
	 *            <code>BlockingQueue</code> with <code>Reading</code>s
	 * @see data.Reading
	 */
	public void setDataQueue(BlockingQueue<Reading> newDataQueue) {
		this.readings = newDataQueue;
	}

	/**
	 * Adds the reading to queue.
	 * 
	 * @param reading
	 *            <code>Reading</code> object
	 * @throws InterruptedException
	 *             <code>InterruptedException</code> exception
	 * @see <code>data.Reading</code>
	 */
	public void addReadingToQueue(Reading reading) throws InterruptedException {
		this.readings.put(reading);
	}

	/**
	 * Gets the batch signal queue.
	 * 
	 * @return <code>BlockingQueue</code> that contains batches of signal that have to be passed to the algorithm
	 */
	public BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> getBatchSignalQueue() {
		return batchSignalQueue;
	}

	/**
	 * Sets the batch signal queue.
	 * 
	 * @param batchSignalQueue
	 *           <code>BlockingQueue</code> instance - the batch signal queue
	 */
	public void setBatchSignalQueue(BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> batchSignalQueue) {
		this.batchSignalQueue = batchSignalQueue;
	}

	/**
	 * Adds the batch signal to queue.
	 * 
	 * @param batchSignal
	 *            the batch signal
	 */
	public void addBatchSignalToQueue(HashMap<Integer, HashMap<Integer, Double>> batchSignal) {
		this.batchSignalQueue.add(batchSignal);
	}

	/**
	 * Gets the calculated positions queue.
	 *
	 * @return <code>BlockingQueue</code> object
	 */
	public BlockingQueue<WatchPositionData> getCalculatedPositionsQueue() {
		return calculatedPositionsQueue;
	}

	/**
	 * Sets the calculated positions queue.
	 *
	 * @param calculatedPositionsQueue New <code>BlockingQueue</code> with calculated positions
	 * @see data.WatchPositionData
	 */
	public void setCalculatedPositionsQueue(BlockingQueue<WatchPositionData> calculatedPositionsQueue) {
		this.calculatedPositionsQueue = calculatedPositionsQueue;
	}

	/**
	 * Adds the watch position to queue.
	 * 
	 * @param watchPosition
	 *            <code>WatchPositionData</code> object
	 */
	public void addWatchPositionToQueue(WatchPositionData watchPosition) {

		this.calculatedPositionsQueue.add(watchPosition);
	}

}
