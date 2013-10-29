package com.example.chronolocalization;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity
{
	private Spinner spinner1;
	private Button buttonDisplayPosition;
	private Button buttonDisplayLastNPositions;
	private DrawableImage imageView;
	private TextView positionText;
	private DataManager dataManager;

	Random rand = new Random(42);
	ArrayList<int[]> lastCoordinates = new ArrayList<int[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Connects the Application with the Data Source (at the moment a dummy implementation)
		dataManager = new DataManager();
		
		
		buttonDisplayPosition = (Button) findViewById(R.id.displayPosition);
		buttonDisplayPosition.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		 		    
			    //int width = imageView.getMeasuredWidth();
			    //int height = imageView.getMeasuredHeight();
			    
			    Point lastPosition = dataManager.getLastPoint("watchA");
			    
			    int x = lastPosition.getX();
			    int y = lastPosition.getY();
			    
			    int[] coordinates = new int[2];
			    coordinates[0] = x;
			    coordinates[1] = y;
			    lastCoordinates.add(coordinates);
			    
			    imageView.setX(x);
			    imageView.setY(y);
			    
			    imageView.invalidate();
			    String positionString = "x = " + (float)(x)/10.0 + "m, " + "y = " + (float)(y)/10.0 + "m"; 
			    positionText.setText(positionString);
			    
			    
			  }
		 
			});

		
		
		
		buttonDisplayLastNPositions = (Button) findViewById(R.id.displayLastNPositions);
		
		buttonDisplayLastNPositions.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		 		    			    
			    ArrayList<Point> lastPositions = dataManager.getLastNPositions("watchA",3);
			    
			    for(int index = 0; index < lastPositions.size(); ++index)
			    {
			    	Point lastPosition = lastPositions.get(index);
			    	int x = lastPosition.getX();
				    int y = lastPosition.getY();
				    
				    int[] coordinates = new int[2];
				    coordinates[0] = x;
				    coordinates[1] = y;
				    lastCoordinates.add(coordinates);
				    
				    imageView.setX(x);
				    imageView.setY(y);
				    
				    imageView.invalidate();
				    String positionString = "x = " + (float)(x)/10.0 + "m, " + "y = " + (float)(y)/10.0 + "m"; 
				    positionText.setText(positionString);
				    positionText.invalidate();
				    
				    //TODO Remove this bad solution after mockup is presented
				    SystemClock.sleep(1000);
				    
			    }
			    
			    
			    
			  }
		 
			});

		
		spinner1 = (Spinner) findViewById(R.id.WatchSpinner);
		imageView = (DrawableImage) findViewById(R.id.mapImage);
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
