package dataobjects;

import java.util.Date;
/**
 * This class contains the information of the map. It includes the 
 * height and width of the map and mapImage Id for retrieving the mapImage 
 * @author Maheswari
 *
 */public class MapRecord
 {
	 String mapId = "";//unique map id
 
	 float height = 0.0f;//height of the map which is set to imageview height
	 float width = 0.0f;//width of the map  which is set to imageview width
	 
	 float scalingX = 0.0f;//To scale the X line of imageview
	 float scalingY = 0.0f;//To scale the Y line of imageview
	 
	 float offsetX = 0.0f;//To remove extra X line of white space from mapimage
	 float offsetY = 0.0f;//To remove extra X line of white space from mapimage
	 
	 private Date updateTime = new Date();//To specify the time when the map is updated
	 private String id = "";// unique map image id
 
 
 /**
	 * The following method to set and get the value of above parameter 
	 * @return
	 */
 public void setMapId(String mapId)
 {
	 this.mapId = mapId;
 }
 
 public String getMapId()
 {
	 return mapId;
 }
 
 
 public void setHeight(float height)
 {
	 this.height = height;
 }
 
 public float getHeight()
 {
	 return height;
 }

 
 public void setWidth(float width)
 {
	 this.width = width;
 }
 
 public float getWidth()
 {
	 return width;
 }
 
 
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
 

	
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

}
