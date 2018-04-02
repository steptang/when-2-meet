package com.a4.cis350.when2meetmobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

public class GroupFragment extends Fragment {

    private Button confirmationBtn;
    private Button lockBtn;
    private Button settingsBtn;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // creating buttons (and their onClick methods)
        // for use by the organiser only

        confirmationBtn = getView().findViewById(R.id.confirmation_btn);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            // if the event is locked, send a confirmation message to all users
            @Override
            public void onClick(View v) {
                // since we don't have firebase users yet, we will redirect each user to a new
                // layout xml file after the admin has confirmed the event. this screen will contain
                // the time selected by the organiser

                // if not locked, prompt organiser to first lock
                if ( !((boolean) EventActivity.eventDataSnapshot.child("isLocked").getValue()) ) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Event must be locked before confirmation is sent",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // disabling the organiser's buttons
                hideAndDisableButtons();

                // prompt organiser (i.e. this user) to decide on final time
                String confirmedTimeAndDate = "Monday, 7pm-8pm";

                // TODO - save to the db
                EventActivity.eventDataSnapshot.child("confirmedDateTime").getRef().setValue(confirmedTimeAndDate);

                // display this time on the group tab
                displayConfirmedDateTime();

                // updating database
                EventActivity.eventDataSnapshot.child("isConfirmed").getRef().setValue(true);

            }
        });
        lockBtn = getView().findViewById(R.id.lock_btn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lock the event from further user input
                // updating the database
                EventActivity.eventDataSnapshot.child("isLocked").getRef().setValue(true);
            }
        });
        settingsBtn = getView().findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to the event settings activity
                Intent eventSettingsIntent = new Intent(getActivity().getApplicationContext(),
                        EventSettingsActivity.class);
                startActivity(eventSettingsIntent);

            }
        });
        Button availabilitiesBtn = getView().findViewById(R.id.view_availabilities_btn);
        availabilitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        AvailabilitiesActivity.class);
                intent.putExtra("EVENT_KEY", getActivity().getIntent().getStringExtra("EVENT_KEY"));
                intent.putExtra("EVENT_NAME", getActivity().getIntent().getStringExtra("EVENT_NAME"));
                startActivity(intent);
            }
        });

        // if this user is not the organiser or if the event has been confirmed, disable and hide
        // the buttons
        if (!EventActivity.isOrganiser ||
                ((boolean)EventActivity.eventDataSnapshot.child("isConfirmed").getValue())) {
//        if (false) {
            hideAndDisableButtons();
        }


    }

    private void hideAndDisableButtons() {
        confirmationBtn.setEnabled(false);
        confirmationBtn.setVisibility(View.INVISIBLE);
        lockBtn.setEnabled(false);
        lockBtn.setVisibility(View.INVISIBLE);
        settingsBtn.setEnabled(false);
        settingsBtn.setVisibility(View.INVISIBLE);
    }

    // sets the confirmed date-time string
    public void displayConfirmedDateTime() {
        TextView textView = getView().findViewById(R.id.confirmation_textview);
        String message = "Confirmed time and date: " +
                EventActivity.eventDataSnapshot.child("confirmedDateTime").getValue().toString();
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

}
