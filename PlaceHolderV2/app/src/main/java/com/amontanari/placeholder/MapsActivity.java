package com.amontanari.placeholder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amontanari.placeholder.utils.GPSTracker;
import com.amontanari.placeholder.utils.PlaceMisc;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by amontanari on 07/07/16.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private GPSTracker gps;
    private LatLng latlng;
    private double latitude, longitude;
    private String streetName;
    private final int ZOOM_LEVEL = 15;


    static List<Address> addresses;
    
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setToolBar();

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
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
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
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            latlng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_location_white_48dp))
                    .title("Last saved place!"));
            streetName = getAddress(latitude, longitude);
            storePlace(latitude, longitude, streetName);
        }

        FloatingActionButton add_note_btn = (FloatingActionButton) findViewById(R.id.add_note_fab);
        add_note_btn.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.place_saved, Toast.LENGTH_SHORT).show();
    }

    public void addNotePlace(View v) {

        PlaceMisc.storePlaceNote("", latitude, longitude, this);

        FloatingActionButton add_note_btn = (FloatingActionButton) findViewById(R.id.add_note_fab);
        add_note_btn.setVisibility(View.GONE);
        mMap.clear();
    }
    
    public void storePlace(double latitude, double longitude, String streetName) {

        // Create the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        realm.beginTransaction();
        Place place = realm.createObject(Place.class); // Create a new Place object
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setStreetName(streetName);
        place.setSavedOn();
        place.setPlaceComment(getString(R.string.place_comment_intro));
        realm.commitTransaction();
    }

    //---- save coordinates as addresses -----
    public String getAddress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        addresses = null;
        String notFound = this.getString(R.string.address_not_found);

        try {
            addresses = geocoder.getFromLocation(latitude,  longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), addresses.get(0).toString(), Toast.LENGTH_SHORT).show();
        if (addresses != null && addresses.size() > 0) {
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
                    address.getCountryCode());
            return addressText;

        } else {
            return notFound;
        }
    }

}
