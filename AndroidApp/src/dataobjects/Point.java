package dataobjects;

/**
 * This class represent a simple 2D point with decimal x and y coordinate
 * @author Silvio
 *
 */
public class Point
{
	private float x; //The x coordinate of this point
	private float y; //The y coordinate of this point
	
	/**
	 * The default constructor for a point which gives as the Point (0/0)
	 * @author Silvio
	 */
	public Point()
	{
		x = 0.0f;
		y = 0.0f;
	}
	
	/**
	 * A special constructor for a point objects which sets the coordinate to the given x and y value
	 * @author Silvio
	 * @param x The x coordinate of the point
	 * @param y The y coordinate of the point
	 */
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter for the x coordinate
	 * @return The x coordinate of this point
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * Getter for the y coordinate
	 * @return The y coordinate of this point
	 */
	public float getY()
	{
		return y;
	}
	
	public void scale(float scalingX, float scalingY)
	{
		x = x * scalingX;
		y = y * scalingY;
	}
	
	@Override
	/**
	 * The toString method which returns a string representation of this object
	 * @author Silvio
	 * @return The string that represent this object
	 */
	public String toString()
	{
		return "(" + x + " / " + y +")";
	}
	
}
