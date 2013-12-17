/*
 * 
 * 
 */
package main;

import gui.MainFrame;

import java.util.ArrayList;
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
 * This is the starting point of the java application. It contains references to: <br>
 * <br>
 * <ul>
 * <li><code>MainFrame</code> that represents graphical user interface for the application.</li>
 * <li><code>Controller</code> that handles flow of batches of <code>Reading</code>s from one queue to another.</li>
 * <li><code>MapDAO</code> that handles reading and writing of <code>RoomMap</code>s to and from data source.</li>
 * <li><code>ReceiverDAO</code> that handles reading and writing of <code>Receiver</code>s to and from data source.</li>
 * <li><code>PositionLocalizationAlgorithm</code> that calculates <code>WatchPositionData</code>.</li>
 * </ul>
 * It is implemented as a Singleton.
 * 
 * @author Danilo
 * @see MainFrame
 * @see Controller
 * @see MapDAO
 * @see ReceiverDAO
 * @see PositionLocalizationAlgorithm
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

	/** The map dao. */
	private MapDAO mapDAO;

	/** The receiver dao. */
	private ReceiverDAO receiverDAO;

	/**
	 *  Private constructor of Singleton class. To instantiate an object of type <code>Application</code>, static method
	 * <code>getApplication</code> should be called.
	 * 
	 * @see #getApplication()
	 */
	private Application() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		receiverDAO = new HardcodedReceiverDAO();
		mapDAO = new HardcodedMapDAO();
		readConfigurationFile();
		controller = new Controller();
		logger.info("Application started.");
		algorithm = new ProbabilityBasedAlgorithm(roomMap, receivers);
	}

	/**
	 * Initializes graphical user interface.
	 */
	public void initializeGUI() {
		mainFrame = new MainFrame();
	}

	/**
	 * Reads configuration file.
	 */
	private void readConfigurationFile() {

		// This initialization will be done from the configuration file
		// Receiver r1 = new Receiver(0, 8.0, 8.0, 45.0);
		// Receiver r2 = new Receiver(3, 10.0, 8.0, 135.0);
		// Receiver r3 = new Receiver(6, 10.0, 10.0, 225.0);
		// Receiver r4 = new Receiver(9, 1.5, -1.5, 135.0);
		// Receiver r1 = new Receiver(3, 0.0, 0.0, 0.0);
		// Receiver r2 = new Receiver(0, 1.0, 0.0, 0.0);
		// Receiver r3 = new Receiver(0, 2.0, 0.0, 0.0);
		// Receiver r4 = new Receiver(0, 3.0, 0.0, 0.0);
		// Receiver r5 = new Receiver(0, 4.0, 0.0, 0.0);
		// Receiver r6 = new Receiver(0, 5.0, 0.0, 0.0);
		// Receiver r7 = new Receiver(0, 6.0, 0.0, 0.0);
		// Receiver r8 = new Receiver(0, 7.0, 0.0, 0.0);
		// Receiver r9 = new Receiver(0, 8.0, 0.0, 0.0);

		receivers = new ArrayList<Receiver>();

		Receiver r1 = new Receiver(4, 0.0, 0.0, 0.0);
		Receiver r2 = new Receiver(3, 5.0, 0.0, 0.0);
		Receiver r3 = new Receiver(9, 0.0, 10.0, 0.0);
		Receiver r4 = new Receiver(2, 5.0, 10.0, 0.0);
		// receivers.add(r4);
		// receivers.add(r5);
		// receivers.add(r6);
		// receivers.add(r7);
		// receivers.add(r8);
		// receivers.add(r9);

		// roomMap = new RoomMap(0.0, 25.0, 0.0, 25.0);

		roomMap = new RoomMap(-1.0, 6.0, -1.0, 11.0, null);

	}

	/**
	 * Gets the application Singleton object. It is also lazy-initialized.
	 * 
	 * @return the application
	 */
	public static Application getApplication() {

		if (application == null) {
			application = new Application();
		}

		return application;
	}

	/**
	 * Gets the list of receivers.
	 *
	 * @return <code>List</code> of receivers
	 */
	public ArrayList<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * Sets the list of receivers.
	 *
	 * @param receivers <code>List</code> of <code>Receiver</code>s
	 */
	public void setReceivers(ArrayList<Receiver> receivers) {
		this.receivers = receivers;
	}

	/**
	 * Gets the controller.
	 *
	 * @return <code>Controller</code> object
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Gets the room map.
	 *
	 * @return <code>RoomMap</code> object
	 */
	public RoomMap getRoomMap() {
		return roomMap;
	}

	/**
	 * Sets the room map.
	 *
	 * @param roomMap <code>RoomMap</code> object
	 */
	public void setRoomMap(RoomMap roomMap) {
		this.roomMap = roomMap;
	}

	/**
	 * Gets the algorithm.
	 *
	 * @return <code>PositionLocalizationAlgorithm</code>
	 */
	public PositionLocalizationAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm <code>PositionLocalizationAlgorithm</code>
	 */
	public void setAlgorithm(PositionLocalizationAlgorithm algorithm) {

		// XXX this check should be removed in final version
		if (algorithm == null) {
			return;
		}

		if (algorithm.getClass() != this.algorithm.getClass()) { // in order to avoid possibly expensive instantiation
			this.algorithm = algorithm;
		}

	}

	/**
	 * Gets the main frame of the application.
	 *
	 * @return <code>MainFrame</code> of the application
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * Gets the map dao.
	 *
	 * @return <code>MapDao</code>
	 */
	public MapDAO getMapDAO() {
		return mapDAO;
	}

	/**
	 * Gets the receiver dao.
	 *
	 * @return <code>ReceiverDAO</code>
	 */
	public ReceiverDAO getReceiverDAO() {
		return receiverDAO;
	}

}
