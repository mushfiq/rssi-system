/*
 * File: Line.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 */
package algorithm.helper;

/**
 * The class Line represents a line from a start point p0 to an end point p1.
 * 
 * @version 1.0 08 Nov 2013
 * @author Tommy Griese
 */
public class Line {
    
    /** The start point of the line. */
    private Point p0;
    
    /** The end point of the line. */
    private Point p1;

    /**
     * Instantiates a new line.
     *
     * @param p0 the start point p0
     * @param p1 the end point p1
     */
    public Line(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }
    
    /**
     * Gets the start point of the line.
     *
     * @return the start point
     */
    public Point getStartPoint() {
    	return this.p0;
    }
    
    /**
     * Gets the end point of the line.
     *
     * @return the end point
     */
    public Point getEndPoint() {
    	return this.p1;
    }
    
    /**
     * Gets the normalized direction vector of the line in direction of p0 to p1.
     *
     * @return the vector
     */
    public Point getVector() {
        return p1.sub(p0).normalize();
    }

    /**
     * Distance of the given point p to the line.
     *
     * @param p the point
     * @return the distance
     */
    public double distanceOf(Point p) {
        return Math.abs(p.sub(p0).cross(getVector()));
    }

    /**
     * Representation of the line as a string.
     *
     * @return a string that represents the line
     */
    public String toString() {
        return p0 + " " + p1;
    }
}
