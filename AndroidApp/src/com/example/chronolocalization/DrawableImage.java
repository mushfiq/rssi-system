package com.example.chronolocalization;

import java.util.*;

import dataobjects.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DrawableImage extends ImageView {
	
    Paint paint = new Paint();

    public DrawableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLUE);
    }

    private TreeMap<String, ArrayList<Point>> watchToPositions = new TreeMap<String, ArrayList<Point>>();
    public void addWatchPosition(String watchID, Point position)
    {
    	if( !watchToPositions.containsKey(watchID) )
    	{
    		watchToPositions.put(watchID, new ArrayList<Point>());
    	}    	
    	watchToPositions.get(watchID).add(position);
    }
    public void clearWatchPositions(String watchID)
    {
    	if( watchToPositions.containsKey(watchID))
    	{
    		watchToPositions.get(watchID).clear();
    	}
    }
    
    
    private Map<String, Point> receiverPoints = new TreeMap<String, Point>();
    public void addReceiver(String receiverID, Point position)
    {
		receiverPoints.put(receiverID, position);
    }
    public void removeReceiver(String receiverID)
    {
    	receiverPoints.remove(receiverID);
    }
    
    private Set<String> watchesToDraw = new TreeSet<String>();
    public void addWatchToDraw(String watchID)
    {
    	watchesToDraw.add(watchID);
    }
    public void removeWatchToDraw(String watchID)
    {
    	watchesToDraw.remove(watchID);
    }
    public void clearWatchesToDraw()
    {
    	watchesToDraw.clear();
    }
    
        @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        
        //Draw Receiver Points
        for(Point receiverPoint : receiverPoints.values() )
        {
        	p.setColor(Color.BLUE);
        	p.setStrokeWidth(2);
        	canvas.drawCircle(receiverPoint.getX(), receiverPoint.getY(), 5, p );
        }
        
        //Draw all points of the watches we want to visualize
        for (String watchID : watchesToDraw )
		{
        	ArrayList<Point> points = watchToPositions.get(watchID);
        	if( points != null )
        	{
        		for(int index = 0; index < points.size(); ++index )
        		{
        			Point point = points.get(index);
        			if(watchID.equals("Watch A") )
        			{
        				p.setColor(Color.GREEN);
        			}
        			else
        			{
        				p.setColor(Color.RED);
        			}
                	p.setStrokeWidth(2);
                	canvas.drawCircle(point.getX(), point.getY(), 10, p );
           		}
	    	}
		}
    }       
}