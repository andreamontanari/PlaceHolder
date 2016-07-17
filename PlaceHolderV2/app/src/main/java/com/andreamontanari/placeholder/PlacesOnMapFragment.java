package com.andreamontanari.placeholder;

/**
 * Created by andreamontanari on 08/07/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andreamontanari.placeholder.adapter.RVAdapter;
import com.andreamontanari.placeholder.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class PlacesOnMapFragment extends Fragment implements OnMapReadyCallback {


    private Realm realm;

    private GoogleMap mMap;
    private GPSTracker gps;
    private LatLng latlng;
    private double latitude, longitude;
    private String streetName;
    private final int ZOOM_LEVEL = 12;

    public PlacesOnMapFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_map_places, container, false);

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney, Australia, and move the camera.
        gps = new GPSTracker(getContext());
        if(gps.canGetLocation()) {
            //retrieve coordinates
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            latlng = new LatLng(latitude, longitude);
            //zoom in the map by level 16
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, ZOOM_LEVEL));
        }

        final Context context = getActivity().getApplicationContext();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();

        // load map
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        RealmResults<Place> places = realm.where(Place.class)
                .findAll(); // will place function to load saved places

        Log.d("TEST",places.toString());

        if (places != null && places.size() > 0) {

            // load markers in map
            for (Place place : places) {
                Log.d("TEST",place.toString());
                latlng = new LatLng(place.getLatitude(), place.getLongitude());

                mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_location_white_48dp))
                        .title(place.getPlaceComment() + " - " + place.getStreetName()));
            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.list_no_places), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.list_view);
        item.setVisible(true);
        MenuItem hide_item = menu.findItem(R.id.map_view);
        hide_item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}


