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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreamontanari.placeholder.adapter.RVAdapter;

import java.util.List;

public class PlacesOnListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public PlacesOnListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_list_places, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);

        return rootView;
    }

    @Override
    public void onStart() {

        final Context context = getActivity().getApplicationContext();
        List<Place> places = null; // will place function to load saved places
        if (places != null && places.size() > 0) {

            mAdapter = new RVAdapter(places);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(context, getResources().getString(R.string.list_no_places), Toast.LENGTH_SHORT).show();
        }
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.share_btn:
                                Toast.makeText(context, "SHARE", Toast.LENGTH_SHORT).show();
                                    break;
                            case R.id.direction_btn:
                                Toast.makeText(context, "GO THERE", Toast.LENGTH_SHORT).show();
                                    break;
                        }
                    }
                })
        );

        super.onStart();
    }




}


