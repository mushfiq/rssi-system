package com.example.chronolocalization;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import dataobjects.Point;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class MapImageView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public MapImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScaleDetector.isInProgress()) {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);
                }
                break;
            }
           /** case MotionEvent.ACTION_POINTER_1_DOWN: {
                if (mScaleDetector.isInProgress()) {
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();
                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }
                break;
            }
            */
            case MotionEvent.ACTION_MOVE: {

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();

                    mLastTouchX = x;
                    mLastTouchY = y;
                }
                else{
                    final float gx = mScaleDetector.getFocusX();
                    final float gy = mScaleDetector.getFocusY();

                    final float gdx = gx - mLastGestureX;
                    final float gdy = gy - mLastGestureY;

                    mPosX += gdx;
                    mPosY += gdy;

                    invalidate();

                    mLastGestureX = gx;
                    mLastGestureY = gy;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                else{
                    final int tempPointerIndex = ev.findPointerIndex(mActivePointerId);
                    mLastTouchX = ev.getX(tempPointerIndex);
                    mLastTouchY = ev.getY(tempPointerIndex);
                }

                break;
            }
        }

        return true;
    }


    Point zeroPoint = null;
	public void setZeroPoint(Point zeroPoint)
	{
		this.zeroPoint = zeroPoint;
	}
	
    Paint paint = new Paint();
    boolean drawPath = false;
    public void setDrawPath(boolean drawPath)
    {
    	this.drawPath = drawPath;
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
    	
        canvas.save();
        limitPositions();
        canvas.translate(mPosX, mPosY);
      
        if (mScaleDetector.isInProgress()) {
            canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        }
        else{
            canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);
        }
        super.onDraw(canvas);
        
        //Draw the whole data
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
        		if( drawPath )
        		{
	        		for(int index = 0; index < points.size() - 1; ++index )
	        		{
	        			Point point = points.get(index);
	        			
	        			p.setColor(Color.GREEN);
	        			if( watchID.equals("watch2"))
			    		{
	        				p.setColor(Color.RED);
			    		}
			    		else if( watchID.equals("watch3"))
			    		{
			    			p.setColor(Color.MAGENTA);
			    		}
			    		else if( watchID.equals("watch4"))
			    		{
			    			p.setColor(Color.CYAN);
			    		}
	        			
	        			
	                	p.setStrokeWidth(2);
	                	canvas.drawCircle(point.getX(), point.getY(), 10, p );
	                	Point nextPoint = points.get(index+1);
	                	canvas.drawLine(point.getX(), point.getY(), nextPoint.getX(), nextPoint.getY(), p);
	           		}
	        		
	        		Point point = points.get(points.size() - 1);
	        		p.setColor(Color.GREEN);
        			if( watchID.equals("watch2"))
		    		{
        				p.setColor(Color.RED);
		    		}
		    		else if( watchID.equals("watch3"))
		    		{
		    			p.setColor(Color.MAGENTA);
		    		}
		    		else if( watchID.equals("watch4"))
		    		{
		    			p.setColor(Color.CYAN);
		    		}
                	p.setStrokeWidth(2);
                	canvas.drawCircle(point.getX(), point.getY(), 10, p );
        		}
        		else
        		{
        			for(int index = 0; index < points.size(); ++index )
	        		{
	        			Point point = points.get(index);
	        			p.setColor(Color.GREEN);
	        			if( watchID.equals("watch2"))
			    		{
	        				p.setColor(Color.RED);
			    		}
			    		else if( watchID.equals("watch3"))
			    		{
			    			p.setColor(Color.MAGENTA);
			    		}
			    		else if( watchID.equals("watch4"))
			    		{
			    			p.setColor(Color.CYAN);
			    		}
	                	p.setStrokeWidth(2);
	                	canvas.drawCircle(point.getX(), point.getY(), 10, p );
	           		}
        		}
        		if( zeroPoint != null) 
        		{
        			canvas.drawText("(0/0)", zeroPoint.getX(), zeroPoint.getY(), p);
        		}
	    	}
		}
        
        
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 1.5f));

            invalidate();
            return true;
        }
    }
    
    private void limitPositions()
    {
    	int width = getWidth();
    	int height = getHeight();
    	mPosX = Math.min(Math.max(mPosX, -width), width);
    	mPosY = Math.min(Math.max(mPosY, -height), height);
    }
}