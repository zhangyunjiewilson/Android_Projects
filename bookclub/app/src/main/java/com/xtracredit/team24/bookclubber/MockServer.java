package com.xtracredit.team24.bookclubber;

import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class MockServer {

    public User currentUser;
    public HashMap<String, User> allUsers;
    public HashMap<String, Club> allClubs;
    public ArrayList<User> otherUsers;
    public FirebaseDatabase database;
    public DatabaseReference currentUserReference,usersReference, clubsReference;

    public static final MockServer ourInstance = new MockServer();

    public static MockServer getInstance() {
        return ourInstance;
    }

    public MockServer() {
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");
        clubsReference = database.getReference("Clubs");

        allUsers = new HashMap<>();
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    User user = userSnapShot.getValue(User.class);
                    if (!userSnapShot.hasChild("clubIds")) {
                        user.clubIds = new ArrayList<>();
                    }
                    allUsers.put(user.id, user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        allClubs = new HashMap<>();
        clubsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot clubSnapShot : dataSnapshot.getChildren()) {
                    String address = clubSnapShot.child("address").getValue(String.class);
                    String author = clubSnapShot.child("author").getValue(String.class);
                    String bookTitle = clubSnapShot.child("bookTitle").getValue(String.class);
                    String description = clubSnapShot.child("description").getValue(String.class);
                    String endTime = clubSnapShot.child("endTime").getValue(String.class);
                    String meetingDate = clubSnapShot.child("meetingDate").getValue(String.class);
                    String startTime = clubSnapShot.child("startTime").getValue(String.class);
                    String id = clubSnapShot.child("id").getValue(String.class);
                    ArrayList<String> bookGenres = (ArrayList<String>)clubSnapShot.child("bookGenres").getValue();
                    ArrayList<String> userIds = (ArrayList<String>) clubSnapShot.child("userIds").getValue();
                    Double longitude = clubSnapShot.child("location").child("longitude").getValue(Double.class);
                    Double latitude = clubSnapShot.child("location").child("latitude").getValue(Double.class);

                    Location location = new Location("");
                    location.setLongitude(longitude);
                    location.setLatitude(latitude);

                    Club club = new Club();
                    club.id = id;
                    club.author = author;
                    club.bookTitle = bookTitle;
                    club.meetingDate = meetingDate;
                    club.address = address;
                    club.location = location;
                    club.userIds = userIds;
                    club.description = description;
                    club.startTime = startTime;
                    club.endTime = endTime;
                    club.bookGenres = bookGenres;

                    allClubs.put(id, club);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public String createClub(Club club) {
        String ID = UUID.randomUUID().toString();
        club.id = ID;
        club.userIds = new ArrayList<>();
        club.userIds.add(currentUser.id);
        currentUser.clubIds.add(club.id);
        allClubs.put(club.id, club);
        usersReference.child(currentUser.id).setValue(currentUser);
        clubsReference.child(club.id).setValue(club);

        return ID;
    }

    public ArrayList<Club> getCurrentUserClubs() {
        ArrayList<Club> clubs = new ArrayList<>();
        ArrayList<String> clubIds = currentUser.clubIds;
        for (String clubId : clubIds) {
            Club club = allClubs.get(clubId);
            clubs.add(club);
        }

        return clubs;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createClubs() {
        for (int i = 0; i < 2; i += 1) {
            String id = UUID.randomUUID().toString();
            Location clubLocation = new Location("locationA");
            String bookTitle = "";
            if (i == 0) {
                clubLocation.setLatitude(currentUser.latitude - 1.0);
                clubLocation.setLongitude(currentUser.longitude - 1.001);
                bookTitle = "Bossypants";

            } else {
                clubLocation.setLatitude(currentUser.latitude - 1.0);
                clubLocation.setLongitude(currentUser.longitude - 2.001);
                bookTitle = "The Story of My Life";
            }
            Club newClub = new Club(id, bookTitle, "A Random Author XXX", "Soda hall 287", "June 1st", clubLocation, "this is the book club desciption", "3:00PM", "4:00PM");
            newClub.bookGenres.add("Biography & Autobiography");
            for (User user : otherUsers) {
                newClub.userIds.add(user.id);
                user.clubIds.add(newClub.id);
            }


            allClubs.put(id, newClub);

        }
    }

    public void createOtherUsers() {
        otherUsers = new ArrayList<>();
        for (int i = 0; i < 2; i += 1) {
            String id = UUID.randomUUID().toString();
            User user = new User();
            user.id = id;
            user.username = "Test user " + Integer.toString(i);
            user.longitude = currentUser.longitude;
            user.latitude = currentUser.latitude;
            user.genres = currentUser.genres;
            user.bio = "test users blah blah blah";
            user.phone_id = "phone_id - " + id;
            user.clubIds = new ArrayList<>();
            otherUsers.add(user);
            allUsers.put(id, user);
        }


    }

    public void updateOtherUsersClubs() {
        for (User user : otherUsers) {
            DatabaseReference newUserReference = usersReference.child(user.id);
            newUserReference.setValue(user);
//            newUserReference.child("id").setValue(user.id);
//            newUserReference.child("name").setValue(user.username);
//            newUserReference.child("clubs").setValue(user.clubIds);
//            newUserReference.child("genres").setValue(user.genres);
//            newUserReference.child("bio").setValue(user.bio);
//            newUserReference.child("latitude").setValue(user.latitude);
//            newUserReference.child("longitude").setValue(user.longitude);
        }

        for (String clubId : allClubs.keySet()) {
            Club club = allClubs.get(clubId);
            DatabaseReference newClubReference = clubsReference.child(club.id);
            newClubReference.setValue(club);
//            newClubReference.child("address").setValue(club.address);
//            newClubReference.child("meetingDate").setValue(club.meetingDate);
//            newClubReference.child("description").setValue(club.description);
//            newClubReference.child("author").setValue(club.author);
//            newClubReference.child("userIds").setValue(club.userIds);
//            newClubReference.child("location").setValue(club.location);
//            newClubReference.child("startTime").setValue(club.startTime);
//            newClubReference.child("endTime").setValue(club.endTime);
        }
    }

    public ArrayList<User> getClubMembers(Club club) {
        ArrayList<User> members = new ArrayList<>();
        for (String userId : club.userIds) {
            User user = allUsers.get(userId);
            members.add(user);
        }
        return members;
    }

    public void removeCurrentUserFromClub(Club club) {
        currentUserReference = database.getReference("Users").child(currentUser.id);
        currentUser.clubIds.remove(club.id);
        club.userIds.remove(currentUser.id);
        currentUserReference.child("clubIds").setValue(currentUser.clubIds);
        DatabaseReference selectedClubReference = clubsReference.child(club.id);
        selectedClubReference.child("userIds").setValue(club.userIds);

    }

    public void addCurrentUserToClub(Club club) {
        currentUserReference = database.getReference("Users").child(currentUser.id);
        currentUser.clubIds.add(club.id);
        club.userIds.add(currentUser.id);
        currentUserReference.child("clubIds").setValue(currentUser.clubIds);
        DatabaseReference selectedClubReference = clubsReference.child(club.id);
        selectedClubReference.child("userIds").setValue(club.userIds);
    }

    public void updateProfilePic(String newImagePath) {
        currentUser.profileImagePath = newImagePath;
        usersReference.child(currentUser.id).setValue(currentUser);
    }

}
