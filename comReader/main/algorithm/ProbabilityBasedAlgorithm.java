/*
 * File: ProbabilityBasedAlgorithm.java
 * Date				Author				Changes
 * 09 Nov 2013		Tommy Griese		create version 1.0
 * 22 Nov 2013		Tommy Griese		added an extended debug information (showing each step of calculation)
 * ?? Nov 2013		Yentran Tran		adding kalman-filter
 * 30 Nov 2013		Tommy Griese		started to implement a more complicated room map weight function
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 * 03 Dec 2013		Tommy Griese		complex (room map) weight function can be applied for elliptical prob map now
 * 03 Dec 2013		Tommy Griese		Bug fix in method calculate (method returns null when invalid values)
 * 03 Dec 2013		Tommy Griese		Added missing JavaDoc comments
 */
package algorithm;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import main.Application;

import org.apache.log4j.Level;

import Jama.Matrix;
import algorithm.helper.FastConvexHull;
import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import algorithm.probabilityMap.ProbabilityMap;
import algorithm.probabilityMap.ProbabilityMapPathLossCircle;
import algorithm.weightFunction.WeightFunction;
import algorithm.weightFunction.WeightFunctionSimple;

import components.Receiver;
import components.RoomMap;

/**
 * The class ProbabilityBasedAlgorithm. This class implements a special probability based algorithm for an indoor localization.
 * 
 * The underlying algorithm is based on the paper "RSSI based Position estimation employing probability Maps" by S. Knauth,
 * D. Geisler, G. Ruzicka from Faculty Computer Science, Mathematics and Geomatics by University of Applied Sciences Stuttgart.
 * 
 * @version 1.1 01 Dec 2013
 * @author Tommy Griese, Yentran Tran
 */
public class ProbabilityBasedAlgorithm extends PositionLocalizationAlgorithm {

	// --- Start --- variables for grayscale image
    /** Counts each gray-scaled picture, so that a new picture becomes a new number. */
	private int grayscaleImagePicCounter;

	/** The path for the grayscale image. */
	private String grayscaleImagePath;
	
    /** A flag to enable/disable the writing of grayscale images (for debugging purpose). */
	private boolean grayscaleDebugInformation;
	
	/** A flag to enable/disable the writing of grayscale images (for debugging purpose). With this flag each step will be showed. */
	private boolean grayscaleDebugInformationExtended;
	
	/** A flag to enable/disable the writing of convex hull images (for debugging purpose). */
	private boolean convexhullDebugInformation;
	
	/** A default flag to enable/disable the writing of grayscale images (for debugging purpose). */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_DEFAULT = true;
	
	/** A default flag to enable/disable the writing of grayscale images (for debugging purpose). With this flag each step will be showed. */
	public static final boolean GRAYSCALE_DEBUG_INFORMATION_DEFAULT_EXTENDED = false;
	
	/** A default flag to enable/disable the writing of convex hull images (for debugging purpose). */
	public static final boolean CONVEXHULL_DEBUG_INFORMATION = false;
	
	/** Defines the start intensity of the grayscale image. */
	public static final double GRAYSCALE_IMAGE_START = 75.0;
	
	/** Defines the end intensity of the grayscale image. */
	public static final double GRAYSCALE_IMAGE_END = 225.0;
	
	/** The color black. */
	public static final int GRAYSCALE_IMAGE_BLACK = 0;
	
	/** The color white. */
	public static final int GRAYSCALE_IMAGE_WHITE = 255;
	
	/** The scale factor in x to increase the complete image. */
	public static final int GRAYSCALE_IMAGE_SCALE_X = 5;
	
	/** The scale factor in y to increase the complete image. */
	public static final int GRAYSCALE_IMAGE_SCALE_Y = 5;
	// --- End --- variables for grayscale image
	
	// --- Start --- variables for weight function
	/** Each entry in this hash map links a receiver-id {@link components.Receiver#getID()} to a room map weight function {@link WeightFunction.RoomMapWeightFunction}. */
	private HashMap<Integer, WeightFunction> weightFunctions;
	
