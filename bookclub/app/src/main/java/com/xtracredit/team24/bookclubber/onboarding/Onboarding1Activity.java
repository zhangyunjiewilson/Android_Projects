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

import java.util.List;

public class Onboarding1Activity extends AppCompatActivity{

    SwipeDetectListener swipeDetectListener;
    float x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);
        swipeDetectListener.setNextIntent(new Intent(Onboarding1Activity.this, Onboarding2Activity.class));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        LinearLayout background = (LinearLayout) findViewById(R.id.linearLayout);

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("hm", "check it out");
                        swipeDetectListener.swipe(x1, event.getX());
                        break;
                }
                return true;
            }
        });

        //FIXME remove after finishing proj
//        Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
//        startActivity(intent);
        
    }
}
