package com.andreamontanari.placeholder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            showInfo();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void beginLocation(View v) {
		
		 gps = new GPSTracker(this);
	        if(gps.canGetLocation()){
	        	Intent intent = new Intent(this, LocatingActivity.class);
	    		startActivity(intent);
	        }else {
	        	gps.showSettingsAlert();
	        }
		
	}
	
	
	public void showInfo() {
		Intent intent = new Intent(this, InformationActivity.class);
		startActivity(intent);
	}
	
	public void showGPShint(View v) {
		
		  LayoutInflater layoutInflater = LayoutInflater.from(this);  
	  	  View hintView = layoutInflater.inflate(R.layout.gpshint, null);
	  	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);  
	  	  // set prompts.xml to be the layout file of the alertdialog builder
	  	  alertDialogBuilder.setView(hintView);
	  	  
	  	alertDialogBuilder
	        .setCancelable(false)
	        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	             }   
	            });
			// create an alert dialog
	        AlertDialog alert = alertDialogBuilder.create();
          alert.show();  
		}
	}
	