package com.xtracredit.team24.bookclubber;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtracredit.team24.bookclubber.Club;
import com.xtracredit.team24.bookclubber.ClubAdapter;
import com.xtracredit.team24.bookclubber.R;
import com.xtracredit.team24.bookclubber.User;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<User> clubMembers;


    public MemberAdapter(Context context, ArrayList<User> members) {
        mContext = context;
        clubMembers = members;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_cell_layout , parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        User clubMember = clubMembers.get(i);
        ((MemberViewHolder) viewHolder).bind(clubMember);

    }

    @Override
    public int getItemCount() {
        return clubMembers.size();
    }


    class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        User member;
        TextView memberName;

        public MemberViewHolder(final View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.memberName_cell);
        }

        public void bind(User clubMember) {
            member = clubMember;
            memberName.setText(clubMember.username);
        }


        @Override
        public void onClick(View v) {
//            int selectedRow = getLayoutPosition();
//            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
//            intent.putExtra("memberId", clubMembers.get(selectedRow).id);
//            mContext.startActivity(intent);
        }

    }



}
