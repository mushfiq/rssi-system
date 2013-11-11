/*
 * File: ProbabilityMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 */
package algorithm.probabilityMap;

import java.util.ArrayList;
import algorithm.helper.PointProbabilityMap;

/**
 * The class ProbabilityMap. This abstract class should be inherited by all implemented probability map classes.
 * It defines a method to create a probability map.
 * 
 * @version 1.0 08 Nov 2013
 * @author Tommy Griese
 */
public abstract class ProbabilityMap {
	
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
	public abstract ArrayList<PointProbabilityMap> getProbabilityMap(
			double xFrom, double xTo, 
			double yFrom, double yTo,
			double granularity);
}
