package dataobjects;

public class ReceiverRecord {
	
	public String receiverId;
	public float x;
	
	
	
	
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
	public float y;
	
	public Point getReceiverPosition()
	{
		return new Point(x,y);
	}
	
	
	

}
