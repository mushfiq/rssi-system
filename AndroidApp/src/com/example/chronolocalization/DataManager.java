package com.example.chronolocalization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class DataManager
{

	public DataManager()
	{
		initializePositionsHistory(10);
	}
	/**
	 * Gets the last n recorded positions of the watch with the unique watchID
	 * @param watchID The ID of the watch
	 * @param n Number of Positions we want to get
	 * @return ArrayList with the last n positions where the watch have been tracked
	 */
	public ArrayList<Point> getLastNPositions(String watchID, int n)
	{	
		ArrayList<Point> ret = new ArrayList<Point>();
		if( watchToPositionsHistory.containsKey(watchID) )
		{
			ArrayList<Point> positions = watchToPositionsHistory.get(watchID);
			
			if( !positions.isEmpty() && positions.size() > n )
			{
				int index = positions.size() - n;
				for(int insertedPositions = 0; insertedPositions < n; insertedPositions++)
				{
					ret.add(positions.get(index));
					index++;
				}
			}
			else
			{
				ret = new ArrayList<Point>(positions);
			}
		}
		
		return ret;
	}
	
	/**
	 * Gets the last known position of the watch with the unique ID watchID
	 * @param watchID The ID of the watch
	 * @return The last known point or null, if there is no Point in the data source
	 */
	public Point getLastPoint(String watchID)
	{
		Point lastPosition = null;
		
		if( watchToPositionsHistory.containsKey(watchID) )
		{
			ArrayList<Point> positions = watchToPositionsHistory.get(watchID);
			if(!positions.isEmpty())
			{
				lastPosition = positions.get(positions.size() - 1);
			}
		}
		
		return lastPosition;
	}
	
	
	
	Map<String, ArrayList<Point>> watchToPositionsHistory = new TreeMap<String, ArrayList<Point>>();
	
	
	
	/**
	 * Functionality only for test until we get access the real DB
	 * @return
	 */
	private Point getRandomPoint()
	{
	    int x = 15 + rand.nextInt(235);
	    int y = 120 + rand.nextInt(175);
	    
	    return new Point(x,y);
	}
	private Random rand = new Random(42);
	
	private void initializePositionsHistory(int numberOfPoints)
	{
		watchToPositionsHistory.put("Watch A", new ArrayList<Point>());
		watchToPositionsHistory.put("Watch B", new ArrayList<Point>());		
		
		for (Map.Entry<String, ArrayList<Point>> iterable_element : watchToPositionsHistory.entrySet())
		{
			int index = 0;
			while(index < numberOfPoints)
			{
				iterable_element.getValue().add(getRandomPoint());
				index++;
			}
		}
	}
}
