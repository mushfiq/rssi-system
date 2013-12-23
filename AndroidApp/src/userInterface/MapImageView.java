package userInterface;

import java.util.ArrayList;
import java.util.List;
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

/**
 * This class is a specialization of the imageView in a way that:
 * - we can draw points on top of it
 * - we can zoom in and zoom out
 * - we can pan the image 
 * @author Silvio
 *
 */
public class MapImageView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;

    private float posX; //The actual x coordinate of the image
    private float posY; //The actual y coordinate of the image
    
    private boolean isPanAndZoomable = false; // Says either this imageView is pan- and zoomable or not
    /**
     * Setter for the ability to pan and zoom the imageView
     * @author Silvio
     * @param isPanAndZoomable Is this imageView pan- and zoomable?
     */
    public void setIsPanAndZoomable(boolean isPanAndZoomable)
    {
    	this.isPanAndZoomable = isPanAndZoomable;
    }
    
    private int positionsToDraw = 1; //Number of Positions that should be drawn on top of the imageView
    
    /**
     * Setter for how many positions should be drawn
     * @author Silvio
     * @param positionsToDraw Number of positions that should be drawn 
     */
    public void setPositionsToDraw(int positionsToDraw)
    {
    	this.positionsToDraw = positionsToDraw;
    }

    private float lastTouchPositionX; //The x coordinate of the last touch position
    private float lastTouchPositionY; //The y coordinate of the last touch position
    private float lastGesturePositionX; //The x coordinate of the last gesture position
    private float lastGesturePositionY; //The y coordinate of the last gesture position
    private int activePointerId = INVALID_POINTER_ID;

    
    private ScaleGestureDetector scaleDetector; //The scale detector of this imageView
    private float scaleFactor = 1.f; //The actual scale factor of this imageView

    public MapImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    /**
     * Method which is responsible for detecting which kind of event has occured
     * @author unknown
     */
    public boolean onTouchEvent(MotionEvent ev)
    {
    	
    	//If this imageView is pan- and zoomable when can perform the touch events
    	if( isPanAndZoomable )
    	{
	        // Let the ScaleGestureDetector inspect all events.
	        scaleDetector.onTouchEvent(ev);
	
	        final int action = ev.getAction();
	        switch (action & MotionEvent.ACTION_MASK) {
	            case MotionEvent.ACTION_DOWN: {
	                if (!scaleDetector.isInProgress()) {
	                    final float x = ev.getX();
	                    final float y = ev.getY();
	
	                    lastTouchPositionX = x;
	                    lastTouchPositionY = y;
	                    activePointerId = ev.getPointerId(0);
	                }
	                break;
	            }
	            case MotionEvent.ACTION_MOVE: {
	
	                // Only move if the ScaleGestureDetector isn't processing a gesture.
	                if (!scaleDetector.isInProgress()) {
	                    final int pointerIndex = ev.findPointerIndex(activePointerId);
	                    final float x = ev.getX(pointerIndex);
	                    final float y = ev.getY(pointerIndex);
	
	                    final float dx = x - lastTouchPositionX;
	                    final float dy = y - lastTouchPositionY;
	
	                    posX += dx;
	                    posY += dy;
	
	                    invalidate();
	
	                    lastTouchPositionX = x;
	                    lastTouchPositionY = y;
	                }
	                else{
	                    final float gx = scaleDetector.getFocusX();
	                    final float gy = scaleDetector.getFocusY();
	
	                    final float gdx = gx - lastGesturePositionX;
	                    final float gdy = gy - lastGesturePositionY;
	
	                    posX += gdx;
	                    posY += gdy;
	
	                    invalidate();
	
	                    lastGesturePositionX = gx;
	                    lastGesturePositionY = gy;
	                }
	
	                break;
	            }
	            case MotionEvent.ACTION_UP: {
	                activePointerId = INVALID_POINTER_ID;
	                break;
	            }
	            case MotionEvent.ACTION_CANCEL: {
	                activePointerId = INVALID_POINTER_ID;
	                break;
	            }
	            case MotionEvent.ACTION_POINTER_UP: {
	
	                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
	                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	                final int pointerId = ev.getPointerId(pointerIndex);
	                if (pointerId == activePointerId) {
	                    // This was our active pointer going up. Choose a new
	                    // active pointer and adjust accordingly.
	                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
	                    lastTouchPositionX = ev.getX(newPointerIndex);
	                    lastTouchPositionY = ev.getY(newPointerIndex);
	                    activePointerId = ev.getPointerId(newPointerIndex);
	                }
	                else{
	                    final int tempPointerIndex = ev.findPointerIndex(activePointerId);
	                    lastTouchPositionX = ev.getX(tempPointerIndex);
	                    lastTouchPositionY = ev.getY(tempPointerIndex);
	                }
	
	                break;
	            }
	        }
	
	        return true;
    	}
    	else
    	{
    		return true;
    	}
    	
    }
    

    /**
     * Private inner class which implements the special behaviour that we need to zoom and pan the imageView
     * @author unknown
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 1.5f));

            invalidate();
            return true;
        }
    }
    
    /**
     * This method recalculate the border positions in a way that we are not able to pan it out of screen
     * @author Silvio
     */
    private void recalculateBorderPositions()
    {
    	int width = getWidth();
    	int height = getHeight();
    	posX = Math.min(Math.max(posX, -width), width);
    	posY = Math.min(Math.max(posY, -height), height);
    }


    Point zeroPoint = null; //The zero point of this imageView
    /**
     * Setter for the zero point of this imageView
     * @author Silvio
     * @param zeroPoint The zero point to set
     */
	public void setZeroPoint(Point zeroPoint)
	{
		this.zeroPoint = zeroPoint;
	}
	
	Paint paint = new Paint(); //The paint which is used to draw the positions on top of the image
    boolean drawPath = false; //Should a path between the positions should be drawn
    /**
     * Setter for the attribute drawPath
     * @author Silvio
     * @param drawPath Should a path between the positions drawn
     */
    public void setDrawPath(boolean drawPath)
    {
    	this.drawPath = drawPath;
    }

    //The Map which holds the positions (Value) of each watch (Key)
    private TreeMap<String, ArrayList<Point>> watchToPositions = new TreeMap<String, ArrayList<Point>>();
    
    /**
     * This method adds a position to the list of the specified watch
     * @author Silvio
     * @param watchID The unique watch id of the watch to which this position belongs
     * @param position The position of the watch that should be drawn
     */
    public void addWatchPosition(String watchID, Point position)
    {
    	//If we have no list for this watch id we created a new one
    	if( !watchToPositions.containsKey(watchID) )
    	{
    		watchToPositions.put(watchID, new ArrayList<Point>());
    	}    	
    	
    	//We get existing list of watch positions to this watch id
    	ArrayList<Point> watchPositions = watchToPositions.get(watchID);
    	
    	//If we have more than 1000 positions we remove one position so that we do not exceed more than 1000 positions each watch
    	if( watchPositions.size() > 1000 && !watchPositions.isEmpty() )
    	{
    		watchPositions.remove(0);
    	}
    	
    	watchPositions.add(position);
    }
    
    /**
     * This method clears all positions of the watch which belongs to the unique watch id
     * @author Silvio
     * @param watchID The unique watch id to which all positions should be removed
     */
    public void clearWatchPositions(String watchID)
    {
    	// If this watch id existing in our mapping than we can clear the list of positions
    	if( watchToPositions.containsKey(watchID))
    	{
    		watchToPositions.get(watchID).clear();
    	}
    }
    
    
    private Map<String, Point> receiverPoints = new TreeMap<String, Point>(); //The receiver points which should be displayed
    
    /**
     * This method adds a receiver position to the imageView
     * @author Silvio
     * @param receiverID The unique id of the receiver to add to the imageView
     * @param position The position where the receiver is located on the map
     */
    public void addReceiver(String receiverID, Point position)
    {
		receiverPoints.put(receiverID, position);
    }
    
    /**
     * This method remove a receiver from the list of receivers that should be drawn
     * @author Silvio
     * @param receiverID The unique receiver id of the receiver that should be removed
     */
    public void removeReceiver(String receiverID)
    {
    	receiverPoints.remove(receiverID);
    }
    
    private Set<String> watchesToDraw = new TreeSet<String>(); //The set of watch ids that should be drawn
    
    /**
     * This method add a watch id to the set of watches to draw
     * Only the watche with a key in the map are drawn
     * @author Silvio
     * @param watchID The unique watch id of the watch we want to add to the set
     */
    public void addWatchToDraw(String watchID)
    {
    	watchesToDraw.add(watchID);
    }
    
    /**
     * This method removes a watch id from the set of ids to draw
     * @author Silvio
     * @param watchID The unique watch id of the watch that should be removed
     */
    public void removeWatchToDraw(String watchID)
    {
    	watchesToDraw.remove(watchID);
    }
    
    /**
     * This method clears thet set of watches to draw
     * @author Silvio
     */
    public void clearWatchesToDraw()
    {
    	watchesToDraw.clear();
    }
    
    @Override
    /**
     * The overriden method for the draw action which is called if we call the .invalidate() method
     * @author Silvio
     * @param canvas The canvas on which we drawn the positions
     */
    public void onDraw(Canvas canvas) {
    	
        canvas.save();
        recalculateBorderPositions(); //Recalculate the borders that we are not panning the image outside the view
        canvas.translate(posX, posY);
      
        if (scaleDetector.isInProgress()) {
            canvas.scale(scaleFactor, scaleFactor, scaleDetector.getFocusX(), scaleDetector.getFocusY());
        }
        else{
            canvas.scale(scaleFactor, scaleFactor, lastGesturePositionX, lastGesturePositionY);
        }
        super.onDraw(canvas);
        
        //Draw the whole data
        Paint p = paint;
        
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
        	List<Point> allPointsOfWatch = watchToPositions.get(watchID);
        	List<Point> pointsOfWatchToDraw = null;
        	if(allPointsOfWatch.size() > positionsToDraw )
        	{	
        		pointsOfWatchToDraw = allPointsOfWatch.subList(allPointsOfWatch.size() - positionsToDraw, allPointsOfWatch.size());
        	}
        	else
        	{
        		pointsOfWatchToDraw = allPointsOfWatch;
        	}
        	
        	if( pointsOfWatchToDraw != null )
        	{
        		if( drawPath )
        		{
	        		for(int index = 0; index < pointsOfWatchToDraw.size() - 1; ++index )
	        		{
	        			Point point = pointsOfWatchToDraw.get(index);
	        			
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
	                	Point nextPoint = pointsOfWatchToDraw.get(index+1);
	                	canvas.drawLine(point.getX(), point.getY(), nextPoint.getX(), nextPoint.getY(), p);
	           		}
	        		
	        		Point point = pointsOfWatchToDraw.get(pointsOfWatchToDraw.size() - 1);
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
        			for(int index = 0; index < pointsOfWatchToDraw.size(); ++index )
	        		{
	        			Point point = pointsOfWatchToDraw.get(index);
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
}