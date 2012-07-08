package edu.montclair.hci.drawit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DrawItActivity extends Activity {
	
	private static final int CAMERA_PHOTO_REQUEST = 329;
	
	private Bitmap bitmap;
	private File storageDir;
	
	private ImageView imageView;
	private Button photoButton;
	private Button leafButton;
	private Button nonleafButton;
	
	private String currentPhotoPath;
	private File currentPhotoFile;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        imageView = (ImageView) findViewById(R.id.imageView);
        
        photoButton = (Button) findViewById(R.id.photoButton);
        leafButton = (Button) findViewById(R.id.leafButton);
        nonleafButton = (Button) findViewById(R.id.nonleafButton);
        
        photoButton.setOnClickListener(takePhoto);
        
        storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Leaves" );
        storageDir.mkdir();
        
        File image = new File(storageDir + "/leaf.jpg");
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoFile = image;
     
    }

    private OnClickListener takePhoto = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentPhotoFile));
			startActivityForResult(takePhotoIntent, CAMERA_PHOTO_REQUEST);
		}
    	
    };    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if ( requestCode == CAMERA_PHOTO_REQUEST && resultCode == Activity.RESULT_OK ) {
    		
    		if ( bitmap != null ) {
    			bitmap.recycle();
    		}
    				
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
    	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
    	  
    	    // Decode the image file into a Bitmap sized to fill the View
    	    bmOptions.inJustDecodeBounds = false;
    	    bmOptions.inSampleSize = scaleFactor;
    	    bmOptions.inPurgeable = true;
    	  
    	    bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    		
    		imageView.setImageBitmap(bitmap);

			super.onActivityResult(requestCode, resultCode, data);
    	}
    }
}