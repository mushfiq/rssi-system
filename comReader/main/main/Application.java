package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;
import data.Controller;

// TODO: Auto-generated Javadoc
/**
 *  This is the starting point of the RSSI reader. It contains information
 *  about receivers, room map and parameters. It is implemented as a Singleton.
 */
public final class Application {

	/** Application singleton instance. */
	private static Application application;
	
	/** The configuration file. */
	private String pathToConfigurationFile;

	/** List of receivers. */
	private ArrayList<Receiver> receivers;  
	
	/** Room map. */
	private RoomMap roomMap;

	/** The controller. */
	private Controller controller;
	
	/**
	 * Private constructor of Singleton class. To instantiate an object
	 * of type Application, static method 'getApplication()' should be called.
	 */
	private Application() {
		
		pathToConfigurationFile = "comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "config.ini";
		readConfigurationFile();
		controller = new Controller();
		initializeGUI();
		
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
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(pathToConfigurationFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// iterating over properties file
		for(String key : properties.stringPropertyNames()) {
			  String value = properties.getProperty(key);
			}
		
		// This initialization will be done from the configuration file
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
	 * Gets the application Singleton object. 
	 * It is also lazy-initialized.
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

