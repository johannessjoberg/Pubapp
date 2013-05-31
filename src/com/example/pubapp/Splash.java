package com.example.pubapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity{
	
	/**
	 * 
	 * Creates a splash showing our icon for 3000 ms.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent startMain = new Intent("com.example.pubapp.MAIN");
					startActivity(startMain);
				}
			}
		};
		timer.start();
	}
	
}
