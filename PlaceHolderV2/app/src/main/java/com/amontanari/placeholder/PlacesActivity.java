package com.amontanari.placeholder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.amontanari.placeholder.utils.PlaceMisc;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, message + " #placeholder");
                tweetIntent.setType("text/plain");

                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for(ResolveInfo resolveInfo: resolvedInfoList){
                    if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                        tweetIntent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name );
                        resolved = true;
                        break;
                    }
                }
                if(resolved){
                    startActivity(tweetIntent);
                }else{
                    Intent i = new Intent();
                    i.putExtra(Intent.EXTRA_TEXT, message + " #placeholder");
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/intent/tweet?text="+urlEncode(message + " #placeholder")));
                    startActivity(i);
                    Toast.makeText(this, getResources().getString(R.string.twitter_not_installed), Toast.LENGTH_LONG).show();
                }

                break;

        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            Log.wtf("ERROR", "UTF-8 should always be supported", e);
            return "";
        }
    }
}
