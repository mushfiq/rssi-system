package data;

public class DataProcessor {

	public DataProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void processData() {
		
		DataProcessorRunnable runnable = new DataProcessorRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
