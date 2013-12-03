package dataobjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ResponseParser {
	
	public static ArrayList<WatchPositionRecord> getParsedResponse(String results) {
		
		ArrayList<WatchPositionRecord> deviceInformation = new ArrayList<WatchPositionRecord>();
		if (results!=null) {
			
            try {
            	
            	JSONObject obj = new JSONObject(results);
            	JSONArray  responseObject = obj.getJSONArray("objects");

            	for(int i = 0 ; i < responseObject.length() ; i++){
            		
            		WatchPositionRecord watchPositionRecord = new WatchPositionRecord();
            		
                    JSONObject jsonObj = (JSONObject)responseObject.get(i);
                    
                    watchPositionRecord.setMapId(jsonObj.getInt("mapId"));
                    watchPositionRecord.setWatchId(jsonObj.getString("watchId").toString());
                    
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" );
                    try
					{
						Date insertedAt = dateFormatter.parse(jsonObj.getString("insertedAt").toString());
						watchPositionRecord.setInsertedAt(insertedAt);
					} catch (ParseException e)
					{
						e.printStackTrace();
					}

                    String x = jsonObj.getString("x").toString();
                    Double parseX = Double.parseDouble(x);
                                        
                    String y = jsonObj.getString("y").toString();
                    Double parseY = Double.parseDouble(y);
                    
                    Point position = new Point(parseX.floatValue(),  parseY.floatValue());
                    watchPositionRecord.setPosition(position);
                    
                    deviceInformation.add(watchPositionRecord);
                }
            	return deviceInformation;
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return null;
	}


public static MapRecord parseMapRecord(String results) {
	
	MapRecord mapRecord = null;
		if (results!=null) {
			
            try {
            	
            	JSONObject obj = new JSONObject(results);
            	JSONArray  responseObject = obj.getJSONArray("objects");

            	for(int i = 0 ; i < responseObject.length() ; i++){
            		
            		 mapRecord = new MapRecord();
            		
                    JSONObject jsonObj = (JSONObject)responseObject.get(i);
                    mapRecord.setMapId(jsonObj.getString("id").toString());
                    
                    String height = jsonObj.getString("height").toString();
                    Float parseHeight = Float.parseFloat(height);
                    mapRecord.setHeight(parseHeight);

                    String width = jsonObj.getString("width").toString();
                    Float parsewidth =  Float.parseFloat(width);
                    mapRecord.setWidth(parsewidth);
                    
                    String scaling = jsonObj.getString("scaling").toString();
                    Float parseScaling =  Float.parseFloat(scaling);
                    mapRecord.setSclaing(parseScaling);
                    
                    String offsetX = jsonObj.getString("offsetX").toString();
                    Float parseOffsetX =  Float.parseFloat(offsetX);
                    mapRecord.setOffsetX(parseOffsetX);
                    
                    String offsetY = jsonObj.getString("offsetY").toString();
                    Float parseOffsetY =  Float.parseFloat(offsetY);
                    mapRecord.setOffsetY(parseOffsetY);
                    
                    
                    
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" );
                    try
					{
						Date updatedTime = dateFormatter.parse(jsonObj.getString("updateTime").toString());
						mapRecord.setUpdateTime(updatedTime);
					} catch (ParseException e)
					{
						e.printStackTrace();
					}
                    
//                    mapInformation.add(mapRecord);
                }
            	
            	return mapRecord;
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return null;
	}
}
