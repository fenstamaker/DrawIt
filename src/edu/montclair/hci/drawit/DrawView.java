package edu.montclair.hci.drawit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class DrawView extends SurfaceView {

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
