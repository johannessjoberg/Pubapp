package com.example.pubapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Pubar extends Activity {

	private String pubName, sektion, colorCode;
	private int pubId;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pubar);

		createContent();
	}

	@Override
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
	 * @param 
	 * 
	 * @return
	 * 
	 */
	private void createContent() {
		JSONArray content = CustomHttpClient.getJSON("1",
				"http://trainwemust.com/pubapp/jsonscriptpubbar.php");
		// fetches a JSONArray from the MySQL database through a Php-script
		displayJSONContent(content);
	}
	
	/**
	 * 
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param  jArray
	 * @return 
	 */	
	private void displayJSONContent(JSONArray jArray) {
		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag", "id: " + json_data.getInt("id")
						+ ", pubName: " + json_data.getString("pubName")
						+ ", sektion: " + json_data.getString("sektion")
						+ ", info: " + json_data.getString("info")
						+ ", weburl: " + json_data.getString("weburl")
						+ ", imgurl: " + json_data.getString("imgurl")
						+ ", lat: " + json_data.getDouble("lat") + ", lng: "
						+ json_data.getDouble("lng") + ", colorCode: "
						+ json_data.getString("colorCode"));

				pubId = json_data.getInt("id");
				pubName = json_data.getString("pubName");
				sektion = json_data.getString("sektion");
				colorCode = json_data.getString("colorCode");

				colorCode.toUpperCase();
				TextView tv = addDynamicTextView(sektion, 50);
				Button bt = addDynamicButton(pubId, Pub.class, pubName,
						colorCode);

				LinearLayout llinner = (LinearLayout) findViewById(R.id.llinner);
				llinner.addView(tv);
				llinner.addView(bt);

			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

	}

	/**
	 * 
	 * Takes a String and max length and puts it in a TextView
	 * 
	 * @param text
	 *            The string displayed in the TextView
	 * @param max
	 *            Max length that will fit in the TextView
	 * @return TextView
	 */
	private TextView addDynamicTextView(String text, int max) {
		// creates a button dynamically
		TextView tv = new TextView(this);
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(max);
		tv.setFilters(fArray);

		if (text.length() > max) {
			text = text.substring(0, (max - 4));
			text += "...";
		}

		tv.setText(text);
		return tv;
	}

	/**
	 * 
	 * Creates a button with a listener to a specific class.
	 * 
	 * @param id
	 *            Id to send extra with the intent
	 * @param goClass
	 *            Target class for the intent
	 * @param text
	 *            The string displayed in the Button
	 * @return Button
	 */
	private Button addDynamicButton(final int id, final Class<?> goClass,
			String text, String color) {
		// creates a button dynamically
		Button btn = new Button(this);
		// sets button properties
		btn.setHeight(0);
		btn.setText(text);
		// btn.setBackgroundResource(R.drawable.green_button);
		// btn.setPadding(15, 15, 15, 15);
		btn.getBackground().setColorFilter(Color.parseColor(color),
				PorterDuff.Mode.OVERLAY);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), goClass);
				intent.putExtra("id", id);

				startActivity(intent);
			}
		});

		return btn;

	}

}