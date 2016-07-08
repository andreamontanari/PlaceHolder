package com.andreamontanari.placeholder;

/**
 * Created by andreamontanari on 08/07/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andreamontanari.placeholder.adapter.RVAdapter;

import java.util.List;

public class PlacesOnMapFragment extends Fragment {


    public PlacesOnMapFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_map_places, container, false);

        return rootView;
    }

    @Override
    public void onStart() {

        // load map

        final Context context = getActivity().getApplicationContext();
        List<Place> places = null; // will place function to load saved places
        if (places != null && places.size() > 0) {

            // load markers in map
        } else {
            Toast.makeText(context, getResources().getString(R.string.list_no_places), Toast.LENGTH_SHORT).show();
        }

        super.onStart();
    }




}


