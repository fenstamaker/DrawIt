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
	
	public ArrayList<Point> leafPoints;
	public ArrayList<Point> nonleafPoints;
	
	private boolean leaf;
	
	private Paint paint;
	private int radius = 4;
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private int l = 0, t = 0, r = 0, b = 0;
	
	public class Point {
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private void init() {
		leafPoints = new ArrayList<Point>();
		nonleafPoints = new ArrayList<Point>();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		setLeaf();
		paint.setStrokeWidth(radius / getResources().getDisplayMetrics().density);
		surfaceHolder = getHolder();
	}
	
	public void setLeaf() {
		paint.setARGB(255, 255, 255, 255);
		leaf = true;
	}
	
	public void setNonLeaf() {
		paint.setARGB(255, 255, 0, 0);
		leaf = false;
	}
	
	public void setBoundaries(int l, int t, int r, int b) {
		this.l = l;
		this.t = t;
		this.r = r;
		this.b = b;
	}
	
	public void clearCanvas() {
		leafPoints.clear();
		nonleafPoints.clear();
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
		Point pt = null;
		for ( int i = (int) (x-radius/2); i < (int) (x+radius/2); i++ ) {
			for ( int j = (int) (y-radius/2); j < (int) (y+radius/2); j++ ) {
				if ( i >= l && i <= r && j >= t && j <= b ) {
					canvas.drawPoint(i, j, paint);
					if ( leaf )
						leafPoints.add(new Point(i, j));
					else
						nonleafPoints.add(new Point(i, j));
				}
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
