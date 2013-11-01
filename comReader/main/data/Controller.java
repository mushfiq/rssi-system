package data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

	private static Controller controller;

	private Controller() {
		// TODO Auto-generated constructor stub
		dataQueue = new LinkedBlockingQueue<Reading>();
	}
	
	public static Controller getController(){
		
		if(controller == null){
			controller = new Controller();
		}
		
		return controller;
	}
	
	private BlockingQueue<Reading> dataQueue;

	public BlockingQueue<Reading> getDataQueue() {
		return dataQueue;
	}

	public void setDataQueue(BlockingQueue<Reading> dataQueue) {
		this.dataQueue = dataQueue;
	}
	
}
