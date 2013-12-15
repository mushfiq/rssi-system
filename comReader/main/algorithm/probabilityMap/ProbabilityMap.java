/*
 * File: ProbabilityMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 * 14 Dec 2013		Tommy Griese		Adapted code: class reads the needed parameters from the configuration file now
 * 										(Utilities.getConfigurationValue and Utilities.getBooleanConfigurationValue)
 * 										Adapted JavaDoc comments
 */
package algorithm.probabilityMap;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.ProbabilityBasedAlgorithm;
import algorithm.filter.KalmanFilterOneDim;
import algorithm.helper.PointProbabilityMap;
import algorithm.weightFunction.WeightFunctionExtended;

/**
 * The class ProbabilityMap. This abstract class should be inherited by all implemented probability map classes.
 * It defines a method to create a probability map.
 * 
 * @version 1.2 14 Dec 2013
 * @author Tommy Griese
 */
public abstract class ProbabilityMap {
	
	/** The default granularity constant (value = 0.25) for the probability map. */
	public static final double GRANULARITY_DEFAULT = 0.25;
	
	/** The current applied granularity constant for the probability map. */
	protected double granularity;
	
	/** The default start value in x (value = -10.0) for the probability map. */
	public static final double X_FROM_DEFAULT = -10.0;
	
	/** The current applied start value in x for the probability map. */
	protected double xFrom;
	
	/** The default end value in x (value = 10.0) for the probability map. */
	public static final double X_TO_DEFAULT = 10.0;
	
	/** The current applied end value in x for the probability map. */
	protected double xTo;
	
	/** The default start value in y (value = -10.0) for the probability map. */
	public static final double Y_FROM_DEFAULT = -10.0;
	
	/** The current applied start value in y for the probability map. */
	protected double yFrom;
	
	/** The default end value in y (value = 10.0) for the probability map. */
	public static final double Y_TO_DEFAULT = 10.0;
	
	/** The current applied end value in y for the probability map. */
	protected double yTo;
	
	/** The logger. */
    private Logger logger;
    
	/**
	 * Instantiates a new ProbabilityMap with default parameters. The ProbabilityMap will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. Following parameters will be 
	 * read from the file:<br>
	 * <br>
	 * probability_map.x_from (default value is {@link ProbabilityMap#X_FROM_DEFAULT})<br>
	 * probability_map.x_to (default value is {@link ProbabilityMap#X_TO_DEFAULT})<br>
	 * probability_map.y_from (default value is {@link ProbabilityMap#Y_FROM_DEFAULT})<br>
	 * probability_map.y_to (default value is {@link ProbabilityMap#Y_TO_DEFAULT})<br>
	 * <br>
	 * probability_map.granularity (default value is {@link ProbabilityMap#GRANULARITY_DEFAULT})<br>
	 */
	ProbabilityMap() {
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName());
        
        readConfigParameters();
	}
	
	/**
	 * Instantiates a new ProbabilityMap with the given parameters.
	 *
	 * @param xFrom the start value for the probability map in x
	 * @param xTo the end value for the probability map in x
	 * @param yFrom the start value for the probability map in y
	 * @param yTo the end value for the probability map in y
	 * @param granularity the granularity for the probability map
	 */
	ProbabilityMap(double xFrom, double xTo, double yFrom, double yTo, double granularity) {
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
        this.xFrom = xFrom;
		this.xTo = xTo;
		this.yFrom = yFrom;
		this.yTo = yTo;
		this.granularity = granularity;
	}
	
	/**
	 * Abstract method getProbabilityMap. Should return a list of PointProbabilityMap that represents a
	 * ProbabilityMap.
	 * 
	 * @return the new probability map
	 */
	public abstract ArrayList<PointProbabilityMap> getProbabilityMap();
	
	/**
	 * Gets the granularity of this ProbabilityMap.
	 *
	 * @return the granularity
	 */
	public double getGranularity() {
		return this.granularity;
	}
	
	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * probability_map.x_from (default value is {@link ProbabilityMap#X_FROM_DEFAULT})<br>
	 * probability_map.x_to (default value is {@link ProbabilityMap#X_TO_DEFAULT})<br>
	 * probability_map.y_from (default value is {@link ProbabilityMap#Y_FROM_DEFAULT})<br>
	 * probability_map.y_to (default value is {@link ProbabilityMap#Y_TO_DEFAULT})<br>
	 * <br>
	 * probability_map.granularity (default value is {@link ProbabilityMap#GRANULARITY_DEFAULT})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readConfigParameters() {
		String res = "";
		double value = ProbabilityMap.GRANULARITY_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map.granularity");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map.granularity failed, default value was set.");
		}
		this.granularity = value;
		
		
		value = ProbabilityMap.X_FROM_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map.x_from");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map.x_from failed, default value was set.");
		}
		this.xFrom = value;
		
		value = ProbabilityMap.X_TO_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map.x_to");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map.x_to failed, default value was set.");
		}
		this.xTo = value;
		
		value = ProbabilityMap.Y_FROM_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map.y_from");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map.y_from failed, default value was set.");
		}
		this.yFrom = value;
		
		value = ProbabilityMap.Y_TO_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map.y_to");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map.y_to failed, default value was set.");
		}
		this.yTo = value;
	}
}