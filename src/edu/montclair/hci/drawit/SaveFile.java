package edu.montclair.hci.drawit;
import java.io.*;
import java.util.ArrayList;

import edu.montclair.hci.drawit.DrawView.Point;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;


public class SaveFile extends AsyncTask<Void, Void, Void> {
	
	private ProgressDialog pd;
	private Bitmap bitmap;
	private ArrayList<Point> leafPoints;
	private ArrayList<Point> nonleafPoints;
	private ArrayList<Integer> leafArgb;
	private ArrayList<Integer> nonleafArgb;
	
	public SaveFile(Bitmap bmp, ArrayList<Point> lp, ArrayList<Point> nlp, ProgressDialog pd) {
		this.bitmap = bmp;
		this.leafPoints = lp;
		this.nonleafPoints = nlp;
		this.pd = pd;
		
		leafArgb = new ArrayList<Integer>();
		nonleafArgb = new ArrayList<Integer>();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		for ( Point pt : leafPoints ) {
			try {
				leafArgb.add( bitmap.getPixel(pt.x, pt.y) );
			} catch (Exception e) {}
		}
		for ( Point pt : nonleafPoints ) {
			try {
				nonleafArgb.add( bitmap.getPixel(pt.x, pt.y) );
			} catch (Exception e) {}
		}
		
		File leafFile = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Leaves/leaf.txt" );
		File nonleafFile = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Leaves/nonleaf.txt" );
		
		DataOutputStream leafOut, nonleafOut;
		
		try {
			
			leafOut = new DataOutputStream(new FileOutputStream(leafFile, true));
			nonleafOut = new DataOutputStream(new FileOutputStream(nonleafFile, true));
			
			for ( Integer i : leafArgb ) {
				leafOut.writeInt(i);
			}
			for ( Integer i : nonleafArgb ) {
				nonleafOut.writeInt(i);
			}

			leafOut.close();
			nonleafOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void onPostExecute(Void p) {
		pd.dismiss();					
	}

}
