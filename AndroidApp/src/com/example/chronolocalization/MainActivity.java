package com.example.chronolocalization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
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
	private EditText urlText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// just a simple comment
		//Connects the Application with the Data Source (at the moment a dummy implementation)
		dataManager = new DataManager();

		imageView = (DrawableImage) findViewById(R.id.mapImage);
		
		positionText = (TextView) findViewById(R.id.CurrentPositionText);
		urlText = (EditText) findViewById(R.id.editURL);

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
				
				  // Basic Example of a JSON Response of some webservice
				String urlFromTextField = urlText.getText().toString();
				String urlDefault = "http://echo.jsontest.com/x/123/y/135";

				if( urlFromTextField.startsWith("http://echo.jsontest.com/") )
					new GetPositionTask().execute(urlFromTextField);
				else
				{
					new GetPositionTask().execute(urlDefault);
					Toast.makeText(getApplicationContext(), 
                            "Invalid URL, " + urlDefault + " is used instead", Toast.LENGTH_LONG).show();
				}
				
			  }
			  
			  class GetPositionTask extends AsyncTask<String, Void, String>
				{
				    /** The system calls this to perform work in a worker thread and
				      * delivers it the parameters given to AsyncTask.execute() */
				    protected String doInBackground(String... urls)
				    {
				    	String httpResponse = "";
				    	try
				    	{
					    	URL obj = new URL(urls[0]);
					    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					    	// optional default is GET
							con.setRequestMethod("GET");
				 
							int responseCode = con.getResponseCode();
					 
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
				    
				    /** The system calls this to perform work in the UI thread and delivers
				      * the result from doInBackground() */
				    protected void onPostExecute(String result)
				    {
				    	imageView.clearWatchesToDraw();
				    	
				    	String watchID = spinnerChooseWatch.getSelectedItem().toString();
						imageView.addWatchToDraw(watchID);
					    
						imageView.clearWatchPositions(watchID);
						try
						{
					    	JSONObject jsonObject = new JSONObject(result);
							Double x = Double.parseDouble((String)jsonObject.get("x"));
							Double y = Double.parseDouble((String)jsonObject.get("y"));
					    	Point lastPosition = new Point(x.intValue(),y.intValue());
					    	imageView.addWatchPosition(watchID, lastPosition);
						    
						    imageView.invalidate();
						    String positionString = "x = " + x/10.0 + "m, " + "y = " + y/10.0 + "m"; 
						    positionText.setText(positionString);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							positionText.setText(result);
						}
				    }
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
