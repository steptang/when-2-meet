package com.a4.cis350.when2meetmobile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class InviteUsersActivity extends AppCompatActivity {

    private String eventKey;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_users);

        Intent intent = getIntent();
        eventKey = intent.getStringExtra("EVENT_KEY");
        eventName = intent.getStringExtra("EVENT_NAME");

        // buttons
        Button inviteUserBtn = findViewById(R.id.invite_user_btn);
        inviteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send an invite to a user

                String userEmail = ((EditText) findViewById(R.id.user_email_text)).getText().toString();

                // if not a valid email, do not send an invite
                if (!isValidEmail(userEmail)) {
                    Toast.makeText(getApplicationContext(), "Invalid / non-user email entered. Retry.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 1. Finding this user and their list of invites
                String key = HomeScreenActivity.userEmailKeyMap.get(userEmail);
                DatabaseReference user = HomeScreenActivity.userDatabase.child(key);
                DatabaseReference userInvites = user.child("invites");

                // 2. Sending this user an invite notification via email
                sendEmailNotification(userEmail);

                // 3. Adding this invite to the user's firebase database
                userInvites.push().setValue(new Invite(eventKey, eventName, HomeScreenActivity.currentUser.email));

            }
        });

        Button doneBtn = findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // move to the event settings activity
                Intent intent = new Intent(getApplicationContext(), EventSettingsActivity.class);
                startActivity(intent);
            }
        });

        // add the admin to the event
        String userEmail = HomeScreenActivity.currentUser.email;
        String userKey = HomeScreenActivity.userEmailKeyMap.get(userEmail);
        DatabaseReference userRef = HomeScreenActivity.userDatabase.child(userKey);
        userRef.child("joinedEvents").push().setValue(eventKey);

    }


//    https://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application?noredirect=1&lq=1
    private void sendEmailNotification(String recipientAddress) {

        // TODO - streamline the process to a single button click
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{recipientAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "When2Meet Mobile: Event Invitation");
        intent.putExtra(Intent.EXTRA_TEXT   , "You have been invited to : " + eventName);
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InviteUsersActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // Before we spam someones inbox (or send nowhere), double-check that this is a (valid) user's
    // email
    private boolean isValidEmail(String email) {

        for (User user : HomeScreenActivity.userList) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }

        // otherwise
        return false;
    }
}