	private WeightFunction weightFunction;
	// --- End --- variables for weight function
	
	// --- Start --- variables for probability map
	/** Each entry in this hash map links a receiver-id {@link components.Receiver#getID()} to its current probability map {@link algorithm.helper.PointProbabilityMap}. */
	private HashMap<Integer, ProbabilityMap> probabilityMaps;
	
	/** Created standard probability map. */
	private ProbabilityMap probabilityMap;
	// --- End --- variables for probability map
	
	
	
	//TODO: YENNI, make it checkstyle conform and write comments
	// --- Start --- Kalmanfilter
	public static final boolean ENABLE_KALMANFILTER = false;
	private boolean enableKalmanfilter;
	
	private KalmanFilter kf;
	private double dt = 5.0;
	private double processNoiseStdev = 0.5;
	private double measurementNoiseStdev = 1000.0;
	// --- End --- Kalmanfilter
	
	/**
	 * Instantiates a new probability based algorithm. As default the ProbabilityMapPathLossCircle will be 
	 * initialized as ProbabilityMap as well as the WeightFunctionSimple. These values can be 
	 * changed with the corresponding methods.
	 *
	 * @param roommap defines the room map dimensions (is needed to create a list of weighted room map points)
	 * @param receivers a list of receivers that the algorithm shall take into account
	 */
	public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);
		
		this.probabilityMap = new ProbabilityMapPathLossCircle();
		this.weightFunction = new WeightFunctionSimple();
		
		setUpConstructor();
	}
	
	/**
	 * Instantiates a new probability based algorithm. The default probability map and weight function will be set 
	 * by the given parameter. These values can be changed with the corresponding methods later.
	 *
	 * @param roommap defines the room map dimensions (is needed to create a list of weighted room map points)
	 * @param receivers a list of receivers that the algorithm shall take into account
	 * @param signalPropagationConstant the signal propagation constant for calculation of the probability map 
	 * @param signalStrengthOneMeter the signal strength one meter for calculating the probability map
	 * @param xFrom the start value for the probability map in x, needed for its calculation
	 * @param xTo the end value for the probability map in x, needed for calculation
	 * @param yFrom the start value for the probability map in y, needed for its calculation
	 * @param yTo the end value for the probability map in y, needed for its calculation
	 * @param granularity the granularity for the probability map, needed for its calculation
	 */
	public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers, 
									 ProbabilityMap probabilityMap, WeightFunction weightFunction) {
		super(roommap, receivers);
		
		this.probabilityMap = probabilityMap;
		this.weightFunction = weightFunction;
		
		setUpConstructor();
	}
	
	/**
	 * Help function to sets the up the constructor.
	 */
	private void setUpConstructor() {
		this.grayscaleImagePicCounter = 0;
		setGrayscaleDebugInformation(ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_DEFAULT);
		setGrayscaleDebugInformationExtended(ProbabilityBasedAlgorithm.GRAYSCALE_DEBUG_INFORMATION_DEFAULT_EXTENDED);
		setConvexhullDebugInformation(ProbabilityBasedAlgorithm.CONVEXHULL_DEBUG_INFORMATION);
		
//		setGrayscaleImagePath(System.getProperty("java.io.tmpdir"));
		setGrayscaleImagePath("Z:\\Studium\\Master\\23_workspace\\workspace_SP\\rssi-system-comReader-tommy\\img");
		
		enableKalmanfilter(ProbabilityBasedAlgorithm.ENABLE_KALMANFILTER);
		
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
	 * @param value the new room map weight function
	 */
	public void setWeightFunction(WeightFunction weightFunction) {
		for (int i = 0; i < this.receivers.size(); i++) {
			setWeightFunctionForOneReceiver(this.receivers.get(i), weightFunction);
		}
	}
	
	/**
	 * With this method it is possible to define a special new probability map for one receiver. If the given receiver in the parameter
	 * list already exists, it will get the given calculated probability map. If the given receiver doesn't exist nothing will happen.
	 *
	 * @param receiver an existing receiver that shall get a new probability map
	 * @param probabilityMap the probability map for the receiver
	 */
	public void setProbabilityMapForOneReceiver(Receiver receiver, ProbabilityMap probabilityMap) {		
		if (getReceiver(this.receivers, receiver.getID()) != null && this.probabilityMaps.containsKey(receiver.getID())) {
			this.probabilityMaps.put(receiver.getID(), probabilityMap);
		}	
	}
	
	/**
	 * With this method it is possible to define a special (room map) weight function for one receiver. If the given receiver in the parameter
	 * list already exists, it will get the given calculated probability map. If the given receiver doesn't exist nothing will happen.
	 *
	 * @param receiver an existing receiver that shall get a new (room map) weight function
	 * @param value the (room map) weight function
	 */
	public void setWeightFunctionForOneReceiver(Receiver receiver, WeightFunction weightFunction) {
		if (getReceiver(this.receivers, receiver.getID()) != null && this.weightFunctions.containsKey(receiver.getID())) {
			this.weightFunctions.put(receiver.getID(), weightFunction); 
		}
	}
	
	/**
	 * Adds a new receiver to the configuration. The receiver will get a default probability map and a
	 * RoomMapWeightFunction or based on the parameters the constructor got.
	 *
	 * @param receiver the new receiver
	 * @param probabilityMap the probability map for the receiver
	 * @param value the value the (room map) weight function for the receiver
	 */
	public void addNewReceiver(Receiver receiver) {
		if (getReceiver(this.receivers, receiver.getID()) == null && 
				!this.probabilityMaps.containsKey(receiver.getID()) && 
				!this.weightFunctions.containsKey(receiver.getID())) {
			this.receivers.add(receiver);
			setProbabilityMapForOneReceiver(receiver, this.probabilityMap);
			setWeightFunctionForOneReceiver(receiver, this.weightFunction);
		}	
	}
	
	/**
	 * Adds a new receiver to the configuration.
	 *
	 * @param receiver the new receiver
	 * @param probabilityMap the probability map for the receiver
	 * @param value the value the (room map) weight function for the receiver
	 */
	public void addNewReceiver(Receiver receiver, ProbabilityMap probabilityMap, WeightFunction weightFunction) {
		if (getReceiver(this.receivers, receiver.getID()) == null && 
				!this.probabilityMaps.containsKey(receiver.getID()) && 
				!this.weightFunctions.containsKey(receiver.getID())) {
			this.receivers.add(receiver);
			setProbabilityMapForOneReceiver(receiver, probabilityMap);
			setWeightFunctionForOneReceiver(receiver, weightFunction);
		}	
	}
	
	/**
	 * Sets the room map.
	 *
	 * @param roommap the new room map for the algorithm
	 * @param granularity the new granularity for the room map
	 */
	public void setRoomMap(RoomMap roommap) {
		this.roommap = roommap;
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
	
	public void setConvexhullDebugInformation(boolean debug) {
		this.convexhullDebugInformation = debug;
	}
	
	/**
	 * Calculates the position depending of the given readings. The position is located within the borders of the room.
	 * If the given readings are NOT valid or if the readings is empty, null will be returned.
	 * 
	 * @param readings the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	@Override
	public Point calculate(HashMap<Integer, Double> readings) {
		int counter = 0;
		
		Application.getApplication().getLogger().log(Level.INFO, "[ProbabilityBasedAlgorithm - calculate( ... )] Start calculation");
		
		this.roommap.initialize();
		
		// test if some readings are given
		if(readings.isEmpty()) {
			return null;
		}
	
		// go through each receiver and calculate a weighted map
		for (Map.Entry<Integer, Double> e : readings.entrySet()) {
			
			// At least one configured receiver for one reading must exist to calculate a position
			// It could happen that we get a signal for a receiver that is not configured...
			ProbabilityMap probMap = this.probabilityMaps.get(e.getKey());
			if(probMap == null) {
				Application.getApplication().getLogger().log(Level.ERROR, "[ProbabilityBasedAlgorithm - calculate( ... )] The receiver id couldn't be found in probabilityMaps (HashMap)");
				continue;
			}
			// find the right probability map for the receiver in hashmap
			ArrayList<PointProbabilityMap> pointsProbabilityMap = probMap.getProbabilityMap();
			if(pointsProbabilityMap == null) {
				Application.getApplication().getLogger().log(Level.ERROR, "[ProbabilityBasedAlgorithm - calculate( ... )] Empty probability map for receiver " + e.getKey());
				continue;
			}
			
			// find the points in the probability map where the rssi value is above the given value
			ArrayList<PointProbabilityMap> newPointsProbabilityMap = findValuesAboveRssi(pointsProbabilityMap, e.getValue());
			if (newPointsProbabilityMap.size() <= 2) {
				Application.getApplication().getLogger().log(Level.ERROR, "[ProbabilityBasedAlgorithm - calculate( ... )] The are less than two values above the rssi in pointsProbabilityMap (ArrayList)");
				continue;
			}
			
			// calculate the convex hull of the probability map	
			FastConvexHull fch = new FastConvexHull();
			ArrayList<PointProbabilityMap> convexHull = fch.computeHull(newPointsProbabilityMap);
	
			// look for the right receiver
			Receiver receiver = getReceiver(this.receivers, e.getKey());
			if (receiver == null) {
				Application.getApplication().getLogger().log(Level.ERROR, "[ProbabilityBasedAlgorithm - calculate( ... )] The receiver id couldn't be found in receivers (ArrayList)");
				continue;
			}
			
			// transform the convex hull into the correct position
			ArrayList<PointProbabilityMap> convexHullTransformed = 
					convexHullTransformation(convexHull, receiver.getAngle(), receiver.getXPos(), receiver.getYPos());
			
			// Test each point from room map if it lies in the transformed convex hull and weight each point
			weightRoomMap(receiver, convexHullTransformed);
			counter++; // when a weighting of the room map was done, increase the counter
			
			if (this.convexhullDebugInformation) {
				newGrayScaleImageConvexHull(convexHullTransformed, this.grayscaleImagePicCounter + "_convexHull" + e.getKey(), probMap.getGranularity());
			}
			if(this.grayscaleDebugInformationExtended) {
				ArrayList<PointRoomMap> highestPointsRoomMap = giveMaxWeightedValue();
				Point p = getPosition(highestPointsRoomMap);
				newGrayScaleImageRoomMap(p, this.grayscaleImagePicCounter + "_calculatedMap");
				this.grayscaleImagePicCounter++;
			}
		}
		
		if(counter == 0) {
			return null; // no calculation for a point was done
		}
		
		// Find points with the highest weighted value
		ArrayList<PointRoomMap> highestPointsRoomMap = giveMaxWeightedValue();
					
		// calculate point
		Point p = getPosition(highestPointsRoomMap);
//		System.out.println("[" + p.getX() + ";" + p.getY() + "]");
		if(this.enableKalmanfilter) {
			p = useKalmanfilter(p);
		}
		
		Application.getApplication().getLogger().log(Level.INFO, "[ProbabilityBasedAlgorithm - calculate( ... )] End calculation, calculated position: [" + p.getX() + ";" + p.getY() + "]");
//		System.out.println("[" + p.getX() + ";" + p.getY() + "]");
		
		if (this.grayscaleDebugInformation) {
			newGrayScaleImageRoomMap(p, this.grayscaleImagePicCounter + "_calculatedMap");
			this.grayscaleImagePicCounter++;
		}
		return p;
	}
	
	/**
	 * Goes through the given probability map and filters all values that are above the given rssi. A new List of possible points
	 * will be returned then.
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
	 * @param angle the angle for the transformation
	 * @param xPos the x-coordinate for the translation
	 * @param yPos the y-coordinate for the translation
	 * @return the transformed convex hull
	 */
	private ArrayList<PointProbabilityMap> convexHullTransformation(ArrayList<PointProbabilityMap> points, double angle, double xPos, double yPos) {
		// Define a new List for the transformed coordinates
		ArrayList<PointProbabilityMap> transformationProbMap = new ArrayList<PointProbabilityMap>();
		
		// Rotation of the coordinates
		for (int i = 0; i < points.size(); i++) {
			double xRotation = Math.cos(angle) * points.get(i).getX() - Math.sin(angle) * points.get(i).getY();
			double yRotation = Math.sin(angle) * points.get(i).getX() + Math.cos(angle) * points.get(i).getY();
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
	 * Each point of the room map will be tested if it is located inside the given convex hull or not. If the point is located inside the 
	 * convex hull, the weight of the point will be multiplied by FACTOR_FOR_WEIGHTING_ROOMMAP.
	 *
	 * @param roomMap the points of the room map
	 * @param convexHull the points of the convex hull
	 */
	private void weightRoomMap(Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		this.weightFunctions.get(receiver.getID()).weight(this.roommap, receiver, convexHull);
	}

	/**
	 * Searches for the highest weighted points in the room map.
	 *
	 * @param roomMap the room map to search for the highest weighted points
	 * @return a array list with the highest weighted points
	 */
	private ArrayList<PointRoomMap> giveMaxWeightedValue() {
		ArrayList<PointRoomMap> roomMapPoints = this.roommap.getRoomMapPoints();
		
		ArrayList<PointRoomMap> maxWeightedValue = new ArrayList<PointRoomMap>();
		double maxValue = roomMapPoints.get(0).getWeightValue();
		
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
	 * Sets the image path for the grayscale image. It also sets the counter to zero.
	 *
	 * @param path the new path for the grayscale image
	 */
	public void setGrayscaleImagePath(String path) {
		this.grayscaleImagePicCounter = 0;
		if (path.endsWith("\\")) {
			this.grayscaleImagePath = path;
		} else {
			this.grayscaleImagePath = path + "\\";
		}
	}
	
	/**
	 * Creates a new grayscale image of the room map regarding the weighted values of each point. Moreover the receivers itself
	 * will be drawn as black points and the calculated position as a white point.
	 *
	 * @param roomMapPoints the points of the room map (weighted) 
	 * @param pointCalculatedPosition the calculated position
	 * @param receivers the receivers
	 * @param imageName the name of the image
	 */
	private void newGrayScaleImageRoomMap(Point pointCalculatedPosition, String imageName) {
		ArrayList<PointRoomMap> roomMapPoints = this.roommap.getRoomMapPoints();
		
		double smallestWeightedValue = roomMapPoints.get(0).getWeightValue(); 	// smallest weighted value in room map
		double highestWeightedValue = roomMapPoints.get(0).getWeightValue(); 		// highest weighted value in room map
		
		double smallestX = roomMapPoints.get(0).getX();		// smallest position in x in room map
		double smallestY = roomMapPoints.get(0).getY();		// smallest position in y in room map
		
		double highestX = roomMapPoints.get(0).getX();		// highest position in x in room map
		double highestY = roomMapPoints.get(0).getY();		// highest position in y in room map
		
		for (int i = 1; i < roomMapPoints.size(); i++) {
			if (roomMapPoints.get(i).getX() < smallestX) {
				smallestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() < smallestY) {
				smallestY = roomMapPoints.get(i).getY();
			}
			if (roomMapPoints.get(i).getX() > highestX) {
				highestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() > highestY) {
				highestY = roomMapPoints.get(i).getY();
			}
			
			if (roomMapPoints.get(i).getWeightValue() > highestWeightedValue) {
				highestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
			if (roomMapPoints.get(i).getWeightValue() < smallestWeightedValue) {
				smallestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
		}
		
		double factor = 1.0 / this.roommap.getGranularity();	// determine the factor that is needed to create the picture
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor); // determine the length for the needed picture in x 
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor); // determine the length for the needed picture in y
		
		// create point of origin (for the transformation of each point, here just y value is needed). The coordinates of this point are related to the "old" CS
		Point pointOfOriginNewCoordinateSystem = new Point(0, imageLenghtY);
		
		BufferedImage theImage = new BufferedImage(imageLenghtX + 1, imageLenghtY + 1, BufferedImage.TYPE_BYTE_GRAY); // +1, because there is the need of "0"
	    for (int i = 0; i < roomMapPoints.size(); i++) {
	    	
	    	int value = (int) linearInterpolation(roomMapPoints.get(i).getWeightValue(), smallestWeightedValue, highestWeightedValue, GRAYSCALE_IMAGE_START, GRAYSCALE_IMAGE_END);
	    	Color c = new Color(value, value, value);
	    	
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	Point pointAsWholeNumber = new Point((roomMapPoints.get(i).getX() - smallestX) * factor, (roomMapPoints.get(i).getY() - smallestY) * factor);
	    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	theImage.setRGB((int) Math.round(positionPointInImage.getX()), (int) Math.round(positionPointInImage.getY()), c.getRGB());
	    }
	    
	    // the position of the receivers will be displayed as a black pixel in the image
	    for (int i = 0; i < this.receivers.size(); i++) {
	    	Color c = new Color(GRAYSCALE_IMAGE_BLACK, GRAYSCALE_IMAGE_BLACK, GRAYSCALE_IMAGE_BLACK);
	    	
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	Point pointAsWholeNumber = new Point((this.receivers.get(i).getXPos() - smallestX) * factor, 
	    										 (this.receivers.get(i).getYPos() - smallestY) * factor);
	    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	theImage.setRGB((int) Math.round(positionPointInImage.getX()), (int) Math.round(positionPointInImage.getY()), c.getRGB());
	    	
	    }
	    
	    // The determined position found by the algorithm will be displayed in white
	    Color c = new Color(ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE, 
	    					ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE, 
	    					ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE);
	    Point pointAsWholeNumber = new Point((pointCalculatedPosition.getX() - smallestX) * factor, (pointCalculatedPosition.getY() - smallestY) * factor);
    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    theImage.setRGB((int) Math.round(positionPointInImage.getX()), (int) Math.round(positionPointInImage.getY()), c.getRGB());
	    
	    writeImage(theImage, imageName, ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_SCALE_X, ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_SCALE_Y);
	}
	
	/**
	 * Creates a new grayscale image of the convex hull.
	 *
	 * @param points the points of the convex hull
	 * @param imageName the name of the image
	 */
	private void newGrayScaleImageConvexHull(ArrayList<PointProbabilityMap> points, String imageName, double granularity) {
		double smallestX = points.get(0).getX();
		double smallestY = points.get(0).getY();
		
		double highestX = points.get(0).getX();
		double highestY = points.get(0).getY();
		
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).getX() < smallestX) {
				smallestX = points.get(i).getX();
			}
			if (points.get(i).getY() < smallestY) {
				smallestY = points.get(i).getY();
			}
			if (points.get(i).getX() > highestX) {
				highestX = points.get(i).getX();
			}
			if (points.get(i).getY() > highestY) {
				highestY = points.get(i).getY();
			}
		}
		
		double factor = 1.0 / granularity;
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor) + 1;
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor) + 1;
		
		BufferedImage theImage = new BufferedImage(imageLenghtX, imageLenghtY, BufferedImage.TYPE_BYTE_GRAY);
	    for (int i = 0; i < points.size(); i++) {
	    	Color c = new Color(ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE, 
	    						ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE, 
	    						ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_WHITE);
	    	theImage.setRGB((int) Math.round((points.get(i).getX() - smallestX) * factor), 
							(int) Math.round((points.get(i).getY() - smallestY) * factor),
							c.getRGB());
	    }
	    
	    writeImage(theImage, imageName, ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_SCALE_X, ProbabilityBasedAlgorithm.GRAYSCALE_IMAGE_SCALE_Y);
	}
	
	/**
	 * Writes the image into a file defined by the image name and the scaling in x and y.
	 *
	 * @param theImage the BufferedImage
	 * @param imageName the name of the image
	 * @param scaleX the scaling in x
	 * @param scaleY the scaling y
	 */
	private void writeImage(BufferedImage theImage, String imageName, int scaleX, int scaleY) {
		AffineTransform tx = new AffineTransform();
	    tx.scale(scaleX, scaleY);

	    AffineTransformOp op = new AffineTransformOp(tx,
	        AffineTransformOp.TYPE_BILINEAR);
	    theImage = op.filter(theImage, null);
	    
	    File outputfile = new File(this.grayscaleImagePath + imageName + ".png");
	    try {
			ImageIO.write(theImage, "png", outputfile);
		} catch (IOException e) {
			Application.getApplication().getLogger().log(Level.ERROR, "[ProbabilityBasedAlgorithm - writeImage( ... )] Can't write debug image into file " + imageName + ".png");
		}
	}
	
	/**
	 * Performs a linear interpolation for the given value regarding the two given intervals.
	 * <br><br>
	 * interval A: [a0|a1]<br>
	 * interval B: [b0|b1]<br>
	 * A -> B; [a0|a1] -> [b0->b1]<br>
	 * value should lie in interval A (there is no explicit testing)
	 *
	 * @param value the value to be interpolated
	 * @param intervalA0 the lower border of interval A
	 * @param intervalA1 the upper border of interval A
	 * @param intervalB0 the lower border of interval B
	 * @param intervalB1 the upper border of interval B
	 * @return the interpolated value
	 */
	private double linearInterpolation(double value, double intervalA0, double intervalA1, double intervalB0, double intervalB1) {
		// Check borders
		if(value == intervalA0) {
			return intervalB0;
		} else if(value == intervalA1) {
			return intervalB1;
		}
		double r = (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
		double s = intervalB0 - intervalA0 * (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
		
		return (r * value + s);
	}
	
	/**
	 * Makes a transformation of a point from standard math coordinate system into image coordinate system.<br>
	 * (The point of origin of an image is located at the top left corner, the point of origin of a math coordinate
	 * system is located at the bottom left corner). The transformation results out of an horizontal flip and a
	 * transformation afterwards.
	 *
	 * @param pointToTransform the point to transform
	 * @param pointNewCS the point of the new coordinate system
	 * @return the transformed point
	 */
	private Point pointToImageTransformation(Point pointToTransform, Point pointNewCS) {
		Point negPoint = pointNewCS.neg();
		
		double x = 1.0 * pointToTransform.getX() + /*0.0 * pointToTransform.y*/ +negPoint.getX() * 1;
		double y = /*0.0 * pointToTransform.x*/ -1.0 * pointToTransform.getY() - negPoint.getY() * 1;
				
		return new Point(x, y);
	}
	
	public void enableKalmanfilter(boolean enable) {
		this.enableKalmanfilter = enable;
	}
	
	/**
	 * 
	 * @param lastPosition
	 * @return
	 */
	public Point useKalmanfilter(Point p) {
		kf = KalmanFilter.buildKF(0, 0, dt, Math.pow(processNoiseStdev, 2) / 2, Math.pow(measurementNoiseStdev, 2));
	    kf.setX(new Matrix(new double[][]{{p.getX()}, {p.getY()},{0},{0}}));
	      
	    kf.predict();
	    kf.correct(new Matrix(new double[][]{{p.getX()}, {p.getY()}}));
        Point newPoint = new Point (kf.getX().get(0,0), kf.getX().get(1, 0));
        
        return newPoint;
	}
}
