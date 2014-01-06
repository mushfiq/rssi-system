package dataobjects;

import java.util.Date;

public class MapRecord
{
	 String mapId = "";
     public void setMapId(String mapId)
     {
    	 this.mapId = mapId;
     }
     
     public String getMapId()
     {
    	 return mapId;
     }
     
     float height = 0.0f;
     public void setHeight(float height)
     {
    	 this.height = height;
     }
     
     public float getHeight()
     {
    	 return height;
     }

     float width = 0.0f;
     public void setWidth(float width)
     {
    	 this.width = width;
     }
     
     public float getWidth()
     {
    	 return width;
     }
     
     float scalingX = 0.0f; //The scaling which give us the information how many pixel represent one meter
     
     /**
      * Getter for the scalingX value
      * @author Silvio
      * @return The scaling for the x-direction
      */
     public float getScalingX()
     {
    	 return scalingX;
     }
     
     /**
      * Setter for the scalingX value
      * @author Silvio
      * @param scaling The scaling value to set
      */
     public void setScalingX(float scaling)
     {
    	 this.scalingX = scaling;
     }
     
     float scalingY = 0.0f; //The scaling which give us the information how many pixel represent one meter
     
     /**
      * Getter for the scalingY value
      * @author Silvio
      * @return The scaling for the y-direction
      */
     public float getScalingY()
     {
    	 return scalingY;
     }
     
     /**
      * Setter for the scalingY value
      * @author Silvio
      * @param scaling The scaling value to set
      */
     public void setScalingY(float scaling)
     {
    	 this.scalingY = scaling;
     }
     
     float offsetX = 0.0f; //The first offset value for the x-direction
     
     /**
      * Getter for the first x-coordinate offset of the map
      * @author Silvio
      * @return The first x-direction offset value of the map
      */
     public float getOffsetX()
     {
    	 return offsetX;
     }
     
     /**
      * Setter for the first offset coordinate
      * @author Silvio
      * @param offsetX The offset value to set
      */
     public void setOffsetX(float offsetX)
     {
    	 this.offsetX = offsetX;
     }
     
     float offset2X = 0.0f; //The second offset value for the x-direction
     
     /**
      * Getter for the second x-coordinate offset of the map
      * @author Silvio
      * @return The second x-direction offset value of the map
      */
     public float getOffset2X()
     {
    	 return offset2X;
     }
     
     /**
      * Setter for the second offset coordinate
      * @author Silvio
      * @param offsetX The offset value to set
      */
     public void setOffset2X(float offsetX)
     {
    	 this.offset2X = offsetX;
     }
     
     float offsetY = 0.0f; //The first offset value for the y-coordinate
     
     /**
      * Getter for the first offset y-coordinate
      * @author Silvio
      * @return The first offset value for the y-coordinate
      */
     public float getOffsetY()
     {
    	 return offsetY;
     }
     
     /**
      * Setter for the first offset coordinate
      * @author Silvio
      * @param offsetY The offset value to set
      */
     public void setOffsetY(float offsetY)
     {
    	 this.offsetY = offsetY;
     }
     
     float offset2Y = 0.0f; //The second offset value for the y-coordinate
     
     /**
      * Getter for the second offset y-coordinate
      * @author Silvio
      * @return The second offset value for the y-coordinate
      */
     public float getOffset2Y()
     {
    	 return offset2Y;
     }
     
     /**
      * Setter for the second offset coordinate
      * @author Silvio
      * @param offsetY The offset value to set
      */
     public void setOffset2Y(float offsetY)
     {
    	 this.offset2Y = offsetY;
     }
     
    private Date updateTime = new Date();
 	
 	public Date getUpdateTime()
 	{
 		return updateTime;
 	}
 	public void setUpdateTime(Date updateTime)
 	{
 		this.updateTime = updateTime;
 	}
 	
 	private String id = "";
 	public String getId()
 	{
 		return id;
 	}
 	
 	public void setId(String id)
 	{
 		this.id = id;
 	}

}
