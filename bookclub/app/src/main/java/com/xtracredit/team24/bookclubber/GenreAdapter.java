package com.xtracredit.team24.bookclubber;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> genres;

    public GenreAdapter(Context context, ArrayList<String> genres) {
        mContext = context;
        this.genres = genres;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.genre_cell_layout , parent, false);
        return new GenreAdapter.GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String genre = genres.get(i);
        ((GenreViewHolder) viewHolder).bind(genre);

    }

    @Override
    public int getItemCount() {
        return genres.size();
    }


    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView genre_name;


        public GenreViewHolder(final View itemView) {
            super(itemView);
            genre_name = itemView.findViewById(R.id.genre_name_cell);
        }

        public void bind(String genre) {
            genre_name = itemView.findViewById(R.id.genre_name_cell);
            genre_name.setText(genre);
        }

    }



}