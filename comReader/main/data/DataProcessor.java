package data;

import java.util.HashMap;

public class DataProcessor {

	public DataProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void processData() {
		
		HashMap<Integer, HashMap<Integer, Integer>> data;
		
		DataProcessorRunnable runnable = new DataProcessorRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
