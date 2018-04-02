package com.a4.cis350.when2meetmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class CalendarTimePickerActivity extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener, CalendarDatePickerDialogFragment.OnDateSetListener{

    private Button selectDatesBtn;
    private Button selectStartTimeBtn;
    private boolean startEnd; //true is start
    private Button selectEndTimeBtn;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private String militaryStartTime = "";
    private String militaryEndTime = "";
    public ArrayList<String> dateList;
    ArrayAdapter<String> listAdapter;
    public static DatabaseReference eventDatabase;
    public static DatabaseReference userDatabase;
    private String eventTitle;
    private static List<Integer> selectedDays = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_time_picker);

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

        selectDatesBtn = findViewById(R.id.select_dates_btn);
        selectDatesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(CalendarTimePickerActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        selectStartTimeBtn = findViewById(R.id.start_time_btn2);
        selectStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnd = true;
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(CalendarTimePickerActivity.this)
                        .setStartTime(12, 00)
                        .setDoneText("Complete")
                        .setCancelText("Cancel")
                        .setThemeCustom(R.style.CustomRadialPickerStyle);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        selectEndTimeBtn = findViewById(R.id.end_time_btn2);
        selectEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnd = false;
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(CalendarTimePickerActivity.this)
                        .setStartTime(12, 00)
                        .setDoneText("Complete")
                        .setCancelText("Cancel")
                        .setThemeCustom(R.style.CustomRadialPickerStyle);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        ListView listview = findViewById(R.id.date_list);
        dateList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dateList);
        listview.setAdapter(listAdapter);

        Button nextbtn2 = findViewById(R.id.next_btn2);
        nextbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - using a dummy event for now
                // here, the event is created
                String eventKey = createDummyDateEvent();

                // TODO - add callbacks so that this works
//                // moving to the new event
//                Intent newEventIntent = new Intent(getApplicationContext(), EventActivity.class);
//                newEventIntent.putExtra("EVENT_NAME", eventTitle);
//                startActivity(newEventIntent);

                // now moving to the invite users activity
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
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateSelected = getMonth(monthOfYear) + " " + Integer.toString(dayOfMonth) + ", " + Integer.toString(year);
        if (dateList.contains(dateSelected)){
            return;
        }
        dateList.add(dateSelected);
        listAdapter.notifyDataSetChanged();
    }

    private String getMonth(int index){
        switch (index){
            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            case 11: return "December";
            default: return "Error";
        }
    }

    private String createDummyDateEvent(){
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
        LinkedList days = null;
        HashMap<String, Schedule> availabilities = new HashMap<>();
        availabilities.put("Stephanie", schedule1);
        if (militaryStartTime.equals("") || militaryEndTime.equals("")){
            createAlert("Error", "Start and end times not selected.");
        } else if(dateList.size() == 0) {
            createAlert("Error", "No date selected.");
        } else {
            Event event = new Event(user, eventTitle, false, dateList, selectedDays,
                    militaryStartTime, militaryEndTime, availabilities, false,
                    false, "");
            eventDatabase.child(eventKey).setValue(event);
        }

        return eventKey;
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
