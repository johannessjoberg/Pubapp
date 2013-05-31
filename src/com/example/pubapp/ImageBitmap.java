package com.example.pubapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.json.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/** 
 * 
 * ImageBitmap class contains a function to download from the web and convert it to a bitmap object.
 * It design as a singleton because it also holds some variables for the class Runda.
 *
 */
public class ImageBitmap {

	private static ImageBitmap ourInstance = new ImageBitmap();
	/** 
	 * 
	 * Basic singleton setup.
	 * 
	 */
	public static ImageBitmap getInstance() {
		return ourInstance;
	}

	private ImageBitmap() {
	}

	private int pubId;
	public String pubName;
	
	/**
	 * 
	 * Setters and Getters for pubId and pubName.
	 * 
	 */
	public int getPubId() {
		return pubId;
	}
	
	public void setPubId(int id) {
		pubId = id;
	}
	
	public void setPubName(String name) {
		pubName = name;
	}
	
	public String getPubName() {
		return pubName;
	}
	
	private static final String TAG = null;

	/**
	 * 
	 * Takes an url to an image and returns the bitmap object.
	 * 
	 * @param url		the url adress for the picture
	 * 
	 * @return the Bitmap of the specified URL
	 */
	public static Bitmap getImageBitmap(String url) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Error getting bitmap", e);
		}
		return bm;
	}

}
