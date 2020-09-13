package com.xtracredit.team24.bookclubber;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetLocationActivity extends AppCompatActivity {

    private double latitude, longitude;
    private String username, bio;
    private  LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final SwipeDetectListener swipeDetectListener;
        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);

        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        bio = intent.getStringExtra("userbio");
        Button nextBtn = findViewById(R.id.set_loc_next);
        Button backBtn = findViewById(R.id.set_loc_back);
        final RadioButton radioButton = findViewById(R.id.option_use_cur_loc);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        updateCurrentLocation();

        // If next button is clicked on, we gather information of current location, user name and bio, and proceed to next interface.
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.isChecked()) {
                    updateCurrentLocation();
                    Intent nextIntent = new Intent(SetLocationActivity.this, SelectGenreActivity.class);
                    nextIntent.putExtra("latitude", latitude);
                    nextIntent.putExtra("longitude", longitude);
                    nextIntent.putExtra("username", username);
                    nextIntent.putExtra("userbio", bio);
                    swipeDetectListener.setNextIntent(nextIntent);
                    swipeDetectListener.swipe(100, 0);
                } else {
                    Intent intent = new Intent(SetLocationActivity.this, SetLocationManualActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("userbio", bio);
                    swipeDetectListener.setNextIntent(intent);
                    swipeDetectListener.swipe(100, 0);
                }

            }

        });

        // If back button is clicked on. The user is done on this interface and head back to previous interface.
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeDetectListener.setPrevIntent(new Intent(SetLocationActivity.this, CreateProfileActivity.class));
                swipeDetectListener.swipe(0, 100);
            }
        });

    }

    private void updateCurrentLocation() {
        mLocationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            ActivityCompat.requestPermissions(this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  }, 1);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (SecurityException e) {
            Log.e("SetLocationActivity ###: ", "No permission for location manager.");
        }
    }
}
