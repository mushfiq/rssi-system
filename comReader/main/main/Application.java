package main;

import gui.MainFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.PositionLocalizationAlgorithm;
import algorithm.ProbabilityBasedAlgorithm;

import components.Receiver;
import components.RoomMap;

import dao.HardcodedMapDAO;
import dao.HardcodedReceiverDAO;
import dao.MapDAO;
import dao.ReceiverDAO;
import data.Controller;


/**
 *  This is the starting point of the java application. It contains information
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
	
	/** Main frame of the application. Shown when application is started. */
	private MainFrame mainFrame;

	/** The configuration file. */
	private Properties configurationFile;
	
	private MapDAO mapDAO;
	
	private ReceiverDAO receiverDAO;

	
	/**
	 * Private constructor of Singleton class. To instantiate an object
	 * of type Application, static method 'getApplication()' should be called.
	 */
	private Application() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
        pathToConfigurationFile = "comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "config.ini";
        receiverDAO = new HardcodedReceiverDAO();
        mapDAO 		= new HardcodedMapDAO();
        readConfigurationFile();
        controller = new Controller();
        initializeGUI();
        logger.info("Application started.");
        algorithm = new ProbabilityBasedAlgorithm(roomMap, receivers);
	}

	/**
	 * Initialize gui.
	 */
	private void initializeGUI() {
		
		mainFrame = new MainFrame();
		
	}

	/**
	 * Read configuration file.
	 */
	private void readConfigurationFile() {
		
		configurationFile = new Properties();
		try {
			configurationFile.load(new FileInputStream(pathToConfigurationFile));
		} catch (IOException e) {
			
			logger.log(Level.SEVERE, "Couldn't open configuration file.\n" + e.getMessage());
		}
		
		// iterating over properties file
		for(String key : configurationFile.stringPropertyNames()) {
			  @SuppressWarnings("unused")
			String value = configurationFile.getProperty(key);
			}
		
		// This initialization will be done from the configuration file
//		Receiver r1 = new Receiver(0, 8.0, 8.0, 45.0);
//        Receiver r2 = new Receiver(3, 10.0, 8.0, 135.0);
//        Receiver r3 = new Receiver(6, 10.0, 10.0, 225.0);
//        Receiver r4 = new Receiver(9, 1.5, -1.5, 135.0);
//		Receiver r1 = new Receiver(3, 0.0, 0.0, 0.0);
//		Receiver r2 = new Receiver(0, 1.0, 0.0, 0.0);
//		Receiver r3 = new Receiver(0, 2.0, 0.0, 0.0);
//		Receiver r4 = new Receiver(0, 3.0, 0.0, 0.0);
//		Receiver r5 = new Receiver(0, 4.0, 0.0, 0.0);
//		Receiver r6 = new Receiver(0, 5.0, 0.0, 0.0);
//		Receiver r7 = new Receiver(0, 6.0, 0.0, 0.0);
//		Receiver r8 = new Receiver(0, 7.0, 0.0, 0.0);
//		Receiver r9 = new Receiver(0, 8.0, 0.0, 0.0);

        receivers = new ArrayList<Receiver>();
        
        Receiver r1 = new Receiver(4, 0.0, 0.0, 0.0);
        Receiver r2 = new Receiver(3, 5.0, 0.0, 0.0);
        Receiver r3 = new Receiver(9, 0.0, 10.0, 0.0);
        Receiver r4 = new Receiver(2, 5.0, 10.0, 0.0);
//        receivers.add(r4);
//        receivers.add(r5);
//        receivers.add(r6);
//        receivers.add(r7);
//        receivers.add(r8);
//        receivers.add(r9);
        
//        roomMap = new RoomMap(0.0, 25.0, 0.0, 25.0);
        roomMap = new RoomMap(-1.0, 6.0, -1.0, 11.0, "");
		
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

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	
	/**
	 * Helper method that returns the value from 'config.ini'
	 * file for a given parameter.
	 *
	 * @param parameter
	 * @return value for given parameter
	 */
	public String getConfigurationValue(String key) {
		
		String configurationValue = this.configurationFile.getProperty(key);
		
		return (configurationValue != null) ? configurationValue : "";
	}
	
	public void setConfigurationValue(String key, String value) {
		
		this.configurationFile.setProperty(key, value);
		
	}

	public MapDAO getMapDAO() {
		return mapDAO;
	}

	public ReceiverDAO getReceiverDAO() {
		return receiverDAO;
	}

	
}

