package com.example.chronolocalization;

import java.util.ArrayList;
import java.util.Random;

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
		ArrayList<Point> positions = new ArrayList<Point>();
		
		if( !positionsHistory.isEmpty() && positionsHistory.size() > n )
		{
			int index = positionsHistory.size() - n;
			for(int insertedPositions = 0; insertedPositions < n; insertedPositions++)
			{
				positions.add(positionsHistory.get(index));
				index++;
			}
		}
		else
		{
			positions = new ArrayList<Point>(positionsHistory);
		}
		
		return positions;
	}
	
	/**
	 * Gets the last known position of the watch with the unique ID watchID
	 * @param watchID The ID of the watch
	 * @return The last known point or null, if there is no Point in the data source
	 */
	public Point getLastPoint(String watchID)
	{
		Point lastPosition = null;
		
		if(!positionsHistory.isEmpty())
		{
			lastPosition = positionsHistory.get(positionsHistory.size() - 1);
		}
		
		return lastPosition;
	}
	
	ArrayList<Point> positionsHistory = new ArrayList<Point>();
	
	
	
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
		int index = 0;
		while(index < numberOfPoints)
		{
			positionsHistory.add(getRandomPoint());
			index++;
		}
	}
	
	
}
