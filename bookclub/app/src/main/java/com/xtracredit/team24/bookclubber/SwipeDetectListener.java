package com.xtracredit.team24.bookclubber;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeDetectListener {

    private Activity currActivity;

    private Intent nextIntent = null;
    private Intent prevIntent = null;

    public void setCurrActivity(AppCompatActivity activity) {
        this.currActivity = activity;
    }

    public void setNextIntent(Intent intent) {
        this.nextIntent = intent;
    }

    public void setPrevIntent(Intent intent) {
        this.prevIntent = intent;
    }

    public void swipe(float x1, float x2) {
        float deltaX = x1 - x2;
        if (deltaX > 0) {
            if (nextIntent != null) {
                currActivity.startActivity(nextIntent);
                currActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        } else {
            if (prevIntent != null) {
                currActivity.startActivity(prevIntent);
                currActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }
    }


}
