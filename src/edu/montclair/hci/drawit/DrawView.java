package edu.montclair.hci.drawit;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawView extends SurfaceView {
	
	public ArrayList<Point> points;
	
	private Paint paint;
	private int radius = 20;
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	
	public class Point {
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private void init() {
		points = new ArrayList<Point>();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(radius / getResources().getDisplayMetrics().density);
		surfaceHolder = getHolder();
	}
	
	public void clearCanvas() {
		points.clear();
		canvas = surfaceHolder.lockCanvas(null);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		canvas = surfaceHolder.lockCanvas(null);
		
		if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
			
			drawPoint(event.getX(), event.getY());
			
		} else if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
			
			int size = event.getHistorySize();
			
			for ( int i = 0; i < size; i++ ) {
				drawPoint(event.getHistoricalX(i), event.getHistoricalY(i));
			}

			drawPoint(event.getX(), event.getY());
			
		} else if ( event.getAction() == MotionEvent.ACTION_UP ) {

			drawPoint(event.getX(), event.getY());
			
		}
		
		surfaceHolder.unlockCanvasAndPost(canvas);
		
		return true;
	}
	
	public void drawPoint(float x, float y) {
		for ( int i = (int) (x-radius/2); i < (int) (x+radius/2); i++ ) {
			for ( int j = (int) (y-radius/2); j < (int) (y+radius/2); j++ ) {
				canvas.drawPoint(x, y, paint);
				points.add(new Point(i, j));
			}
		}
	}
	
	public DrawView(Context context) {
		super(context);
		init();
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	


}
