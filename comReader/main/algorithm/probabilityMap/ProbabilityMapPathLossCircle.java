/*
 * File: ProbabilityMapPathLossCircle.java
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
import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMapPathLossCircle represents a probability map created with the path loss formula
 * <br>
 * RSSI = -(10 * n * log(d) + A)
 * <br>
 * n = signal propagation constant, also named propagation exponent<br>
 * d = distance from sender<br>
 * A = received signal strength at a distance of one meter
 * <br>
 * (this class is inherited by class {@link algorithm.probabilityMap.ProbabilityMap}).
 * 
 * @version 1.2 14 Dec 2013
 * @author Tommy Griese
 * @see algorithm.helper.PointProbabilityMap
 */
public class ProbabilityMapPathLossCircle extends ProbabilityMap {
	
	/** The default propagation constant (value = 4.0) for the probability map. */
	public static final double SIGNAL_PROPAGATION_CONSTANT_DEFAULT = 4.0;
	
	/** The current applied propagation constant for the probability map. */
	private double signalPropagationConstant;
	
	/** The default signal strength constant at a distance of one meter (value = 51.0) for the probability map. */
	public static final double SIGNAL_STRENGTH_ONE_METER_DEFAULT = 51.0;
	
	/** The current applied signal strength constant at a distance of one meter for the probability map. */
	private double signalStrengthOneMeter;
	
	/** A list of PointProbabilityMap that represents the ProbabilityMapPathLossCircle. */
	private ArrayList<PointProbabilityMap> pMap;
	
	/** Default path loss parameter for the path loss formula. */
	private static final double PROPAGATION_CONSTANT = 10.0;
	
	/** The logger. */
    private Logger logger;
	
	/**
	 * Instantiates a new ProbabilityMapPathLossCircle. The ProbabilityMapPathLossCircle will be initialized regarding the configuration
	 * file 'config.ini'. If there are any invalid parameters, these parameters will get default values. Following parameters will be 
	 * read from the file:<br>
	 * <br>
	 * probability_map_path_loss_circle.signal_propagation_constant (default value is {@link ProbabilityMapPathLossCircle#SIGNAL_PROPAGATION_CONSTANT_DEFAULT})<br>
	 * probability_map_path_loss_circle.signal_strength_one_meter (default value is {@link ProbabilityMapPathLossCircle#SIGNAL_STRENGTH_ONE_METER_DEFAULT})<br>
	 * <br>
	 * Moreover it also creates the map depending on theses values.
	 */
	public ProbabilityMapPathLossCircle() {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		readConfigParameters();
		
		initialize();
	}
	
	/**
	 * Instantiates a new ProbabilityMapPathLossCircle depending on the given parameters. Moreover it creates the 
	 * map depending on theses values.
	 *
	 * @param signalPropagationConstant the signal propagation constant
	 * @param signalStrengthOneMeter the received signal strength at a distance of one meter
	 * @param xFrom the start value for the probability map in x
	 * @param xTo the end value for the probability map in x
	 * @param yFrom the start value for the probability map in y
	 * @param yTo the end value for the probability map in y
	 * @param granularity the granularity for the probability map
	 */
	public ProbabilityMapPathLossCircle(double signalPropagationConstant, double signalStrengthOneMeter,
								  		double xFrom, double xTo, 
								  		double yFrom, double yTo,
								  		double granularity) {
		super(xFrom, xTo, yFrom, yTo, granularity);
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		this.signalPropagationConstant = signalPropagationConstant;
		this.signalStrengthOneMeter = signalStrengthOneMeter;
		
		initialize();
	}
	
	/**
	 * Creates a list of PointProbabilityMap that represents the probability map for this class.
	 */
	private void initialize() {
		this.pMap = new ArrayList<PointProbabilityMap>();
		
		for (double i = this.xFrom; i <= this.xTo; i += this.granularity) { // x-axis
			for (double j = this.yFrom; j <= this.yTo; j += this.granularity) { // y-axis
				double distance = 0;
				if (i == 0 && j == 0) {
					distance = this.granularity;
				} else {
					distance = Math.sqrt(i * i + j * j);
				}
				this.pMap.add(new PointProbabilityMap(i, j, distanceToRSSI(distance)));
			}
		}
	}
	
	/**
	 * Returns the probability map.
	 * 
	 * @return the list of PointProbabilityMap that represents the probability map.
	 */
	@Override
	public ArrayList<PointProbabilityMap> getProbabilityMap() {
		return this.pMap;
	}
	
	/**
	 * Calculates the rssi value based on the given distance with the help of the
	 * path loss formula.
	 *
	 * @param distance the distance
	 * @return the rssi value
	 */
	public double distanceToRSSI(double distance) {
		double rssi = -(ProbabilityMapPathLossCircle.PROPAGATION_CONSTANT * this.signalPropagationConstant * Math.log10(distance) + this.signalStrengthOneMeter);
		return rssi;
	}
	
	/**
	 * This method reads and initializes following parameters from the 'config.ini' file:<br>
	 * <br>
	 * probability_map_path_loss_circle.signal_propagation_constant (default value is {@link ProbabilityMapPathLossCircle#SIGNAL_PROPAGATION_CONSTANT_DEFAULT})<br>
	 * probability_map_path_loss_circle.signal_strength_one_meter (default value is {@link ProbabilityMapPathLossCircle#SIGNAL_STRENGTH_ONE_METER_DEFAULT})<br>
	 * <br>
	 * If there are any invalid parameters, these will be initialized with default values. 
	 */
	private void readConfigParameters() {		
		String res = "";
		double value = ProbabilityMapPathLossCircle.SIGNAL_PROPAGATION_CONSTANT_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map_path_loss_circle.signal_propagation_constant");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map_path_loss_circle.signal_propagation_constant failed, default value was set.");
		}
		this.signalPropagationConstant = value;
		
		
		value = ProbabilityMapPathLossCircle.SIGNAL_STRENGTH_ONE_METER_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("probability_map_path_loss_circle.signal_strength_one_meter");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading probability_map_path_loss_circle.signal_strength_one_meter failed, default value was set.");
		}
		this.signalStrengthOneMeter = value;
	}
}
