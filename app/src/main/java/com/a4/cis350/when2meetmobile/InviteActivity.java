package com.a4.cis350.when2meetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class InviteActivity extends AppCompatActivity {

    private String inviteKey;
    private String inviteName;
    private String eventKey;
    private String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        // get intent
        Intent intent = getIntent();
        inviteKey = intent.getStringExtra("INVITE_KEY");
        inviteName = intent.getStringExtra("INVITE_NAME");
        eventKey = intent.getStringExtra("EVENT_KEY");
        adminEmail = intent.getStringExtra("ADMIN_EMAIL");

        // Setting up textViews:
        TextView inviteNameText = findViewById(R.id.invite_name_text);
        inviteNameText.setText(inviteName);

        TextView adminEmailText = findViewById(R.id.admin_email_text);
        adminEmailText.setText(adminEmail);


        // Setup button Views:
        // Accept button
        Button acceptBtn = findViewById(R.id.accept_btn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Link this event to this User's firebase account, so that it is displayed on their homescreenactivity
                // use the eventKey to find the Event to link / attach

                // adding the eventKey to this user's firebase list of events joined
                String userEmail = HomeScreenActivity.currentUser.email;
                String userKey = HomeScreenActivity.userEmailKeyMap.get(userEmail);
                DatabaseReference userRef = HomeScreenActivity.userDatabase.child(userKey);
                userRef.child("joinedEvents").push().setValue(eventKey);

                // remove this invite from this User's account
                removeInvite();

                Toast.makeText(getApplicationContext(), "Invitation Accepted", Toast.LENGTH_SHORT).show();

                // returning to their invites screen
                finish();
            }
        });

        // Decline button
        Button declineBtn = findViewById(R.id.decline_btn);
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - remove this invite from this User's account
                removeInvite();

                Toast.makeText(getApplicationContext(), "Invitation Declined", Toast.LENGTH_SHORT).show();

                // returning to their invites screen
                finish();
            }
        });
    }

    // TODO
    // remove this invite from this user's account
    private void removeInvite() {
        InvitesScreenActivity.inviteDatabase.child(inviteKey).removeValue();
    }

}
