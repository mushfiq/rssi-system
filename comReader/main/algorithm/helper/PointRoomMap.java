/*
 * File: PointRoomMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Yentran Tran		create version 1.0
 * 03 Dec 2013		Tommy Griese		added UID (because of serialization of Point2D.Double)
 */
package algorithm.helper;

/**
 * The class PointRoomMap represents a special point in a room map. 
 * Each point has a variable that represents the current weight of this point in a map
 * (this class is inherited by class {@link algorithm.helper.Point}).
 * 
 * @version 1.1 03 Dev 2013
 * @author Yentran Tran
 * @see algorithm.helper.Point
 */
public class PointRoomMap extends Point {
	
	/** UID of this class. */
	private static final long serialVersionUID = -5499256205329251359L;
	
	/** The weighted value in this point. */
	private double weightValue;

	/**
	 * Instantiates a new PointRoomMap. The weight of each point will be set by 1 default.
	 *
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 */
	public PointRoomMap(double x, double y) {
		super(x, y);
		this.weightValue = 1;
	}

	/**
	 * Sets the weight value in this point.
	 *
	 * @param weight the new weight value
	 */
	public void setNewWeightValue(double weight) {
		this.weightValue = weight;
	}
	
	/**
	 * Gets the weight value in this point.
	 *
	 * @return the weight value
	 */
	public double getWeightValue() {
		return this.weightValue;
	}
    
	/**
	 * Represents a string of this point.
	 *
	 * @return a string
	 */
	@Override
    public String toString() {
        return "[" + this.getX() + ";" + this.getY() + ";weight=" + this.weightValue + "]";
    }	
}