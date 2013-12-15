/*
 * File: ProbabilityBasedAlgorithm.java
 * Date				Author				Changes
 * 09 Nov 2013		Tommy Griese		create version 1.0
 * 22 Nov 2013		Tommy Griese		added an extended debug information (showing each step of calculation)
 * 27 Nov 2013		Yentran Tran		adding kalman-filter
 * 30 Nov 2013		Tommy Griese		started to implement a more complicated room map weight function
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 * 03 Dec 2013		Tommy Griese		complex (room map) weight function can be applied for elliptical prob map now
 * 										Bug fix in method calculate (method returns null when invalid values)
 * 										Added missing JavaDoc comments
 * 06 Dec 2013		Tommy Griese		Refactoring the grayscaled image part (separate class)
 * 12 Dec 2013		Tommy Griese		Added a method to test if point is located inside the room map or at least on
 * 										its borders. If not, a new point is created so that the point is located on
 * 										the border at least.
 * 14 Dec 2013		Tommy Griese		Adapted code: class reads the needed parameters from the configuration file now
 * 										(Utilities.getConfigurationValue and Utilities.getBooleanConfigurationValue)
 * 										Adapted JavaDoc comments
 */
package algorithm;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.filter.Filter;
import algorithm.filter.KalmanFilterOneDim;
import algorithm.helper.FastConvexHull;
import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import algorithm.images.GrayscaleImages;
import algorithm.probabilityMap.ProbabilityMap;
import algorithm.probabilityMap.ProbabilityMapPathLossCircle;
import algorithm.probabilityMap.ProbabilityMapPathLossElliptic;
import algorithm.weightFunction.WeightFunction;
import algorithm.weightFunction.WeightFunctionExtended;
import algorithm.weightFunction.WeightFunctionSimple;

import components.Receiver;
import components.RoomMap;


/**
 * The class ProbabilityBasedAlgorithm. This class implements a special probability based algorithm for an 
 * indoor localization.
 * 
 * The underlying algorithm is based on the paper "RSSI based Position estimation employing probability Maps" 
 * by S. Knauth, D. Geisler, G. Ruzicka from Faculty Computer Science, Mathematics and Geomatics by University 
 * of Applied Sciences Stuttgart.
 * 
 * @version 1.3 14 Dec 2013
 * @author Tommy Griese, Yentran Tran
 */
public class ProbabilityBasedAlgorithm extends PositionLocalizationAlgorithm {
	
	// --- Start --- variables for grayscale image
	private GrayscaleImages grayscaledimage;
	
	/** The default flag (value = false) to disable the writing of grayscale images (for debugging purpose). */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_DEFAULT = false;
	
	/** The default flag (value = false) to disable the writing of grayscale images. With this flag each step will be showed 
	 * (for debugging purpose). */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_EXTENDED_DEFAULT = false;
	
	/** The default flag (value = true) to enable the debug information of the room map in an image. */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_ROOMMAP_DEFAULT = true;
	
	/** The default flag (value = false) to disable the debug information of the convex hulls in an image. */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_CONVEXHULLS_DEFAULT = false;
	
	/** The default flag (value = true) to enable the debug information of the receivers in an image. */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_RECEIVERS_DEFAULT = true;
	
	/** The default flag (value = true) to enable the debug information of the calculated point in an image. */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_POINT_DEFAULT = true;
	
    /** The current applied status (enable/disable) of writing grayscale images (for debugging purpose). */
	private boolean grayscaleDebugInformation;
	
	/** The current applied status (enable/disable) of writing grayscale images. With this flag each step will be 
	 * showed (for debugging purpose). */
	private boolean grayscaleDebugInformationExtended;
	
	/** The current applied status (enable/disable) of room map information in an grayscale images. */
	private boolean grayscaleDebugInformationRoommap;
	
	/** The current applied status (enable/disable) of convex hull information in an grayscale images. */
	private boolean grayscaleDebugInformationConvexhulls;
	
	/** The current applied status (enable/disable) of receiver information in an grayscale images. */
	private boolean grayscaleDebugInformationReceivers;
	
	/** The current applied status (enable/disable) of the calculated point information in an grayscale images. */
	private boolean grayscaleDebugInformationPoint;
	// --- End --- variables for grayscale image
	
	
	
