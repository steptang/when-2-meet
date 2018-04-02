package com.a4.cis350.when2meetmobile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by truongd on 3/19/18.
 */

public class AvailabilitiesActivity extends Activity {

    public static DatabaseReference database;
    GridView availabilitiesGrid;
    String eventKey;
    private ArrayList<Integer> dayPositions;
    public ArrayList<String> list;
    public int[][] guestCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availabilities);
        // get event title
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventKey = null;
            } else {
                eventKey = extras.getString("EVENT_KEY");
            }
        } else {
            eventKey= (String) savedInstanceState.getSerializable("EVENT_KEY");
        }

        availabilitiesGrid = findViewById(R.id.availabilitiesGrid);
        dayPositions = new ArrayList<>();
        list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.WHITE;
                if (list.get(position).isEmpty()) {

                }

                view.setBackgroundColor(color);
                return view;
            }
        };
        availabilitiesGrid.setAdapter(adapter);

        HomeScreenActivity.firebaseDatabase.getReference("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    String key = eventSnapshot.getKey();
                    if (eventKey.equals(key)) {
                        if (eventSnapshot.child("weekly").getValue(Boolean.class)) {
                            int columnCount = (int) eventSnapshot.child("weekdays").getChildrenCount();
                            int rowCount = (int) (4 * timeDiff(roundTime(eventSnapshot.child("startTime").getValue(String.class)),
                                    roundTime(eventSnapshot.child("endTime").getValue(String.class))));
                            guestCount = new int[rowCount][columnCount];
                            availabilitiesGrid.setNumColumns(columnCount + 1);
                            list.add("");
                            for(DataSnapshot days : eventSnapshot.child("weekdays").getChildren()) {
                                list.add(weekday(days.getValue(Long.class)));
                                dayPositions.add(days.getValue(Long.class).intValue());
                            }
                            for (int i = 0; i < rowCount * (columnCount + 1); i++) {
                                if(i % (columnCount + 1) == 0) {
                                    list.add(incrementTimeByXMinutes(roundTime(eventSnapshot.child("startTime").getValue(String.class)),
                                            i/(columnCount + 1)));
                                }
                                else list.add("");
                            }
                        }
                        else {
                            int columnCount = (int) eventSnapshot.child("days").getChildrenCount();
                            availabilitiesGrid.setNumColumns(columnCount);
                            for(DataSnapshot days : eventSnapshot.child("days").getChildren()) {
                                list.add(days.getValue(String.class));
                            }
                            int rowCount = (int) (4 * timeDiff(roundTime(eventSnapshot.child("startTime").getValue(String.class)),
                                    roundTime(eventSnapshot.child("endTime").getValue(String.class))));
                            guestCount = new int[rowCount][columnCount];
                            for (int i = 0; i < rowCount * (columnCount + 1); i++) {
                                if(i % (columnCount + 1) == 0) {
                                    list.add(incrementTimeByXMinutes(roundTime(eventSnapshot.child("startTime").getValue(String.class)),
                                            i / (columnCount + 1)));
                                }
                                else list.add("");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
            }
        });
    }

    private String weekday(long number) {
        switch((int) number) {
            case 1: return "Sun.";
            case 2: return "Mon.";
            case 3: return "Tues.";
            case 4: return "Wed.";
            case 5: return "Thurs.";
            case 6: return "Fri.";
            case 7: return "Sat.";
            default: return "";
        }
    }

    private int weekdayToInt(String day) {
        switch(day) {
            case "Sunday": return 1;
            case "Monday": return 2;
            case "Tuesday": return 3;
            case "Wednesday": return 4;
            case "Thursday": return 5;
            case "Friday": return 6;
            case "Saturday": return 7;
            default: return 0;
        }
    }

    private int[] roundTime(String time) {
        String[] timeArray = time.split(":", 2);
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        minute = (int) Math.round(minute / 15.0) * 15;
        int[] output = new int[2];
        output[0] = hour;
        output[1] = minute;
        return output;
    }

    private double timeDiff(int[] startTime, int[] endTime) {
        int hourDiff = endTime[0] - startTime[0];
        int minDiff = endTime[1] - startTime[1];
        if (minDiff == 60) {
            hourDiff++;
            minDiff = 0;
        }
        return (hourDiff + (minDiff/60.0));
    }

    private String incrementTimeByXMinutes(int[] time, int x) {
        time[1] += x;
        if (x != 0) {
            time[0] += (time[1] / 60);
            time[1] %= 60;
        }
        return time[0] + ":" + time[1];
    }

    private int timeRange(String range) {
        String[] timesArray = range.split("-", 2);
        int[] start = roundTime(timesArray[0]);
        int[] end = roundTime(timesArray[1]);
        return (int) (4 * timeDiff(start, end));
    }

    private String[] availabilityTimes(String range) {
        return range.split("-", 2);
    }

}
