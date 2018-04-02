package com.a4.cis350.when2meetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class AddEventActivity extends AppCompatActivity {

    final int WEEKLY_TYPE = 0;
    final int CALENDAR_TYPE = 1;
    final int UNASSIGNED_TYPE = 2;

    public static DatabaseReference testDatabase;

    private int eventType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //test add data to database
        testDatabase = HomeScreenActivity.firebaseDatabase.getReference("start_message");

        // initialising event type selection
        eventType = UNASSIGNED_TYPE;

    }

    public void onWeekDaysBtnClick(View view) {
        eventType = WEEKLY_TYPE;
    }

    public void onCalendarDaysBtnClick(View view) {
        eventType = CALENDAR_TYPE;
    }

    public void onCreateEventBtnClick(View view) {

        // getting event name text input
        EditText inputText = (EditText) findViewById(R.id.event_name_text);
        String eventName = inputText.getText().toString();

        if (eventName.isEmpty()) {
            // notify user
            Toast toast = Toast.makeText(getApplicationContext(), "Enter an event name",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (eventType == WEEKLY_TYPE) {
            Intent addWeeklyEventIntent = new Intent(getApplicationContext(),
                    WeeklyTimePickerActivity.class);
            addWeeklyEventIntent.putExtra("EVENT_NAME", eventName);
            startActivity(addWeeklyEventIntent);
//            finish();
        }

        // TODO - make a CalendarTimePickerActivity class
        else if (eventType == CALENDAR_TYPE) {
            Intent addCalendarEventIntent = new Intent(getApplicationContext(),
                    CalendarTimePickerActivity.class);
            addCalendarEventIntent.putExtra("EVENT_NAME", eventName);
            startActivity(addCalendarEventIntent);
            finish();
        }

        else if (eventType == UNASSIGNED_TYPE) {
            // notify user of insufficient inputs
            Toast toast = Toast.makeText(getApplicationContext(), "Select an event type",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
