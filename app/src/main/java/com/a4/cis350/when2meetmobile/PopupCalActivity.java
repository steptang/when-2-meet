package com.a4.cis350.when2meetmobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;

import java.util.ArrayList;

public class PopupCalActivity extends AppCompatActivity {

    ArrayList<Day> listSelectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_cal);

        listSelectedDates = new ArrayList<Day>();

        //snackbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final OneCalendarView calendarView = (OneCalendarView) findViewById(R.id.oneCalendar);

        // the following fragment can be used to capture the swipes in the calendar
        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {

            /*** notifies the user that the calendar has changed to the previous month */
            @Override
            public void prevMonth() {

            }

            @Override
            public void nextMonth() {

            }
        });

        // the following code fragment shows how to get the data of a day in the calendar
        // in addition to performing other actions
        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {

            /** when you click on a day in the calendar shown
            ** @param day a Day object of which we can call your getDate () method to retrieve a date
            * @param position position from 0-41, which occupies in the current calendar
            */
                @Override
                public void dateOnClick(Day day, int position) {
                    calendarView.addDaySelected(position); //show selected
                    day.position = position;
                    listSelectedDates.add(day); //add to arrayList
                }
            /*** when prolonged click on a day in the calendar shown
            *
            *@param day a Day object of which we can call your getDate () method to retrieve a date
            *@param position position from 0-41, which occupies in the current calendar
            */
                @Override
                public void dateOnLongClick(Day day, int position) {

                }
        });



    }
}
