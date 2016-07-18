package com.andreamontanari.placeholder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by andreamontanari on 13/07/16.
 */
public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

        setToolBar();

        TextView linklabel = (TextView) findViewById(R.id.linkLabel);

        SpannableString mobile_underline = new SpannableString("placeholder.it");
        mobile_underline.setSpan(new UnderlineSpan(), 0, "placeholder.it".length(), 0);
        linklabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.web_url)));
                startActivity(intent);
            }
        }
        );
    }


    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void eraseAll(View view) {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogLayout = inflater.inflate(R.layout.dialog_place_deletion, null);
        ((TextView) dialogLayout.findViewById(R.id.deletion_body)).setText(getResources().getString(R.string.ask_erase_all));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

        Button confirm_btn = (Button) dialogLayout.findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceMisc.deleteAllPlaces(getApplicationContext());
                customAlertDialog.dismiss();
            }
        });
    }

    public void visitFacebook(View v) {

        String facebookUrl = getResources().getString(R.string.facebook_page);
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));;
            } else {
                // open the Facebook app using the old method (fb://profile/id or fb://page/id)mi
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

}