	// --- Start --- variables for weight function
	/** Each entry in this hash map links a receiver-id {@link components.Receiver#getID()} to a room map weight 
	 * function {@link WeightFunction.RoomMapWeightFunction}. */
	private HashMap<Integer, WeightFunction> weightFunctions;
	
	/** The default weight function that was set (depending on which constructor is used). */
	private WeightFunction weightFunction;
	// --- End --- variables for weight function
	
	// --- Start --- variables for probability map
	/** Each entry in this hash map links a receiver-id {@link components.Receiver#getID()} to its current 
	 * probability map {@link algorithm.helper.PointProbabilityMap}. */
	private HashMap<Integer, ProbabilityMap> probabilityMaps;
	
	/** The default probability map that was set (depending on which constructor is used). */
	private ProbabilityMap probabilityMap;
	// --- End --- variables for probability map
	
	// --- Start --- Filter
	/** A default flag (value = false) to enable/disable a filter. */
	public static final boolean ENABLE_FILTER = false;
	
	/** The current applied status (enable/disable) of the filtering. */
	private boolean enableFilter;
	
	/** The default filter that was set (depending on which constructor is used). */
	private Filter filter;
	// --- End --- Filter
	
	/** The logger. */
    private Logger logger;
    
    // --- Start --- room map
    /** Extended room map for this algorithm. */
    private RoomMap extendedRoomMap;
    
    /** A default parameter (value = 1.0) that extends the room map. */
    private static final double ROOM_MAP_EXTENSION = 1.0;
    
    /** The current applied extension for the room map. */
    private double roommapExtension;
    // --- End --- room map
    
