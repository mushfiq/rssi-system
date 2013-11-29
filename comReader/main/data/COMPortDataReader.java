package data;


public class COMPortDataReader extends DataReader {

	
	private ComPortDataReaderRunnable runnable;
	
	public COMPortDataReader() {
		super();
	}

	@Override
	public void readData() {
		
		ComPortDataReaderRunnable runnable = new ComPortDataReaderRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void stopReading() {
		runnable.terminate();
	}
      
}
		

	

