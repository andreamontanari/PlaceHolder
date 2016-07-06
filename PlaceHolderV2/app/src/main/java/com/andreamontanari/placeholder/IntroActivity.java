package com.andreamontanari.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by andreamontanari on 27/06/16.
 */
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        setToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void watchPlaces(View view) {
        Intent intent = new Intent(IntroActivity.this, PlacesActivity.class);
        startActivity(intent);
    }

    public void savePlace(View view) {
        Intent intent = new Intent(IntroActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }
}
