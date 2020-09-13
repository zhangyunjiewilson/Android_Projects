package com.xtracredit.team24.bookclubber;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    Button exploreBtn, HomeBtn;
    MockServer mockServer;
    RecyclerView.Adapter mAdapter, profileClubsAdapter;
    RecyclerView userGenreRecyclerView, userClubsRecyclerView;
    User currentUser;
    TextView bookClubTitleProfile, userName;
    Button updateProfileImage;
    String imagePath;
    ImageView profileImage;
    TextView desciption;
    Bitmap mImageBitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mockServer = MockServer.getInstance();
        currentUser = mockServer.getCurrentUser();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        String selectedMember = intent.getStringExtra("memberId");

        bookClubTitleProfile = findViewById(R.id.book_club_title_profile);
        profileImage = findViewById(R.id.profile_image);
        desciption = findViewById(R.id.user_desciption);
        updateProfileImage = findViewById(R.id.updateProfilePicture);
        userName = findViewById(R.id.userName);


        if (!currentUser.profileImagePath.isEmpty()) {
            imagePath = currentUser.profileImagePath;
            setImage();
            profileImage.setClipToOutline(true);
        } else {
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile, null));
        }


        if (selectedMember != null) {
            currentUser = mockServer.allUsers.get(selectedMember);
        } else {
            currentUser = mockServer.currentUser;
        }

        desciption.setText(currentUser.bio);
        userName.setText(currentUser.username);

        setAdapterAndUpdateData();
        userGenreRecyclerView = findViewById(R.id.current_user_genre_recyclerview);
        userGenreRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        userGenreRecyclerView.setLayoutManager(linearLayoutManager);

        setClubsAdapterAndUpdateData();
        userClubsRecyclerView = findViewById(R.id.user_book_club);
        userClubsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager clubLinearLayoutManager = new LinearLayoutManager(this);
        clubLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        userClubsRecyclerView.setLayoutManager(clubLinearLayoutManager);



        bookClubTitleProfile.setText(currentUser.username + "'s Book Clubs");
        getSupportActionBar().setTitle(currentUser.username);
        exploreBtn = findViewById(R.id.explore);
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(ProfileActivity.this, ExploreClubsActivity.class);
                startActivity(toExploreActivity);
                ProfileActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        HomeBtn = findViewById(R.id.home);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExploreActivity = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(toExploreActivity);
                ProfileActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }

        });
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException ex) {
            System.out.println("something went wrong boi");
        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null && file != null) {
            Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this,  "com.example.android.hector.fileprovider", file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, 1);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = File.createTempFile(
                "JPEG_" + timeStamp + "_",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );

        imagePath = "file:"  + image.getAbsolutePath();
        return image;
    }



    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        userGenreRecyclerView = findViewById(R.id.current_user_genre_recyclerview);
        ArrayList<String> genres = mockServer.currentUser.genres;
        mAdapter = new GenreAdapter(this, genres);
        userGenreRecyclerView.setAdapter(mAdapter);


        // scroll to the last comment
        if (genres.size() == 0 )
            userGenreRecyclerView.smoothScrollToPosition(0);
        else
            userGenreRecyclerView.smoothScrollToPosition(genres.size() - 1);
    }

    private void setClubsAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        userClubsRecyclerView = findViewById(R.id.user_book_club);
        ArrayList<Club> clubs = mockServer.getCurrentUserClubs();
        profileClubsAdapter = new ProfileClubAdapter(this, clubs);
        userClubsRecyclerView.setAdapter(profileClubsAdapter);


        // scroll to the last comment
        if (clubs.size() == 0 )
            userClubsRecyclerView.smoothScrollToPosition(0);
        else
            userClubsRecyclerView.smoothScrollToPosition(clubs.size() - 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setImage();
        mockServer.updateProfilePic(imagePath);
    }

    protected void setImage() {
        try {
            mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imagePath));
            profileImage.setImageBitmap(mImageBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
