package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExploreClubsActivity extends AppCompatActivity {

    private RecyclerView clubs_recyclerview;
    private Button homeBtn, profileBtn;
    private MockServer mockServer;
    private ArrayList<Club> allClubs;
    private RecyclerView.Adapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_clubs);

        homeBtn = findViewById(R.id.home);
        profileBtn = findViewById(R.id.profile);
        clubs_recyclerview = findViewById(R.id.explore_clubs_recyclerview);


        mockServer = MockServer.getInstance();

        allClubs = new ArrayList<>(mockServer.allClubs.values());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



//
        allClubs = new ArrayList<>();
        ArrayList<String> genre_prefrence = mockServer.currentUser.genres;
        System.out.println(genre_prefrence);

        System.out.println("----");
        System.out.println(mockServer.currentUser.latitude);
        System.out.println(mockServer.currentUser.longitude);
        System.out.println("----");


        for (Club bookCLub : mockServer.allClubs.values() ) {
            System.out.println(bookCLub.bookTitle);
            bookCLub.location.getLatitude();
            System.out.println(bookCLub.location.getLongitude());
            if (!Collections.disjoint(genre_prefrence, bookCLub.bookGenres)) {
                allClubs.add(bookCLub);

            }
        }



        allClubs.sort(new Comparator<Club>() {
            @Override
            public int compare(Club o1, Club o2) {
                double lat = mockServer.currentUser.latitude;
                double log = mockServer.currentUser.longitude;

                Location userLocation = new Location("");
                userLocation.setLatitude(lat);
                userLocation.setLongitude(log);

                return Math.round(o1.location.distanceTo(userLocation) - o2.location.distanceTo(userLocation));
            }
        });

        setAdapterAndUpdateData();
        clubs_recyclerview.setHasFixedSize(true);
        clubs_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(ExploreClubsActivity.this, HomeActivity.class);
                startActivity(toExploreActivity);
                ExploreClubsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(ExploreClubsActivity.this, ProfileActivity.class);
                startActivity(toExploreActivity);
                ExploreClubsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }


    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new ClubAdapter(this, allClubs);
        clubs_recyclerview.setAdapter(mAdapter);

        // scroll to the last comment
        if (allClubs.size() == 0 )
            clubs_recyclerview.smoothScrollToPosition(0);
        else
            clubs_recyclerview.smoothScrollToPosition(allClubs.size() - 1);
    }
}
