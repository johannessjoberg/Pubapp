package com.example.pubapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Pubar extends Activity {
	
	Button bGoldenI;
	
	
	public void Pubar(){
	}
	
	private static class PubarHolder{
		private static final Pubar INSTANCE = new Pubar();
	}
	
	public static Pubar getInstance(){
		return PubarHolder.INSTANCE;
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
		setContentView(R.layout.pubar);
		
		Button goldenI = (Button) findViewById(R.id.bGoldenI);
		goldenI.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Pub.class);
				intent.putExtra("id", "1");
				startActivity(intent);
			}
		});
		
		Button basen = (Button) findViewById(R.id.bBasen);
		basen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Pub.class);
				intent.putExtra("id", "2");
				startActivity(intent);
			}
		});
		
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
}
