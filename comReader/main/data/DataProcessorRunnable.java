package data;

import java.util.HashMap;
import java.util.Map;

import main.Application;

public class DataProcessorRunnable implements Runnable {

	private static final int TIME_TO_SLEEP_IF_QUEUE_EMPTY = 25;
	
	public DataProcessorRunnable() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void run() {
		
		while (true) {
			
			if (!Application.getApplication().getController().getBatchSignalQueue().isEmpty()) {
				
				HashMap<Integer, HashMap<Integer, Double>> allSignalStrengths = 
						Application.getApplication().getController().getBatchSignalQueue().poll();
				
				for (Map.Entry<Integer, HashMap<Integer, Double>> entry : allSignalStrengths.entrySet()) {
					
					System.out.println(entry.getKey() + ", " + entry.getValue());
					//Application.getApplication().getController().getAlgorithm().calculate(entry.getValue());
					System.out.println("Calculated something: " + Application.getApplication().getController().getAlgorithm().calculate(entry.getValue()) );
					
				}
				
			} else {
				try {
					Thread.sleep(TIME_TO_SLEEP_IF_QUEUE_EMPTY);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
