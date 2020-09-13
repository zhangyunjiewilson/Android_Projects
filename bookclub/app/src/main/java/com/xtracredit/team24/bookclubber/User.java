package com.xtracredit.team24.bookclubber;

import java.util.ArrayList;

public class User {
    public String username, bio, id;
    public ArrayList<String> genres;
    public ArrayList<String> clubIds;
    public double latitude, longitude;
    public String phone_id;
    public String profileImagePath;

    public User() {}

    public User(String id, String username, String bio, ArrayList<String> genres, double latitude, double longitude, String phone_id) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.genres = genres;
        this.latitude = latitude;
        this.longitude = longitude;
        this.clubIds = new ArrayList<>();
        this.phone_id = phone_id;
    }
}
