/*
 * File: ProbabilityMapPathLossElliptic.java
 * Date				Author				Changes
 * 28 Nov 2013		Tommy Griese		initialized file
 * 30 Nov 2013		Yentran Tran		adapted the method getProbabilityMap to create elliptical maps
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 */
package algorithm.probabilityMap;

import java.util.ArrayList;

import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMapPathLossElliptic represents a probability map created with an combination of 
 * the path loss formula and an elliptical approach.
 * <br>
 * (this class is inherited by class {@link algorithm.probabilityMap.ProbabilityMap}).
 * 
 * @version 1.1 01 Dec 2013
 * @author Yentran Tran, Tommy Griese
 * @see algorithm.helper.PointProbabilityMap
 */
public class ProbabilityMapPathLossElliptic extends ProbabilityMap {
	
	/** The default propagation constant for the probability map. */
	public static final double SIGNAL_PROPAGATION_CONSTANT_DEFAULT = 4.0;
	
	/** The current applied propagation constant for the probability map. */
	private double signalPropagationConstant;
	
	/** The default signal strength constant at a distance of one meter for the probability map. */
	public static final double SIGNAL_STRENGTH_ONE_METER_DEFAULT = 51.0;
	
	/** The current applied signal strength constant at a distance of one meter for the probability map. */
	private double signalStrengthOneMeter;
	
	/** The default length of the half axis in x. */
	public static final double LENGTH_HALFAXIS_X_DEFAULT = 1.0;
	
	/** The default length of the half axis in y. */
	public static final double LENGTH_HALFAXIS_Y_DEFAULT = 0.75;
	
	/** The length of the half-axis in direction x. */
	private double lengthHalfAxisX;
	
	/** The length of the half-axis in direction y. */
	private double lengthHalfAxisY;
	
	/** Default path loss parameter for the path loss formula. */
	private static final double PRPAGATION_CONSTANT = 10.0;
	
	/** A list of PointProbabilityMap that represents the ProbabilityMapPathLossElliptic. */
	private ArrayList<PointProbabilityMap> pMap;
	
	
	/**
	 * Instantiates a new ProbabilityMapPathLossElliptic with default 'propagation constant', 'signal strength constant'
	 * and 'length of the elliptical half axis' parameters. Moreover it also creates the map depending on theses values.
	 */
	public ProbabilityMapPathLossElliptic() {
		super();
		
		this.signalPropagationConstant = ProbabilityMapPathLossCircle.SIGNAL_PROPAGATION_CONSTANT_DEFAULT;
		this.signalStrengthOneMeter = ProbabilityMapPathLossCircle.SIGNAL_STRENGTH_ONE_METER_DEFAULT;
		
		this.lengthHalfAxisX = ProbabilityMapPathLossElliptic.LENGTH_HALFAXIS_X_DEFAULT;
		this.lengthHalfAxisY = ProbabilityMapPathLossElliptic.LENGTH_HALFAXIS_Y_DEFAULT;
		
		initialize();
	}
	
	/**
	 * Instantiates a new ProbabilityMapPathLossElliptic depending on the given parameters. Moreover it creates the 
	 * map depending on theses values.
	 *
	 * @param signalPropagationConstant the signal propagation constant
	 * @param signalStrengthOneMeter the received signal strength at a distance of one meter
	 * @param xFrom the start value for the probability map in x
	 * @param xTo the end value for the probability map in x
	 * @param yFrom the start value for the probability map in y
	 * @param yTo the end value for the probability map in y
	 * @param granularity the granularity for the probability map
	 * @param lengthHalfAxisX the length of the half-axis in direction x
	 * @param lengthHalfAxisY the length of the half-axis in direction y
	 */
	public ProbabilityMapPathLossElliptic(double signalPropagationConstant, double signalStrengthOneMeter, 
								  		  double xFrom, double xTo, 
								  		  double yFrom, double yTo,
								  		  double granularity,
								  		  double lengthHalfAxisX, double lengthHalfAxisY) {
		super(xFrom, xTo, yFrom, yTo, granularity);
		
		this.signalPropagationConstant = signalPropagationConstant;
		this.signalStrengthOneMeter = signalStrengthOneMeter;
		
		this.lengthHalfAxisX = lengthHalfAxisX;
		this.lengthHalfAxisY = lengthHalfAxisY;
		
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
					distance = Math.sqrt((i * i) / this.lengthHalfAxisX + (j * j) / this.lengthHalfAxisY);
				}
				pMap.add(new PointProbabilityMap(i, j, distanceToRSSI(distance)));
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
		double rssi = -(ProbabilityMapPathLossElliptic.PRPAGATION_CONSTANT * this.signalPropagationConstant * Math.log10(distance) + this.signalStrengthOneMeter);
		return rssi;
	}
}
