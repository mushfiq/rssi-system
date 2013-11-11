package data;

public class DatabaseDataWriter implements DataWriter{

	public DatabaseDataWriter() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void writeData() {
		
		DatabaseDataWriterRunnable runnable = new DatabaseDataWriterRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	

}
