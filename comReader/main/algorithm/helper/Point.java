/*
 * File: Point.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 * 20 Nov 2013		Tommy Griese		Added hashcode and equals method
 * 30 Nov 2013		Tommy Griese		Added function norm2
 */
package algorithm.helper;

import java.awt.geom.Point2D;

/**
 * The class Point represents a point in a 2-Dimensional space. This class is inherited by the java class Point2D.Double
 *
 * @version 1.0 08 Nov 2013
 * @author Yentran Tran, Tommy Griese
 */
public class Point extends Point2D.Double {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2141258328792556002L;

    /**
     * Instantiates a new point.
     *
     * @param x the position on the x-axis
     * @param y the position on the y-axis
     */
    public Point(double x, double y) {
    	super(x, y);
    }
    
    /**
     * Addition of a point.
     *
     * @param p the point to be added
     * @return a new point
     */
    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    /**
     * Negation of the point.
     *
     * @return the negated point
     */
    public Point neg() {
    	return new Point(-x, -y);
    }

    /**
     * Subtraction of a point.
     *
     * @param p the point to be subtracted
     * @return a new point
     */
    public Point sub(Point p) {
        return add(p.neg());
    }
}

