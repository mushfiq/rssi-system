package stu.project.chronolocalization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.example.chronolocalization.R;

import dataobjects.MapRecord;
import dataobjects.ResponseParser;
import dataobjects.WatchPositionRecord;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RestService {
	
public Activity activity;
public static MapRecord[] mapRecord = null;

	public RestService(Activity _activity){
		this.activity = _activity;
	}	
	
	public class LongRunningGetIO extends AsyncTask <Void, Void, String> {
			
											
			protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

			       InputStream in = entity.getContent();
			         StringBuffer out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
			         return out.toString();
			    }
				
				@Override
				protected String doInBackground(Void... params) {
					 HttpClient httpClient = new DefaultHttpClient();
					 HttpContext localContext = new BasicHttpContext();
		             HttpGet httpGet = new HttpGet("http://shironambd.com/api/v1/map/?access_key=529a2d308333d14178f5c54d&format=json");
		             String text = null;
		             try {
		                   HttpResponse response = httpClient.execute(httpGet, localContext);
		                   HttpEntity entity = response.getEntity();
		                   text = getASCIIContentFromEntity(entity);
		             } catch (Exception e) {
		            	 return e.getLocalizedMessage();
		             }

		             return text;
				}	
				
				
				protected void onPostExecute(String results) {					

					MapRecord records = ResponseParser.parseMapRecord(results);
					try
					{	
								
						if( records != null)
				    	{
			    	         new BitmapTask().execute(records);
				    	}
					}catch(Exception e)
					{
						e.printStackTrace();
					}	
				}


			
			} 
	public class BitmapTask extends AsyncTask<MapRecord, Void, Bitmap> {
		
		public Activity getActivity(){
			return activity;
			
		}
	   
		@Override
		protected Bitmap doInBackground(MapRecord... records) {
	        Bitmap map = null;   
    			
	    		for (MapRecord record : records){
	    			
	    			String mapid = record.getMapId();
	    		    String image_url = "http://shironambd.com/api/v1/image/?access_key=529a2d308333d14178f5c54d&id="+mapid;
	    		    
	    		    //temporary For testing the image with another url
//	    			String image_url = "http://www.housing.uic.edu/halls/cmw/map-cmsw-double-room.jpg";
		            map = downloadImage(image_url);
	    			
	    		}
	    		mapRecord = records;
	    		return map;
	    }

	    // Sets the Bitmap returned by doInBackground
	    @Override
	    protected void onPostExecute(Bitmap result) {
			ImageView imageView = (ImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImage);
	        
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			result.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream is = new ByteArrayInputStream( baos.toByteArray());
	
	           //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=200;
            int scale=2;

            float width = 0.0f,height = 0.0f;
			for (MapRecord record : mapRecord){
				
				width = record.getWidth();
				height = record.getHeight();
				  while(true){
		                if(width<REQUIRED_SIZE || height<REQUIRED_SIZE){
		                	width = REQUIRED_SIZE;
		                	height = REQUIRED_SIZE;
		                    break;
		                } 
		                width/=2;
		                height/=2;
		                }

			}
	    		
			 //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap resizedbitmap =  BitmapFactory.decodeStream(is, null, o2);
	    
		    imageView.setImageBitmap(resizedbitmap);

	    }

	    // Creates Bitmap from InputStream and returns it
	    private Bitmap downloadImage(String url) {

	        Bitmap bitmap = null;
	        InputStream stream = null;
	        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	        bmOptions.inSampleSize = 1;

	        try {
	            stream = getHttpConnection(url);
	            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
	            stream.close();

	            return bitmap;
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	        return bitmap;
	    }

	    // Makes HttpURLConnection and returns InputStream
	    private InputStream getHttpConnection(String urlString) throws IOException {
	        InputStream stream = null;
	        URL url = new URL(urlString);

	        URLConnection connection = url.openConnection();

	        try {
	            HttpURLConnection httpConnection = (HttpURLConnection) connection;
	            httpConnection.setRequestMethod("GET");
	            httpConnection.connect();
	            stream = httpConnection.getInputStream();

	            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                stream = httpConnection.getInputStream();

	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return stream;
	    }
	}

}

