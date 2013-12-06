package com.chrono.rest.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class ParseResponse {
	
public ArrayList<Device> getParsedResponse(String results) {
		
		ArrayList<Device> device = new ArrayList<Device>();
		if (results!=null) {
			
try {
            	
       JSONObject obj = new JSONObject(results);
       JSONArray responseObject = obj.getJSONArray("objects");
       
       for(int i = 0 ; i < responseObject.length() ; i++){
   		
   		Device pojoDevice = new Device();
   		
           JSONObject jsonObj = (JSONObject)responseObject.get(i);
           
           pojoDevice.setMapId(jsonObj.getString("mapId").toString());
           pojoDevice.setWatchId(jsonObj.getString("watchId").toString());

           String x = jsonObj.getString("x").toString();
           Double parseX = Double.parseDouble(x);
           pojoDevice.setX(parseX);
           
           String y = jsonObj.getString("y").toString();
           Double parseY = Double.parseDouble(y);
           pojoDevice.setX(parseY);
           
           device.add(pojoDevice);
       }
   	return device;
	} catch (JSONException e) {
		
		e.printStackTrace();
	}

}
return null;
}
}




		



