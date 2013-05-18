package com.example.pubapp;



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class Main extends Activity {

	private TextView tvMainEventTitle, tvMainEventPubname, tvMainEventDate; // to show the information of the pub
	private String pubName, eventName, eventDateStart; // to store the result of MySQL query after decoding JSON

	
	
	
	private static class MainHolder{
		private static final Main INSTANCE = new Main();
	}
	
	public static Main getInstance(){
		return MainHolder.INSTANCE;
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
		.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 tvMainEventTitle 	= (TextView) findViewById(R.id.tvMainEventTitle);
	     tvMainEventPubname 	= (TextView) findViewById(R.id.tvMainEventPubname);
	     tvMainEventDate 	= (TextView) findViewById(R.id.tvMainEventDate);
		
	     getInfo("bajs");
	}
	
	@Override
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
		// declare parameters that are passed to PHP script i.e. the id "id" and its value submitted by the app   
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id",id));
		String response = null;

		// call executeHttpPost method passing necessary parameters 
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://trainwemust.com/pubapp/jsonnewsfeed.php",  // in case of a remote server
					postParameters);


			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","eventName: "+json_data.getString("eventName")+
							", pubName: "+json_data.getString("pubName")+
							", eventDateStart: "+json_data.getString("eventDateStart")
							);
					
					pubName 	= json_data.getString("pubName");
					eventName 	= json_data.getString("eventName");
					eventDateStart = json_data.getString("eventDateStart");
				}

			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Log.e("log_tag", "Failed data was:\n" + result);
			}

			try{
				tvMainEventTitle.setText(eventName);
				tvMainEventPubname.setText("Vart : " + pubName);
				tvMainEventDate.setText("Datum : " + eventDateStart);
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
