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
import android.widget.Spinner;
import android.widget.TextView;

public class Pub extends Activity {

	private static final String TAG = null;
	private ImageView ivImgUrl; // for showing the "sektion" picture
	private TextView tvTitle, tvSektion, tvWebUrl, tvInfo; // to show the
															// information of
															// the pub
	private String pubName, sektion, weburl, imgurl, info, id; // to store the
																// result
																// of MySQL
																// query
																// after
																// decoding
																// JSON

	private Spinner eventSpinner;
	private List<String> eventList;
	private List<Integer> eventIds;

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
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		// StrictMode to catch accidental disk or network access on the
		// application's main thread

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub);

		Intent sender = getIntent();
		id = sender.getExtras().getString("id");

		createContent();

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

	/**
	 * 
	 * Creates all content using different functions.
	 * 
	 * @param
	 * @return
	 */
	public void createContent() {
		JSONArray jArrayContent = CustomHttpClient.getJSON(id,
				"http://trainwemust.com/pubapp/jsonscriptpub.php");
		//JSONArray jArraySpinner = CustomHttpClient.getJSON(id,
		//		"http://trainwemust.com/pubapp/jsonscriptnewsfeed.php");
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvSektion = (TextView) findViewById(R.id.tvSektion);
		tvWebUrl = (TextView) findViewById(R.id.tvWebUrl);
		ivImgUrl = (ImageView) findViewById(R.id.ivImgUrl);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		displayJSONContent(jArrayContent);
		//createEventSpinner(jArraySpinner);
	}

	/**
	 * 
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param jArray
	 * @return
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
								+ ", eventId: " + json_data.getInt("eventId")
								+ ", pubId: " + json_data.getInt("pubId"));

				String eventName = json_data.getString("pubName");
				String eventDateStart = json_data.getString("eventName");
				int eventId = json_data.getInt("eventId");
				eventList.add(eventName + " - " + eventDateStart);
				eventIds.add(eventId);
			}
		} 
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

	}

}