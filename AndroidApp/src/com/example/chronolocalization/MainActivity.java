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

public class MainActivity extends Activity
{
	private Spinner spinnerChooseWatch;
	private Button buttonDisplayPosition;
	private Button buttonTrackPosition;
	private DrawableImage imageView;
	private TextView positionText;
	private DataManager dataManager;
	int offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		
		buttonDisplayPosition = (Button) findViewById(R.id.displayPosition);
		buttonDisplayPosition.setOnClickListener(new OnClickListener() {
			 
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
				    		String url = "http://shironambd.com/api/v1/watch/?watchId=" + watchID + "&offset=" + offset + "&limit=1&format=json";
				    		url = "http://shironambd.com/api/v1/watch/?limit=1&format=json";
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
						    	
						    	float x_inPixel = x / 3.0f * (width-20);
						    	float y_inPixel = y / 3.0f * (height-20);
						    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
						    	 
						    	// Needed for the coordinate transformation of accessed position and the imageview
							    Point oldZero = new Point(0,0);
							    Point newZero = new Point((width)/2,-((height)/2));
							    
							    /*
							    Point oldZero = new Point(0,0);
							    Point newZero = new Point(15,-360);
							    */
							    
							    //lastPosition = dataManager.transformPosition(oldZero, newZero, lastPosition);
							    positionInPixel = dataManager.transformPosition(oldZero, newZero, positionInPixel);
							    
							    Point transformedZero = dataManager.transformPosition(oldZero, newZero, oldZero);
						    	//imageView.addWatchPosition(watchID, lastPosition);
						    	imageView.addWatchPosition(watchID, positionInPixel);
						    	imageView.setZeroPoint(transformedZero);
							    
							    imageView.invalidate();
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
				    		
				    		String url = "http://shironambd.com/api/v1/watch/?watchId=" + watchID + "&offset=0&limit=1&format=json";
				    		url = "http://shironambd.com/api/v1/watch/?offset=" + offset + "&limit=" + limit + "&format=json";
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
