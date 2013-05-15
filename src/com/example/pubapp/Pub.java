package com.example.pubapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Pub extends Activity {

	
	private static final String TAG = null;
	TextView tv;      // TextView to show the result of MySQL query 
	TextView tvTitle, tvSektion, tvWebUrl, tvInfo, tvNextEvent;
	ImageView ivImgUrl;
	String title, sektion, weburl, imgurl, info, nextevent;

	String returnString;   // to store the result of MySQL query after decoding JSON
	
	private static class PubHolder{
		private static final Pub INSTANCE = new Pub();
	}
	
	public static Pub getInstance(){
		return PubHolder.INSTANCE;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		.penaltyLog().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub);
		
		Intent sender=getIntent();
        String id = sender.getExtras().getString("id");
		
        //tv 			= (TextView) findViewById(R.id.showresult);
        tvTitle 	= (TextView) findViewById(R.id.tvTitle);
        tvSektion 	= (TextView) findViewById(R.id.tvSektion);
        tvWebUrl 	= (TextView) findViewById(R.id.tvWebUrl);
        ivImgUrl	= (ImageView) findViewById(R.id.ivImgUrl);
        tvInfo 		= (TextView) findViewById(R.id.tvInfo);
		getInfo(id);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.mPubar:
	        	Intent pubar = new Intent(this, Pubar.class);
	        	startActivity(pubar);
	            return true;
	        case R.id.mKarta:
	        	Intent kar = new Intent(this, Karta.class);
	        	startActivity(kar);
	        	return true;
	        case R.id.mRunda:
	        	Intent rund = new Intent(this, Runda.class);
	        	startActivity(rund);
	        	return true;
	        case R.id.mKal:
	        	Intent kal = new Intent(this, Kalender.class);
	        	startActivity(kal);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void getInfo(String id) {

		// declare parameters that are passed to PHP script i.e. the name "birthyear" and its value submitted by user   
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id",id));
		String response = null;

		// call executeHttpPost method passing necessary parameters 
		try {
			response = CustomHttpClient.executeHttpPost(
					//"http://localhost:8888/jsonscript.php", // your ip address if using localhost server
					"http://trainwemust.com/pubapp/jsonscript.php",  // in case of a remote server
					postParameters);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				returnString = "";
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","id: "+json_data.getInt("id")+
							", pubnamn: "+json_data.getString("pubnamn")+
							", sektion: "+json_data.getString("sektion")+
							", weburl: "+json_data.getString("weburl")+
							", imgurl: "+json_data.getString("imgurl")+
							", info: "+json_data.getString("info")
							);
					//Get an output to the screen
					//returnString += "\n" + json_data.getString("pubnamn") + "\n" + json_data.getString("sektion") + "\n" + json_data.getString("info");
					
					title = json_data.getString("pubnamn");
					sektion = json_data.getString("sektion");
					imgurl = json_data.getString("imgurl");
					weburl = json_data.getString("weburl");
					info = json_data.getString("info");
					
				}

			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Log.e("log_tag", "Failed data was:\n" + result);
			}

			try{
				tvTitle.setText(title);
				tvSektion.setText(sektion);
				tvWebUrl.setText(weburl);
				ivImgUrl.setImageBitmap(getImageBitmap(imgurl));	
				tvInfo.setText(info);
			}
			catch(Exception e){
				Log.e("log_tag","Error in Display!" + e.toString());;          
			}   
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());     
		}
	}
	
    private Bitmap getImageBitmap(String url) {
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