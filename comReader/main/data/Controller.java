package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import main.Application;
import algorithm.PositionLocalizationAlgorithm;
import algorithm.ProbabilityBasedAlgorithm;
import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
public final class Controller {

	/** The data queue. */
	private BlockingQueue<Reading> dataQueue;
	
	/** The batch queue. */
	private BlockingQueue<ArrayList<Reading>> batchQueue;
	
	/** The batch signal queue. */
	private BlockingQueue<HashMap<Integer, HashMap<Integer, Double>>> batchSignalQueue;
	
	private BlockingQueue<WatchPositionData> calculatedPositionsQueue;
	
	private PositionLocalizationAlgorithm algorithm;
	
	/**
	 * Instantiates a new controller.
	 */
	public Controller() {
		// TODO Auto-generated constructor stub
		dataQueue  		 = new LinkedBlockingQueue<Reading>();
		batchQueue 		 = new LinkedBlockingQueue<ArrayList<Reading>>();
		batchSignalQueue = new LinkedBlockingQueue<HashMap<Integer, HashMap<Integer, Double>>>();
//		RoomMap roomMap  = Application.getApplication().getRoomMap();
//		ArrayList<Receiver> receivers = Application.getApplication().getReceivers();
//		algorithm		 = new ProbabilityBasedAlgorithm(roomMap, receivers);
	}
	
	public void test() {
		RoomMap roomMap  = Application.getApplication().getRoomMap();
		ArrayList<Receiver> receivers = Application.getApplication().getReceivers();
		algorithm		 = new ProbabilityBasedAlgorithm(roomMap, receivers);
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
	
	public void addWatchPositionToQueue(WatchPositionData watchPosition){
		
		this.calculatedPositionsQueue.add(watchPosition);
	}

	public PositionLocalizationAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(PositionLocalizationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	
}
