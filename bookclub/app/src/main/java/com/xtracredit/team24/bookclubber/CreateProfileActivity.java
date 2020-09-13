 package com.xtracredit.team24.bookclubber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xtracredit.team24.bookclubber.onboarding.Onboarding1Activity;
import com.xtracredit.team24.bookclubber.onboarding.Onboarding2Activity;

 public class CreateProfileActivity extends AppCompatActivity {

     SwipeDetectListener swipeDetectListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button nextButton = (Button)  findViewById(R.id.create_profile_next);
        final EditText profileNameField = (EditText)findViewById(R.id.create_profile_name);
        final EditText bioField = (EditText) findViewById(R.id.create_profile_bio);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = profileNameField.getText().toString();
                String bio = bioField.getText().toString();
                if (name.isEmpty() || bio.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled in", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CreateProfileActivity.this, SetLocationActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("userbio", bio);
                    swipeDetectListener.setNextIntent(intent);
                    swipeDetectListener.swipe(100, 0);
                }
            }
        });



    }
}
