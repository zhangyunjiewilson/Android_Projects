package com.xtracredit.team24.bookclubber.create_club;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xtracredit.team24.bookclubber.Club;
import com.xtracredit.team24.bookclubber.HomeActivity;
import com.xtracredit.team24.bookclubber.R;
import com.xtracredit.team24.bookclubber.SwipeDetectListener;
import com.xtracredit.team24.bookclubber.gBooksWrapper;
import com.xtracredit.team24.bookclubber.onboarding.Onboarding1Activity;
import com.xtracredit.team24.bookclubber.onboarding.Onboarding2Activity;

import java.util.List;

public class CreateClub1Activity extends AppCompatActivity {

    EditText title;
    EditText author;
    EditText description;

    Button next;
    Button back;

    final Gson gson = new Gson();

    SwipeDetectListener swipeDetectListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);
        swipeDetectListener.setPrevIntent(new Intent(CreateClub1Activity.this, HomeActivity.class));

        title = findViewById(R.id.bookName_create_book_club1);
        author = findViewById(R.id.author_create_book_club1);
        description = findViewById(R.id.description_create_book_club1);

        next = findViewById(R.id.next_create_club1);
        back = findViewById(R.id.back_create_club1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Club newClub = new Club();
                Intent i = new Intent(CreateClub1Activity.this, CreateClub2Activity.class);

                newClub.bookTitle = title.getText().toString();
                newClub.author = author.getText().toString();
                newClub.description = description.getText().toString();
                gBooksWrapper book = new gBooksWrapper(newClub.bookTitle);

                newClub.bookGenres = book.fetchGenres();

                i.putExtra("club", gson.toJson(newClub, Club.class));

                swipeDetectListener.setNextIntent(i);
                swipeDetectListener.swipe(100, 0);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Are you sure you want to leave this page?")
                        .setMessage("Leaving this page will delete any changes you've made")
                        .setPositiveButton("Stay on this page", null)
                        .setNegativeButton("Leave this page", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                swipeDetectListener.swipe(0, 100);
                            }
                        }).show();
            }
        });
    }
}
