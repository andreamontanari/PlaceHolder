package com.andreamontanari.placeholder;

/**
 * Created by andreamontanari on 08/07/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreamontanari.placeholder.adapter.RVAdapter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class PlacesOnListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    
    public PlacesOnListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_list_places, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);

        return rootView;
    }

    @Override
    public void onStart() {

        final Context context = getActivity().getApplicationContext();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();

        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        RealmResults<Place> places = realm.where(Place.class)
                            .findAllSorted("savedOn", Sort.DESCENDING); // will place function to load saved places
        if (places != null && places.size() > 0) {
            mRecyclerView.setAdapter(new RVAdapter((PlacesActivity) this.getActivity(), places));
        } else {
            Toast.makeText(context, getResources().getString(R.string.list_no_places), Toast.LENGTH_SHORT).show();
        }
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        super.onStart();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.map_view);
        item.setVisible(true);
        MenuItem hide_item = menu.findItem(R.id.list_view);
        hide_item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}


