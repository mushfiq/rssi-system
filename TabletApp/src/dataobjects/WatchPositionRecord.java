package dataobjects;

import java.util.Date;

/**
 * This class represent a single position of the watch. It includes the time when this position was tracked and the map
 * where the watch was detected
 * @author Silvio
 *
 */
public class WatchPositionRecord
{	
	private String watchId = ""; //The unique watch id
	
	/**
	 * Getter for watchId
	 * @author Silvio
	 * @return The id of the watch
	 */
	public String getWatchId()
	{
		return watchId;
	}
	
	/**
	 * Setter for the watchId
	 * @author Silvio
	 * @param watchId The id of the watch
	 */
	public void setWatchId(String watchId)
	{
		this.watchId = watchId;
	}
	
	private Point position; //The position where the watch was detected
	
	/**
	 * Getter for the watch position
	 * @author Silvio
	 * @return The position where the watch was detected
	 */
	public Point getPosition()
	{
		return position;
	}
	
	/**
	 * Setter for the watch position 
	 * @author Silvio
	 * @param position The position to set
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}
	
	private Date insertedAt = new Date(); // The time when the watch was detected
	
	/**
	 * Getter for the insertion date
	 * @author Silvio
	 * @return The date when the watch was detected
	 */
	public Date getInsertedAt()
	{
		return insertedAt;
	}
	
	/**
	 * Setter for the insertion date
	 * @author Silvio
	 * @param insertedAt The date when the watch was detected
	 */
	public void setInsertedAt(Date insertedAt)
	{
		this.insertedAt = insertedAt;
	}
	
	private int mapId = -1; // The unique map id of the map on which the watch was detected
	
	/**
	 * Getter for the map id
	 * @author Silvio
	 * @return The unique map id of the map on which the watch was detected
	 */
	public int getMapId()
	{
		return mapId;
	}
	
	/**
	 * Setter for the map id
	 * @author Silvio
	 * @param mapId The unique map id of the map on which the watch was detected
	 */
	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	@Override
	/**
	 * This returns the object representation as a string
	 * @author Silvio
	 * @return The string which represent this object 
	 */
	public String toString()
	{
		String ret = "Location of " + watchId + " located at map " + mapId + " at position" + position + " at time " + insertedAt;
		return ret;
	}
}
