package com.example.pubapp;



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
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

	private TextView tvMainEventTitle, tvMainEventPubname, tvMainEventDate; // to show the information of the pub
	private String pubName, eventName, eventDateStart = ""; // to store the result of MySQL query after decoding JSON
	private int id;
	
	
	
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
		
		 /*tvMainEventTitle 	= (TextView) findViewById(R.id.tvMainEventTitle);
	     tvMainEventPubname 	= (TextView) findViewById(R.id.tvMainEventPubname);
	     tvMainEventDate 	= (TextView) findViewById(R.id.tvMainEventDate);
		 */ 
	     getInfo("bajs");
	     createTableRowButton(addDynamicButton(1, Kalender.class, "G� till kaleneder f�r fler events"));
	     
	     
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

	
	public void getInfo(String urlId) {
		// declare parameters that are passed to PHP script i.e. the id "id" and its value submitted by the app   
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		// define the parameter
		postParameters.add(new BasicNameValuePair("id",urlId));
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
						createTableRowDate(addDynamicTextView(eventDateStart));
					}
					
					createTableRowEvent(addDynamicTextView(eventName),addDynamicTextView(pubName),addDynamicButton(id, Event.class, "Visa"));
					
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
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());     
		}
	}
	
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
	
	private TextView addDynamicTextView(String text) {
		// creates a button dynamically
        TextView tv = new TextView(this);
        int maxLength = 20;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        tv.setFilters(fArray);
        if (text.length() > 20) {
            text = text.substring(0, 16);
            text += "...";	
        }
        tv.setText(text);
        return tv;
        // retrieve a reference to the container layout
    }

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
	
	public void createTableRowButton(Button date) {
		  TableLayout tl = (TableLayout) findViewById(R.id.mainTableLayout);
		  TableRow tr = new TableRow(this);
		  TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		  lp.setMargins(0,30,0,0);
		  lp.span = 3;
		  date.setTextSize(18);
		  date.setTypeface(null, Typeface.BOLD_ITALIC);
		  tr.addView(date, lp);

		  tl.addView(tr);
		}
	

}
