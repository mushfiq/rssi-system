package dataobjects;

public class HelperFunctions
{
	public static Point transformPosition(Point oldZero, Point newZero, Point point)
	{
		float dx = newZero.getX() - oldZero.getX();
		float dy = newZero.getY() - oldZero.getY();
		float x = point.getX() + dx;
		float y = -point.getY() - dy;
		return new Point(x, y);
	}

}
