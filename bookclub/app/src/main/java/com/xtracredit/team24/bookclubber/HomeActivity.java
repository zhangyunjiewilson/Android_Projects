package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xtracredit.team24.bookclubber.create_club.CreateClub1Activity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    Button exploreBtn, profileBtn;
    FloatingActionButton createClubBtn;
    User currentUser;
    String currentUserId;
    ArrayList<Club> myClubs;
    ArrayList<String> myClubIds;
    MockServer mockServer;
    private RecyclerView.Adapter mAdapter;
    RecyclerView clubs_recyclerview;
    FirebaseDatabase database;
    DatabaseReference usersRefernce, currentUserReference, clubsReference;
    ArrayList<String> currentUserClubIds;
    HashMap<String, Object> currentUserHashMap, usersHashMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Book Clubs");
        actionBar.hide();

        mockServer = MockServer.getInstance();
        currentUser = mockServer.getCurrentUser();
        myClubs = mockServer.getCurrentUserClubs();


        clubs_recyclerview = findViewById(R.id.my_clubs_recycler_view);

        setAdapterAndUpdateData();
        clubs_recyclerview.setHasFixedSize(true);
        clubs_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        exploreBtn = findViewById(R.id.explore);
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(HomeActivity.this, ExploreClubsActivity.class);
                startActivity(toExploreActivity);
                HomeActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        profileBtn =  findViewById(R.id.profile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(toExploreActivity);
                HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        createClubBtn = findViewById(R.id.newButton);
        createClubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCreateClub = new Intent(HomeActivity.this, CreateClub1Activity.class);
                startActivity(toCreateClub);

            }
        });



    }
    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new ClubAdapter(this, myClubs);
        clubs_recyclerview.setAdapter(mAdapter);

        // scroll to the last comment
        if (myClubs.size() == 0 )
            clubs_recyclerview.smoothScrollToPosition(0);
        else
            clubs_recyclerview.smoothScrollToPosition(myClubs.size() - 1);
    }


}
