package com.example.chronolocalization;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import stu.project.chronolocalization.RestService;
import stu.project.chronolocalization.TabLayoutActivity;
import stu.project.chronolocalization.Utilities;


import dataobjects.HelperFunctions;
import dataobjects.ResponseParser;
import dataobjects.Point;
import dataobjects.WatchPositionRecord;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity
{
	private Spinner spinnerChooseWatch;
	private Button buttonDisplayPosition;
	private Button buttonTrackPosition;
	private MapImageView imageView;
	private TextView positionText;
	private DataManager dataManager;
	public static ProgressDialog dialog;
	private CheckBox checkBoxDrawPath;

	boolean drawWatchPath = false;
	
	//TODO Get the Update Rate from a configuration file or the second tab of the activity
	static int UPDATE_RATE = 200; // in milliseconds 
	
	//TODO Get the length and width from the maprecord
	static float MAP_LENGTH = 6.0f; // meassured in meter
	static float MAP_HEIGHT = 6.0f; // meassured in meter

	
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
		
		start = (ImageView)findViewById(R.id.startImg);
		stop = (ImageView)findViewById(R.id.stopImg);

		Utilities util =new Utilities();
		
		
		
imageView = (MapImageView) findViewById(R.id.mapImage);
		
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
		checkBoxDrawPath = (CheckBox) findViewById(R.id.checkBoxDrawPath);
	
		//Positions of Receiver hard coded in pixel
		//for the test_room_fifth_floor to fit the real environment
		
		/*int x1 = 40;
		int x2 = 155;
		int x3 = 310;
		int x4 = 435;
		int y1 = 13;
		int y2 = 235;
		int y3 = 305;
		int y4 = 405;
		imageView.addReceiver("Receiver1", new Point(x1,y1));
		imageView.addReceiver("Receiver2", new Point(x2,y1));
		imageView.addReceiver("Receiver3", new Point(x4,y1));
		imageView.addReceiver("Receiver4", new Point(x1,y2));
		imageView.addReceiver("Receiver5", new Point(x4,y2));
		imageView.addReceiver("Receiver6", new Point(x3,y3));
		imageView.addReceiver("Receiver7", new Point(x4,y4));
	*/
		
		spinnerChooseWatch = (Spinner) findViewById(R.id.WatchSpinner);
		spinnerChooseWatch.requestFocus();
		
	
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
		if(TabLayoutActivity.isVoiceEnabled)
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
	                    	Log.e("GET_POSITION_TASK", "New GetPositionTaskStarted");
	            			new GetPositionTask(watchID).execute();

	                    }
	                });
	            }
	        }, 0,2000);
	        
		
	}
	
	public void stopApplication(View view){

//		Log.d("stopApplication","******");
		LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_id));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Application is stopped");
        // Toast configuration
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 200, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

		stop.setVisibility(View.GONE);
		start.setVisibility(View.VISIBLE);

		if(timer !=null){
			if(TabLayoutActivity.isVoiceEnabled)
				TabLayoutActivity.tts.speak("Device is stopped its moving", TextToSpeech.QUEUE_FLUSH, null);

			TabLayoutActivity.tts.stop();
			timer.cancel();
		}

	}

	@Override
	protected void onStart() {
	    super.onStart();
         dialog = new ProgressDialog(MainActivity.this);

	    RestService resService = new RestService(MainActivity.this);
		watchID = spinnerChooseWatch.getSelectedItem().toString();
//		   progressBar.setVisibility(View.VISIBLE);
//		   dialog.setTitle("Loading...");
		dialog.setMessage("Loading Map Please wait...");

dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
           
		resService.new GetMapTask().execute(); 
	    
	}
	class GetPositionTask extends AsyncTask<String, Void, String>
	  {
		  String watchID = "";
		  
		  public GetPositionTask(String watchID)
		  {
			  this.watchID = watchID;
		  }
		   
		  protected void onPreExecute() {
//			   progressBar.setVisibility(View.VISIBLE);
			          
			     }
		  protected String doInBackground(String... str)
		  {
		    	String httpResponse = "";
		    	try
		    	{

		    		
		    		int limit = 1;
		    		
		    		// If the checkbox is checked, we need to visualize the path of the watch
		    		// therefore we need the number of positions we want to display
		    		if( checkBoxDrawPath.isChecked() )
		    		{
		    			drawWatchPath = true;
		    			
			    		EditText editNumberOfSteps = (EditText) findViewById(R.id.editNumberOfSteps);
			    		String stepsValue = editNumberOfSteps.getText().toString();
			    		
			    		Integer limitFromString = null;
			    		
			    		if(!stepsValue.matches("Steps"))
			    		 limitFromString = Integer.parseInt(editNumberOfSteps.getText().toString());
	
			    		if( limitFromString != null)
			    			limit = limitFromString;
		    		}
	

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
               		
		    		Log.e("WATCH_USER_ACTIVITY", "Http request started");
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
					Log.e("WATCH_USER_ACTIVITY", "Http request finished");
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

		    	Log.e("WATCH_USER_ACTIVITY", "Display position started");

		    	String watchID = spinnerChooseWatch.getSelectedItem().toString();
		    	Point positionInPixel = null;
		    	
		    	imageView.clearWatchesToDraw();
		    	imageView.addWatchToDraw(watchID);					    
				imageView.clearWatchPositions(watchID);
				try
				{	
					
					Point lastPosition = null;
					float lastXPosition = 0.0f;
					float lastYPosition = 0.0f;;
					Date lastTimestamp = new Date();
					
			    	ArrayList<WatchPositionRecord> records = ResponseParser.getParsedResponse(result);
			    	if( !records.isEmpty() )
			    	{
			    		for (WatchPositionRecord record : records)
						{	
			    			lastPosition = record.getPosition();
			    			lastXPosition = lastPosition.getX();
			    			lastYPosition = lastPosition.getY();
			    			lastTimestamp = record.getInsertedAt();
					    	
					    	int width = imageView.getWidth();
					    	int height = imageView.getHeight();
					    	
					    	float x_inPixel = lastXPosition / MAP_LENGTH * (width-20);
					    	float y_inPixel = lastYPosition / MAP_HEIGHT * (height-20);
					    	positionInPixel = new Point(x_inPixel, y_inPixel);
					    	 
					    	// Needed for the coordinate transformation of accessed position and the imageview
						    Point oldZero = new Point(0,0);
						    Point newZero = new Point(0,-height);
						    
						    positionInPixel = HelperFunctions.transformPosition(oldZero, newZero, positionInPixel);
					    	imageView.addWatchPosition(watchID, positionInPixel);
					    	

						}
			    		
			    		int positionsToDraw = 1;
			    		EditText editNumberOfSteps = (EditText) findViewById(R.id.editNumberOfSteps);
			    		String stepsValue = editNumberOfSteps.getText().toString();
			    		
			    		Integer limitFromString = null;
			    		
			    		if(!stepsValue.matches("Steps"))
			    		 limitFromString = Integer.parseInt(editNumberOfSteps.getText().toString());

			    		if( limitFromString != null)
			    			positionsToDraw = limitFromString;
			    		
			    		imageView.setPositionsToDraw(positionsToDraw);
			    		
			    		String positionString = "x = " + lastXPosition + "m, " + "y = " + lastYPosition + "m at " + lastTimestamp; 
			    		//String positionString = "x = " + lastXPosition + "m, " + "y = " + lastYPosition + "m at " + new Date();
					    positionText.setText(positionString);
			    		
			    		imageView.setDrawPath(drawWatchPath); // drawWatchPath == true => draw a path of the last positions
					    //Thread.sleep(UPDATE_RATE); // wait UPDATE_RATE milliseconds until we update the positions
			    		
			    		 Point currentLocation = new Point(positionInPixel.getX(),positionInPixel.getY());
				    		String direction = Utilities.getDirection(currentLocation);
				    		Log.d("********",""+direction);
				    		Thread.sleep(2000);
				    		if(TabLayoutActivity.isVoiceEnabled)
				    			TabLayoutActivity.tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null);
				    		
			    		
			    		imageView.invalidate(); // redraw the map with the positions
			    		
			    	}

					/*Point lastPosition = null;
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
					    	
					    	float x_inPixel = x / 5.0f * (width-20);
					    	float y_inPixel = y / 10.0f * (height-20);
					    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
					    	 
					    	// Needed for the coordinate transformation of accessed position and the imageview
						    Point oldZero = new Point(0,0);
						    Point newZero = new Point((width)/2,-((height)/2));
						    
						    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
						    
					    	//imageView.addWatchPosition(watchID, lastPosition);
					    	imageView.addWatchPosition(watchID, positionInPixel);
						    Log.d("positionInPixel X***************","*******************"+positionInPixel.getX());
						    Log.d("positionInPixel Y***************","*******************"+positionInPixel.getY());

					    	String positionString = "x = " + x + "m, " + "y = " + y + "m at " + record.getInsertedAt(); 
						    positionText.setText(positionString);

						}
			    		imageView.setDrawPath(true);
					    Point currentLocation = new Point(lastPosition.getX(),lastPosition.getY());
			    		String direction = Utilities.getDirection(currentLocation);
			    		Log.d("********",""+direction);
			    		Thread.sleep(2000);
			    		if(TabLayoutActivity.isVoiceEnabled)
			    			TabLayoutActivity.tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null);
			    		imageView.invalidate();
			    		
			    		
*/			    	
				}
				catch(Exception e)
				{
					e.printStackTrace();
					positionText.setText(result);
				}
				Log.e("WATCH_USER_ACTIVITY", "Display position finished");

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
