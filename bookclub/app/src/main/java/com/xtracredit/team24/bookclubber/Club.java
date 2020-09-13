package com.xtracredit.team24.bookclubber;

import android.location.Location;
import android.location.LocationManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Club {
    public String id, bookTitle, address, meetingDate, description, author, startTime, endTime;
    public ArrayList<String> userIds;
    public List<String> bookGenres;
    public Location location;

    public Club() {
    }

    public Club(String id, String bookTitle, String author, String address, String meetingDate, Location location, String description, String startTime, String endTime) {
        this.id = id;
        this.author = author;
        this.bookTitle = bookTitle;
        this.meetingDate = meetingDate;
        this.address = address;
        this.location = location;
        this.userIds = new ArrayList<>();
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookGenres =  new ArrayList<>();
    }


}
