/*
 * File: Point.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 * 20 Nov 2013		Tommy Griese		Added hashcode and equals method
 */
package algorithm.helper;

/**
 * The class Point represents a point in a 2-Dimensional space.
 *
 * @version 1.0 08 Nov 2013
 * @author Yentran Tran, Tommy Griese
 */
public class Point {

    /** The position on the x-axis (abscissa). */
    private double x;
    
    /** The position on the y-axis (axis of ordinates). */
    private double y;

    /**
     * Instantiates a new point.
     *
     * @param x the position on the x-axis
     * @param y the position on the y-axis
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the position on the x-axis.
     *
     * @return the position on the x-axis
     */
    public double getX() {
    	return this.x;
    }
    
    /**
     * Gets the position on the y-axis.
     *
     * @return the position on the y-axis
     */
    public double getY() {
    	return this.y;
    }
    
    /**
     * Sets the position on the x-axis.
     *
     * @param x the new position in x
     */
    public void setX(double x) {
    	this.x = x;
    }
    
    /**
     * Sets the position on the y-axis.
     *
     * @param y the new position in y
     */
    public void setY(double y) {
    	this.y = y;
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
     * Euclidean norm
     * 
     * @return
     */
    public double norm2() {
        return Math.sqrt(x * x + y * y);
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

    /**
     * Cross product (outer product).
     *
     * @param p the second point for the cross product
     * @return the result of the cross product
     */
    public double cross(Point p) {
        return x * p.y - y * p.x;
    }

    // TODO make comment (look what this function is doing in detail when time is available)!!!
    /**
     * 
     * 
     * @param p0 the p0
     * @param p1 the p1
     * @return the double
     */
    public double area2(Point p0, Point p1) {
        return sub(p0).cross(sub(p1));
    }

    /**
     * Checks if the point is located on the right side of the given line.
     *
     * @param g the line for the test 
     * @return true, if the point is located on the right side, false otherwise
     */
    public boolean isRightOf(Line g) {
        return area2(g.getStartPoint(), g.getEndPoint()) < 0;
    }

    /**
     * A string that represents the point.
     * 
     * @return string of the point
     */
    public String toString() {
        return "[" + x + ";" + y + "]";
    }
    
    @Override
    public int hashCode() {
    	return 0;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (this == obj) {
    		return true;
    	}
        if (obj == null) {
        	return false;
        }
        if (getClass() != obj.getClass()) {
        	return false;
        }
        
        Point p = (Point) obj;
        if (x != p.x || y != p.y) {
        	return false;
        }
        return true;
    }
}

