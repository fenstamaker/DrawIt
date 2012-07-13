package edu.montclair.hci.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Not used any more.
 * @author gf102
 *
 */
public class PhotoView extends SurfaceView {
	
	private Paint paint;
	private SurfaceHolder surfaceHolder;
	
	public void init() {
		paint = new Paint();
		surfaceHolder = getHolder();
	}
	
	public void drawImage(Bitmap bitmap) {

		Canvas canvas = surfaceHolder.lockCanvas(null);
		
		canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
		
		surfaceHolder.unlockCanvasAndPost(canvas);
		
	}
	
	public PhotoView(Context context) {
		super(context);
		init();
	}
	
	public PhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	

	

}
