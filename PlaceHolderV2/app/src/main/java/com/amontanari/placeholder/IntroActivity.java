package com.amontanari.placeholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amontanari.placeholder.utils.GPSTracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by andreamontanari on 27/06/16.
 */
public class IntroActivity extends AppCompatActivity {

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    GPSTracker gps;
    
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        
        prefs = getSharedPreferences("PLACEHOLDER_SP", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            // show walkthrough --> then set toolbar
            // initialise Realm.io
            prefs.edit().putBoolean("firstrun", false).apply();
        }
        setToolBar();
    }

    public void watchPlaces(View view) {
        Intent intent = new Intent(IntroActivity.this, PlacesActivity.class);
        startActivity(intent);
    }

    public void savePlace(View view) {

        gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            Intent intent = new Intent(IntroActivity.this, MapsActivity.class);
            startActivity(intent);
        }else {
            displayLocationSettingsRequest(this);
        }
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //All location settings are satisfied
                        Intent intent = new Intent(IntroActivity.this, MapsActivity.class);
                        startActivity(intent);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Location settings are not satisfied. Show the user a dialog to upgrade location settings

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(IntroActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // PendingIntent unable to execute request
                            Toast.makeText(IntroActivity.this, getResources().getString(R.string.error_gps_generic), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Location settings are inadequate, and cannot be fixed here. Dialog not created
                        Toast.makeText(IntroActivity.this, getResources().getString(R.string.error_gps_generic), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.intro_menu, menu);
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
            return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info_task:
                Intent intent = new Intent(IntroActivity.this, InformationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
