package data;

public abstract class DataReader {

	private Controller controller;
	
	public DataReader() {
		// TODO Auto-generated constructor stub
		controller = Controller.getController();
	}

	public abstract void readData();
}