	/**
	 * Instantiates a new probability based algorithm. The algorithm will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. All parameters
	 * can also be set by corresponding methods afterwards. Following parameters will be read from the file:<br>
	 * <br>
	 * probability_based_algorithm.probability_map (default value is {@link ProbabilityMapPathLossCircle})<br>
	 * probability_based_algorithm.filter (default value is {@link KalmanFilterOneDim})<br>
	 * probability_based_algorithm.weight_function (default value is {@link WeightFunctionExtended})<br>
	 * <br>
	 * probability_based_algorithm.grayscale_debug_information_path (default value is 'System.getProperty("java.io.tmpdir")')<br>
	 * probability_based_algorithm.grayscale_debug_information (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_extended (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_EXTENDED_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_roommap (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_ROOMMAP_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_convexhull (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_CONVEXHULLS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_receiver (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_RECEIVERS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_point (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_POINT_DEFAULT})<br>
	 * <br>
	 * probability_based_algorithm.roommap_extension (default value is false {@link ProbabilityBasedAlgorithm#ROOM_MAP_EXTENSION})<br>
	 * probability_based_algorithm.enable_filter (default value is false {@link ProbabilityBasedAlgorithm#ENABLE_FILTER})<br>
	 *
	 * @param roommap defines the room map dimensions (is needed to create a list of weighted room map points)
	 * @param receivers a list of receivers that the algorithm should take into account
	 */
	public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);
		
		this.extendedRoomMap = new RoomMap((roommap.getXFrom() - ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
										   (roommap.getXTo() + ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
									  	   (roommap.getYFrom() - ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
									  	   (roommap.getYTo() + ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
									  	    roommap.getGranularity(), 
									  	    roommap.getImage());
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
		
		readSpecialAlgoParameters();
		
		setUpConstructor();
	}
	
	/**
	 * Instantiates a new probability based algorithm. The 'probability map', 'weight function' and 'filter' 
	 * will be set by the given parameters. These values can also be changed with the corresponding methods
	 * afterwards. The other needed parameters for the algorithm will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. All parameters
	 * can also be set by corresponding methods afterwards. Following parameters will be read from the file:<br>
	 * <br>
	 * probability_based_algorithm.grayscale_debug_information_path (default value is System.getProperty("java.io.tmpdir"))<br>
	 * probability_based_algorithm.grayscale_debug_information (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_extended (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_EXTENDED_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_roommap (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_ROOMMAP_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_convexhull (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_CONVEXHULLS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_receiver (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_RECEIVERS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_point (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_POINT_DEFAULT})<br>
	 * <br>
	 * probability_based_algorithm.roommap_extension (default value is false {@link ProbabilityBasedAlgorithm#ROOM_MAP_EXTENSION})<br>
	 * probability_based_algorithm.enable_filter (default value is false {@link ProbabilityBasedAlgorithm#ENABLE_FILTER})<br>
	 *
	 * @param roommap defines the room map dimensions (is needed to create a list of weighted room map points)
	 * @param receivers a list of receivers that the algorithm shall take into account
	 * @param probabilityMap the probability map that should be used for the calculation
	 * @param weightFunction the weight function that should be used for the calculation
	 * @param filter the filter that should be used for the calculation
	 */
	public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers, 
									 ProbabilityMap probabilityMap, WeightFunction weightFunction, Filter filter) {
		super(roommap, receivers);
		
		this.extendedRoomMap = new RoomMap((roommap.getXFrom() - ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
				   						   (roommap.getXTo() + ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
				   						   (roommap.getYFrom() - ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
				   						   (roommap.getYTo() + ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION), 
				   						    roommap.getGranularity(), 
				   						    roommap.getImage());
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName());
		
        this.probabilityMap = probabilityMap;
		this.weightFunction = weightFunction;
		this.filter = filter;
		
		setUpConstructor();
	}
	
	/**
	 * Help function to setting up the constructor.
	 */
	private void setUpConstructor() {
		readConfigParameters();
		
		// calculate for each receiver (id) its probability map and store it in the hashmap
		this.probabilityMaps = new HashMap<Integer, ProbabilityMap>();
				
		// calculate for each receiver (id) its weight function and store it in the hashmap
		this.weightFunctions = new HashMap<Integer, WeightFunction>(); 
		
		for (int i = 0; i < this.receivers.size(); i++) {
			this.probabilityMaps.put(this.receivers.get(i).getID(), this.probabilityMap);
			this.weightFunctions.put(this.receivers.get(i).getID(), this.weightFunction); 
		}
	}
	
	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * probability_based_algorithm.probability_map (default value is {@link ProbabilityMapPathLossCircle})<br>
	 * probability_based_algorithm.filter (default value is {@link KalmanFilterOneDim})<br>
	 * probability_based_algorithm.weight_function (default value is {@link WeightFunctionExtended})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readSpecialAlgoParameters() {		
		String res = Utilities.getConfigurationValue("probability_based_algorithm.probability_map");
		if(res.equals("probability_map_path_loss_circle")) {
			this.probabilityMap = new ProbabilityMapPathLossCircle();
			
		} else if(res.equals("probability_map_path_loss_elliptic")) {
			this.probabilityMap = new ProbabilityMapPathLossElliptic();
			
		} else {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.probability_map failed, default value was set.");
			this.probabilityMap = new ProbabilityMapPathLossCircle();
		}
		
		res = Utilities.getConfigurationValue("probability_based_algorithm.filter");
		if(res.equals("kalman_filter")) {
			this.filter = new KalmanFilterOneDim();
			
		} else {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.filter failed, default value was set.");
			this.filter = new KalmanFilterOneDim();
		}
		
		res = Utilities.getConfigurationValue("probability_based_algorithm.weight_function");
		if(res.equals("weight_function_simple")) {
			this.weightFunction = new WeightFunctionSimple();
			
		} else if(res.equals("weight_function_extended")) {
			this.weightFunction = new WeightFunctionExtended();
			
		} else {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.weight_function failed, default value was set.");
			this.weightFunction = new WeightFunctionExtended();
		}
	}
	
	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * probability_based_algorithm.grayscale_debug_information_path (default value is System.getProperty("java.io.tmpdir"))<br>
	 * probability_based_algorithm.grayscale_debug_information (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_extended (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_EXTENDED_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_roommap (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_ROOMMAP_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_convexhull (default value is {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_CONVEXHULLS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_receiver (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_RECEIVERS_DEFAULT})<br>
	 * probability_based_algorithm.grayscale_debug_information_point (default value is false {@link ProbabilityBasedAlgorithm#GRAYSCALE_DEBUG_INFORMATION_POINT_DEFAULT})<br>
	 * <br>
	 * probability_based_algorithm.roommap_extension (default value is false {@link ProbabilityBasedAlgorithm#ROOM_MAP_EXTENSION})<br>
	 * probability_based_algorithm.enable_filter (default value is false {@link ProbabilityBasedAlgorithm#ENABLE_FILTER})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readConfigParameters() {		
		String path = System.getProperty("java.io.tmpdir");
		String res = Utilities.getConfigurationValue("probability_based_algorithm.grayscale_debug_information_path");
		if(!res.equals("")) {
			if((new File(res)).exists()) {
				path = res;
			} else {
				this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_path failed, default value was set.");
			}
		}
		this.grayscaledimage = new GrayscaleImages(path);
		
		
		boolean value = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information");
			value = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information failed, default value was set.");
		}
		setGrayscaleDebugInformation(value);
		
		
		
		value = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_EXTENDED_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information_extended");
			value = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_extended failed, default value was set.");
		}
		setGrayscaleDebugInformationExtended(value);
		

		
		boolean valueS1 = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_ROOMMAP_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information_roommap");
			valueS1 = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_roommap failed, default value was set.");
		}
		
		boolean valueS2 = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_CONVEXHULLS_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information_convexhull");
			valueS2 = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_convexhull failed, default value was set.");
		}
		
		boolean valueS3 = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_RECEIVERS_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information_receiver");
			valueS3 = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_receiver failed, default value was set.");
		}
		
		boolean valueS4 = ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_POINT_DEFAULT;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.grayscale_debug_information_point");
			valueS4 = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.grayscale_debug_information_point failed, default value was set.");
		}
		setGrayscaleDebugImageSettings(valueS1, valueS2, valueS3, valueS4);
		
		
		
		double ext = ProbabilityBasedAlgorithm.ROOM_MAP_EXTENSION;
		try {
			res = Utilities.getConfigurationValue("probability_based_algorithm.roommap_extension");
			ext = Double.parseDouble(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.roommap_extension failed, default value was set.");
		}
		setRoomMapExtension(ext);
		
				
		value = ProbabilityBasedAlgorithm.ENABLE_FILTER;
		try {
			res = Utilities.getBooleanConfigurationValue("probability_based_algorithm.enable_filter");
			value = Boolean.parseBoolean(res);
		} catch(Exception e) {
			this.logger.log(Level.WARNING, "Reading probability_based_algorithm.enable_filter failed, default value was set.");
		}
		enableFilter(value);
	}
	
	/**
	 * With this method the probability map for all receiver can be changed.
	 *
	 * @param probabilityMap the new probability map for the receivers
	 */
	public void setProbabilityMap(ProbabilityMap probabilityMap) {
		for (int i = 0; i < this.receivers.size(); i++) {
			setProbabilityMapForOneReceiver(this.receivers.get(i), probabilityMap);
		}
	}
	
	/**
	 * With this method the (room map) weight function for all receiver can be changed.
	 *
	 * @param weightFunction the new (room map) weight function
	 */
	public void setWeightFunction(WeightFunction weightFunction) {
		for (int i = 0; i < this.receivers.size(); i++) {
			setWeightFunctionForOneReceiver(this.receivers.get(i), weightFunction);
		}
	}
	
	/**
	 * With this method a new filter can be used for the calculation. 
	 * 
	 * @param filter the new filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	/**
	 * With this method it is possible to define a special new probability map for one receiver. If the given 
	 * receiver already exists in the configuration it gets the given probability map. If the given receiver 
	 * doesn't exist nothing will happen.
	 *
	 * @param receiver an existing receiver that shall get a new probability map
	 * @param probabilityMap the probability map for the receiver
	 * @return true if the given probability map was successfully changed for the given receiver, false otherwise
	 */
	public boolean setProbabilityMapForOneReceiver(Receiver receiver, ProbabilityMap probabilityMap) {		
		if (getReceiver(this.receivers, receiver.getID()) != null && this.probabilityMaps.containsKey(receiver.getID())) {
			this.probabilityMaps.put(receiver.getID(), probabilityMap);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * With this method it is possible to define a special (room map) weight function for one receiver. If the given 
	 * receiver already exists in the configuration it gets the given weight function. If the given receiver doesn't 
	 * exist nothing will happen.
	 *
	 * @param receiver an existing receiver that shall get a new (room map) weight function
	 * @param weightFunction the (room map) weight function
	 * @return true if the given weight function map was successfully changed for the given receiver, false otherwise
	 */
	public boolean setWeightFunctionForOneReceiver(Receiver receiver, WeightFunction weightFunction) {
		if (getReceiver(this.receivers, receiver.getID()) != null && this.weightFunctions.containsKey(receiver.getID())) {
			this.weightFunctions.put(receiver.getID(), weightFunction);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a new receiver to the configuration. The receiver will be connected with a standard 
	 * 'probability map' and 'weight function' (depending on which constructor was chosen).
	 *
	 * @param receiver the new receiver
	 * @return true if the receiver was added to the configuration, false otherwise
	 */
	public boolean addNewReceiver(Receiver receiver) {
		if (getReceiver(this.receivers, receiver.getID()) == null 
			 && !this.probabilityMaps.containsKey(receiver.getID()) 
			 && !this.weightFunctions.containsKey(receiver.getID())) {
			this.receivers.add(receiver);
			this.probabilityMaps.put(receiver.getID(), this.probabilityMap);
			this.weightFunctions.put(receiver.getID(), this.weightFunction);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a new receiver to the configuration. The receiver will be connected with the given 
	 * 'probability map' and 'weight function'
	 *
	 * @param receiver the new receiver
	 * @param probabilityMap the probability map for the receiver
	 * @param weightFunction the (room map) weight function for the receiver
	 */
	public void addNewReceiver(Receiver receiver, ProbabilityMap probabilityMap, WeightFunction weightFunction) {
		if (getReceiver(this.receivers, receiver.getID()) == null 
			 && !this.probabilityMaps.containsKey(receiver.getID()) 
			 && !this.weightFunctions.containsKey(receiver.getID())) {
			this.receivers.add(receiver);
			this.probabilityMaps.put(receiver.getID(), probabilityMap);
			this.weightFunctions.put(receiver.getID(), weightFunction);
		}	
	}
	
	/**
	 * Sets the room map.
	 *
	 * @param roommap the new room map for the algorithm
	 */
	public void setRoomMap(RoomMap roommap) {
		this.roommap = roommap;
		
		this.extendedRoomMap = new RoomMap((roommap.getXFrom() - this.roommapExtension), 
										   (roommap.getXTo() + this.roommapExtension), 
										   (roommap.getYFrom() - this.roommapExtension), 
										   (roommap.getYTo() + this.roommapExtension), 
										    roommap.getGranularity(), 
										    roommap.getImage());
	}
	
	/**
	 * Enables/Disables the debug information for the grayscale image.
	 *
	 * @param debug true = enable debugging - false = disable debugging
	 */
	public void setGrayscaleDebugInformation(boolean debug) {
		this.grayscaleDebugInformation = debug;
	}
	
	/**
	 * Enables/Disables the debug information for the grayscale image extended.
	 * This means each step will be showed.
	 *
	 * @param debug true = enable debugging extended - false = disable debugging extended
	 */
	public void setGrayscaleDebugInformationExtended(boolean debug) {
		this.grayscaleDebugInformationExtended = debug;
	}
	
	/**
	 * With this method it is possible to enable/disable different settings for the grayscale images.
	 * 
	 * @param grayscaleDebugInformationRoommap true/false = enable/disable the room map information
	 * @param grayscaleDebugInformationConvexhulls true/false = enable/disable the convex hull information
	 * @param grayscaleDebugInformationReceivers true/false = enable/disable the receiver information
	 * @param grayscaleDebugInformationPoint true/false = enable/disable the calculated point information
	 */
	public void setGrayscaleDebugImageSettings(boolean grayscaleDebugInformationRoommap, 
										  boolean grayscaleDebugInformationConvexhulls,
										  boolean grayscaleDebugInformationReceivers,
										  boolean grayscaleDebugInformationPoint) {
		
		this.grayscaleDebugInformationRoommap = grayscaleDebugInformationRoommap;
		this.grayscaleDebugInformationConvexhulls = grayscaleDebugInformationConvexhulls;
		this.grayscaleDebugInformationReceivers = grayscaleDebugInformationReceivers;
		this.grayscaleDebugInformationPoint = grayscaleDebugInformationPoint;
	}
	
	/**
	 * Calculates the position of a point depending of the given readings. The position is located within the 
	 * borders of the room. If the given readings are NOT valid or if the readings is empty null will be returned.
	 * 
	 * @param readings the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	@Override
	public Point calculate(HashMap<Integer, Double> readings) {
		HashMap<Integer, ArrayList<PointProbabilityMap>> convexhulls = new HashMap<Integer, ArrayList<PointProbabilityMap>>();
		int counter = 0;
		
		this.logger.log(Level.INFO, "[ProbabilityBasedAlgorithm - calculate( ... )] Start calculation");
		
		// test if some readings are given
		if (readings.isEmpty()) {
			return null;
		}
				
		this.extendedRoomMap.initialize();

		
		// go through each receiver and calculate a weighted map
		for (Map.Entry<Integer, Double> e : readings.entrySet()) {
			
			// At least one configured receiver for one reading must exist to calculate a position
			// It could happen that we get a signal for a receiver that is not configured...
			ProbabilityMap probMap = this.probabilityMaps.get(e.getKey());
			if (probMap == null) {
				this.logger.log(Level.SEVERE, 
						"[ProbabilityBasedAlgorithm - calculate( ... )] The receiver id couldn't be found in probabilityMaps (HashMap)");
				continue;
			}
			// find the right probability map for the receiver in hashmap
			ArrayList<PointProbabilityMap> pointsProbabilityMap = probMap.getProbabilityMap();
			if (pointsProbabilityMap == null) {
				this.logger.log(Level.SEVERE, 
						"[ProbabilityBasedAlgorithm - calculate( ... )] Empty probability map for receiver " + e.getKey());
				continue;
			}
			
			// find the points in the probability map where the rssi value is above the given value
			ArrayList<PointProbabilityMap> newPointsProbabilityMap = findValuesAboveRssi(pointsProbabilityMap, e.getValue());
			if (newPointsProbabilityMap.size() <= 2) {
				this.logger.log(Level.SEVERE, 
						"[ProbabilityBasedAlgorithm - calculate( ... )] The are less than two values above the rssi in pointsProbabilityMap (ArrayList)");
				continue;
			}
			
			// calculate the convex hull of the probability map	
			FastConvexHull fch = new FastConvexHull();
			ArrayList<PointProbabilityMap> convexHull = fch.computeHull(newPointsProbabilityMap);
	
			// look for the right receiver
			Receiver receiver = getReceiver(this.receivers, e.getKey());
			if (receiver == null) {
				this.logger.log(Level.SEVERE, 
						"[ProbabilityBasedAlgorithm - calculate( ... )] The receiver id couldn't be found in receivers (ArrayList)");
				continue;
			}
			
			// transform the convex hull into the correct position
			ArrayList<PointProbabilityMap> convexHullTransformed = 
					convexHullTransformation(convexHull, receiver.getAngle(), receiver.getXPos(), receiver.getYPos());
			convexhulls.put(e.getKey(), convexHullTransformed);
			
			// Test each point from room map if it lies in the transformed convex hull and weight each point
			weightRoomMap(receiver, convexHullTransformed);
			counter++; // when a weighting of the room map was done, increase the counter
			
			if (this.grayscaleDebugInformationExtended) {
				ArrayList<PointRoomMap> highestPointsRoomMap = giveMaxWeightedValue();
				Point p = getPosition(highestPointsRoomMap);
				this.grayscaledimage.newGrayScaleImage(this.extendedRoomMap, this.grayscaleDebugInformationRoommap,
													   convexhulls, this.grayscaleDebugInformationConvexhulls,
													   receivers, this.grayscaleDebugInformationReceivers,
													   p, this.grayscaleDebugInformationPoint);
			}
		}
		
		if (counter == 0) {
			return null; // no calculation for a point was done
		}
		
		// Find points with the highest weighted value
		ArrayList<PointRoomMap> highestPointsRoomMap = giveMaxWeightedValue();
					
		// calculate point
		Point p = getPosition(highestPointsRoomMap);
		if (this.enableFilter) {
			p = this.filter.applyFilter(p);	
		}
		
		// checks if the point is inside the boundaries, if not the point will be set to the 
		// nearest boundary
		p = checkIfPointIsInRoom(p);
		
		this.logger.log(Level.INFO, 
				"[ProbabilityBasedAlgorithm - calculate( ... )] End calculation, calculated position: [" + p.getX() + ";" + p.getY() + "]");
		
		if (this.grayscaleDebugInformation) {
			this.grayscaledimage.newGrayScaleImage(this.extendedRoomMap, this.grayscaleDebugInformationRoommap,
					   							   convexhulls, this.grayscaleDebugInformationConvexhulls,
					   							   receivers, this.grayscaleDebugInformationReceivers,
					   							   p, this.grayscaleDebugInformationPoint);
		}
		
		if(Double.isNaN(p.x) || Double.isNaN(p.y)) {
			this.logger.log(Level.SEVERE, 
					"[ProbabilityBasedAlgorithm - calculate( ... )] calculated position: [" + p.getX() + ";" + p.getY() + "]");
			return null;
		}
		return p;
	}
	
	/**
	 * Goes through the given probability map and filters all values that are above the given rssi. A new 
	 * list of possible points will be returned then.
	 *
	 * @param probabilityMap the probability map that shall be filtered
	 * @param rssi the rssi value
	 * @return an array list of possible points 
	 */
	private ArrayList<PointProbabilityMap> findValuesAboveRssi(ArrayList<PointProbabilityMap> probabilityMap, double rssi) {
		ArrayList<PointProbabilityMap> pMap = new ArrayList<PointProbabilityMap>();
		for (int i = 0; i < probabilityMap.size(); i++) {
			if (probabilityMap.get(i).getRSSIValue() >= rssi) {
				pMap.add(probabilityMap.get(i));
			}
		}
		return pMap;
	}
		
	/**
	 * Transforms each point of the given array list (rotation and translation) regarding the given parameters. After the
	 * transformation you will obtain a new transformed list of points in an array list. First of all a rotation and after
	 * that the translation will be performed. 
	 *
	 * @param points the points that shall be transformed
	 * @param angle the angle for the transformation (in degree)
	 * @param xPos the x-coordinate for the translation
	 * @param yPos the y-coordinate for the translation
	 * @return the transformed convex hull
	 */
	private ArrayList<PointProbabilityMap> convexHullTransformation(ArrayList<PointProbabilityMap> points, double angle, double xPos, double yPos) {
		double radian = Math.toRadians(angle);
		
		// Define a new List for the transformed coordinates
		ArrayList<PointProbabilityMap> transformationProbMap = new ArrayList<PointProbabilityMap>();
		
		// Rotation of the coordinates
		for (int i = 0; i < points.size(); i++) {
			double xRotation = Math.cos(radian) * points.get(i).getX() - Math.sin(radian) * points.get(i).getY();
			double yRotation = Math.sin(radian) * points.get(i).getX() + Math.cos(radian) * points.get(i).getY();
			transformationProbMap.add(new PointProbabilityMap(xRotation, yRotation, points.get(i).getRSSIValue()));
		}
		
		// Translation of the coordinates
		for (int i = 0; i < points.size(); i++) {
			transformationProbMap.get(i).x = transformationProbMap.get(i).getX() + xPos; 
			transformationProbMap.get(i).y = transformationProbMap.get(i).getY() + yPos;
		}
		return transformationProbMap;
	}
	
	/**
	 * This method weights the room map depending on the current weight function for the given receiver.
	 *
	 * @param receiver the receiver that has to be weighted
	 * @param convexHull the points of the convex hull regarding the given receiver
	 */
	private void weightRoomMap(Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		this.weightFunctions.get(receiver.getID()).weight(this.extendedRoomMap, receiver, convexHull);
	}

	/**
	 * Searches for the highest weighted points in the room map.
	 *
	 * @return a array list with the highest weighted points
	 */
	private ArrayList<PointRoomMap> giveMaxWeightedValue() {
		ArrayList<PointRoomMap> roomMapPoints = this.extendedRoomMap.getRoomMapPoints();
		
		ArrayList<PointRoomMap> maxWeightedValue = new ArrayList<PointRoomMap>();
		double maxValue = roomMapPoints.get(0).getWeightValue();
		maxWeightedValue.add(roomMapPoints.get(0));
		
		for (int i = 1; i < roomMapPoints.size(); i++) {
			if (roomMapPoints.get(i).getWeightValue() > maxValue) {
				maxValue = roomMapPoints.get(i).getWeightValue();
				maxWeightedValue.clear();
				maxWeightedValue.add(roomMapPoints.get(i));
				
			} else if (roomMapPoints.get(i).getWeightValue() == maxValue) {
				maxWeightedValue.add(roomMapPoints.get(i));
			}
		}
		return maxWeightedValue;
	}
	
	/**
	 * Calculates the average point of the suitable points.
	 *
	 * @param roomMap the points of the room map with the highest weighted values
	 * @return the average position as a point
	 */
	private Point getPosition(ArrayList<PointRoomMap> roomMap) {
		double sumX = 0, sumY = 0;
		for (int i = 0; i < roomMap.size(); i++) {
			sumX += roomMap.get(i).getX();
			sumY += roomMap.get(i).getY();
		}
		double x = sumX / roomMap.size();
		double y = sumY / roomMap.size();
		
		return new Point(x, y);
	}
	
	/**
	 * Searches for the receiver with the given id in the receiver array list.
	 *
	 * @param receivers the list of receivers in that the search will be performed
	 * @param id the id of the possible receiver
	 * @return the receiver if found, null otherwise
	 */
	private Receiver getReceiver(ArrayList<Receiver> receivers, int id) {
		for (int i = 0; i < receivers.size(); i++) {
			if (receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Enables/Disables the filter.
	 * 
	 * @param value true/false = enable/disable the filter
	 */
	public void enableFilter(boolean value) {
		this.enableFilter = value;
	}
	
	/**
	 * Sets the location/path of the grayscale images.
	 * 
	 * @param path the path for the images
	 */
	public void setGrayscaleImagePath(String path) {
		this.grayscaledimage.setPath(path);
	}
	
	/**
	 * With this method it is possible to extend the room map with the given parameter.
	 * It is just an imaginary extension for the calculation of the position. The point
	 * still is located inside the room map or at least on its border.
	 * 
	 * @param extension the value to extend the room map with
	 */
	public void setRoomMapExtension(double extension) {
		this.roommapExtension = extension;
	}
	
	/**
	 * This method checks if the point is inside the room boundaries. If not, 
	 * a new point will be calculated (to the nearest border of the room map).
	 * 
	 * @param point the point to be tested
	 * @return a new point
	 */
	public Point checkIfPointIsInRoom(Point point) {
		Point newPoint = null;
		
		// point is located inside the room map
		if(point.x >= this.roommap.getXFrom() && point.x <= this.roommap.getXTo() 
			&& point.y >= this.roommap.getYFrom() && point.y <= this.roommap.getYTo()) {
			newPoint = new Point(point.x, point.y);
			
		// point is located outside the room map
		} else {
			
			double x = point.getX();
			double y = point.getY();
			
			// test if point is located left from the left border of the room
			if (Line2D.relativeCCW(this.roommap.getXFrom(), this.roommap.getYFrom(), 
								   this.roommap.getXFrom(), this.roommap.getYTo(), 
								   point.x, point.y) == -1) {
				x = this.roommap.getXFrom();
			}
			
			// test if point is located above from the upper border of the room
			if(Line2D.relativeCCW(this.roommap.getXFrom(), this.roommap.getYTo(), 
					 					 this.roommap.getXTo(), this.roommap.getYTo(), 
					 					 point.x, point.y) == -1) {
				y = this.roommap.getYTo();
			} 
			
			// test if point is located right from the right border of the room
			if(Line2D.relativeCCW(this.roommap.getXTo(), this.roommap.getYTo(), 
					 					 this.roommap.getXTo(), this.roommap.getYFrom(), 
					 					 point.x, point.y) == -1) {
				x = this.roommap.getXTo();
			} 
			
			// test if point is located beyond from the under border of the room
			if(Line2D.relativeCCW(this.roommap.getXTo(), this.roommap.getYFrom(), 
					 					 this.roommap.getXFrom(), this.roommap.getYFrom(), 
					 					 point.x, point.y) == -1) {
				y = this.roommap.getYFrom();
			}
			newPoint = new Point(x, y);			
		}
		return newPoint;
	}
}
