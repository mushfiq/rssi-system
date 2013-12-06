package com.example.chronolocalization;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import dataobjects.ResponseParser;
import dataobjects.Point;
import dataobjects.WatchPositionRecord;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class WatchUserActivity extends Activity
{
	private Spinner spinnerChooseWatch;
	private Button buttonDisplayPosition;
	private Button buttonTrackPosition;
	private MapImageView imageView;
	private TextView positionText;
	private DataManager dataManager;
	int offset = 0;
	
	float original_x = 0.0f;
	float original_y = 0.0f;
	float pixel_per_meter = 100.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_user);
		
		//Connects the Application with the Data Source (at the moment a dummy implementation)
		dataManager = new DataManager();

		imageView = (MapImageView) findViewById(R.id.mapImage);
		
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
	
		//Positions of Receiver hard coded in pixel
		//for the test_room_fifth_floor to fit the real environment
		imageView.addReceiver("Receiver1", new Point(40,13));
		imageView.addReceiver("Receiver2", new Point(140,13));
		imageView.addReceiver("Receiver3", new Point(390,13));
		imageView.addReceiver("Receiver4", new Point(40,209));
		imageView.addReceiver("Receiver5", new Point(390,209));
		imageView.addReceiver("Receiver6", new Point(280,270));
		imageView.addReceiver("Receiver7", new Point(390,360));
	
		
		spinnerChooseWatch = (Spinner) findViewById(R.id.WatchSpinner);
		spinnerChooseWatch.requestFocus();
		
		buttonDisplayPosition = (Button) findViewById(R.id.displayPosition);
		buttonDisplayPosition.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {				
				  String watchID = spinnerChooseWatch.getSelectedItem().toString();

				float picture_pixel_x = imageView.getWidth();
				float picture_pixel_y = imageView.getHeight();
				
				String output = "Pic(x) = " + picture_pixel_x + " Pic(y) = " + picture_pixel_y;
				
				
				Toast.makeText(getApplicationContext(), output , Toast.LENGTH_SHORT).show();

				  
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
						imageView.setDrawPath(false);
						
						try
						{	
					    	ArrayList<WatchPositionRecord> records = ResponseParser.getParsedResponse(result);
					    	if( !records.isEmpty() )
					    	{
					    		WatchPositionRecord firstRecord = records.get(0);
						    	Point lastPosition = firstRecord.getPosition();
						    	float x = lastPosition.getX();
						    	float y = lastPosition.getY();
						    	
						    	int width = imageView.getWidth();
						    	int height = imageView.getHeight();
						    	
						    	float x_inPixel = x / 10.0f * (width-20);
						    	float y_inPixel = y / 10.0f * (height-20);
						    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
						    	 
						    	// Needed for the coordinate transformation of accessed position and the imageview
							    Point oldZero = new Point(0,0);
							    Point newZero = new Point((width)/2,-((height)/2));
							    newZero = new Point(0,0);
							 
							    /**
							    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
							    
							    Point transformedZero = dataManager.transformPosition(oldZero, newZero, oldZero);

							     */
						    	imageView.addWatchPosition(watchID, positionInPixel);
						    	//imageView.setZeroPoint(transformedZero);
							    
							    imageView.invalidate(); // This yields to a redraw of the view
							    
							    // Rounding to two digits after decimal point to get a shorter text
							    // 0.17293092 => 0.17
							    x = Math.round(x * 100) / 100.0f;
							    y = Math.round(y * 100) / 100.0f;
							    String positionString = "x = " + x + "m, " + "y = " + y + "m at " + firstRecord.getInsertedAt(); 
							    positionText.setText(positionString);
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
							    	
							    	float x_inPixel = x / 10.0f * (width-20);
							    	float y_inPixel = y / 10.0f * (height-20);
							    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
							    	 
							    	// Needed for the coordinate transformation of accessed position and the imageview
								    /**
							    	Point oldZero = new Point(0,0);
								    Point newZero = new Point((width)/2,-((height)/2));
								    newZero = new Point(0,0);
								    
								    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
								    	
							    	*/
							    	
							    	imageView.addWatchPosition(watchID, positionInPixel);
							    	
							    	// Rounding to two digits after decimal point to get a shorter text
								    // 0.17293092 => 0.17
								    x = Math.round(x * 100) / 100.0f;
								    y = Math.round(y * 100) / 100.0f;
							    	String positionString = "x = " + x + "m, " + "y = " + y + "m at " + record.getInsertedAt(); 
								    positionText.setText(positionString);
								    
								}
					    		imageView.setDrawPath(true); //The edges between the positions will be drawn
					    		imageView.invalidate(); // This yields to a redraw of the view
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
