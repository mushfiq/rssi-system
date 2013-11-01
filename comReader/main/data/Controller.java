package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
public final class Controller {

	/** The controller. */
	private static Controller controller;
	
	/** The data queue. */
	private BlockingQueue<Reading> dataQueue;
	
	/** The batch queue. */
	private BlockingQueue<ArrayList<Reading>> batchQueue;
	
	/** The batch signal queue. */
	private BlockingQueue<HashMap<Integer, HashMap<Integer, Integer>>> batchSignalQueue;
	
	/**
	 * Instantiates a new controller.
	 */
	private Controller() {
		// TODO Auto-generated constructor stub
		dataQueue  		 = new LinkedBlockingQueue<Reading>();
		batchQueue 		 = new LinkedBlockingQueue<ArrayList<Reading>>();
		batchSignalQueue = new LinkedBlockingQueue<HashMap<Integer, HashMap<Integer, Integer>>>();
	}
	
	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public static Controller getController() {
		
		if (controller == null) {
			controller = new Controller();
		}
		
		return controller;
	}

	/**
	 * Gets the data queue.
	 *
	 * @return the data queue
	 */
	public BlockingQueue<Reading> getDataQueue() {
		return dataQueue;
	}

	/**
	 * Sets the data queue.
	 *
	 * @param newDataQueue the new data queue
	 */
	public void setDataQueue(BlockingQueue<Reading> newDataQueue) {
		this.dataQueue = newDataQueue;
	}
	
	/**
	 * Adds the reading to queue.
	 *
	 * @param reading the reading
	 * @throws InterruptedException the interrupted exception
	 */
	public void addReadingToQueue(Reading reading) throws InterruptedException {
		
		this.dataQueue.put(reading);
	}

	/**
	 * Gets the batch queue.
	 *
	 * @return the batch queue
	 */
	public BlockingQueue<ArrayList<Reading>> getBatchQueue() {
		return batchQueue;
	}

	/**
	 * Sets the batch queue.
	 *
	 * @param newBatchQueue the new batch queue
	 */
	public void setBatchQueue(BlockingQueue<ArrayList<Reading>> newBatchQueue) {
		this.batchQueue = newBatchQueue;
	}
	
	/**
	 * Adds the batch to queue.
	 *
	 * @param batch the batch
	 */
	public void addBatchToQueue(ArrayList<Reading> batch) {
		this.batchQueue.add(batch);
	}

	/**
	 * Gets the batch signal queue.
	 *
	 * @return the batch signal queue
	 */
	public BlockingQueue<HashMap<Integer, HashMap<Integer, Integer>>> getBatchSignalQueue() {
		return batchSignalQueue;
	}

	/**
	 * Sets the batch signal queue.
	 *
	 * @param batchSignalQueue the batch signal queue
	 */
	public void setBatchSignalQueue(
			BlockingQueue<HashMap<Integer, HashMap<Integer, Integer>>> batchSignalQueue) {
		this.batchSignalQueue = batchSignalQueue;
	}
	
	public void addBatchSignalToQueue(HashMap<Integer, HashMap<Integer, Integer>> batchSignal) {
		this.batchSignalQueue.add(batchSignal);
	}
	
}
