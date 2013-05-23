package com.example.pubapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
>>>>>>> origin/event
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Main extends Activity {

<<<<<<< HEAD
	private TextView tvMainEventTitle, tvMainEventPubname, tvMainEventDate; // to show the information of the pub
	private String pubName, eventName, eventDateStart = ""; // to store the result of MySQL query after decoding JSON
	private int id;
	
	
=======
	private String pubName, eventName, eventDateStart = ""; // to store the result of MySQL query after decoding JSON
	private int id;
	private boolean flag = true;
>>>>>>> origin/event
	
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
		
<<<<<<< HEAD
		 /*tvMainEventTitle 	= (TextView) findViewById(R.id.tvMainEventTitle);
	     tvMainEventPubname 	= (TextView) findViewById(R.id.tvMainEventPubname);
	     tvMainEventDate 	= (TextView) findViewById(R.id.tvMainEventDate);
		 */ 
	     getInfo("bajs");
	     createTableRowButton(addDynamicButton(1, Kalender.class, "GŒ till kalender fšr fler events"));
	     
	     
=======
		createContent();
	    
>>>>>>> origin/event
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
	
<<<<<<< HEAD
	public void getInfo(String urlId) {
		// declare parameters that are passed to PHP script i.e. the id "id" and its value submitted by the app   
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id",urlId));
		String response = null;
=======
	/**
	 * 
	 * Creates all content using different functions.
	 * 
	 * @param  
	 * @return 
	 */
	public void createContent() {
		
		JSONArray content = CustomHttpClient.getJSON("1", "http://trainwemust.com/pubapp/jsonnewsfeed.php");
		// fetches a JSONArray from the MySQL database through a PhP-script
	    displayJSONContent(content);
		
	    Button btn = addDynamicButton(1, Kalender.class, "GŒ till kalender fšr fler events");
		btn.setTextSize(18);
		btn.setTypeface(null, Typeface.BOLD_ITALIC);
	    createTableRowButton(btn);
	    // adds a button to the calendar at the bottom
		
	}
	
	/**
	 * 
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param  jArray
	 * @return 
	 */	
	public void displayJSONContent(JSONArray jArray) {
		try{
			for(int i=0;i<jArray.length();i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag","eventName: "+json_data.getString("eventName")+
						", pubName: "+json_data.getString("pubName")+
						", eventDateStart: "+json_data.getString("eventDateStart")+
						", id: "+json_data.getInt("id")
						);
				
				pubName 	= json_data.getString("pubName");
				eventName 	= json_data.getString("eventName");
				id = json_data.getInt("id");
				
				String tempEventDateStart = json_data.getString("eventDateStart");
				if (!(eventDateStart.equals(tempEventDateStart))) { // makes sure date only shows once
					eventDateStart = tempEventDateStart;
					createTableRowDate(addDynamicTextView(eventDateStart, 10));
				}
				
				// creates an event-row with title, pubname and button
				createTableRowEvent(addDynamicTextView(eventName, 20),addDynamicTextView(pubName, 12),addDynamicButton(id, Event.class, "Visa"));
				
			}	
		}
		catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
>>>>>>> origin/event

	}

	/**
	 * 
	 * Creates a button with a listener to a specific class.
	 * 
	 * @param  id		Id to send extra with the intent
	 * @param  goClass	Target class for the intent
	 * @param  text		The string displayed in the Button
	 * @return Button
	 */
	private Button addDynamicButton(final int id, final Class<?> goClass, String text) {
		// creates a button dynamically
        Button btn = new Button(this);
        // sets button properties
        btn.setText(text);
        btn.getBackground().setColorFilter(Color.parseColor("#75E781"), PorterDuff.Mode.DARKEN);
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
	
	/**
	 * 
	 * Takes a String and max length and puts it in a TextView
	 * 
	 * @param  text		The string displayed in the TextView
	 * @param  max		Max length that will fit in the TextView
	 * @return TextView
	 */
	private TextView addDynamicTextView(String text, int max) {
		// creates a button dynamically
        TextView tv = new TextView(this);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(max);
        tv.setFilters(fArray);
        
        if (text.length() > max) {
            text = text.substring(0, (max-4));
            text += "...";	
        }
        
        tv.setText(text);
        return tv;
    }
	
	/**
	 * 
	 * Creates a row with three cells, with a TextView in each cell.
	 * 
	 * @param  left		TextView to the left
	 * @param  center	TextView in the center
	 * @param  right	TextView to the right
	 * @return 
	 */
	public void createTableRowEvent(TextView left, TextView center, TextView right) {
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  tr.setLayoutParams(lp);

<<<<<<< HEAD
			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","eventName: "+json_data.getString("eventName")+
							", pubName: "+json_data.getString("pubName")+
							", eventDateStart: "+json_data.getString("eventDateStart")+
							", id: "+json_data.getInt("id")
							);
					
					pubName 	= json_data.getString("pubName");
					eventName 	= json_data.getString("eventName");
					id = json_data.getInt("id");
					
					String tempEventDateStart = json_data.getString("eventDateStart");
					if (!(eventDateStart.equals(tempEventDateStart))) {
						eventDateStart = tempEventDateStart;
						createTableRowDate(addDynamicTextView(eventDateStart, 10));
					}
					
					createTableRowEvent(addDynamicTextView(eventName, 20),addDynamicTextView(pubName, 12),addDynamicButton(id, Event.class, "Visa"));
					
				}	
			}
			
				
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Log.e("log_tag", "Failed data was:\n" + result);
			}

			/*try{
				tvMainEventTitle.setText(eventName);
				tvMainEventPubname.setText("Vart : " + pubName);
				tvMainEventDate.setText("Datum : " + eventDateStart);
			}
			catch(Exception e){
				Log.e("log_tag","Error in Display!" + e.toString());;          
			}   */
