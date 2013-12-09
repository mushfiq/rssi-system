package data;

import algorithm.helper.Point;

public class WatchPositionData {

	private int watchId;
	private long time;
	private Point position;
	
	public WatchPositionData() {
		// TODO Auto-generated constructor stub
	}

	public int getWatchId() {
		return watchId;
	}

	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public WatchPositionData(int watchId, long time, Point position) {
		super();
		this.watchId = watchId;
		this.time = time;
		this.position = position;
	}

	@Override
	public String toString() {
		
		return "Watch id: " + watchId + ", time: " + time + ", position: " + position.toString();
	}
	
}
