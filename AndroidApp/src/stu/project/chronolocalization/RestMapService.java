package stu.project.chronolocalization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.example.chronolocalization.DrawableImage;
import com.example.chronolocalization.MapImageView;
import com.example.chronolocalization.R;
import com.example.chronolocalization.WatchUserActivity;

import dataobjects.MapRecord;
import dataobjects.Point;
import dataobjects.ResponseParser;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class RestMapService {
	
public Activity activity;
public static MapRecord[] mapRecord = null;
public ArrayList<ReceiverRecord> receiverRecord;


	public RestMapService(Activity _activity){
		this.activity = _activity;
	}	
	
	public class GetMapRecordTask extends AsyncTask <Void, Void, String> {
			
											
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
	    		    
	    		    //for temporary implemetation
	    		    mapid="0";
	    		    
	    		    String receiver_url = "http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&mapId="+mapid+"&format=json";

	    		    receiverRecord = downloadReceiverInformation(receiver_url);
	    		    

	    		    map = downloadImage(image_url);
	    			
	    		}
	    		mapRecord = records;
	    		return map;
	    }

	    // Sets the Bitmap returned by doInBackground
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	
          WatchUserActivity.dialog.dismiss();

			ImageView imageView = (ImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImage);
	        
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			result.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream is = new ByteArrayInputStream( baos.toByteArray());
	
	           //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=360;
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
		    MapImageView drawableImage = (MapImageView)this.getActivity().findViewById(R.id.mapImage);

		    int count = 1;
		     float[] x = new float[6];
		     float[] y= new float[6];
		    
	     if( !receiverRecord.isEmpty() )
	    	{
	    		for (ReceiverRecord record : receiverRecord)
				{	
	    			x[count] = record.getX();
	    			y[count] = record.getY();
	    			count++;
//	    	        drawableImage.addReceiver(record.getReceiverId(), new Point(record.getX(),record.getY()));
				}
	    	}	
	     
	     int x1 = 20;
			int x2 = 130;
			int x3 = 285;
			int x4 = 410;
			int y1 = 13;
			int y2 = 235;
			int y3 = 305;
			int y4 = 405;
			drawableImage.addReceiver("Receiver1", new Point(x1,y1));
			drawableImage.addReceiver("Receiver2", new Point(x2,y1));
			drawableImage.addReceiver("Receiver3", new Point(x4,y1));
			drawableImage.addReceiver("Receiver4", new Point(x1,y2));
			drawableImage.addReceiver("Receiver5", new Point(x4,y2));
			drawableImage.addReceiver("Receiver6", new Point(x3,y3));
			drawableImage.addReceiver("Receiver7", new Point(x4,y4));
		
	     /* drawableImage.addReceiver("Receiver1", new Point(x[1],y[1]));
	     drawableImage.addReceiver("Receiver2", new Point(x[2],y[1]));
	     drawableImage.addReceiver("Receiver3", new Point(x[4],y[1]));
	     drawableImage.addReceiver("Receiver4", new Point(x[1],y[2]));
	     drawableImage.addReceiver("Receiver5", new Point(x[4],y[2]));
	     drawableImage.addReceiver("Receiver6", new Point(x[3],y[3]));
	     drawableImage.addReceiver("Receiver7", new Point(x[4],y[4]));
		
	    */ 

	        //Correct Implementation
	        /*if( !receiverRecord.isEmpty() )
	    	{
	    		for (ReceiverRecord record : receiverRecord)
				{	
	    	        drawableImage.addReceiver(record.getReceiverId(), new Point(record.getX(),record.getY())); 

				}
	    	}	
*/
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

	private ArrayList<ReceiverRecord> downloadReceiverInformation(String url){
		   
        InputStream stream = null;
    	String result = "";
    	ArrayList<ReceiverRecord> receiverRecords = null;
        try {
        	
        	HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet(url);
             String text = null;
             StringBuffer out;
            
                   HttpResponse response = httpClient.execute(httpGet, localContext);
                   HttpEntity entity = response.getEntity();
                   InputStream in = entity.getContent();
			          out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
            		    	receiverRecords = ResponseParser.parseReceiverRecord(out.toString());

            return receiverRecords;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return receiverRecords;

    }

}

