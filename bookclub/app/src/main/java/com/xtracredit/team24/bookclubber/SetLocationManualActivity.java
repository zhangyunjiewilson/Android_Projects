package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class SetLocationManualActivity extends AppCompatActivity {

    String username;
    String bio;
    EditText addressField;
    double latitude;
    double longitude;
    SwipeDetectListener swipeDetectListener;

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_manual);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        bio = intent.getStringExtra("bio");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button backButtton = (Button) findViewById(R.id.set_loc_back_manual);
        Button nextButton = (Button) findViewById(R.id.set_loc_next_manual);
        addressField = (EditText) findViewById(R.id.address_manual);

        Places.initialize(getApplicationContext(), "AIzaSyDDutQa82ie2cf0z05HkwOkogMhPNY-0lI");
        Places.createClient(this);

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);

        final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        addressField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(SetLocationManualActivity.this,SelectGenreActivity.class);

                if (addressField.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
                } else {
                    nextIntent.putExtra("latitude", latitude);
                    nextIntent.putExtra("longitude", longitude);
                    nextIntent.putExtra("username", username);
                    nextIntent.putExtra("userbio", bio);
                    swipeDetectListener.setNextIntent(nextIntent);
                    swipeDetectListener.swipe(100, 0);
                }
            }
        });

        backButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent = new Intent(SetLocationManualActivity.this, SetLocationActivity.class);
                swipeDetectListener.setPrevIntent(prevIntent);
                swipeDetectListener.swipe(0, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                addressField.setText(place.getAddress());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("LOCATION ERROR", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


}
