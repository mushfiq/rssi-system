package services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import userInterface.MapImageView;
import userInterface.WatchUserActivity;

import dataobjects.CoordinateTransformation;
import dataobjects.Point;
import dataobjects.ReceiverRecord;
import dataobjects.MapRecord;
import dataobjects.ResponseParser;
import dataobjects.WatchPositionRecord;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class RestMapService
{
	

	public WatchUserActivity activity; //The activity which include main activity for our application to display map and receiver position 
	public static MapRecord mapRecord = null; //The mapRecord which is for getting map details from dataBase server
	public ArrayList<ReceiverRecord> receiverRecords;//The receiverRecords which is for getting receiver details from dataBase server
	
	public static int mapId;//This is to get MapId corresponding to that watch(Device)
	/**
	 * This method is to retrieve the  map details from server by using rest call
	 * 
	 * @author Maheswari
	 *
	 */
	
	public RestMapService(WatchUserActivity activity)
	{
		this.activity = activity;
	}	
	
	public class GetMapRecordTask extends AsyncTask <String, Void, String>
	{
		String mapId = null;
		public GetMapRecordTask(String mapId)
		{
			this.mapId = mapId;
		}
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
		{
	       InputStream in = entity.getContent();
	       StringBuffer out = new StringBuffer();
	       int n = 1;
	       while (n>0)
	       {
	    	   byte[] b = new byte[4096];
	    	   n =  in.read(b);
	    	   if (n>0) out.append(new String(b, 0, n));
    	   }
	       return out.toString();
	    }
			
		@Override
		protected String doInBackground(String... mapIds)
		{
			Log.e("GET_MAP_TASK", "Http-GET MAP started...");
			String text = null;
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://shironambd.com/api/v1/map/?access_key=529a2d308333d14178f5c54d&mapId=" + mapId + "&format=json");
			try 
			{
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			}
			catch (Exception e)
			{
				return e.getLocalizedMessage();
			}
		

			Log.e("GET_MAP_TASK", "Http-GET MAP finished...");
             return text;
		}	
			
			
		protected void onPostExecute(String result)
		{
			Log.e("GET_MAP_TASK", "Parse JSON started...");
			MapRecord record = ResponseParser.parseMapRecord(result);
			Log.e("GET_MAP_TASK", "Parse JSON finished");
			try
			{							
				if( record != null)
		    	{
					new BitmapTask().execute(record);
		    	}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		}
	} 
	
	/**
	 * This method is to retrieve the  map image from server by using rest call
	 * 
	 * @author Maheswari
	 *
	 */
	public class BitmapTask extends AsyncTask<MapRecord, Void, Bitmap>
	{
		ProgressDialog dialog;
		public Activity getActivity()
		{
			return activity;			
		}
	   
		@Override
		protected Bitmap doInBackground(MapRecord... record)
		{
	        Bitmap map = null;   
	        
	        if( record.length > 0)
	        {	        	
	        	Log.e("GET_MAP_TASK", "Getting Image started...");
	        	mapRecord = record[0];
    			String id = mapRecord.getId();
    		    String image_url = "http://shironambd.com/api/v1/image/?access_key=529a2d308333d14178f5c54d&id="+id;
    		    map = downloadImage(image_url);
    		    Log.e("GET_MAP_TASK", "Getting Image finished");
   		    
    		    String receiver_url = "http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&mapId="
						+ mapRecord.getMapId() + "&format=json";

    		    receiverRecords = downloadReceiverInformation(receiver_url);
					
	    	}
	        return map;
	    }

	    // Sets the Bitmap returned by doInBackground
	    @Override
	    protected void onPostExecute(Bitmap result)
	    {
			MapImageView imageView = (MapImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImage);
	        
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			result.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream is = new ByteArrayInputStream( baos.toByteArray());
	
	           //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=360;
            int scale=2;

            float width = 0.0f,height = 0.0f;
			
			width = mapRecord.getWidth();
			height = mapRecord.getHeight();
			while(true)
			{
				if(width<REQUIRED_SIZE || height<REQUIRED_SIZE)
				{
					width = REQUIRED_SIZE;
					height = REQUIRED_SIZE;
					break;
				} 
				width/=2;
				height/=2;
			}
			
	    		
			 //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap resizedbitmap =  BitmapFactory.decodeStream(is, null, o2);
	    
		    imageView.setImageBitmap(resizedbitmap);
		    imageView.clearReceiverPositions();
		    
		    float scalingX = mapRecord.getScalingX();
	    	float scalingY = mapRecord.getScalingY();
	    	
	    	float offsetX = mapRecord.getOffsetX();
	    	float offsetY = mapRecord.getOffsetY();
	    	
	    	Point oldZero = new Point(0.0f, 0.0f);
	    	Point newZero = new Point(offsetX, offsetY);
	    	
	    	CoordinateTransformation coordinateTransformation = new CoordinateTransformation(oldZero, newZero);
		    
		    for(ReceiverRecord receiverRecord : receiverRecords)
		    {
		    	Point receiverPosition = receiverRecord.getReceiverPosition();
		    	System.out.println( receiverPosition + " -> " + coordinateTransformation.transformPosition(receiverPosition));
		    	imageView.addReceiver(receiverRecord.getReceiverId(), receiverRecord.getReceiverPosition());
		    }
		    		    
		    imageView.invalidate();

	    }

	    // Creates Bitmap from InputStream and returns it
	    private Bitmap downloadImage(String url)
	    {

	        Bitmap bitmap = null;
	        InputStream stream = null;
	        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	        bmOptions.inSampleSize = 1;

	        try
	        {
	            stream = getHttpConnection(url);
	            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
	            stream.close();

	            return bitmap;
	        }
	        catch (IOException e1)
	        {
	            e1.printStackTrace();
	        }
	        return bitmap;
	    }

	    // Makes HttpURLConnection and returns InputStream
	    private InputStream getHttpConnection(String urlString) throws IOException
	    {
	        InputStream stream = null;
	        URL url = new URL(urlString);

	        URLConnection connection = url.openConnection();

	        try 
	        {
	            HttpURLConnection httpConnection = (HttpURLConnection) connection;
	            httpConnection.setRequestMethod("GET");
	            httpConnection.connect();
	            stream = httpConnection.getInputStream();

	            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
	            {
	                stream = httpConnection.getInputStream();
	            }
	        } 
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	        return stream;
	    }
	}
	//This method is to retrieve the  receiver information from server by using rest call
	private ArrayList<ReceiverRecord> downloadReceiverInformation(String url)
	{

		ArrayList<ReceiverRecord> receiverRecords = null;
		try
		{

			URL obj = new URL(url);
	    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    	con.setRequestMethod("GET");
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			Log.e("GET_RECEIVER", "Http request finished");
			
			receiverRecords = ResponseParser
					.parseReceiverRecord(response.toString());

			return receiverRecords;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		return receiverRecords;

	}

	/*
	 * This method is to retrieve the  watch information from server by using rest call
	 * 
	 * @author Maheswari
	 *
	 */
	public class GetMap extends AsyncTask <String, Void, String>
	{
		
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
		{
	       InputStream in = entity.getContent();
	       StringBuffer out = new StringBuffer();
	       int n = 1;
	       while (n>0)
	       {
	    	   byte[] b = new byte[4096];
	    	   n =  in.read(b);
	    	   if (n>0) out.append(new String(b, 0, n));
    	   }
	       return out.toString();
	    }
			
		@Override
		protected String doInBackground(String... arg)
		{
			Log.e("GET_MAP_TASK", "Http-GET MAP started...");
			String text = null;
    		String url = "http://shironambd.com/api/v1/watch/?access_key=529a2d308333d14178f5c54d&watchId=" + 0 + "&format=json";
//String url = "http://shironambd.com/api/v1/map/?access_key=529a2d308333d14178f5c54d&format=json";
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(url);
			try 
			{
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			}
			catch (Exception e)
			{
				return e.getLocalizedMessage();
			}
		

			Log.e("GET_MAP_ID_TASK", "Http-GET MAP finished...");
             return text;
		}	
			
			
		protected void onPostExecute(String result)
		{

			if(result!=null){
	    	WatchPositionRecord record = ResponseParser.getMapIdResponse(result);
			
		    	if(record!=null){
					mapId = record.getMapId();
	
					new GetMapRecordTask(String.valueOf(record.getMapId())).execute();
		    	}
			}
		
		}
	} 
	

}

