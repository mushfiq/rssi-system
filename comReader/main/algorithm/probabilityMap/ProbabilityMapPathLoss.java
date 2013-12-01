/*
 * File: ProbabilityMapEmpiric.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 */
package algorithm.probabilityMap;

import java.util.ArrayList;

import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMapPathLoss represents a probability map created with the path loss formula
 * <br>
 * RSSI = -(10 * n * log(d) + A)
 * <br>
 * n = signal propagation constant, also named propagation exponent<br>
 * d = distance from sender<br>
 * A = received signal strength at a distance of one meter
 * <br>
 * (this class is inherited by class {@link algorithm.probabilityMap.ProbabilityMap}).
 * 
 * @version 1.1 01 Dec 2013
 * @author Tommy Griese
 * @see algorithm.helper.PointProbabilityMap
 */
public class ProbabilityMapPathLoss extends ProbabilityMap {
	
	/** The signal propagation constant, also named propagation exponent. */
	private double n;
	
	/** The received signal strength at a distance of one meter. */
	private double a;
	
	private ArrayList<PointProbabilityMap> pMap;
	
	private static final double PROPAGATION_CONSTANT = 10.0;
	
	/**
	 * Instantiates and creates a new ProbabilityMapPathLoss based on the given parameters.
	 *
	 * @param n the signal propagation constant
	 * @param a the received signal strength at a distance of one meter
	 * @param xFrom the start value for the probability map in x
	 * @param xTo the end value for the probability map in x
	 * @param yFrom the start value for the probability map in y
	 * @param yTo the end value for the probability map in y
	 * @param granularity the granularity for the probability map
	 */
	public ProbabilityMapPathLoss(double n, double a,
								  double xFrom, double xTo, 
								  double yFrom, double yTo,
								  double granularity) {
		super(xFrom, xTo, yFrom, yTo, granularity);
		
		this.n = n;
		this.a = a;
		
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
	 * Returns the probability map based on the given parameters.
	 * 
	 * @return the probability map
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
		double rssi = -(ProbabilityMapPathLoss.PROPAGATION_CONSTANT * this.n * Math.log10(distance) + this.a);
		return rssi;
	}
}
