package com.example.chronolocalization;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import stu.project.chronolocalization.RestService;
import stu.project.chronolocalization.TabLayoutActivity;
import stu.project.chronolocalization.Utilities;
import stu.project.chronolocalization.RestService.LongRunningGetIO;


import dataobjects.ResponseParser;
import dataobjects.Point;
import dataobjects.WatchPositionRecord;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity
{
	private Spinner spinnerChooseWatch;
	private Button buttonDisplayPosition;
	private Button buttonTrackPosition;
	private DrawableImage imageView;
	private TextView positionText;
	private DataManager dataManager;
	

	public String watchID;
	int offset = 0;

	//For rest and voice
	Timer timer = null;
	TextToSpeech tts;
		
	ImageView start;
	ImageView stop;
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		
			if(TabLayoutActivity.tts!=null){
				TabLayoutActivity.tts.stop();
				TabLayoutActivity.tts.shutdown();
				
			}
		
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		setContentView(R.layout.home);
		
		//Intialize the Speech
		start = (ImageView)findViewById(R.id.startImg);
		stop = (ImageView)findViewById(R.id.stopImg);

		Utilities util =new Utilities();
		/*tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
//				Log.d("*****onInit****","onInit********");

				if(status!=TextToSpeech.ERROR){
					
					tts.setLanguage(Locale.UK);
				}
			}
		});

		
*/		
//		setContentView(R.layout.activity_main);
		
		
		// just a simple comment
		//Connects the Application with the Data Source (at the moment a dummy implementation)
		dataManager = new DataManager();

		imageView = (DrawableImage) findViewById(R.id.mapImage);
		
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
		

		int pos_a = 10;
		int pos_b = 350;
		imageView.addReceiver("Receiver1", new Point(pos_a,pos_a));
		imageView.addReceiver("Receiver2", new Point(pos_a,pos_b));
		imageView.addReceiver("Receiver3", new Point(pos_b,pos_a));
		//imageView.addReceiver("Receiver4", new Point(pos_b,pos_b));
		
		/*
		imageView.addReceiver("Receiver1", new Point(20,55));
		imageView.addReceiver("Receiver2", new Point(20,350));
		imageView.addReceiver("Receiver3", new Point(250,350));
		imageView.addReceiver("Receiver4", new Point(400,55));
		*/
		
		spinnerChooseWatch = (Spinner) findViewById(R.id.WatchSpinner);
				
		buttonTrackPosition = (Button) findViewById(R.id.trackPosition);
		buttonTrackPosition.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {				
				  String watchID = spinnerChooseWatch.getSelectedItem().toString();
					new GetPositionTask(watchID).execute();
			  }
			  
			  class GetPositionTask extends AsyncTask<String, Void, String>
			  {
				  String watchID = "";
				  public GetPositionTask(String watchID)
				  {
					  this.watchID = watchID;
				  }
				   
				  protected String doInBackground(String... str)
				  {
				    	String httpResponse = "";
				    	try
				    	{
				    		int limit = 1;
				    		EditText editNumberOfSteps = (EditText) findViewById(R.id.editNumberOfSteps);
				    		Integer limitFromString = Integer.parseInt(editNumberOfSteps.getText().toString());
				    		if( limitFromString != null)
				    			limit = limitFromString;
				    		
				    		//Hard coded watchIDs only for testing => will be removed after code cleaning
				    		int watchNr = 0;
				    		if( watchID.equals("watch2"))
				    		{
				    			watchNr = 4;
				    		}
				    		else if( watchID.equals("watch3"))
				    		{
				    			watchNr = 10;
				    		}
				    		else if( watchID.equals("watch4"))
				    		{
				    			watchNr = 11;
				    		}
				    		String url = "http://shironambd.com/api/v1/watch/?watchId=" + watchID + "&offset=" + offset + "&limit=1&format=json";
				    		url = "http://shironambd.com/api/v1/watch/?access_key=529a2d308333d14178f5c54d&limit=1&watchId=" + watchNr + "&format=json";
					    	
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
							httpResponse = response.toString();
				    	}
				    	catch(Exception e)
				    	{
				    		e.printStackTrace();
				    	}
					 				
						return httpResponse;
				    }

				    protected void onPostExecute(String result)
				    {
				    	String watchID = spinnerChooseWatch.getSelectedItem().toString();
				    	
				    	imageView.clearWatchesToDraw();
				    	imageView.addWatchToDraw(watchID);					    
						imageView.clearWatchPositions(watchID);
						
						try
						{	
							
					    	ArrayList<WatchPositionRecord> records = ResponseParser.getParsedResponse(result);
					    	
					    	if( !records.isEmpty() )
					    	{
					    		for (WatchPositionRecord record : records)
								{	
					    			Point lastPosition = record.getPosition();
							    	float x = lastPosition.getX();
							    	float y = lastPosition.getY();
							    	
							    	int width = imageView.getWidth();
							    	int height = imageView.getHeight();
							    	
							    	float x_inPixel = x / 3.0f * (width-20);
							    	float y_inPixel = y / 3.0f * (height-20);
							    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
							    	 
							    	// Needed for the coordinate transformation of accessed position and the imageview
								    Point oldZero = new Point(0,0);
								    Point newZero = new Point((width)/2,-((height)/2));
								    
								    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
								    
							    	//imageView.addWatchPosition(watchID, lastPosition);
							    	imageView.addWatchPosition(watchID, positionInPixel);
							    	String positionString = "x = " + x + "m, " + "y = " + y + "m at " + record.getInsertedAt(); 
								    positionText.setText(positionString);
								    
								}
					    		imageView.setDrawPath(true);
//					    		String direction = Utilities.getDirection(lastPosition.getX(), p2)
					    		imageView.invalidate();
					    		
					    		
					    	}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							positionText.setText(result);
						}
				    }
				}

			});		
			

	 }
	
	public void startApplication(View view){
		
		LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_id));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Application is started");
        // Toast configuration
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 200, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

		watchID = spinnerChooseWatch.getSelectedItem().toString();
		 
		timer = new Timer();
 		TabLayoutActivity.tts.speak("Device is started to move", TextToSpeech.QUEUE_FLUSH, null);
 		start.setVisibility(View.GONE);
 		stop.setVisibility(View.VISIBLE);

	        timer.scheduleAtFixedRate(new TimerTask() {         
	            @Override
	            public void run() {
	                runOnUiThread(new Runnable()
	                {
	                    @Override
	                    public void run()
	                    {
//	                    	Log.d("inside timer****", "*************");

	            			new GetPositionTask(watchID).execute();

	                    }
	                });
	            }
	        }, 3000,3000);
	        
		
	}
	
	public void stopApplication(View view){

//		Log.d("stopApplication","******");
		
		stop.setVisibility(View.GONE);
		start.setVisibility(View.VISIBLE);

		if(timer !=null){
			TabLayoutActivity.tts.speak("Device is stopped its moving", TextToSpeech.QUEUE_FLUSH, null);

			TabLayoutActivity.tts.stop();
			timer.cancel();
		}

	}

	@Override
	protected void onStart() {
	    super.onStart();
	    
	    RestService resService = new RestService(MainActivity.this);
		watchID = spinnerChooseWatch.getSelectedItem().toString();
		resService.new LongRunningGetIO().execute(); 
	    
	}
	class GetPositionTask extends AsyncTask<String, Void, String>
	  {
		  String watchID = "";
		  public GetPositionTask(String watchID)
		  {
			  this.watchID = watchID;
		  }
		   
		  protected String doInBackground(String... str)
		  {
		    	String httpResponse = "";
		    	try
		    	{

		    		int limit = 1;
		    		EditText editNumberOfSteps = (EditText) findViewById(R.id.editNumberOfSteps);
		    		String stepsValue = editNumberOfSteps.getText().toString();
		    		
		    		Integer limitFromString = null;
		    		
		    		if(!stepsValue.matches("Steps"))
		    		 limitFromString = Integer.parseInt(editNumberOfSteps.getText().toString());

		    		if( limitFromString != null)
		    			limit = limitFromString;
		    		
		    		//Hard coded watchIDs only for testing => will be removed after code cleaning
		    		int watchNr = 0;
		    		if( watchID.equals("watch2"))
		    		{
		    			watchNr = 4;
		    		}
		    		else if( watchID.equals("watch3"))
		    		{
		    			watchNr = 10;
		    		}
		    		else if( watchID.equals("watch4"))
		    		{
		    			watchNr = 11;
		    		}
		    		String url = "http://shironambd.com/api/v1/watch/?watchId=" + watchID + "&offset=" + offset + "&limit=1&format=json";
		    		url = "http://shironambd.com/api/v1/watch/?access_key=529a2d308333d14178f5c54d&limit=" + limit + "&watchId=" + watchNr + "&format=json";
		    		
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
					httpResponse = response.toString();
		    	}
		    	catch(Exception e)
		    	{
		    		e.printStackTrace();
		    	}
			 				
				return httpResponse;
		    }

		    protected void onPostExecute(String result)
		    {
		    	String watchID = spinnerChooseWatch.getSelectedItem().toString();
		    	
		    	imageView.clearWatchesToDraw();
		    	imageView.addWatchToDraw(watchID);					    
				imageView.clearWatchPositions(watchID);
				
				try
				{	
					Point lastPosition = null;
			    	ArrayList<WatchPositionRecord> records = ResponseParser.getParsedResponse(result);
			    	if( !records.isEmpty() )
			    	{
			    		for (WatchPositionRecord record : records)
						{	
			    			 lastPosition = record.getPosition();
					    	float x = lastPosition.getX();
					    	float y = lastPosition.getY();
					    	
					    	int width = imageView.getWidth();
					    	int height = imageView.getHeight();
					    	
					    	float x_inPixel = x / 3.0f * (width-20);
					    	float y_inPixel = y / 3.0f * (height-20);
					    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
					    	 
					    	// Needed for the coordinate transformation of accessed position and the imageview
						    Point oldZero = new Point(0,0);
						    Point newZero = new Point((width)/2,-((height)/2));
						    
						    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
						    
					    	//imageView.addWatchPosition(watchID, lastPosition);
					    	imageView.addWatchPosition(watchID, positionInPixel);
					    	String positionString = "x = " + x + "m, " + "y = " + y + "m at " + record.getInsertedAt(); 
						    positionText.setText(positionString);

						}
			    		imageView.setDrawPath(true);
					    Point currentLocation = new Point(lastPosition.getX(),lastPosition.getY());
			    		String direction = Utilities.getDirection(currentLocation);
//			    		Log.d("********",""+direction);
			    		Thread.sleep(2000);
			    		tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null);
			    		imageView.invalidate();
			    		
			    		
			    	}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					positionText.setText(result);
				}
		    }
		}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

//Code of trackPosition which you have done 
