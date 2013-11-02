package data;

import algorithm.helper.Point_RoomMap;

public class WatchPositionData {

	private int watchId;
	private long time;
	private Point_RoomMap position;
	
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

	public Point_RoomMap getPosition() {
		return position;
	}

	public void setPosition(Point_RoomMap position) {
		this.position = position;
	}

	public WatchPositionData(int watchId, long time, Point_RoomMap position) {
		super();
		this.watchId = watchId;
		this.time = time;
		this.position = position;
	}

	
	
}
