package com.andreamontanari.placeholder;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PositionActivity extends Activity {
    
	TextView t1, t2, t3;
	int index = LocatingActivity.i;
	private Vibrator myVib;
	ImageView iv;
	String uri = "";
	final Context context = this;
	String note;
	private EditText editTextMainScreen;
	String a1 = LocatingActivity.address[0];
	String a2 = LocatingActivity.address[1];
	String a3 = LocatingActivity.address[2];
	String a4 = LocatingActivity.address[3];
	String a5 = LocatingActivity.address[4];
	
	String no1 = LocatingActivity.notes[0];
	String no2 = LocatingActivity.notes[1];
	String no3 = LocatingActivity.notes[2];
	String no4 = LocatingActivity.notes[3];
	String no5 = LocatingActivity.notes[4];
	
	String notFound = context.getString(R.string.addrnotfound);
	
    protected void onCreate(Bundle savedInstanceState) {
    	
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_position);

    myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
    
	    
	
    	TextView t1 = (TextView)findViewById(R.id.address1);
        t1.setText(a1);
        TextView t2 = (TextView)findViewById(R.id.address2);
        t2.setText(a2);
        TextView t3 = (TextView)findViewById(R.id.address3);
        t3.setText(a3);
        TextView t4 = (TextView)findViewById(R.id.address4);
        t4.setText(a4);
        TextView t5 = (TextView)findViewById(R.id.address5);
        t5.setText(a5);
        
        
        TextView n1 = (TextView)findViewById(R.id.TextView01);
        n1.setText(no1);
        if (a1 == null || a1 == notFound){
        	n1.setClickable(false);
        } else {
        	n1.setClickable(true);
        }
        TextView n2 = (TextView)findViewById(R.id.TextView02);
        n2.setText(no2);
        if (a2 == null || a2 == notFound){
        	n2.setClickable(false);
        } else {
        	n2.setClickable(true);
        }
        TextView n3 = (TextView)findViewById(R.id.TextView03);
        n3.setText(no3);
        if (a3 == null || a3 == notFound){
        	n3.setClickable(false);
        } else {
        	n3.setClickable(true);
        }
        TextView n4 = (TextView)findViewById(R.id.TextView04);
        n4.setText(no4);
        if (a4 == null || a4 == notFound){
        	n4.setClickable(false);
        } else {
        	n4.setClickable(true);
        }
        TextView n5 = (TextView)findViewById(R.id.TextView05);
        n5.setText(no5);
        if (a5 == null || a5 == notFound){
        	n5.setClickable(false);
        } else {
        	n5.setClickable(true);
        } 
        editTextMainScreen = (EditText) findViewById(R.id.editTextResult1);
	}
    
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.position, menu);
		return true;
	}
	
	@Override
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
	
	
	protected void onStop() {
		
		super.onStop();
		ImageView iv01 = (ImageView)findViewById(R.id.iv1);
		ImageView iv02 = (ImageView)findViewById(R.id.iv2);
		ImageView iv03 = (ImageView)findViewById(R.id.iv3);
		ImageView iv04 = (ImageView)findViewById(R.id.iv4);
		ImageView iv05 = (ImageView)findViewById(R.id.iv5);
		
		iv01.setBackgroundResource(R.drawable.normal);
		iv02.setBackgroundResource(R.drawable.normal);
		iv03.setBackgroundResource(R.drawable.normal);
		iv04.setBackgroundResource(R.drawable.normal);
		iv05.setBackgroundResource(R.drawable.normal);
		
		Save.saveArray(LocatingActivity.notes, LocatingActivity.not, context);
	
	}
	
	public void goToMaps(View v) {
		switch(v.getId()) {
		case R.id.iv1:
			 iv = (ImageView)findViewById(R.id.iv1);
			 iv.setBackgroundResource(R.drawable.highlight1);
			 if (a1 == null) {
				  a1 = "";
			 }
			 uri = String.format(Locale.ENGLISH, "geo:0,0?q="+a1);
			break;
		case R.id.iv2:
			iv = (ImageView)findViewById(R.id.iv2);
			iv.setBackgroundResource(R.drawable.highlight2);
			if (a2 == null) {
				  a2 = "";
			 }
			uri = String.format(Locale.ENGLISH, "geo:0,0?q="+a2);
			break;
		case R.id.iv3:
			iv = (ImageView)findViewById(R.id.iv3);
			iv.setBackgroundResource(R.drawable.highlight3);
			if (a3 == null) {
				  a3 = "";
			 }
			uri = String.format(Locale.ENGLISH, "geo:0,0?q="+a3);
			break;
		case R.id.iv4:
			iv = (ImageView)findViewById(R.id.iv4);
			iv.setBackgroundResource(R.drawable.highlight4);
			if (a4 == null) {
				  a4 = "";
			 }
			uri = String.format(Locale.ENGLISH, "geo:0,0?q="+a4);
			break;
		case R.id.iv5:
			iv = (ImageView)findViewById(R.id.iv5);
			iv.setBackgroundResource(R.drawable.highlight5);
			if (a5 == null) {
				  a5 = "";
			 }
			uri = String.format(Locale.ENGLISH, "geo:0,0?q="+a5);
			break;
		}		
		myVib.vibrate(40);
		//to open google maps on the address selected'
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);		
	}

	
	public void showInfo() {
		Intent intent = new Intent(this, InformationActivity.class);
		startActivity(intent);
	}
	
	public void modifyNote(View v) throws IllegalAccessException, IllegalArgumentException {
		
	  //textview content (note)	
	  int id = v.getId();
	  final TextView textv = (TextView) findViewById(id);
	  final String text = textv.getText().toString();
	  final String resName = getResources().getResourceEntryName(id);
	   
	  LayoutInflater layoutInflater = LayoutInflater.from(this);  
  	  View promptView = layoutInflater.inflate(R.layout.prompt2, null);
  	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);  
  	  // set prompts.xml to be the layout file of the alertdialog builder
  	  alertDialogBuilder.setView(promptView);
  	
  	  final EditText input = (EditText) promptView.findViewById(R.id.userModInput);
  	  input.setText( text, TextView.BufferType.EDITABLE);
  	
  	  //char counter
  	  final TextView tv = (TextView) promptView.findViewById(R.id.promptModCount);
  	
  	input.addTextChangedListener(new TextWatcher() {
  		@Override
  	    public void onTextChanged(CharSequence s, int start, int before, int count) {
  	    // TODO Auto-generated method stub

  	    }
  	    @Override
  	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  	    // TODO Auto-generated method stub
  	    }

  	    @Override
  	    public void afterTextChanged(Editable s) {
  	    // TODO Auto-generated method stub
  	    	int counter = input.getText().toString().length();
  	    	tv.setText(String.valueOf(counter)+"/80"); 
  			}	    
  	}); 

  	// setup a dialog window
  	alertDialogBuilder
  	        .setCancelable(false)
  	        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
  	             public void onClick(DialogInterface dialog, int id) {
  	        // get user input and set it to result 
  	        editTextMainScreen.setText(input.getText());
  	        //store in note the content of the note
  	       note = editTextMainScreen.getText().toString(); 
  	       textv.setText(note);
  	     
  	        if (resName.equals("TextView01")) {
  	        	LocatingActivity.notes[0] = note;
  	             }
  	        else if (resName.equals("TextView02")) {
  	        	LocatingActivity.notes[1] = note;
  	        }
  	        else if (resName.equals("TextView03")) {
  	        	LocatingActivity.notes[2] = note; 
	        }
  	        else if (resName.equals("TextView04")) {
  	        	LocatingActivity.notes[3] = note;
	        }
  	      else if (resName.equals("TextView05")) {
  	    	    LocatingActivity.notes[4] = note;
	        }
  	      //updating notes' file  
  	      Save.saveArray(LocatingActivity.notes, LocatingActivity.not, context);
  	      
  	             }   
  	            })
  	        .setNegativeButton(R.string.undo, new DialogInterface.OnClickListener() {
  	             public void onClick(DialogInterface dialog, int id) {
  	             }
  	            });
  			// create an alert dialog
  	        AlertDialog alert = alertDialogBuilder.create();
              alert.show();  
     }
}





