/*
 * File: ProbabilityMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 01 Dec 2013		Tommy Griese		general code refactoring and improvements
 */
package algorithm.probabilityMap;

import java.util.ArrayList;
import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMap. This abstract class should be inherited by all implemented probability map classes.
 * It defines a method to create a probability map.
 * 
 * @version 1.1 01 Dec 2013
 * @author Tommy Griese
 */
public abstract class ProbabilityMap {
	
	/** The default granularity constant for the probability map. */
	public static final double GRANULARITY_DEFAULT = 0.25;
	
	/** The current applied granularity constant for the probability map. */
	protected double granularity;
	
	/** The default start value in x for the probability map. */
	public static final double X_FROM_DEFAULT = -10.0;
	
	/** The current applied start value in x for the probability map. */
	protected double xFrom;
	
	/** The default end value in x for the probability map. */
	public static final double X_TO_DEFAULT = 10.0;
	
	/** The current applied end value in x for the probability map. */
	protected double xTo;
	
	/** The default start value in y for the probability map. */
	public static final double Y_FROM_DEFAULT = -10.0;
	
	/** The current applied start value in y for the probability map. */
	protected double yFrom;
	
	/** The default end value in y for the probability map. */
	public static final double Y_TO_DEFAULT = 10.0;
	
	/** The current applied end value in y for the probability map. */
	protected double yTo;
	
	
	ProbabilityMap() {
		this.xFrom = ProbabilityMap.X_FROM_DEFAULT;
		this.xTo = ProbabilityMap.X_TO_DEFAULT;
		this.yFrom = ProbabilityMap.Y_FROM_DEFAULT;
		this.yTo = ProbabilityMap.Y_TO_DEFAULT;
		this.granularity = ProbabilityMap.GRANULARITY_DEFAULT;
	}
	
	ProbabilityMap(double xFrom, double xTo, 
			double yFrom, double yTo,
			double granularity) {
		this.xFrom = xFrom;
		this.xTo = xTo;
		this.yFrom = yFrom;
		this.yTo = yTo;
		this.granularity = granularity;
	}
	
	/**
	 * Abstract method getProbabilityMap.
	 *
	 * @param xFrom the start value for the probability map in x
	 * @param xTo the end value for the probability map in x
	 * @param yFrom the start value for the probability map in y
	 * @param yTo the end value for the probability map in y
	 * @param granularity the granularity for the probability map
	 * @return the new probability map
	 */
	public abstract ArrayList<PointProbabilityMap> getProbabilityMap();
	
	public double getGranularity() {
		return this.granularity;
	}
}
