package com.xtracredit.team24.bookclubber.onboarding;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xtracredit.team24.bookclubber.CreateProfileActivity;
import com.xtracredit.team24.bookclubber.HomeActivity;
import com.xtracredit.team24.bookclubber.MockServer;
import com.xtracredit.team24.bookclubber.R;
import com.xtracredit.team24.bookclubber.SwipeDetectListener;
import com.xtracredit.team24.bookclubber.User;

public class Onboarding3Activity extends AppCompatActivity {

    SwipeDetectListener swipeDetectListener;
    float x1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersReference;
    private String android_id;
    MockServer mockServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding3);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        mockServer = MockServer.getInstance();

        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        Button startBtn = findViewById(R.id.get_started_onboarding3);
        startBtn.setOnClickListener(new View.OnClickListener() {
            Boolean isReturnedUser = false;
           @Override
           public void onClick(View v) {
                if (usersReference != null) {
                    for (User user : mockServer.allUsers.values()) {
                        if (user.phone_id.equals(android_id)) {
                            mockServer.currentUser = user;
                            isReturnedUser = true;
                            break;
                        }
                    }
                }

                if (isReturnedUser) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
                    startActivity(intent);
                }
           }
        });

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);
        swipeDetectListener.setPrevIntent(new Intent(Onboarding3Activity.this, Onboarding2Activity.class));

        LinearLayout background = (LinearLayout) findViewById(R.id.linearLayout);

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        swipeDetectListener.swipe(x1, event.getX());
                        break;
                }
                return true;
            }
        });


    }
}
