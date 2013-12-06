/*
 * File: ProbabilityMapElliptic.java
 * Date				Author				Changes
 * 28 Nov 2013		Tommy Griese		initialized file
 * 30 Nov 2013		Yentran Tran		adapted the method getProbabilityMap to create elliptical maps
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 */
package algorithm.probabilityMap;

import java.util.ArrayList;

import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMapElliptic represents a probability map created with an combination of 
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
	protected double signalPropagationConstant;
	
	/** The default signal strength constant at a distance of one meter for the probability map. */
	public static final double SIGNAL_STRENGTH_ONE_METER_DEFAULT = 51.0;
	
	/** The current applied signal strength constant at a distance of one meter for the probability map. */
	protected double signalStrengthOneMeter;
	
	/** The length of the half-axis in direction x */
	private double lengthHalfAxisX;
	
	/** The length of the half-axis in direction y */
	private double lengthHalfAxisY;
	
	private static final double PRPAGATION_CONSTANT = 10.0;
	
	private ArrayList<PointProbabilityMap> pMap;
	
	/**
	 * Instantiates and creates a new ProbabilityMapElliptic based on the given parameters.
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
		
		this.pMap = new ArrayList<PointProbabilityMap>();
		
		for (double i = this.xFrom; i <= this.xTo; i += this.granularity) { // x-axis
			for (double j = this.yFrom; j <= this.yTo; j += this.granularity) { // y-axis
				double distance = 0;
				if (i == 0 && j == 0) {
					distance = this.granularity;
				} else {
					distance = Math.sqrt((i * i)/this.lengthHalfAxisX + (j * j)/this.lengthHalfAxisY);
				}
				pMap.add(new PointProbabilityMap(i, j, distanceToRSSI(distance)));
			}
		}
	}
	
	/**
	 * Returns the probability map.

	 * @return the new probability map
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
