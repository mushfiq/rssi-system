package data;

import algorithm.helper.PointRoomMap;

public class WatchPositionData {

	private int watchId;
	private long time;
	private PointRoomMap position;
	
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

	public PointRoomMap getPosition() {
		return position;
	}

	public void setPosition(PointRoomMap position) {
		this.position = position;
	}

	public WatchPositionData(int watchId, long time, PointRoomMap position) {
		super();
		this.watchId = watchId;
		this.time = time;
		this.position = position;
	}

	
	
}
