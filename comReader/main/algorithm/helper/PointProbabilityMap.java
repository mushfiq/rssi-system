/*
 * File: PointProbabilityMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 03 Dec 2013		Tommy Griese		added UID (because of serialization of Point2D.Double)
 */
package algorithm.helper;

/**
 * The class PointProbabilityMap represents a special point in a probability map. 
 * Each point has a variable that represents the rssi value in this point
 * (this class is inherited by class {@link algorithm.helper.Point}).
 * 
 * @version 1.1 03 Dec 2013
 * @author Tommy Griese
 * @see algorithm.helper.Point
 */
public class PointProbabilityMap extends Point {

	/** UID of this class. */
	private static final long serialVersionUID = -4891789580153548216L;
	
	/** The rssi value in this point. */
	private double rssiValue;
        
	/**
	 * Instantiates a new PointProbabilityMap.
	 *
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @param rssiValue the rssi value in this point
	 */
	public PointProbabilityMap(double x, double y, double rssiValue) {
		super(x, y);
        this.rssiValue = rssiValue;
	}
    
	/**
	 * Gets the rssi value in this point.
	 *
	 * @return the rssi value
	 */
	public double getRSSIValue() {
		return this.rssiValue;
	}
	
	/**
	 * Sets the rssi value in this point.
	 *
	 * @param rssiValue the new rssi value
	 */
	public void setRSSIValue(double rssiValue) {
		this.rssiValue = rssiValue;
	}
	
	/**
	 * Represents a string of this point.
	 *
	 * @return a string
	 */
	@Override
    public String toString() {
        return "[" + this.getX() + ";" + this.getY() + ";rssi=" + this.rssiValue + "]";
    }
}