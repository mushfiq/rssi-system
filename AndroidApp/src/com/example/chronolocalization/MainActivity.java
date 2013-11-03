package com.example.chronolocalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity
{
	private Spinner spinnerChooseWatch;
	private Button buttonDisplayPosition;
	private Button buttonDisplayLastNPositions;
	private DrawableImage imageView;
	private TextView positionText;
	private DataManager dataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Connects the Application with the Data Source (at the moment a dummy implementation)
		dataManager = new DataManager();

		imageView = (DrawableImage) findViewById(R.id.mapImage);
		positionText = (TextView) findViewById(R.id.CurrentPositionText);

		imageView.addReceiver("Receiver1", new Point(20,55));
		imageView.addReceiver("Receiver2", new Point(20,350));
		imageView.addReceiver("Receiver3", new Point(250,350));
		imageView.addReceiver("Receiver4", new Point(400,55));
		
		spinnerChooseWatch = (Spinner) findViewById(R.id.WatchSpinner);
		
		buttonDisplayPosition = (Button) findViewById(R.id.displayPosition);
		buttonDisplayPosition.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
				//int width = imageView.getMeasuredWidth();
				    //int height = imageView.getMeasuredHeight();  
				
				imageView.clearWatchesToDraw();
			    
				String watchID = spinnerChooseWatch.getSelectedItem().toString();
				imageView.addWatchToDraw(watchID);
			    
				imageView.clearWatchPositions(watchID);
				Point lastPosition = dataManager.getLastPoint(watchID);
			    imageView.addWatchPosition(watchID, lastPosition);
			    
			    int x = lastPosition.getX();
			    int y = lastPosition.getY();
			    
			    imageView.invalidate();
			    String positionString = "x = " + (float)(x)/10.0 + "m, " + "y = " + (float)(y)/10.0 + "m"; 
			    positionText.setText(positionString);
			    
			    
			  }
		 
			});

		
		
		
		buttonDisplayLastNPositions = (Button) findViewById(R.id.displayLastNPositions);
		
		buttonDisplayLastNPositions.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		 		
				imageView.clearWatchesToDraw();
				
				String watchID = spinnerChooseWatch.getSelectedItem().toString();
				imageView.addWatchToDraw(watchID);
			    
				
				imageView.clearWatchPositions(watchID);
				
				EditText stepsInput = (EditText) findViewById(R.id.numberOfSteps);
				int numberOfSteps = Integer.parseInt(stepsInput.getText().toString());
						
			    ArrayList<Point> lastPositions = dataManager.getLastNPositions(watchID,numberOfSteps);
			    
			    for(int index = 0; index < lastPositions.size(); ++index)
			    {	
			    	Point lastPosition = lastPositions.get(index);
			    	imageView.addWatchPosition(watchID, lastPosition);
			    	int x = lastPosition.getX();
				    int y = lastPosition.getY();
				    
				    
				    String positionString = "x = " + (float)(x)/10.0 + "m, " + "y = " + (float)(y)/10.0 + "m"; 
				    positionText.setText(positionString);   
			    }
			    
			    imageView.invalidate();
			    
			    
			    
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
