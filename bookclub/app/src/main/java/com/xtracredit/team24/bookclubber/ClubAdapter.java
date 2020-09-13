package com.xtracredit.team24.bookclubber;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ClubAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<Club> clubs;
    private MockServer mockServer = MockServer.getInstance();

    public ClubAdapter(Context context, ArrayList<Club> clubs) {
        this.mContext = context;
        this.clubs = clubs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.club_cell_layout, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Club club = clubs.get(position);
        ((ClubViewHolder) holder).bind(club);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    class ClubViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mClubItemLayout;
        public ImageView mClubBookImage;
        public TextView mClubBookName;
        public TextView mClubTime;
        public TextView mNumPeople;
        public TextView mAuthor;
        private TextView clubId;
        private TextView address;
        public Button joinBtn;
        private TextView learnMoreTextView;
        private Club club;
        private User currentUser;
        MockServer mockServer;


        public ClubViewHolder(final View itemView) {
            super(itemView);
            mockServer = MockServer.getInstance();
            currentUser = mockServer.getCurrentUser();
            mClubItemLayout = itemView.findViewById(R.id.explore_club_cell_layout);
            mClubBookImage = itemView.findViewById(R.id.book_image);
            mClubBookName = itemView.findViewById(R.id.book_title_text);
            mClubTime = itemView.findViewById(R.id.meeting_time_text);
            mNumPeople = itemView.findViewById(R.id.people);
            mAuthor = itemView.findViewById(R.id.book_author_text);
            address = itemView.findViewById(R.id.address_text);
            joinBtn = itemView.findViewById(R.id.join_button);
            learnMoreTextView = itemView.findViewById(R.id.learn_more_button);
            clubId = itemView.findViewById(R.id.hiddenBookClubIdField);
            mockServer = MockServer.getInstance();

            learnMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ClubDetailActivity.class);
                    intent.putExtra("clubId", clubId.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });

            mClubItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    learnMoreTextView.callOnClick();
                }
            });

        }

        void bind(Club club) {
            this.club = club;
            mClubBookName.setText(club.bookTitle);
            mClubTime.setText(club.meetingDate);
            address.setText(club.address);
            clubId.setText(club.id);
            mAuthor.setText(club.author);

            mNumPeople.setText(String.valueOf(club.userIds.size()) + " people");

            gBooksWrapper book = new gBooksWrapper(club.bookTitle);
            Uri uri =  Uri.parse(book.fetchBookCoverImageURL());
            GlideApp.with(itemView).load(uri).into(mClubBookImage);

            setJoinBtn();
            setLocation();

        }


        private void setJoinBtn() {
            if (currentUser.clubIds.contains(club.id)) {
                joinBtn.setText("Joined");
                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mockServer.removeCurrentUserFromClub(club);
                        joinBtn.setText("Join");
                        Toast.makeText(itemView.getContext(), "You have been removed from this club.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joinBtn.setText("Joined");
                        mockServer.addCurrentUserToClub(club);
                    }
                });

            }
        }

        private void setLocation() {
            if (currentUser.clubIds.contains(club.id)) {
                address.setText(club.address);
            } else {

                Location userLocation = new Location("");
                userLocation.setLatitude(currentUser.latitude);
                userLocation.setLongitude(currentUser.longitude);

                Location clubLocation = club.location;

                System.out.println(currentUser.latitude);
                System.out.println(currentUser.longitude);


                System.out.println(clubLocation.getLatitude());
                System.out.println(clubLocation.getLongitude());

                System.out.println(userLocation.distanceTo(clubLocation));


                Double distnaceAway  = ConvertMetersToMiles(userLocation.distanceTo(clubLocation));
                String distanceDisplayed = String.format("%.1f miles away", distnaceAway);
                address.setText(distanceDisplayed);
            }
        }

        public double ConvertMetersToMiles(double meters) {
            return (meters / 1609.344);
        }


    }







}
