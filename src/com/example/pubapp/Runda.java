package com.example.pubapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pubapp.R.id;
import com.example.pubapp.R;
import com.google.android.gms.internal.cb;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Runda extends Activity {

	/*
	 * public void Runda(){ }
	 * 
	 * 
	 * private static class RundaHolder{ private static final Runda INSTANCE =
	 * new Runda(); }
	 * 
	 * public static Runda getInstance(){ return RundaHolder.INSTANCE; }
	 */
	private String pubName, colorCode, pubNameQR;
	private int pubId, pubIdQR;
	private boolean flag = true;
	private boolean waitForResult = false;
	private ArrayList<CheckBox> checkBoxes;
	// int idTest = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);

		return true;
	}
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runda);
		createContent();

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mHome:
			Intent home = new Intent (this, Main.class);
			startActivity(home);
			return true;
		case R.id.mPubar:
			this.onPause();
			Intent pubar = new Intent(this, Pubar.class);
			startActivity(pubar);
			return true;
		case R.id.mKarta:
			this.onPause();
			Intent kar = new Intent(this, Karta.class);
			startActivity(kar);
			return true;
		case R.id.mRunda:
			this.onPause();
			Intent rund = new Intent(this, Runda.class);
			startActivity(rund);
			return true;
		case R.id.mKal:
			this.onPause();
			Intent kal = new Intent(this, Kalender.class);
			startActivity(kal);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void createContent() {
		checkBoxes = new ArrayList<CheckBox>();
		JSONArray content = CustomHttpClient.getJSON("1",
				"http://trainwemust.com/pubapp/jsonscriptpubbar.php");
		// fetches a JSONArray from the MySQL database through a Php-script
		displayJSONContent(content);
	}

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
				colorCode = json_data.getString("colorCode");

				colorCode.toUpperCase();

				CheckBox cb = addDynamicCheckBox(pubId);
				checkBoxes.add(cb);
				Button bt = addDynamicButton(pubId, cb, pubName, colorCode);
				TextView tv = addDynamicTextView(pubName, 20);

				createTableRowEvent(tv, bt, cb);
				
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}

	private void createTableRowEvent(TextView left, Button center,
			CheckBox right) {
		TableLayout tl = (TableLayout) findViewById(R.id.rundaTableLayout);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		tr.addView(left);
		tr.addView(center);
		tr.addView(right);
		if (flag) {
			tr.setBackgroundColor(Color.parseColor("#FFFFFF"));
			flag = false;
		} else {
			tr.setBackgroundColor(Color.parseColor("#E9E9E9"));
			flag = true;
		}

		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private CheckBox addDynamicCheckBox(int id) {

		CheckBox cb = new CheckBox(this);
		cb.setId(id);
		cb.setChecked(false);
		cb.setEnabled(false);
		
		return cb;
	}

	private Button addDynamicButton(final int id, final CheckBox cb,
			final String name, String color) {
		// creates a button dynamically
		Button btn = new Button(this);
		// sets button properties
		btn.setHeight(0);
		btn.setText(name);
		btn.getBackground().setColorFilter(Color.parseColor(color),
				PorterDuff.Mode.OVERLAY);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("onClick", "Check i onClick name: " + name + " id: " + id);
				ImageBitmap.getInstance().setPubName(name);
				ImageBitmap.getInstance().setPubId(id);
				if (!cb.isChecked()) {
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra(
							"com.google.zxing.client.android.SCAN.SCAN_MODE",
							"QR_CODE_MODE");
					
					startActivityForResult(intent, 1);
					
				}

			}

			// else if (cb.isChecked()) {
			// cb.setChecked(false);
			// }

		});
		return btn;
	}

	private TextView addDynamicTextView(String text, int max) {
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				contents.trim();
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				pubIdQR = ImageBitmap.getInstance().getPubId();
				pubNameQR = ImageBitmap.getInstance().getPubName();
				Log.d("xZing", "contents: " + contents + " format: " + format + " pubid:" + pubIdQR + "pubnamn:" + pubNameQR + " storlek p� array: " + checkBoxes.size());
				// Handle successful scan
				if (contents.equals(pubNameQR)) {
					for(int i = 0; i < checkBoxes.size(); i++){
						if (checkBoxes.get(i).getId() == pubIdQR) {
							checkBoxes.get(i).setChecked(true);
							Log.d("i if-satsen ", "id:" + checkBoxes.get(i).getId());
						}
					}
					
				}
				 
			} 
			else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.i("xZing", "Cancelled");
			}
		}
	}

}
