package services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import dataobjects.MapRecord;
import dataobjects.ResponseParser;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class RestMapService
{
	
	public Activity activity;
	public static MapRecord mapRecord = null;

	public RestMapService(Activity activity)
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
	
	public class BitmapTask extends AsyncTask<MapRecord, Void, Bitmap>
	{
		
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
    			String mapid = mapRecord.getMapId();
    		    String image_url = "http://shironambd.com/api/v1/image/?access_key=529a2d308333d14178f5c54d&id="+mapid;
    		    map = downloadImage(image_url);
    		    Log.e("GET_MAP_TASK", "Getting Image finished");
	    			
	    	}
	        return map;
	    }

	    // Sets the Bitmap returned by doInBackground
	    @Override
	    protected void onPostExecute(Bitmap result)
	    {
			ImageView imageView = (ImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImage);
	        
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

}

