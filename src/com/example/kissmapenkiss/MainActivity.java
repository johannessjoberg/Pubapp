package com.example.kissmapenkiss;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.CameraPosition.Builder;

public class MainActivity extends Activity {
	
	private GoogleMap mMap;
	private Marker Golden_I;
	private Marker Winden;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		addMarkers();
		setLocation();
	}

	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }
	}
	private void addMarkers(){
		mMap.addMarker(new MarkerOptions().position(new LatLng(57.692954, 11.975169)).title("Golden I"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.689474, 11.978273)).title("Winden"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(57.688758, 11.974776)).title("J.A Prips"));
		}
	    
	private void setLocation(){
		CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(57.692954, 11.975169))
        .zoom(15)
        .build();                  
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	    
	}



