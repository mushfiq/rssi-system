package com.chrono.rest.device;

import java.sql.Timestamp;

public class Device {
	private String watchId;
	 private String mapId;
	 private Double x;
	 private Double y;
	 private Timestamp msgTime;
	  

	 public String getWatchId() {
	        return watchId;
	    }
	    public void setWatchId(String watchId) {
	        this.watchId = watchId;
	    }
	  
	  public String getMapId() {
	    return mapId;
	  }

	  public void setMapId(String mapId ) {
	    this.mapId = mapId;
	  }

	  public Double getX() {
	    return x;
	  }

	  public void setX(Double x) {
	    this.x = x;
	  }
	
	  public Double getY() {
		    return y;
		  }

		  public void setY(Double y) {
		    this.y = y;
		  }
		  
		  public Timestamp getMsgTime() {
		        return msgTime;
		    }
		    public void setMsgTime(Timestamp msgTime) {
		        this.msgTime = msgTime;
				
			}
}

