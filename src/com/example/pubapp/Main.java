package com.example.pubapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Main extends Activity {

	
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
		setContentView(R.layout.main);
		
	}



}
