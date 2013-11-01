package data;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

	private static Controller controller;
	private BlockingQueue<Reading> dataQueue;
	private BlockingQueue<ArrayList<Reading>> batchQueue;
	
	private Controller() {
		// TODO Auto-generated constructor stub
		dataQueue  = new LinkedBlockingQueue<Reading>();
		batchQueue = new LinkedBlockingQueue<ArrayList<Reading>>();
	}
	
	public static Controller getController(){
		
		if(controller == null){
			controller = new Controller();
		}
		
		return controller;
	}

	public BlockingQueue<Reading> getDataQueue() {
		return dataQueue;
	}

	public void setDataQueue(BlockingQueue<Reading> dataQueue) {
		this.dataQueue = dataQueue;
	}
	
	public void addReadingToQueue(Reading reading) throws InterruptedException{
		
		this.dataQueue.put(reading);
	}

	public BlockingQueue<ArrayList<Reading>> getBatchQueue() {
		return batchQueue;
	}

	public void setBatchQueue(BlockingQueue<ArrayList<Reading>> batchQueue) {
		this.batchQueue = batchQueue;
	}
	
	public void addBatchToQueue(ArrayList<Reading> batch){
		this.batchQueue.add(batch);
	}
	
}
