package dataobjects;

import java.util.Date;

public class WatchPositionRecord
{	
	private String watchId = "";
	
	public String getWatchId()
	{
		return watchId;
	}
	public void setWatchId(String watchId)
	{
		this.watchId = watchId;
	}
	
	private Point position;
	
	public Point getPosition()
	{
		return position;
	}
	public void setPosition(Point position)
	{
		this.position = position;
	}
	
	private Date insertedAt = new Date();
	
	public Date getInsertedAt()
	{
		return insertedAt;
	}
	public void setInsertedAt(Date insertedAt)
	{
		this.insertedAt = insertedAt;
	}
	
	private int mapId = -1;
	
	public int getMapId()
	{
		return mapId;
	}
	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	@Override
	public String toString()
	{
		String ret = "Location of " + watchId + " located at map " + mapId + " at position" + position + " at time " + insertedAt;
		return ret;
	}
}
