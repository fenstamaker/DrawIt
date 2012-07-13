package edu.montclair.hci.drawit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DrawItActivity extends Activity {
	
	private static final int CAMERA_PHOTO_REQUEST = 329;
	
	private Bitmap bitmap;
	private File storageDir;
	
	public int scaleFactor;
	
	private ImageView imageView;
	private DrawView drawView;
	private Button photoButton;
	private Button leafButton;
	private Button nonleafButton;
	private Button saveButton;
	
	private String currentPhotoPath;
	private File currentPhotoFile;
	
	public ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawView = (DrawView) findViewById(R.id.drawView);
        imageView = (ImageView) findViewById(R.id.imageView);
        
        photoButton = (Button) findViewById(R.id.photoButton);
        leafButton = (Button) findViewById(R.id.leafButton);
        nonleafButton = (Button) findViewById(R.id.nonleafButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        
        drawView.setZOrderOnTop(true);
        drawView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        drawView.setBoundaries(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
        
        photoButton.setOnClickListener(takePhoto);
        leafButton.setOnClickListener(toggleLeaf);
        nonleafButton.setOnClickListener(toggleLeaf);
        saveButton.setOnClickListener(saveData);
        
        storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Leaves" );
        storageDir.mkdir();
        
        File image = new File(storageDir + "/leaf.jpg");
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoFile = image;
     
    }
    
    private OnClickListener saveData = new OnClickListener() {

		public void onClick(View v) {
			pd = ProgressDialog.show(DrawItActivity.this, "", "Saving. Please wait...", true);
			SaveFile saveFile = new SaveFile(bitmap, drawView.leafPoints, drawView.nonleafPoints, pd);
			saveFile.execute(new Void[0]);
		}
    	
    };
    
    private OnClickListener toggleLeaf = new OnClickListener() {

		public void onClick(View v) {
			if ( v.getId() == leafButton.getId() )
				drawView.setLeaf();
			else
				drawView.setNonLeaf();
		}
    	
    };

    private OnClickListener takePhoto = new OnClickListener() {

		public void onClick(View v) {
	        drawView.clearCanvas();
			Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentPhotoFile));
			startActivityForResult(takePhotoIntent, CAMERA_PHOTO_REQUEST);
		}
    	
    };    
    
    @Override
    /**
     * Called after camera intent is closed
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if ( requestCode == CAMERA_PHOTO_REQUEST && resultCode == Activity.RESULT_OK ) {
    		
    		if ( bitmap != null ) {
    			bitmap.recycle();
    		}
    		
    		// code from Google to resize picture ...
    		
    		// Get the dimensions of the View
    	    int targetW = getWindowManager().getDefaultDisplay().getWidth();
    	    int targetH = getWindowManager().getDefaultDisplay().getHeight();
    	  
    	    // Get the dimensions of the bitmap
    	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    	    bmOptions.inJustDecodeBounds = true;
    	    BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    	    int photoW = bmOptions.outWidth;
    	    int photoH = bmOptions.outHeight;
    	  
    	    // Determine how much to scale down the image
    	    scaleFactor = Math.min(photoW/targetW, photoH/targetH);
    	  
    	    // Decode the image file into a Bitmap sized to fill the View
    	    bmOptions.inJustDecodeBounds = false;
    	    bmOptions.inSampleSize = scaleFactor;
    	    bmOptions.inPurgeable = true;
    	  
    	    // ... /
    	    
    	    bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    		
    	    int width = bitmap.getWidth()/scaleFactor;
    	    int height = bitmap.getHeight()/scaleFactor;
    	    
    	    bitmap = Bitmap.createScaledBitmap(bitmap, width+(targetH-height)/2, height+targetH-height, false);
    	    
    		imageView.setImageBitmap(bitmap);
    		drawView.setBoundaries( 0, 0, width+(targetH-height)/2, height+targetH-height);	// set bounds of drawview to bounds of image
    		
    		Log.d("SIZE", targetW + "x" + targetH + "=" + bitmap.getWidth()/scaleFactor + "x" + bitmap.getHeight()/scaleFactor);

			super.onActivityResult(requestCode, resultCode, data);
    	}
    }

}