=======

		  tr.addView(left);
		  tr.addView(center);
		  tr.addView(right);
		  if (flag) {
			  tr.setBackgroundColor(Color.parseColor("#FFFFFF"));
			  flag = false;
		  }
		  else {
			  tr.setBackgroundColor(Color.parseColor("#E9E9E9"));
			  flag = true;
		  }

		  tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
>>>>>>> origin/event
		}
	
	/**
	 * 
	 * Creates a row that spans over 3 cells and shows a date.
	 * 
	 * @param  date		TextView with a date
	 * @return 
	 */
	public void createTableRowDate(TextView date) {
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  lp.setMargins(0,30,0,0);
		  lp.span = 3;
		  date.setTextSize(18);
		  date.setTypeface(null, Typeface.BOLD);
		  tr.addView(date, lp);

		  tl.addView(tr);
		}
<<<<<<< HEAD
	}
	
	/**
	 * 
	 * Creates a button with a listener to a specific class.
	 * 
	 * @param  id		Id to send extra with the intent
	 * @param  goClass	Target class for the intent
	 * @param  text		The string displayed in the Button
	 * @return TextView
	 */
	private Button addDynamicButton(final int id, final Class goClass, String text) {
		// creates a button dynamically
        Button btn = new Button(this);
        // sets button properties
        btn.setText(text);
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
	
	/**
	 * 
	 * Takes a String and max length and puts it in a TextView
	 * 
	 * @param  text		The string displayed in the TextView
	 * @param  max		Max length that will fit in the TextView
	 * @return TextView
	 */
	private TextView addDynamicTextView(String text, int max) {
		// creates a button dynamically
        TextView tv = new TextView(this);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(max);
        tv.setFilters(fArray);
        
        if (text.length() > max) {
            text = text.substring(0, (max-4));
            text += "...";	
        }
        
        tv.setText(text);
        return tv;
    }
	
	/**
	 * 
	 * Creates a row with three cells, with a TextView in each cell.
	 * 
	 * @param  left		TextView to the left
	 * @param  center	TextView in the center
	 * @param  right	TextView to the right
	 * @return 
	 */
	public void createTableRowEvent(TextView left, TextView center, TextView right) {
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  tr.setLayoutParams(lp);


		  tr.addView(left);
		  tr.addView(center);
		  tr.addView(right);

		  tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	
	/**
	 * 
	 * Creates a row that spans over 3 cells and shows a date.
	 * 
	 * @param  date		TextView with a date
	 * @return 
	 */
	public void createTableRowDate(TextView date) {
=======
	
	/**
	 * 
	 * Creates a row that spans over 3 cells and shows a button.
	 * 
	 * @param  btn		Button
	 * @return 
	 */
	public void createTableRowButton(Button btn) {
>>>>>>> origin/event
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  lp.setMargins(0,30,0,0);
		  lp.span = 3;
<<<<<<< HEAD
		  date.setTextSize(18);
		  date.setTypeface(null, Typeface.BOLD);
		  tr.addView(date, lp);
=======
		  tr.addView(btn, lp);
>>>>>>> origin/event

		  tl.addView(tr);
		}
	
<<<<<<< HEAD
	/**
	 * 
	 * Creates a row that spans over 3 cells and shows a button.
	 * 
	 * @param  btn		Button
	 * @return 
	 */
	public void createTableRowButton(Button btn) {
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  lp.setMargins(0,30,0,0);
		  lp.span = 3;
		  btn.setTextSize(18);
		  btn.setTypeface(null, Typeface.BOLD_ITALIC);
		  tr.addView(btn, lp);

		  tl.addView(tr);
		}
	
=======
>>>>>>> origin/event

}
