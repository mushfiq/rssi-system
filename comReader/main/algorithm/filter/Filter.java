/*
 * File: Filter.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 */
package algorithm.filter;

import algorithm.helper.Point;

/**
 * The class Filter. This abstract class should be inherited by all implemented filter classes.
 * It defines a method to apply a filtering to some points.
 * 
 * @version 1.0 08 Dec 2013
 * @author Tommy Griese
 */
public abstract class Filter {

	/**
	 * Instantiates a new filter.
	 */
	public Filter() { }
	
	/**
	 * Abstract method applyFilter. This method can be used to apply a filter on a point to return a new point.
	 *
	 * @param point the point to apply the filter for
	 * @return the new point
	 */
	public abstract Point applyFilter(Point point);
}
