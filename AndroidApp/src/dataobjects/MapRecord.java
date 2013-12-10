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
     
     float scaling = 0.0f;
     public void setScaling(float scaling)
     {
    	 this.scaling = scaling;
     }
     
     float offsetX = 0.0f;
     public void setOffsetX(float offsetX)
     {
    	 this.offsetX = offsetX;
     }
     
     float offsetY = 0.0f;
     public void setOffsetY(float offsetY)
     {
    	 this.offsetY = offsetY;
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

}
