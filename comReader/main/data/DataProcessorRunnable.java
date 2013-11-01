package data;

import java.util.HashMap;

public class DataProcessorRunnable implements Runnable {

	public DataProcessorRunnable() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void run() {
		
		while (true) {
			
			if (!Controller.getController().getBatchSignalQueue().isEmpty()) {
				
				
			}
		}

	}

}
