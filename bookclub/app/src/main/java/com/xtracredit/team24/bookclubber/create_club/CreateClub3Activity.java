package com.xtracredit.team24.bookclubber.create_club;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.xtracredit.team24.bookclubber.MockServer;
import com.xtracredit.team24.bookclubber.R;

public class CreateClub3Activity extends AppCompatActivity {

    EditText memberLimit;
    MockServer mockServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club3);

        mockServer = MockServer.getInstance();
        Intent fromCreateClub2 = getIntent();




    }
}
