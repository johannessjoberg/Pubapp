package com.example.pubapp;



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class Main extends Activity {

	private TextView tvEventTitle, tvEventPubname, tvEventDate; // to show the information of the pub
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 tvEventTitle 	= (TextView) findViewById(R.id.tvEventTitle);
	     tvEventPubname 	= (TextView) findViewById(R.id.tvEventPubname);
	     tvEventDate 	= (TextView) findViewById(R.id.tvEventDate);
		
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

		// define the parameter
				String response = null;
		
		// call executeHttpPost method passing necessary parameters 
		try {
			response = CustomHttpClient.executeHttpGet(
					"http://trainwemust.com/pubapp/jsonnewsfeed.php"  // url of jsonphpscript
					);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","pubName: "+json_data.getString("pubName")+
							", eventName: "+json_data.getString("eventName")+
							", eventDateStart: "+json_data.getInt("eventDateStart")
							);
					
					//Converts json data to strings
					pubName 			= json_data.getString("pubName");
					eventName 		= json_data.getString("eventName");
					eventDateStart = json_data.getString("eventDateStart");
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
