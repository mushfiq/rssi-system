package main;

import java.io.File;
import java.util.ArrayList;

import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;
import data.Controller;

/**
 * The Class Application.
 */
public final class Application {

	/** The application. */
	private static Application application;
	
	/** The configuration file. */
	private File configurationFile;

	private ArrayList<Receiver> receivers;
	private RoomMap roomMap;

	private Controller controller;
	
	/**
	 * Instantiates a new application.
	 */
	private Application() {
		
		readConfigurationFile();
		initializeGUI();
		controller = new Controller();
		
		 Receiver r1 = new Receiver(1, 0.0, 0.0, 45.0);
         Receiver r2 = new Receiver(2, 25.0, 0.0, 135.0);
         Receiver r3 = new Receiver(3, 25.0, 25.0, 225.0);
         Receiver r4 = new Receiver(4, 0.0, 25.0, 315.0);
         
         receivers = new ArrayList<Receiver>();
         
         receivers.add(r1);
         receivers.add(r2);
         receivers.add(r3);
         receivers.add(r4);

         roomMap = new RoomMap(25, 25);
	}
	
	/**
	 * Initialize gui.
	 */
	private void initializeGUI() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Read configuration file.
	 */
	private void readConfigurationFile() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the application.
	 *
	 * @return the application
	 */
	public static Application getApplication() {
		
		if (application == null) {
			application = new Application();
		}
		
		return application;
	}

	public ArrayList<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(ArrayList<Receiver> receivers) {
		this.receivers = receivers;
	}

	public RoomMap getRoomMap() {
		return roomMap;
	}

	public void setRoomMap(RoomMap roomMap) {
		this.roomMap = roomMap;
	}

	public Controller getController() {
		return controller;
	}
	
	
}
