package dataobjects;

/**
 * This class represent a 2D coordinate tranformation
 * @author Silvio
 *
 */
public class CoordinateTransformation
{
	
	private Point oldZero = new Point(); // Zero Point of the initial coordinate system
	private Point newZero = new Point(); // Zero Point of the new coordinate system
	
	/**
	 * Constructor for the coordinate transformation
	 * @param oldZero The zero point of the initial coordinate system
	 * @param newZero The zero point of the new coordinate system
	 */
	public CoordinateTransformation( Point oldZero, Point newZero )
	{
		this.oldZero = oldZero;
		this.newZero = newZero;
	}
	
	/**
	 * This method transforms the given point and return the transformed point
	 * @param point Point which should be transformed to the new coordinate system
	 * @return The point regarding the new zero point
	 */
	public Point transformPosition( Point point)
	{
		float dx = newZero.getX() - oldZero.getX();
		float dy = newZero.getY() - oldZero.getY();
		float x = point.getX() + dx;
		float y = -point.getY() - dy;
		return new Point(x, y);
	}

}
