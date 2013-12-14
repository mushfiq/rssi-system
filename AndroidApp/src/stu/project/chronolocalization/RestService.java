package stu.project.chronolocalization;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


import com.example.chronolocalization.DrawableImage;
import com.example.chronolocalization.MainActivity;
import com.example.chronolocalization.R;
import com.example.chronolocalization.WatchUserActivity;

import dataobjects.MapRecord;
import dataobjects.Point;
import dataobjects.ResponseParser;
import dataobjects.WatchPositionRecord;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class RestService {
	
public Activity activity;
public static MapRecord[] mapRecord = null;

public ArrayList<ReceiverRecord> receiverRecord;
public ProgressBar progressbar;


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
//					Log.d("Map Information ----",""+records);
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
//    		ArrayList<ReceiverRecord> receiverRecord = null;
	    		for (MapRecord record : records){
	    			
	    			String mapid = record.getMapId();
	    		    String image_url = "http://shironambd.com/api/v1/image/?access_key=529a2d308333d14178f5c54d&id="+mapid;
	    		    //for temporary implemetation
	    		    mapid="0";
	    		    
	    		    String receiver_url = "http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&mapId="+mapid+"&format=json";

	    		    receiverRecord = downloadReceiverInformation(receiver_url);
	    		    
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
	    	Log.d("********progress","");
	    	
//            WatchUserActivity.dialog.dismiss();
            MainActivity.dialog.dismiss();
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
		    
		    
			DrawableImage drawableImage = (DrawableImage)this.getActivity().findViewById(R.id.mapImage);

		    int pos_a = 10; 
	        int pos_b = 255; 
	        drawableImage.addReceiver("Receiver1", new Point(pos_a,pos_a)); 
	        drawableImage.addReceiver("Receiver2", new Point(pos_a,pos_b)); 
	        drawableImage.addReceiver("Receiver3", new Point(pos_b,pos_a)); 
	        drawableImage.addReceiver("Receiver4", new Point(pos_b,pos_b)); 
		     Log.d("********drawableImage", ""+drawableImage);
		     
		     
		     Bitmap bmSrc1 = ((BitmapDrawable)drawableImage.getDrawable()).getBitmap();

		   /*  ImageView copyImage = (ImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImageFromDB);;

		     copyImage.setImageBitmap(bmSrc1);
*//*
		     Bitmap bitmap=BitmapFactory.decodeFile(this.getActivity().getResources(), drawableImage);
		     ByteArrayOutputStream stream=new ByteArrayOutputStream();
		     bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
		     byte[] image=stream.toByteArray();
*/		     /*
	     ImageView copyImage = (ImageView)this.getActivity().findViewById(com.example.chronolocalization.R.id.mapImageFromDB);;
	     copyImage = drawableImage;
	     
	     Log.d("********copyImage", ""+copyImage);*/
//	     copyImage.setImageDrawable(Drawable(drawableImage);
	     
	     
	     /*if( !receiverRecord.isEmpty() )
	    	{
	    		for (ReceiverRecord record : receiverRecord)
				{	
	    			Log.d("x----",""+record.getX());
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

