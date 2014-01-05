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
     
     float scalingX = 0.0f;
     /**
      * @author Silvio
      * @return
      */
     public float getScalingX()
     {
    	 return scalingX;
     }
     
     public void setScalingX(float scaling)
     {
    	 this.scalingX = scaling;
     }
     
     float scalingY = 0.0f;
     
     /**
      * @author Silvio
      * @return
      */
     public float getScalingY()
     {
    	 return scalingY;
     }
     
     public void setScalingY(float scaling)
     {
    	 this.scalingY = scaling;
     }
     
     float offsetX = 0.0f;
     /**
      * @author Silvio
      * @return
      */
     public float getOffsetX()
     {
    	 return offsetX;
     }
     public void setOffsetX(float offsetX)
     {
    	 this.offsetX = offsetX;
     }
     
     float offset2X = 0.0f;
     /**
      * @author Silvio
      * @return
      */
     public float getOffset2X()
     {
    	 return offset2X;
     }
     public void setOffset2X(float offsetX)
     {
    	 this.offset2X = offsetX;
     }
     
     float offsetY = 0.0f;
     /**
      * @author Silvio
      * @return
      */
     public float getOffsetY()
     {
    	 return offsetY;
     }
     public void setOffsetY(float offsetY)
     {
    	 this.offsetY = offsetY;
     }
     
     float offset2Y = 0.0f;
     /**
      * @author Silvio
      * @return
      */
     public float getOffset2Y()
     {
    	 return offset2Y;
     }
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
