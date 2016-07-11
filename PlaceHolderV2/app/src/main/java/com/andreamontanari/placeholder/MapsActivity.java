package com.andreamontanari.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.andreamontanari.placeholder.utils.GPSTracker;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by andreamontanari on 07/07/16.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private GPSTracker gps;
    private LatLng latlng;
    private final int ZOOM_LEVEL = 15;
    
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        realm = Realm.getDefaultInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney, Australia, and move the camera.
        gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            //retrieve coordinates
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            latlng = new LatLng(latitude, longitude);
            //zoom in the map by level 16
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, ZOOM_LEVEL));
        }
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, IntroActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public void addNewPlace(View v) {
        gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            //retrieve coordinates
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            latlng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_location_white_48dp))
                    .title("Last saved place!"));
        }

        FloatingActionButton add_note_btn = (FloatingActionButton) findViewById(R.id.add_note_fab);
        add_note_btn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Registered place!", Toast.LENGTH_SHORT).show();
    }

    public void addNotePlace(View v) {
        FloatingActionButton add_note_btn = (FloatingActionButton) findViewById(R.id.add_note_fab);
        add_note_btn.setVisibility(View.GONE);
        mMap.clear();
        Toast.makeText(this, "Registered note for shown place!", Toast.LENGTH_SHORT).show();
    }
    
    public void storePlace(double latitude, double longitude, String streetName) {
        realm.beginTransaction();
        Place place = realm.createObject(Place.class); // Create a new object
        place.setLatitude("");
        place.setLongitude("");
        place.setStreetName("");
        realm.commitTransaction();
    }

}
