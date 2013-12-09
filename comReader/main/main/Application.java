package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import algorithm.PositionLocalizationAlgorithm;
import algorithm.ProbabilityBasedAlgorithm;

import components.Receiver;
import components.RoomMap;

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

	/** The controller. */
	private Controller controller;
	
	/** The room map. */
	private RoomMap roomMap;
	
	/** The algorithm. */
	private PositionLocalizationAlgorithm algorithm;
	
	/** The logger. */
	private Logger logger;
	
	/**
	 * Private constructor of Singleton class. To instantiate an object
	 * of type Application, static method 'getApplication()' should be called.
	 */
	private Application() {
		
		initializeLogger();
        pathToConfigurationFile = "comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "config.ini";
        readConfigurationFile();
        controller = new Controller();
        initializeGUI();
        algorithm = new ProbabilityBasedAlgorithm(roomMap, receivers);
	}
	
	/**
	 * Initialize logger.
	 */
	private void initializeLogger() {
	
		logger = Logger.getLogger(Application.class);
//		logger = Logger.getLogger(Thread.currentThread().getStackTrace()[2].getClass().getCanonicalName());
		
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d %p [%c] - %m%n");
	      FileAppender appender = null;
		try {
			appender = new FileAppender(layout,"comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "log.log", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	      logger.addAppender(appender);
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
			  @SuppressWarnings("unused")
			String value = properties.getProperty(key);
			}
		
		// This initialization will be done from the configuration file
//		Receiver r1 = new Receiver(3, 0.0, 0.0, 0.0);
//		Receiver r1 = new Receiver(0, 8.0, 8.0, 45.0);
//        Receiver r2 = new Receiver(3, 10.0, 8.0, 135.0);
//        Receiver r3 = new Receiver(6, 10.0, 10.0, 225.0);
//		Receiver r1 = new Receiver(0, -1.5, -1.5, 45.0);
//        Receiver r2 = new Receiver(3, -1.5, 1.5, 315.0);
//        Receiver r3 = new Receiver(6, 1.5, 1.5, 225.0);
//        Receiver r4 = new Receiver(9, 1.5, -1.5, 135.0);
		Receiver r1 = new Receiver(4, 0.0, 0.0, 0.0);
		Receiver r2 = new Receiver(3, 5.0, 0.0, 0.0);
		Receiver r3 = new Receiver(9, 0.0, 10.0, 0.0);
		Receiver r4 = new Receiver(2, 5.0, 10.0, 0.0);
//		Receiver r5 = new Receiver(9, 10.0, 5.0, 0.0);
//		Receiver r6 = new Receiver(4, 0.0, 5.0, 0.0);
//		Receiver r7 = new Receiver(0, 6.0, 0.0, 0.0);
//		Receiver r8 = new Receiver(0, 7.0, 0.0, 0.0);
//		Receiver r9 = new Receiver(0, 8.0, 0.0, 0.0);

        receivers = new ArrayList<Receiver>();
        
        receivers.add(r1);
        receivers.add(r2);
        receivers.add(r3);
        receivers.add(r4);
//        receivers.add(r5);
//        receivers.add(r6);
//        receivers.add(r7);
//        receivers.add(r8);
//        receivers.add(r9);
        
        roomMap = new RoomMap(-1.0, 6.0, -1.0, 11.0, "");
//        roomMap = new RoomMap(-10.0, 10.0, -10.0, 10.0);
//        roomMap = new RoomMap(-1.5, 1.5, -1.5, 1.5);
		
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

	public Controller getController() {
		return controller;
	}

	public RoomMap getRoomMap() {
		return roomMap;
	}

	public void setRoomMap(RoomMap roomMap) {
		this.roomMap = roomMap;
	}
	
	public PositionLocalizationAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(PositionLocalizationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Logger getLogger() {
		
		if(logger == null) {
			initializeLogger();
		}
		
		return logger;
	}
	
	
}

