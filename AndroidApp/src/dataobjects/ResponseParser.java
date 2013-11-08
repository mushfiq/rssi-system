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
                    
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ss" );
                    try
					{
						dateFormatter.parse(jsonObj.getString("insertedAt").toString());
					} catch (ParseException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    //want to implement
                    /*String msgTime = jsonObj.getString("insertedAt").toString();
                    pojoDeviceInformation.setMsgTime(msgTime);*/

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
}