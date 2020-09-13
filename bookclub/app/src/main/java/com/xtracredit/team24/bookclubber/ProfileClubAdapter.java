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
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileClubAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<Club> clubs;

    public ProfileClubAdapter(Context context, ArrayList<Club> clubs) {
        this.mContext = context;
        this.clubs = clubs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.club_cell_small_layout, parent, false);
        return new ProfileClubAdapter.ProfileClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Club club = clubs.get(position);
        ((ProfileClubAdapter.ProfileClubViewHolder) holder).bind(club);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    class ProfileClubViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mClubItemLayout;
        public ImageView mClubBookImage;


        MockServer mockServer;


        public ProfileClubViewHolder(final View itemView) {
            super(itemView);
            mClubItemLayout = itemView.findViewById(R.id.explore_club_cell_layout);
            mClubBookImage = itemView.findViewById(R.id.profile_book_image);
            mockServer = MockServer.getInstance();

        }

        void bind(Club club) {
            gBooksWrapper book = new gBooksWrapper(club.bookTitle);
            Uri uri =  Uri.parse(book.fetchBookCoverImageURL());
            GlideApp.with(itemView).load(uri).into(mClubBookImage);

        }
    }
}

