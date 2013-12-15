/*
 * File: WeightFunctionExtended.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 * 14 Dec 2013 		Tommy Griese		Adapted code: class reads the needed parameters from the configuration file now
 * 										(Utilities.getConfigurationValue and Utilities.getBooleanConfigurationValue)
 * 										Adapted JavaDoc comments
 */
package algorithm.weightFunction;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;

import components.Receiver;
import components.RoomMap;

/**
 * The class WeightFunctionExtended. This class weights the room map in a more complex way (see method weight(...)).
 *
 * @version 1.1 14 Dec 2013
 * @author Tommy Griese
 */
public class WeightFunctionExtended extends WeightFunction {

	/** Default percentage step size (value = 0.10) of the weighting. */
	public static final double PERCENTAGE_STEPS_DEFAULT = 0.10;
	
	/** Current applied percentage step size. */
	private double percentageSteps;
	
	/** The logger. */
    private Logger logger;
	
	
	/**
	 * Instantiates a new WeightFunctionExtended. The WeightFunctionExtended will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. Following parameters will be 
	 * read from the file:<br>
	 * <br>
	 * weight_function_extended.percentage_steps (default value is {@link WeightFunctionExtended#PERCENTAGE_STEPS_DEFAULT})<br>
	 */
	public WeightFunctionExtended() {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		readConfigParameters();
	}
	
	/**
	 * Instantiates a new WeightFunctionExtended with the given 'percentage step size'.
	 * 
	 * @param percentageSteps the percentage step for the calculation (see method weight)
	 */
	public WeightFunctionExtended(double percentageSteps) {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		this.percentageSteps = percentageSteps;
	}

	/**
	 * This is the weight method of the extended weight function class. This method covers the approach that
	 * the points close by the convex hull will get the highest weight. If the points are further afar of the
	 * convex hull they will get a lower weight. This differentiation of near by and further afar is done by the
	 * distance of a point to the convex hull and a percentage distance, e.g. all points whose distance lies in 
	 * a range of 10% away from the convex hull will get the same weight. The same with all point whose distance
	 * lies between 10% and 20% away from the convex hull and so on.
	 *
	 * @param roommap the room map that should be weighted
	 * @param receiver the current receiver for which this weighting should be done 
	 * @param convexHull the convex hull for the current receiver
	 */
	@Override
	public void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		ArrayList<PointRoomMap> pointsRoomMap = roommap.getRoomMapPoints();
		
		for (int i = 0; i < pointsRoomMap.size(); i++) {
			
			double distanceConvexhullAndPoint = calcDistanceBtwConvexhullAndPoint(convexHull, pointsRoomMap.get(i));
			double distanceReceiverAndPoint = calcDistanceBtwReceiverAndPoint(receiver, pointsRoomMap.get(i));
			
			double res = -1.0;
			if (liesPointInConvexHull(pointsRoomMap.get(i), convexHull)) {
				res = 1.0 - (distanceReceiverAndPoint / (distanceReceiverAndPoint + distanceConvexhullAndPoint));
			} else {
				res = 1.0 - ((distanceReceiverAndPoint - distanceConvexhullAndPoint) / distanceReceiverAndPoint);
			}
			
			for (double j = this.percentageSteps; j <= 1.0; j += this.percentageSteps) {
				if (res < j) {
					pointsRoomMap.get(i).setNewWeightValue(pointsRoomMap.get(i).getWeightValue() * (2.0 - j));
					break;
				}
			}
		}
	}
	
	/**
	 * Calculates the nearest distance between the convex hull and a point.
	 *
	 * @param convexHull the convex hull for the calculation
	 * @param p the point for the calculation
	 * @return the distance between convex hull and point
	 */
	private double calcDistanceBtwConvexhullAndPoint(ArrayList<PointProbabilityMap> convexHull, Point p) {
		Point pStart = convexHull.get(0);
		Point pEnd = convexHull.get(1);
		double distance = Line2D.ptSegDist(pStart.x, pStart.y, pEnd.x, pEnd.y, p.x, p.y);
		
		int size = convexHull.size();
		for (int i = 0; i < size; i++) {
			pStart = convexHull.get(i % size);
			pEnd = convexHull.get((i + 1) % size);
			
			double distanceTemp = Line2D.ptSegDist(pStart.x, pStart.y, pEnd.x, pEnd.y, p.x, p.y);
			if (distanceTemp < distance) {
				distance = distanceTemp;
			}
		}
		return distance;
	}
	
	/**
	 * Calculates distance between a receiver and a point.
	 *
	 * @param receiver the receiver for the calculation
	 * @param p the point for the calculation
	 * @return the distance between the receiver and point
	 */
	private double calcDistanceBtwReceiverAndPoint(Receiver receiver, Point p) {
		Point vectorReceiverPoint = new Point(p.x - receiver.getXPos(), p.y - receiver.getYPos());
		return Math.sqrt(vectorReceiverPoint.x * vectorReceiverPoint.x + vectorReceiverPoint.y * vectorReceiverPoint.y);
	}
	
	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * weight_function_extended.percentage_steps (default value is {@link WeightFunctionExtended#PERCENTAGE_STEPS_DEFAULT})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readConfigParameters() {
		String res = "";
		double value = WeightFunctionExtended.PERCENTAGE_STEPS_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("weight_function_extended.percentage_steps");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading weight_function_extended.percentage_steps failed, default value was set.");
		}
		this.percentageSteps = value;
	}
}