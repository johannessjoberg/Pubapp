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
	
	//declare vaiables
	private String pubName, colorCode, pubNameQR;
	private int pubId, pubIdQR;
	private boolean flag = true;
	private boolean waitForResult = false;
	private ArrayList<CheckBox> checkBoxes;

	//Create the actionbar
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

	//Create the activity and run the method createContent
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runda);
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
	
	/**
	 * 
	 * Creates all content using different functions.
	 * 
	 * @param  
	 * @return 
	 */
	private void createContent() {
		checkBoxes = new ArrayList<CheckBox>();
		JSONArray content = CustomHttpClient.getJSON("1",
				"http://trainwemust.com/pubapp/jsonscriptpubbar.php");
		// fetches a JSONArray from the MySQL database through a Php-script
		displayJSONContent(content);
	}
	
	/**
	 * 
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param  jArray	the JSON array containing the content
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

	/**
	 * 
	 * Creates a TableRow with three cells.
	 * 
	 * @param  left		TextView showing in the first cell
	 * @param  center	Button showing in the second cell
	 * @param  right	CheckBox showing in the third cell
	 * @return 
	 */
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

	/**
	 * 
	 * Creates a CheckBox with an id.
	 * 
	 * @param  id		id of the CheckBox
	 * @return CheckBox
	 */
	private CheckBox addDynamicCheckBox(int id) {

		CheckBox cb = new CheckBox(this);
		cb.setId(id);
		cb.setChecked(false);
		cb.setEnabled(false);
		
		return cb;
	}

	/**
	 * 
	 * Creates a Button to start the Bar-Code scanner.
	 * 
	 * @param  id		id of the pub
	 * @param  cb		needed to check if CheckBox is "checked"
	 * @param  name		name of the pub
	 * @param  color	color of the Button
	 * @return Button
	 */
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
		});
		return btn;
	}
	
	/**
	 * 
	 * Creates a TextView containing a String with a max length.
	 * 
	 * @param  text		The string displayed in the TextView
	 * @param  max		Max length of the String
	 * @return TextView
	 */
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
	/**
	 * 
	 * Handling the results from the Bar-Code scan.
	 * 
	 * @param  requestCode	Check if their is a request.
	 * @param  resultCode	Used to check if scan result is ok.
	 * @param  data			The Intent started by the Bar-Code scan.
	 * @return 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				contents.trim();
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				pubIdQR = ImageBitmap.getInstance().getPubId();
				pubNameQR = ImageBitmap.getInstance().getPubName();
				Log.d("xZing", "contents: " + contents + " format: " + format + " pubid:" + pubIdQR + "pubnamn:" + pubNameQR + " storlek på array: " + checkBoxes.size());
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
