package com.andreamontanari.placeholder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import com.andreamontanari.placeholder.utils.PlaceMisc;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by andreamontanari on 07/07/16.
 */
public class PlacesActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places);

        setToolBar();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // Create a new Fragment to be placed in the activity layout
        PlacesOnListFragment firstFragment = new PlacesOnListFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.places_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, IntroActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            case R.id.list_view:
                /*Fragment currentFragment = getFragmentManager().findFragmentByTag("FRAGMENT");
                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment); */
                // Create a new Fragment to be placed in the activity layout
                PlacesOnListFragment listFragment = new PlacesOnListFragment();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                listFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, listFragment).commit();
                return true;
            case R.id.map_view:
                // Create a new Fragment to be placed in the activity layout
                PlacesOnMapFragment mapFragment = new PlacesOnMapFragment();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                mapFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mapFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void sharePost(View view) {
        View dialog = (View) view.getRootView();
        String latitude = ((TextView) dialog.findViewById(R.id.lat)).getText().toString();
        String longitude = ((TextView) dialog.findViewById(R.id.lng)).getText().toString();
        String street = ((TextView) dialog.findViewById(R.id.streetName)).getText().toString();
        String comment = ((TextView) dialog.findViewById(R.id.comment)).getText().toString();
        String message = comment + " - " + street + " (" + latitude + ", " + longitude + ")";
        switch (view.getId()) {
            case R.id.fbChk:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Placeholder")
                            .setQuote(message)
                            .setContentDescription(message)
                            .setContentUrl(Uri.parse(getString(R.string.web_url)))
                            .build();

                    shareDialog.show(linkContent);
                }
                break;
            case R.id.twitterChk:
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
