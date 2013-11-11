package dataobjects;

public class Point
{
	private float x = 0.0f;
	private float y = 0.0f;
	
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + " / " + y +")";
	}
	
}
