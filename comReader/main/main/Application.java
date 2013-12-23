package main;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.PositionLocalizationAlgorithm;
import components.Receiver;
import components.RoomMap;
import dao.HardcodedMapDAO;
import dao.HardcodedReceiverDAO;
import dao.MapDAO;
import dao.ReceiverDAO;
import data.COMPortDataReader;
import data.Controller;
import data.DataReader;
import data.DataWriter;
import data.DatabaseDataWriter;


/**
 *  This is the starting point of the java application. It contains information
 *  about receivers, room map and parameters. It is implemented as a Singleton.
 */
public final class Application {

	/** Application singleton instance. */
	private static Application application;
	
	

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
	
	private MapDAO mapDAO;
	
	private ReceiverDAO receiverDAO;

	private DataReader dataReader;
	private DataWriter dataWriter;
	
	/**
	 * Private constructor of Singleton class. To instantiate an object
	 * of type Application, static method 'getApplication()' should be called.
	 */
	private Application() {
		
		receiverDAO = new HardcodedReceiverDAO();
		mapDAO = new HardcodedMapDAO();
		//readConfigurationFile();
		controller = new Controller();
		logger.info("Application started.");
		//algorithm = new ProbabilityBasedAlgorithm(roomMap, receivers);
		dataReader = new COMPortDataReader();
		dataWriter = new DatabaseDataWriter();
	}

	/**
	 * Initialize gui.
	 */
	public void initializeGUI() {
		
		mainFrame = new MainFrame();
		
	}

	/**
	 * Read configuration file.
	 */
	private void readConfigurationFile() {
		
	   
		
		// This initialization will be done from the configuration file
//		Receiver r1 = new Receiver(0, 8.0, 8.0, 45.0);
//        Receiver r2 = new Receiver(3, 10.0, 8.0, 135.0);
//        Receiver r3 = new Receiver(6, 10.0, 10.0, 225.0);
//        Receiver r4 = new Receiver(9, 1.5, -1.5, 135.0);
		
		Receiver r1 = new Receiver(0, 0.0, 0.0, 0.0);
		Receiver r2 = new Receiver(1, 5.0, 0.0, 0.0);
		Receiver r3 = new Receiver(2, 0.0, 5.0, 0.0);
		Receiver r4 = new Receiver(3, 5.0, 5.0, 0.0);
//		Receiver r5 = new Receiver(4, 2.0, 6.0, 0.0);
//		Receiver r6 = new Receiver(6, 6.0, 0.0, 0.0);
//		Receiver r7 = new Receiver(9, 3.70, 2.0, 0.0);
		
//		Receiver r1 = new Receiver(0, 6.0, 2.55, 0.0);
//        Receiver r2 = new Receiver(1, 0.0, 2.55, 0.0);
//        Receiver r3 = new Receiver(2, 0.0, 6.0, 0.0);
//        Receiver r4 = new Receiver(3, 6.0, 6.0, 0.0);
//		Receiver r5 = new Receiver(4, 2.0, 6.0, 0.0);
//		Receiver r6 = new Receiver(6, 6.0, 0.0, 0.0);
//		Receiver r7 = new Receiver(9, 3.70, 2.0, 0.0);

        receivers = new ArrayList<Receiver>();
        
        receivers.add(r1);
        receivers.add(r2);
        receivers.add(r3);
        receivers.add(r4);
//        receivers.add(r5);
//        receivers.add(r6);
//        receivers.add(r7);
        
//        roomMap = new RoomMap(0.0, 25.0, 0.0, 25.0);

        roomMap = new RoomMap(0.0, 5.0, 0.0, 5.0, null);

		
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
		
		// XXX this check should be removed in final version
		if (this.algorithm == null) {
			return;
		}
		
		if (algorithm.getClass() != this.algorithm.getClass()) { // in order to avoid possibly expensive instantiation
			this.algorithm = algorithm;
		}
	
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	
	

	public MapDAO getMapDAO() {
		return mapDAO;
	}

	public ReceiverDAO getReceiverDAO() {
		return receiverDAO;
	}
	
	public void startReadingsAndWritings() {
		
		// start readings from source (COM port or file)
		dataReader.readData();
		// start writing to destination (database or console or file)
		dataWriter.writeData();
	}
	
	public void stopReadingsAndWritings() {
		
		// stop readings from COM port
		dataReader.stopReading();
		// stop writing to destination (database or console or file)
		dataWriter.stopWriting();
	}

	
}

