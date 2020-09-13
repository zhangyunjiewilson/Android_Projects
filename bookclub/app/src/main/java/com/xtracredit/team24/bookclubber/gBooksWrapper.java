package com.xtracredit.team24.bookclubber;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.nio.file.Paths.get;

public class gBooksWrapper {

    JSONObject retrievedBook;

    public gBooksWrapper (String bookTitle) {
        try {
            String i = new GetBook().execute(bookTitle).get();
            try {
                retrievedBook = new JSONObject(i);
            } catch (JSONException jse) {
                jse.printStackTrace();
            }
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }


    public String fetchBookCoverImageURL() {
        try {
            JSONArray items = (JSONArray) retrievedBook.get("items");
            JSONObject allInfo = (JSONObject) items.get(0);
            JSONObject volumeInfo = (JSONObject) allInfo.get("volumeInfo");
            return ((JSONObject) volumeInfo.get("imageLinks")).get("thumbnail").toString();
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return "";
    }


    public String fetchBookTitle() {
        try {
            JSONArray items = (JSONArray) retrievedBook.get("items");
            JSONObject allInfo = (JSONObject) items.get(0);
            JSONObject volumeInfo = (JSONObject) allInfo.get("volumeInfo");
            return volumeInfo.get("title").toString();
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return "";
    }


    public List<String> fetchAuthors() {
        try {
            JSONArray items = (JSONArray) retrievedBook.get("items");
            JSONObject allInfo = (JSONObject) items.get(0);
            JSONObject volumeInfo = (JSONObject) allInfo.get("volumeInfo");
            JSONArray authors = (JSONArray) volumeInfo.get("authors");
            List<String> list = new ArrayList<String>();
            for (int i=0; i<authors.length(); i++) {
                list.add( authors.getString(i) );
            }
            return list;
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return null;
    }

    public List<String> fetchGenres() {
        try {
            JSONArray items = (JSONArray) retrievedBook.get("items");
            JSONObject allInfo = (JSONObject) items.get(0);
            JSONObject volumeInfo = (JSONObject) allInfo.get("volumeInfo");
            JSONArray authors = (JSONArray) volumeInfo.get("categories");
            List<String> list = new ArrayList<String>();
            for (int i=0; i<authors.length(); i++) {
                list.add( authors.getString(i) );
            }
            return list;
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return null;
    }
}
