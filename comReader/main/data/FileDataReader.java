package data;

import java.io.File;

public class FileDataReader extends DataReader {

	private File file;
	
	public FileDataReader(File file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	@Override
	public void readData() {
		
		Runnable runnable = new FileReaderRunnable(file);
		Thread thread = new Thread(runnable);
		thread.start();
	}

}
