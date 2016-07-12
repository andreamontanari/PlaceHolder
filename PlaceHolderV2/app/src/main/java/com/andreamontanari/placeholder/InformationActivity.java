package com.andreamontanari.placeholder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andreamontanari.placeholder.utils.GPSTracker;
import com.andreamontanari.placeholder.utils.PlaceMisc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by andreamontanari on 13/07/16.
 */
public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

        setToolBar();
    }


    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }

    public void eraseAll(View view) {
        PlaceMisc.deleteAllPlaces(getApplicationContext());
    }

    public void visitFacebook(View v) {

        String facebookUrl = getResources().getString(R.string.facebook_page);
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));;
            } else {
                // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.facebook_codepage))));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }


    public void visitWebsite(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getResources().getString(R.string.facebook_page)));
        startActivity(intent);
    }

    public void contactDeveloper(View view) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "andrea.montanari@gmail.com"));
        i.putExtra(Intent.EXTRA_SUBJECT, R.string.mail_object);
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, "Mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InformationActivity.this,  R.string.mail_error, Toast.LENGTH_SHORT).show();
        }
    }
}
