/*
 * File: WeightFunctionSimple.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 * 14 Dec 2013 		Tommy Griese		Adapted code: class reads the needed parameters from the configuration file now
 * 										(Utilities.getConfigurationValue and Utilities.getBooleanConfigurationValue)
 * 										Adapted JavaDoc comments
 */
package algorithm.weightFunction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;

import components.Receiver;
import components.RoomMap;

/**
 * The class WeightFunctionSimple. This class weights the room map in an easy way (see method weight(...)).
 *
 * @version 1.1 14 Dec 2013
 * @author Tommy Griese
 */
public class WeightFunctionSimple extends WeightFunction {

	/** Default factor (value = 2.0) that is used to determine the weighted points of a room map. */
	public static final double FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT = 2.0;
	
	/** Current applied factor for determining the weighted points of a room map. */
	private double factor;
	
	/** The logger. */
    private Logger logger;
	
    /**
	 * Instantiates a new WeightFunctionSimple. The WeightFunctionSimple will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. Following parameters will be 
	 * read from the file:<br>
	 * <br>
	 * weight_function_simple.factor_roommap_weighting (default value is {@link WeightFunctionSimple#FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT})<br>
	 */
	public WeightFunctionSimple() {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		readConfigParameters();
	}
	
	/**
	 * Instantiates a new WeightFunctionSimple with the given 'weight factor'.
	 * 
	 * @param weightFactor the weight factor to use for the weighting (see method weight)
	 */
	public WeightFunctionSimple(double weightFactor) {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		this.factor = weightFactor;
	}

	/**
	 * This is the weight method of the simple weight function class. This method covers the approach that
	 * all points that are located inside the convex hull will get a higher weight than the points that are not
	 * located inside the convex hull, e.g. the weight of a point that is located inside the convex hull will be 
	 * multiplied by a weight factor, the weight of the points not located inside the convex hull remain.
	 * 
	 * @param roommap the room map that should be weighted
	 * @param receiver the current receiver for which this weighting should be done 
	 * @param convexHull the convex hull for the current receiver
	 */
	@Override
	public void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		ArrayList<PointRoomMap> pointsRoomMap = roommap.getRoomMapPoints();
		
		for (int i = 0; i < pointsRoomMap.size(); i++) {
			if (liesPointInConvexHull(pointsRoomMap.get(i), convexHull)) {
				pointsRoomMap.get(i).setNewWeightValue(pointsRoomMap.get(i).getWeightValue() * this.factor);
			}
		}
	}

	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * weight_function_simple.factor_roommap_weighting (default value is {@link WeightFunctionSimple#FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readConfigParameters() {
		String res = "";
		double value = WeightFunctionSimple.FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("weight_function_simple.factor_roommap_weighting");
			value = Double.parseDouble(res);
		} catch (NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading weight_function_simple.factor_roommap_weighting failed, default value was set.");
		}
		this.factor = value;
	}
}
