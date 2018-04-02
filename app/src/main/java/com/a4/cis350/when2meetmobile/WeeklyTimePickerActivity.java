package com.a4.cis350.when2meetmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WeeklyTimePickerActivity extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener{

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    private boolean startEnd; //true is start
    private Button selectStartTimeBtn;
    private Button selectEndTimeBtn;
    private static List<Integer> selectedDays = Arrays.asList(2,3,4,5,6);
    public static DatabaseReference eventDatabase;
    public static DatabaseReference userDatabase;
    private String militaryStartTime = "";
    private String militaryEndTime = "";
    private String eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_time_picker);

        //set up event database
        eventDatabase = HomeScreenActivity.firebaseDatabase.getReference("events");
        userDatabase = HomeScreenActivity.firebaseDatabase.getReference("users");

        // get event title
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventTitle = null;
            } else {
                eventTitle = extras.getString("EVENT_NAME");
            }
        } else {
            eventTitle= (String) savedInstanceState.getSerializable("EVENT_NAME");
        }

        WeekdaysPicker widget = findViewById(R.id.weekdays);
        widget.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
            @Override
            public void onChange(View view, int clickedDayOfWeek, List<Integer> selectedDays) {
                WeeklyTimePickerActivity.selectedDays = selectedDays;
            }
        });

        selectStartTimeBtn = findViewById(R.id.start_time_btn);
        selectStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnd = true;
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(WeeklyTimePickerActivity.this)
                        .setStartTime(12, 00)
                        .setDoneText("Complete")
                        .setCancelText("Cancel")
                        .setThemeCustom(R.style.CustomRadialPickerStyle);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        selectEndTimeBtn = findViewById(R.id.end_time_btn);
        selectEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnd = false;
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(WeeklyTimePickerActivity.this)
                        .setStartTime(12, 00)
                        .setDoneText("Complete")
                        .setCancelText("Cancel")
                        .setThemeCustom(R.style.CustomRadialPickerStyle);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        Button nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - using a dummy event for now
                String eventKey = createDummyWeeklyEvent();

                // TODO - add callbacks so that this works
//                // moving to the new event
//                Intent newEventIntent = new Intent(getApplicationContext(), EventActivity.class);
//                newEventIntent.putExtra("EVENT_NAME", eventTitle);
//                startActivity(newEventIntent);

                // now moving to the invite users activity
                // TODO - perhaps pass the created event (via intent) so that we have something to message the users with / about
                Intent intent = new Intent(getApplicationContext(), InviteUsersActivity.class);
                intent.putExtra("EVENT_KEY", eventKey);
                intent.putExtra("EVENT_NAME", eventTitle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        RadialTimePickerDialogFragment rtpd = (RadialTimePickerDialogFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeSetListener(this);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        if(startEnd){
            selectStartTimeBtn.setText(createTimeString(hourOfDay, minute));
            militaryStartTime = createMilitaryTimeString(hourOfDay, minute);
        } else {
            selectEndTimeBtn.setText(createTimeString(hourOfDay, minute));
            militaryEndTime = createMilitaryTimeString(hourOfDay, minute);
        }
    }

    private String createMilitaryTimeString(int hourOfDay, int minute){
        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        if (hourOfDay < 10){
            hour = "0" + hour;
        }
        if (minute < 10){
            min = "0" + min;
        }
        return hour + ":" + min;
    }

    private String createTimeString(int hourOfDay, int minute){
        String min = Integer.toString(minute);
        String ampm = "";
        if (hourOfDay < 13){
            ampm = " AM";
        } else {
            hourOfDay = hourOfDay - 12;
            ampm = " PM";
        }
        String hour = Integer.toString(hourOfDay);
        if (hourOfDay < 10){
            hour = "0" + hour;
        }
        if (minute < 10){
            min = "0" + min;
        }
        return hour + ":" + min + ampm;
    }

    // creates a dummy event and returns the eventKey
    private String createDummyWeeklyEvent(){
        String userKey =  userDatabase.push().getKey();
        String eventKey =  eventDatabase.push().getKey();
        String email = MainActivity.account.getEmail();

        if (email == null) {
            //TODO: Add error
        }

        LinkedList<String> schedule = new LinkedList<>();
        schedule.add("10:00-10:15");
        HashMap<String, LinkedList<String>> newSchedule = new HashMap<>();
        newSchedule.put("Monday", schedule);
        Schedule schedule1 = new Schedule(newSchedule);
        LinkedList<Schedule> list = new LinkedList<>();
        list.add(schedule1);
        list.add(schedule1);

        User user = new User("Stephanie", "avocado bio", email, list, null, null);
        //userDatabase.child(userKey).setValue(user);
        ArrayList<String> days = null;
        HashMap<String, Schedule> availabilities = new HashMap<>();
        availabilities.put("Stephanie", schedule1);
        if (militaryStartTime.equals("") || militaryEndTime.equals("")){
            createAlert("Error", "Start and end times not selected.");
        } else if(selectedDays.size() == 0) {
            createAlert("Error", "No days of the week selected.");
        } else {
            Event event = new Event(user, eventTitle, true, days, selectedDays,
                    militaryStartTime, militaryEndTime, availabilities, false,
                    false, "");
            eventDatabase.child(eventKey).setValue(event);
        }
        // TODO - need to do something if no times/days are selected
        return eventKey;
    }


    private void createEvent(Event event){
        String eventKey =  eventDatabase.push().getKey();
        eventDatabase.child(eventKey).setValue(event);
    }

    private void createAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
