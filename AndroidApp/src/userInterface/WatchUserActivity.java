package userInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.chronolocalization.R;

import services.RestMapService;
import dataobjects.CoordinateTransformation;
import dataobjects.ResponseParser;
import dataobjects.Point;
import dataobjects.WatchPositionRecord;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

/**
 * This class represent the main activity of our application which includes the imageView where the positions are displayed
 * and the buttons to start and stop the application
 * @author Silvio
 *
 */
public class WatchUserActivity extends Activity
{
	private Spinner spinnerChooseWatch; //The spinner from that we select the watch
	private EditText editText_NumberOfPositions; //The edit field from that we get the number of positions to draw
	private MapImageView imageView; //The imageView that displays the map and the positions
	private TextView positionText; //The text which is display below the imageView
	
	//The coordinate transformation which is used to transform the coordinates from the watch to the zero point of the imageView
	private CoordinateTransformation coordinateTransformation; 
	
	int offset = 0;
	private int lastMapId = 4;

	private float pixel_per_meterX = -1.0f; //The ratio which represent how many pixel are one meter in x-direction
	private float pixel_per_meterY = -1.0f; //The ratio which represent how many pixel are one meter in y-direction

	/**
	 * Setter for the scaling value of the x-direction
	 * @author Silvio
	 * @param scaling The scaling to set for the x-direction
	 */
    public void setScalingX(float scaling)
    {
    	pixel_per_meterX = scaling;
    }
    
    /**
     * Setter for the scaling value of the y-direction
     * @author Silvio
     * @param scaling The scaling to set for the y-direction
     */
    public void setScalingY(float scaling)
    {
    	pixel_per_meterY = scaling;
    }
	
	Timer timer = null; // Controls the periodically update
	String watchID = "";
	
	boolean drawWatchPath = false;
	int numberOfPositions = 1;
	
	static int UPDATE_RATE = 200; // in milliseconds 

	ImageView start;
	ImageView stop;
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_user);
		
		start = (ImageView)findViewById(R.id.startImg);
		stop = (ImageView)findViewById(R.id.stopImg);
		
		spinnerChooseWatch = (Spinner) findViewById(R.id.WatchSpinner);
		spinnerChooseWatch.requestFocus();
		
		imageView = (MapImageView) findViewById(R.id.mapImage);
		
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
		
		editText_NumberOfPositions = (EditText) findViewById(R.id.EditText_NumberOfPositions);
	}


	public void startApplication(View view)
	{
		imageView.setIsPanAndZoomable(true);
		Toast.makeText( getApplicationContext(), "Application started", Toast.LENGTH_SHORT).show();
       	watchID = spinnerChooseWatch.getSelectedItem().toString();
     					 
		timer = new Timer();
 		start.setVisibility(View.GONE);
 		stop.setVisibility(View.VISIBLE);

 		// Create a new timer task that asks periodically for the latest position of the watch
        timer.scheduleAtFixedRate(new TimerTask() {         
	            @Override
	            public void run() {
	                runOnUiThread(new Runnable()
	                {
	                    @Override
	                    public void run()
	                    {
	                     	new GetPositionTask(watchID).execute();
	                    }
	                });
	            }
	        }, 0, 2000);
	}
	
	public void stopApplication(View view)
	{
		stop.setVisibility(View.GONE);
		start.setVisibility(View.VISIBLE);
		
		Toast.makeText( getApplicationContext(), "Application is stoped", Toast.LENGTH_SHORT).show();

		if(timer !=null){
			timer.cancel();
		}
	}

	@Override
	protected void onStart() {
	    super.onStart();	 
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
		    		String numberOfPositionsString = editText_NumberOfPositions.getText().toString(); 
		    		
		    		numberOfPositions = 1;
		    		try
		    		{
		    			numberOfPositions = Integer.parseInt(numberOfPositionsString);
		    		}
		    		catch( Exception e)
		    		{
		    			e.printStackTrace();
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
		    		url = "http://shironambd.com/api/v1/watch/?access_key=529a2d308333d14178f5c54d&limit=" + numberOfPositions + "&watchId=" + watchNr + "&format=json";
		    		
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
		    	imageView.invalidate();
		    	// Needed for the coordinate transformation of accessed position and the imageview
		    	int map_height = imageView.getHeight();
		    	
			    Point oldZero = new Point(0,0);
			    Point newZero = new Point(0,-map_height);
				coordinateTransformation = new CoordinateTransformation(oldZero, newZero);
				
				String watchID = spinnerChooseWatch.getSelectedItem().toString();
		    	
				try
				{	
					Point lastPosition = null;
					float lastXPosition = 0.0f;
					float lastYPosition = 0.0f;
			    	ArrayList<WatchPositionRecord> records = ResponseParser.getParsedResponse(result);
			    	
			    	imageView.clearWatchesToDraw();
			    	imageView.addWatchToDraw(watchID);					    
					imageView.clearWatchPositions(watchID);
					
			    	if( !records.isEmpty() )
			    	{
			    		//Only if the mapId has changed since last time we need to download the image
			    		if( records.get(0).getMapId() != lastMapId )
			    		{
			    			//We reset the ratios to the default values -1.0 and indicate on this way that 
			    			//the ratios for the new map are not available at the moment 
			    			//At the inserting position we check these ratios and only if they are valid values
			    			//we start to draw the positions on the map
			    			resetRatios(); 
			    			
			    			lastMapId = records.get(0).getMapId();
					       	RestMapService restMapService = new RestMapService(WatchUserActivity.this);
							String mapID = ""+ lastMapId;
							restMapService.new GetMapRecordTask(mapID).execute();
			    		}

			    		//Check if the ratios are valid values, then we can add the positions to draw
			    		if( pixel_per_meterX >= 0.0f && pixel_per_meterY >= 0.0f)
			    		{
				    		for (WatchPositionRecord record : records)
							{	
				    			lastPosition = record.getPosition();
				    			lastXPosition = lastPosition.getX();
				    			lastYPosition = lastPosition.getY();
				    								    	
						    	float x_inPixel = lastXPosition * pixel_per_meterX;
						    	float y_inPixel = lastYPosition * pixel_per_meterY;
						    	Point positionInPixel = new Point(x_inPixel, y_inPixel);
							    
							    positionInPixel = coordinateTransformation.transformPosition(positionInPixel);
							    imageView.addWatchPosition(watchID, positionInPixel);
							}
			    		}
			    		
			    		//If we want to see more than one position, the path between these positions should be drawn
			    		if( numberOfPositions > 1 )
			    		{
			    			drawWatchPath = true;
			    		}
			    		
			    		imageView.setPositionsToDraw(numberOfPositions);
			    		
			    		DecimalFormat df = new DecimalFormat("##.##");
			    		df.setRoundingMode(RoundingMode.HALF_UP);
			    		
			    		String positionString = "x = " + df.format(lastXPosition) + "m, " + "y = " + df.format(lastYPosition) + "m";
					    positionText.setText(positionString);
			    		
			    		imageView.setDrawPath(drawWatchPath); // drawWatchPath == true => draw a path of the last positions
			    		imageView.invalidate(); // redraw the map with the positions
			    		
			    	}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					positionText.setText(result);
				}
		    }
		}

	/**
	 * This function reset the ratios to the default value -1.0f, which mean invalid ratio
	 * @author Silvio
	 */
	private void resetRatios()
	{
		pixel_per_meterX = -1.0f;
		pixel_per_meterY = -1.0f;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
