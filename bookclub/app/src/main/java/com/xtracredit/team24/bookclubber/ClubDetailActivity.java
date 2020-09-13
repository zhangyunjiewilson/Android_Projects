package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;

public class ClubDetailActivity extends AppCompatActivity {

    MockServer mockServer = MockServer.getInstance();
    RecyclerView clubMembers_recyclerview;
    private RecyclerView.Adapter mAdapter;
    private  ArrayList<User> clubMembers;
    Button joinBtn;
    User currentUser;
    String selectedClubId;
    Club selectedClub;
    ImageView bookClubImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);

        currentUser = mockServer.getCurrentUser();

        Intent intent = getIntent();
        selectedClubId = intent.getStringExtra("clubId");
        selectedClub = null;


        ArrayList<Club> allClubs = new ArrayList<>(mockServer.allClubs.values());
        for (Club currClub : allClubs) {
            if (currClub.id.equals(selectedClubId)) {
                selectedClub = currClub;
                clubMembers = mockServer.getClubMembers(selectedClub);
                break;
            }
        }

        TextView clubName = findViewById(R.id.club_detail_book_name);
        TextView clubTime = findViewById(R.id.club_detail_time);
        TextView clubLocation = findViewById(R.id.club_detail_location);
        TextView clubDesciption = findViewById(R.id.club_detail_description);
        TextView people = findViewById(R.id.people_detailspage);
        TextView author = findViewById(R.id.book_author_detailpage);

        gBooksWrapper book = new gBooksWrapper(selectedClub.bookTitle);
        bookClubImage = findViewById(R.id.club_detail_image);

        Uri uri =  Uri.parse(book.fetchBookCoverImageURL());
        GlideApp.with(ClubDetailActivity.this).load(uri).into(bookClubImage);

        joinBtn = findViewById(R.id.detail_join_button);
        clubMembers_recyclerview = findViewById(R.id.member_recycleView_clubDetail);


        clubName.setText(selectedClub.bookTitle);
        clubTime.setText(selectedClub.meetingDate);
        author.setText(selectedClub.author);
        people.setText(String.valueOf(selectedClub.userIds.size()) + " people");


        if (currentUser.clubIds.contains(selectedClubId)) {
            clubLocation.setText(selectedClub.address);
        } else {
            Location userLocation = new Location("");
            userLocation.setLatitude(currentUser.latitude);
            userLocation.setLongitude(currentUser.longitude);

            Location clubLocationObject = selectedClub.location;



            Double distnaceAway  = ConvertMetersToMiles(userLocation.distanceTo(clubLocationObject));
            String distanceDisplayed = String.format("%.1f miles away", distnaceAway);
            clubLocation.setText(distanceDisplayed);
        }


        clubDesciption.setText(selectedClub.description);


        setAdapterAndUpdateData();
        clubMembers_recyclerview.setHasFixedSize(true);
        clubMembers_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        setJoinBtn();

    }

    public static double ConvertMetersToMiles(double meters) {
        return (meters / 1609.344);
    }

    private void setJoinBtn() {
        if (currentUser.clubIds.contains(selectedClub.id)) {
            joinBtn.setText("Joined");
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinBtn.setText("Join");
                    mockServer.removeCurrentUserFromClub(selectedClub);
                    Toast.makeText(getApplicationContext(), "You have been removed from this club.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinBtn.setText("Joined");
                    mockServer.addCurrentUserToClub(selectedClub);
                    Toast.makeText(getApplicationContext(), "You have joined this club.", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new MemberAdapter(this, clubMembers);
        clubMembers_recyclerview.setAdapter(mAdapter);


        // scroll to the last comment
        if (clubMembers.size() == 0 )
            clubMembers_recyclerview.smoothScrollToPosition(0);
        else
            clubMembers_recyclerview.smoothScrollToPosition(clubMembers.size() - 1);
        }
}
