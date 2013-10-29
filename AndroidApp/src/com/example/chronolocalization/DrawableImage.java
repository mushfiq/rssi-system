package com.example.chronolocalization;

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

    //Initial Coordinates (not drawn)
    float x = -1.0f;
    float y = -1.0f;
    
    public void setX(float x)
    {
    	this.x = x;
    }
    
    public void setY(float y)
    {
    	this.y = y;
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        
        //Hardcoded Reciever Positions
        p.setColor(Color.BLUE);
        p.setStrokeWidth(2);
        canvas.drawCircle(20, 55, 5, p);
        canvas.drawCircle(400, 55, 5, p);
        canvas.drawCircle(20, 350, 5, p);
        canvas.drawCircle(250, 350, 5, p);
        
        if( x > -1.0f && y > -1.0f)
        {
	        p.setColor(Color.GREEN);
	        p.setStrokeWidth(2);        
	        canvas.drawCircle(x, y, 10, p);
        }
        
        
        
    }       
}