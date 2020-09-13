package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class SelectGenreActivity extends AppCompatActivity {

    ArrayList<String> genres;
    String username, bio;
    Double latitude, longitude;
    Button biographyBtn, dramaBtn, fantasyBtn, historyBtn, horrorBtn, mysteryBtn, nonFictionBtn, sciFiBtn, nextBtn, backBtn;
    FirebaseDatabase database;
    DatabaseReference usersReference, clubsReference;
    SwipeDetectListener swipeDetectListener;
    private String android_id;
    MockServer mockServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_genre);

        Intent intent = getIntent();
        extractExtras(intent);
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");
        clubsReference = database.getReference("Clubs");
        mockServer = MockServer.ourInstance;
        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        genres = new ArrayList<>();

        assignGenreButtons(); // Get corresponding buttons from UI components.

        backBtn = findViewById(R.id.select_genre_back);
        nextBtn = findViewById(R.id.select_genre_next);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        swipeDetectListener = new SwipeDetectListener();
        swipeDetectListener.setCurrActivity(this);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genres.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Please select at least one genre that you like.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent nextIntent = new Intent(SelectGenreActivity.this, HomeActivity.class);
                    String userId = UUID.randomUUID().toString();
                    User newUser = new User();
                    newUser.id = userId;
                    newUser.username = username;
                    newUser.bio = bio;
                    newUser.genres = genres;
                    newUser.latitude = latitude;
                    newUser.longitude = longitude;
                    newUser.clubIds = new ArrayList<>();
                    newUser.phone_id = android_id;
                    newUser.profileImagePath = "";
                    mockServer.currentUser = newUser;
                    mockServer.allUsers.put(newUser.id, newUser);


                    mockServer.createOtherUsers();
                    mockServer.createClubs();
                    mockServer.updateOtherUsersClubs();


                    DatabaseReference newUserReference = usersReference.child(userId);
                    newUserReference.setValue(newUser);
//                    newUserReference.child("id").setValue(userId);
//                    newUserReference.child("name").setValue(username);
//                    newUserReference.child("clubs").setValue(clubIds);
//                    newUserReference.child("genres").setValue(genres);
//                    newUserReference.child("bio").setValue(bio);
//                    newUserReference.child("latitude").setValue(latitude);
//                    newUserReference.child("longitude").setValue(longitude);

                    startActivity(nextIntent);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SelectGenreActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    private void assignGenreButtons() {
        biographyBtn = findViewById(R.id.biography);
        dramaBtn = findViewById(R.id.drama);
        fantasyBtn = findViewById(R.id.fantasy);
        historyBtn = findViewById(R.id.history);
        horrorBtn = findViewById(R.id.horror);
        mysteryBtn = findViewById(R.id.mystery);
        nonFictionBtn = findViewById(R.id.nonfiction);
        sciFiBtn = findViewById(R.id.scifi);

        biographyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biographyBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.biography_selected));
                genres.add("Biography & Autobiography");
            }
        });

        dramaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dramaBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.drama_selected));
                genres.add("Drama");
            }
        });

        fantasyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fantasyBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fantasy_selected));
                genres.add("Fantasy");
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.history_selected));
                genres.add("History");
            }
        });

        horrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horrorBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horror_selected));
                genres.add("Horror");
            }
        });

        mysteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysteryBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.mystery_selected));
                genres.add("Mystery");
            }
        });

        nonFictionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonFictionBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonfiction_selected));
                genres.add("Nonfiction");
            }
        });

        sciFiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sciFiBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sci_fi_selected));
                genres.add("Sci-Fi");
            }
        });

    }

    private void extractExtras(Intent intent) {
        username = intent.getStringExtra("username");
        bio = intent.getStringExtra("userbio");
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
    }



}
