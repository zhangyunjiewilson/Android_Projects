package com.xtracredit.team24.bookclubber.onboarding;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.xtracredit.team24.bookclubber.R;
import com.xtracredit.team24.bookclubber.SwipeDetectListener;

public class Onboarding2Activity extends AppCompatActivity {

    SwipeDetectListener swipeDetectListener;
    float x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);
        swipeDetectListener.setNextIntent(new Intent(Onboarding2Activity.this, Onboarding3Activity.class));
        swipeDetectListener.setPrevIntent(new Intent(Onboarding2Activity.this, Onboarding1Activity.class));

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
