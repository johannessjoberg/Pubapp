package com.example.pubapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Pub extends Activity {

	private static final String TAG = null;
	
	private ImageView ivImgUrl; 
	/*
	 * for showing the "sektion" picture to show the information of
	 * the pub to store the result of MySQL query after decoding JSON.	
	*/
	private TextView tvTitle, tvSektion, tvWebUrl, tvInfo;
	private String pubName, sektion, weburl, imgurl, info, id;

	private Spinner eventSpinner;
	private List<String> eventList;
	private List<Integer> eventIds;
	private int check = 0;

	//Create the actionbar menu. The app does this on every activity page
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	}
	
	//Create the activity and run the method createContent
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		// StrictMode to catch accidental disk or network access on the
		// application's main thread

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub);

		Intent sender = getIntent();
		id = Integer.toString(sender.getExtras().getInt("id"));

		createContent();

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
	 * Creates all content using different functions.
	 * 
	 * @param
	 * 
	 */
	public void createContent() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvSektion = (TextView) findViewById(R.id.tvSektion);
		tvWebUrl = (TextView) findViewById(R.id.tvWebUrl);
		ivImgUrl = (ImageView) findViewById(R.id.ivImgUrl);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		
		JSONArray jArrayContent = CustomHttpClient.getJSON(id,
				"http://trainwemust.com/pubapp/jsonscriptpub.php");
		displayJSONContent(jArrayContent);
		
		JSONArray jArraySpinner = CustomHttpClient.getJSON(id,
				"http://trainwemust.com/pubapp/jsonpubevents.php");
		if (jArraySpinner.length() == 0) {
			createEmptySpinner("Inga kommande events");
		}
		else {
			createEventSpinner(jArraySpinner);
		}

	}

	/**
	 * 
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param  jArray	the JSON array containing the content
	 * 
	 */
	public void displayJSONContent(JSONArray jArray) {

		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag", "id: " + json_data.getInt("id")
						+ ", pubName: " + json_data.getString("pubName")
						+ ", sektion: " + json_data.getString("sektion")
						+ ", weburl: " + json_data.getString("weburl")
						+ ", imgurl: " + json_data.getString("imgurl")
						+ ", info: " + json_data.getString("info"));

				// Converts JSON data to strings
				pubName = json_data.getString("pubName");
				sektion = json_data.getString("sektion");
				imgurl = json_data.getString("imgurl");
				weburl = json_data.getString("weburl");
				info = json_data.getString("info");

			}

		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		try {
			tvTitle.setText(pubName);
			tvSektion.setText(sektion);
			tvWebUrl.setText(weburl);
			ivImgUrl.setImageBitmap(ImageBitmap.getImageBitmap(imgurl)); // fetches
																			// a
																			// bitmap
			tvInfo.setText(info);
		} catch (Exception e) {
			Log.e("log_tag", "Error in Display!" + e.toString());
			;
		}

	}
	
	/**
	 * 
	 * Takes a JSONArray and display events in the spinner.
	 * Starts a new activity when a row is clicked, linked to the correct event.
	 * 
	 * @param  jArray	the JSON array containing the content
	 *
	 */
	public void createEventSpinner(JSONArray jArray) {

		eventList = new ArrayList<String>();
		eventIds = new ArrayList<Integer>();
		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag",
						"eventName: " + json_data.getString("eventName")
								+ ", eventDateStart: "
								+ json_data.getString("eventDateStart")
								+ ", pubName: "
								+ json_data.getString("pubName")
								+ ", eventId: " + json_data.getInt("eventId")
								+ ", pubId: " + json_data.getInt("pubId"));

				String eventName = json_data.getString("eventName");
				String eventDateStart = json_data.getString("eventDateStart");
				int eventId = json_data.getInt("eventId");
				eventList.add(eventName + " - " + eventDateStart);
				eventIds.add(eventId);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		eventSpinner = (Spinner) findViewById(R.id.eventSpinner);

		@SuppressWarnings("unchecked")
		ArrayAdapter dataAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, eventList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		eventSpinner.setAdapter(dataAdapter);
		eventSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	        	
	        }

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				check = check + 1;
				int goId = eventIds.get(arg2);
				if (check > 1) {
				Intent intent = new Intent(arg1.getContext(), Event.class);
				intent.putExtra("id", goId);
				startActivity(intent);
				}
			}
	    });
	}
	
	/**
	 * 
	 * Create a spinner with the param msg showing.
	 * 
	 * @param  msg		String showing in the spinner.
	 * 
	 */
	public void createEmptySpinner(String msg) {
		
		eventList = new ArrayList<String>();
		eventIds = new ArrayList<Integer>();
		eventList.add(msg);
		
		eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
		@SuppressWarnings("unchecked")
		ArrayAdapter dataAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, eventList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		eventSpinner.setAdapter(dataAdapter);
		
	}

}