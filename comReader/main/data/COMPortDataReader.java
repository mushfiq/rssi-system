package data;


public class COMPortDataReader extends DataReader {

	public COMPortDataReader() {
		super();
	}

	@Override
	public void readData() {
		
		Thread thread = new Thread(new ComPortDataReaderRunnable());
		thread.start();
	}
      
}
		

	

