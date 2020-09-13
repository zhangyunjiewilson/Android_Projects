package com.xtracredit.team24.bookclubber;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class GetBook extends AsyncTask<String,Void,String> {


    public GetBook() {

    }

    @Override
    protected String doInBackground(String... strings) {

        if (isCancelled()) {
            return null;
        }


        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String jsonString = null;



        String searchedBookName = strings[0];

        try {
            String baseURL =  "https://www.googleapis.com/books/v1/volumes?";
            Uri uri = Uri.parse(baseURL).buildUpon()
                    .appendQueryParameter("q", searchedBookName)
                    .appendQueryParameter("maxResults", "1")
                    .appendQueryParameter("printType", "books")
                    .build();

            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() != 200) {
                    Log.i(TAG, "doInBackground: didn't get a code 200, something went wrong with connection");
                    connection.disconnect();
                    return null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            connection.disconnect();
            jsonString = builder.toString();
            return jsonString;
        } catch (IOException o) {
            o.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

    }
}
