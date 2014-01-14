package dataobjects;

/**
 * This class contains the receiver information. It includes the 
 * X and Y coordinate value to place the receiver on map 
 * @author Maheswari
 *
 */
public class ReceiverRecord {
	
	public String receiverId;//unique id for receiver
	public float x;// To specify the X coordinate value to position the receiver on map
	public float y;// To specify the Y coordinate value to position the receiver on map

	
	/**
	 * The following method to set and get the value of above parameter 
	 * 
	 */
	
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public Point getReceiverPosition()
	{
		return new Point(x,y);
	}
	
	
	

}

