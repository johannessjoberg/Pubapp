package com.example.pubapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;


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

public class Event extends Activity {

	//Declare variables
	private static final String TAG = null; 
	private ImageView ivEventImgUrl; // for showing the "sektion" picture
	private TextView tvEventTitle, tvEventPubname, tvEventDate, tvEventInfo, tvEventTid; // to show the information of the pub
	private String pubName, eventName, eventInfo, eventStart, eventEnd, eventDateStart; // to store the result of MySQL query after decoding JSON
	
	//Create the actionbar menu. The app does this on every activity page
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	} 
	
	
	//Create the activity and id the textviews
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
		// StrictMode to catch accidental disk or network access on the application's main thread

		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);
		
		Intent sender=getIntent();
        String id = Integer.toString(sender.getExtras().getInt("id"));

        tvEventTitle 	= (TextView) findViewById(R.id.tvEventTitle);
        tvEventPubname 	= (TextView) findViewById(R.id.tvEventPubname);
        tvEventDate 	= (TextView) findViewById(R.id.tvEventDate);
        tvEventTid 		= (TextView) findViewById(R.id.tvEventTid);
        tvEventInfo 	= (TextView) findViewById(R.id.tvEventInfo);
		getInfo(id);
		
	}
	
	//Add the listners and cases to the actionbar
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.mHome:
			Intent home = new Intent (this, Main.class);
			startActivity(home);
			return true;    
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
	
	/**
	 * 
	 * Connects to a MySQL via a php json script and makes information
	 * concerning the specified pub in id available for use. 
	 * 
	 * @param  id 	the "pub" id of the specified pub
	 * @return      
	 */
	public void getInfo(String id) {

		// declare parameters that are passed to PHP script i.e. the id "id" and its value submitted by the app   
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id",id));
		String response = null;

		// call executeHttpPost method passing necessary parameters 
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://trainwemust.com/pubapp/jsonscriptevent.php",  // url of jsonphpscript
					postParameters);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","eventId: "+json_data.getInt("eventId")+
							", pubName: "+json_data.getString("pubName")+
							", eventName: "+json_data.getString("eventName")+
							", eventInfo: "+json_data.getString("eventInfo")+
							", eventDateStart: "+json_data.getString("eventDateStart")+
							", eventStart: "+json_data.getString("eventStart")+
							", eventEnd: "+json_data.getString("eventEnd")
							);
					
					//Converts json data to strings
					pubName 			= json_data.getString("pubName");
					eventName 		= json_data.getString("eventName");
					eventInfo 		= json_data.getString("eventInfo");
					eventDateStart 		= json_data.getString("eventDateStart");
					eventStart 		= json_data.getString("eventStart");
					eventEnd 		= json_data.getString("eventEnd");
					
				}

			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Log.e("log_tag", "Failed data was:\n" + result);
			}

			try{
				tvEventTitle.setText(eventName);
				tvEventPubname.setText("Vart : " + pubName);
				tvEventDate.setText("Datum : " + eventDateStart);
				tvEventTid.setText("Tid: " + eventStart + "-" + eventEnd);
				//ivEventImgUrl.setImageBitmap(ImageBitmap.getImageBitmap(imgurl)); // fetches a bitmap
				tvEventInfo.setText("Info : " + eventInfo);
			}
			catch(Exception e){
				Log.e("log_tag","Error in Display!" + e.toString());;          
			}   
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());     
		}
	}
}
