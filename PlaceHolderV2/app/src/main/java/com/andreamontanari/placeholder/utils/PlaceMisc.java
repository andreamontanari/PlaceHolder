package com.andreamontanari.placeholder.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreamontanari.placeholder.Place;
import com.andreamontanari.placeholder.R;
import com.github.clans.fab.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

/**
 * Created by andreamontanari on 12/07/16.
 */
public class PlaceMisc {

    private static Realm realm;

    public static void storePlaceNote(String note, final double latitude, final double longitude, final Context context) {

        // Create the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogLayout = inflater.inflate(R.layout.dialog_place_comment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText etNote = (EditText) dialogLayout.findViewById(R.id.place_text);
        if(note != context.getString(R.string.place_comment_intro)) {
            etNote.setText(note);
        }

        builder.setView(dialogLayout);

        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.show();

        Button undo_btn = (Button) dialogLayout.findViewById(R.id.undo_btn);
        undo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
            }
        });

        Button post_btn = (Button) dialogLayout.findViewById(R.id.post_btn);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get last saved Place
                Place lastSaved = realm.where(Place.class)
                        .equalTo("latitude", latitude)
                        .equalTo("longitude", longitude).findFirst();

                realm.beginTransaction();
                lastSaved.setPlaceComment(etNote.getText().toString());
                realm.commitTransaction();

                customAlertDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.place_comment_added), Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    public void deleteAllPlaces() {
        
        // Create the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);
        
        // obtain the results of a query
        final RealmResults<Place> results = realm.where(Place.class).findAll();
        
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

}
