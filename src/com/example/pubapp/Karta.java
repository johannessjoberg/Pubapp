package com.example.pubapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Karta extends Activity implements LocationListener, LocationSource {

	private GoogleMap mMap;

	private String pubName;
	private double lat, lng, myLat, myLng;
	private OnLocationChangedListener locationListener;
	private LocationManager locationManager;

	@Override
	public void onPause() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setLocation();
		if (locationManager == null) {
			setUpLocationManagerIfNeeded();
		}
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
		setContentView(R.layout.karta);
		setUpMapIfNeeded();
		setUpLocationManagerIfNeeded();
		createContent();
		setLocation();
	}

	/**
	 * 
	 * Creates all content using different functions.
	 * 
	 * @param
	 * @return
	 */
	public void createContent() {
		JSONArray content = CustomHttpClient.getJSON("cords",
				"http://trainwemust.com/pubapp/jsoncord.php");
		// fetches a JSONArray from the MySQL database through a PhP-script
		displayJSONContent(content);
	}
	
	/**
	 * 
	 * Does exactly what the name says. If there is no map, one will be created.
	 * 
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true); // enables the user to see
													// current location
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

	private void setUpLocationManagerIfNeeded() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		String provider = "";

		if (locationManager != null) {

			boolean gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (gpsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 5000, 16, this);
				provider = LocationManager.GPS_PROVIDER;
			} else if (networkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000, 16, this);
				provider = LocationManager.NETWORK_PROVIDER;
			} else {
				Toast.makeText(this, "GPS unavailable", Toast.LENGTH_LONG)
						.show();
			}
			Location location = locationManager.getLastKnownLocation(provider);
			onLocationChanged(location);
		} else {
			// should never happen since locatioManager never should be zero
		}
	}


	@Override
	public void onLocationChanged(Location location) {

		myLat = location.getLatitude();
		myLng = location.getLongitude();
		// checks if location has changed

		if (locationListener != null) {
			locationListener.onLocationChanged(location);
			LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;
			if (!bounds.contains(new LatLng(myLat, myLng))) {
				// The camera should never move unless outside ny of the two
				// Chalmers campuses
				mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
						myLat, myLng)));
			}
		}
	}

	// Initially sets the map to Chalmers
	private void setLocation() {
		// Chalmers
		if (myLat >= 57.68 && myLat <= 57.69 && myLng >= 11.97
				&& myLng <= 11.98) {
			Camera(57.68806, 11.977978);
		}
		// Out of bounds
		else {
			Camera(myLat, myLng);
			Toast.makeText(this, "Your not at Chalmers", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 
	 * Sets the camera to the location specified by lati and lngi.
	 * 
	 * @param lati
	 * @param lngi
	 */
	private void Camera(double lati, double lngi) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(lati, lngi)).zoom(15) // decide how long the
															// map will zoom
				.build();
		mMap.moveCamera(CameraUpdateFactory // moves the camera to the new
											// location without animations
				.newCameraPosition(cameraPosition));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mHome:
			Intent home = new Intent(this, Main.class);
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
	 * Takes a JSONArray and displays its content.
	 * 
	 * @param jArray
	 * @return
	 */
	public void displayJSONContent(JSONArray jArray) {
		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag", "pubName: " + json_data.getString("pubName")
						+ ", lat: " + json_data.getDouble("lat") + ", lng: "
						+ json_data.getDouble("lng"));

				// Converts json data to strings and doubles
				pubName = json_data.getString("pubName");
				lat = json_data.getDouble("lat");
				lng = json_data.getDouble("lng");
				mMap.addMarker(new MarkerOptions().position(
						new LatLng(lat, lng)).title(pubName));
			}

		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		locationListener = listener;
	}

	@Override
	public void deactivate() {
		locationListener = null;
	}
}
