package com.example.pubapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class Karta extends Activity implements LocationListener, LocationSource{

	private GoogleMap mMap;
	private String pubName;
	private double lat, lng;
	private OnLocationChangedListener mListener;
	private LocationManager locationManager;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.karta);
		setUpMapIfNeeded();
		setUpLocationManagerIfNeeded();
		getInfo("string");
		setLocation();
	}
	
	@Override
	public void onPause(){
	    if(locationManager != null)
	    {
	        locationManager.removeUpdates(this);
	    }
	    super.onPause();
	}

	@Override
	public void onResume(){
	    super.onResume();
	    setUpMapIfNeeded();
	    if(locationManager != null)
	    {
	        mMap.setMyLocationEnabled(true);
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.setLocationSource(this);
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}
	
	private void setUpLocationManagerIfNeeded(){
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	    if(locationManager != null){
	        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	        
	        //Check whether GPS or Network can be reached, else notify user that no form of GPS is available

	        if(gpsIsEnabled)
	        {
	            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
	        }
	        else if(networkIsEnabled)
	        {
	            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
	        }
	        else {
	        	Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show();
	        }
	    }
	    //Location manager not working properly for some reason
	    else {
	    	Toast.makeText(this, "LocationManager not working properly", Toast.LENGTH_LONG).show();
	    }
	}
	
	@Override
	public void onLocationChanged(Location location){
		//checks if location has changed
	    if( mListener != null ){
	        mListener.onLocationChanged(location);
	        
	        LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds; //ändra så att chalmers alltid är låst
	        
	        if(!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude()))){
	             //Move the camera to the user's location if they are off-screen!
	             mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
	        }
	    }
	}
	
	//Initially sets the map to Chalmers
	private void setLocation() {
        CameraPosition cameraPosition = new CameraPosition.Builder() //fixa så att cameran antingen låser över lindholmen eller chalmers beroende på postition
				.target(new LatLng(57.68806, 11.977978)).zoom(15).build();
		mMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
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
					"http://trainwemust.com/pubapp/jsoncord.php",  //url of jsonphpscript
					postParameters);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();       
			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag",
							"pubName: "+json_data.getString("pubName")+
							", lat: "+json_data.getDouble("lat")+
							", lng: "+json_data.getDouble("lng")
							);
					
					//Converts json data to strings and doubles
					pubName 			= json_data.getString("pubName");
					lat 				= json_data.getDouble("lat");
					lng			 		= json_data.getDouble("lng");
					mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(pubName));
				}
			
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
				Log.e("log_tag", "Failed data was:\n" + result);
			}
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());     
		}
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void activate(OnLocationChangedListener listener) 
	{
	    mListener = listener;
	}

	@Override
	public void deactivate() 
	{
	    mListener = null;
	}

}
