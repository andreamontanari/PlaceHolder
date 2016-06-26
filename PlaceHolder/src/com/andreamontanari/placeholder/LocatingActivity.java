package com.andreamontanari.placeholder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocatingActivity extends FragmentActivity {

	GoogleMap map;
	GPSTracker gps;
	static String addr;
    static String[] address = new String[5];
	static String[] notes = new String[5];
	static List<Address> addresses;
	static int i= 0, j = 0;
	final Context context = this;
	private EditText editTextMainScreen;
	String note;
	ImageButton ib;
	ImageView iv;
	int valid_len;
	private Vibrator myVib;

	String add ="address";
	static String not ="notes";
	String ci="i";
	String cj ="j";
	LatLng latlng;
		
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locating);
  
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        //get the map
        map = supportmapfragment.getMap();
        
        //set the blue pointer on the map
        map.setMyLocationEnabled(true);
        
        editTextMainScreen = (EditText) findViewById(R.id.editTextResult);
        
        //set button for notes invisible at the beginning
        ib = (ImageButton) findViewById(R.id.imageButton1);
        ib.setVisibility(View.INVISIBLE);      
        iv = (ImageView) findViewById(R.id.plus);
        iv.setVisibility(View.INVISIBLE);

        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        //LOADING PROCESS
        address = Save.loadArray(add, context);
        notes = Save.loadArray(not, context);
        i = Save.loadCounter(ci, context);
        j = Save.loadCounter(cj, context);       
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.locating, menu);
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
	
	public void onStop() {
		super.onStop();
		
		//SAVING PROCESS
		Save.saveArray(address, add, context); 
        Save.saveArray(notes, not, context);
        Save.saveCounter(i, ci, context);
        Save.saveCounter(j, cj, context);
	}


	public void getPositions(View v) {

    myVib.vibrate(20);
	Intent intent = new Intent(this, PositionActivity.class);
	startActivity(intent);
	}

	public void getCoords(View v) throws IOException {

		    myVib.vibrate(20);		

		    gps = new GPSTracker(this);
	        if(gps.canGetLocation()){
	        //retrieve coordinates
	       	double latitude = gps.getLatitude();
	       	double longitude = gps.getLongitude();
	        latlng = new LatLng(latitude, longitude);  
	        //zoom in the map by level 16
	        
	        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16)); 

	        addr = getAddress();
	        address[i] = addr;
	        notes[i] = "";
	        
	        if (i >= 4) {
	        	i = 0;
	        } else {
	        	i++;
	        }

	        if (j > 4) {
	        	j = 1;
	        } else {
	        	j++;
	        }
	     
	        ib.setVisibility(View.VISIBLE);
	        iv.setVisibility(View.VISIBLE);
	        }else {
	        	gps.showSettingsAlert();
	        }            
	        //display message with hello
	        //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show(); 
	        
	}
	
			//---- save coordinates as addresses -----
	        public String getAddress() throws IOException {
	        
	        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
	        
	        addresses = null;
	        String notFound = context.getString(R.string.addrnotfound);
	        
                addresses = geocoder.getFromLocation(gps.getLatitude(),  gps.getLongitude(), 1);  
                
                //Toast.makeText(getApplicationContext(), addresses.get(0).toString(), Toast.LENGTH_SHORT).show(); 
                int n = addresses.size();
                if (addresses != null && n > 0) {
                    // Get the first address
                    Address address = addresses.get(0);
                    /*
                     * Format the first line of address (if available),
                     * city, and country name.
                     */
                    String addressText = String.format(
                            "%s, %s, %s",
                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?
                                    address.getAddressLine(0) : "",
                            // Locality is usually a city
                            address.getLocality(),
                            // The country of the address
                            address.getCountryName());
                    return addressText;
       
                } else {
                    return notFound;
                }
	        }  
        
	        
       public void showInfo() {
           Intent intent = new Intent(this, InformationActivity.class);
           startActivity(intent);
       }         
       
       public void addNote(View v) {
    	   
    	  myVib.vibrate(20); 
    	  // get prompts.xml view
    	  LayoutInflater layoutInflater = LayoutInflater.from(context);  
    	  View promptView = layoutInflater.inflate(R.layout.prompt, null);
    	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);  
    	  // set prompts.xml to be the layout file of the alertdialog builder
    	  alertDialogBuilder.setView(promptView);
    	
    	final EditText input = (EditText) promptView.findViewById(R.id.userInput);
    	final TextView tv = (TextView) promptView.findViewById(R.id.promptCount);
    	
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
    	        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	             public void onClick(DialogInterface dialog, int id) {
    	        // get user input and set it to result
    	        editTextMainScreen.setText(input.getText());
    	        //store in note the content of the note
    	        note = editTextMainScreen.getText().toString(); 
    	        notes[j-1] = note;  
    	        ib.setVisibility(View.INVISIBLE);
    	        iv.setVisibility(View.INVISIBLE);
    	             }
    	            })
    	        .setNegativeButton(R.string.canc, new DialogInterface.OnClickListener() {
    	             public void onClick(DialogInterface dialog, int id) {
    	             }
    	            });
    			// create an alert dialog
    	        AlertDialog alert = alertDialogBuilder.create();
                alert.show();    
       }
    }
    	       



