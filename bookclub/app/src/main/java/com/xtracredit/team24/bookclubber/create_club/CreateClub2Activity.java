package com.xtracredit.team24.bookclubber.create_club;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.xtracredit.team24.bookclubber.Club;
import com.xtracredit.team24.bookclubber.HomeActivity;
import com.xtracredit.team24.bookclubber.MockServer;
import com.xtracredit.team24.bookclubber.R;
import com.xtracredit.team24.bookclubber.SelectGenreActivity;
import com.xtracredit.team24.bookclubber.gBooksWrapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CreateClub2Activity extends AppCompatActivity {

    EditText date;
    Calendar calendar;

    EditText startTime;
    EditText endTime;

    Button next, back;

    EditText address;

    Club newClub;

    double latitude, longitude;

    final Gson gson = new Gson();

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Intent fromCreateClub1 = getIntent();
        newClub = gson.fromJson(fromCreateClub1.getStringExtra("club"), Club.class);

        date = findViewById(R.id.editDate_create_book_club);
        calendar = Calendar.getInstance();

        startTime = findViewById(R.id.meeting_time_start);
        endTime = findViewById(R.id.meeting_time_end);

        address = findViewById(R.id.editAddress_create_book_club1);
        next = findViewById(R.id.next_create_club2);
        back = findViewById(R.id.back_create_club2);

        Places.initialize(getApplicationContext(), "AIzaSyDDutQa82ie2cf0z05HkwOkogMhPNY-0lI");
        Places.createClient(this);

        final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateFormatString = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString, Locale.US);

                date.setText(dateFormat.format(calendar.getTime()));
            }

        };

        final TimePickerDialog.OnTimeSetListener startTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime.setText(formatTime(hourOfDay, minute));
            }
        };

        final TimePickerDialog.OnTimeSetListener endTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime.setText(formatTime(hourOfDay, minute));
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateClub2Activity.this, datePicker,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateClub2Activity.this, startTimePicker,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateClub2Activity.this, endTimePicker,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(CreateClub2Activity.this, HomeActivity.class);
                String addressText = address.getText().toString();
                String meetingDate = date.getText().toString();
                String startTimeText = startTime.getText().toString();
                String endTimeText = endTime.getText().toString();


                if (addressText.isEmpty() || meetingDate.isEmpty() ||
                        startTimeText.isEmpty() || endTimeText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled in", Toast.LENGTH_SHORT).show();
                }

                newClub.address = addressText;
                newClub.meetingDate = meetingDate;
                newClub.startTime = startTimeText;
                newClub.endTime = endTimeText;
                newClub.userIds = new ArrayList<>();
                newClub.location = new Location("");


                newClub.location.setLatitude(latitude);
                newClub.location.setLongitude(longitude);
                MockServer.ourInstance.createClub(newClub);

                startActivity(toHome);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CreateClub2Activity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    public String formatTime(int hour, int min) {
        String amPm = "AM";
        String time = "";

        if (hour > 12) {
            amPm = "PM";
            hour -= 12;
        }

        if (hour == 0) {
            amPm = "AM";
            hour = 12;
        }

        time += String.valueOf(hour + ":");

        if (min < 10) {
            time += String.valueOf("0" + String.valueOf(min) + amPm);
        } else {
            time += String.valueOf(String.valueOf(min) + amPm);
        }

        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                address.setText(place.getAddress());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("LOCATION ERROR", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
