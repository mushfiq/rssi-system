package algorithm.stubs;

public class RoomMap {

	
	
	private double fromX;
	private double toX;
	
	private double fromY;
	private double toY;
	
	private String pathToImage;
	
	public RoomMap(double fromX, double toX, double fromY, double toY) {
		this.fromX = fromX;
		this.toX = toX;
		
		this.fromY = fromY;
		this.toY = toY;
	}
	
	public RoomMap(double fromX, double toX, double fromY, double toY, String pathToImage) {
		super();
		this.fromX = fromX;
		this.toX = toX;
		
		this.fromY = fromY;
		this.toY = toY;
		this.pathToImage = pathToImage;
	}
	
	public double getFromX() {
		return this.fromX;
	}
	public double getToX() {
		return this.toX;
	}
	
	public double getFromY() {
		return this.fromY;
	}
	public double getToY() {
		return this.toY;
	}
	
	public String getPathToImage() {
		return pathToImage;
	}



	public void setPathToImage(String pathToImage) {
		this.pathToImage = pathToImage;
	}
}