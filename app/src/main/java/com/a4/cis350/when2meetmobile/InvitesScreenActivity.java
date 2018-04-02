package com.a4.cis350.when2meetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvitesScreenActivity extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference database;
    public ArrayList<Data> inviteList;
    public static DataSnapshot currentUserSnapshot;
    public static DatabaseReference inviteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites_screen);

        // Setup real time database
        firebaseDatabase = HomeScreenActivity.firebaseDatabase;
        database = HomeScreenActivity.database;
        inviteDatabase = null;

        // Setup the inviteList, and attach this to an ArrayAdapter so that listview can be
        // populated with invites
        ListView listview = findViewById(R.id.invite_list);
        inviteList = new ArrayList<>();
        final ArrayAdapter<Data> adapter = new ArrayAdapter<Data>(this,
                android.R.layout.simple_dropdown_item_1line, inviteList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {
                String inviteKey = inviteList.get(position).key; //this is what was clicked
                Intent inviteClicked = new Intent(getApplicationContext(), InviteActivity.class);
                inviteClicked.putExtra("INVITE_KEY", inviteKey);
                inviteClicked.putExtra("INVITE_NAME", inviteList.get(position).inviteName);
                inviteClicked.putExtra("EVENT_KEY", inviteList.get(position).eventKey);
                inviteClicked.putExtra("ADMIN_EMAIL", inviteList.get(position).adminEmail);
                startActivity(inviteClicked);
            }
        });

        // Load the the inviteList, from firebase
        // - checks if there are any data changes and flush the old data
        HomeScreenActivity.userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inviteList.clear(); //remove previous entries to avoid duplicates

                // finding the current user
                currentUserSnapshot = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("email").getValue(String.class).equals(
                            HomeScreenActivity.currentUserFB.getEmail())) {
                        currentUserSnapshot = userSnapshot;
                    }
                }

                // storing a reference to this user's firebase invites
                inviteDatabase = HomeScreenActivity.userDatabase.child(currentUserSnapshot.getKey()).child("invites");

                // for the current user, accessing each invite, and then storing it in inviteList
                for (DataSnapshot inviteSnapshot : currentUserSnapshot.child("invites").getChildren()){
                    String key = inviteSnapshot.getKey();
                    String inviteName = inviteSnapshot.child("inviteName").getValue(String.class);
                    String eventKey = inviteSnapshot.child("eventKey").getValue(String.class);
                    String adminEmail = inviteSnapshot.child("adminEmail").getValue(String.class);
                    inviteList.add(new Data(key, inviteName, eventKey, adminEmail));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
            }
        });


        // Setup button Views:
        // My Profile
        Button myProfileBtn = findViewById(R.id.my_profile_btn);
        myProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(myProfileIntent);
            }
        });

        // My Invites
        Button myEventsBtn = findViewById(R.id.my_events_btn);
        myEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myEventsIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
//                startActivity(myEventsIntent);

                // return to the homescreenactivity
                finish();
            }
        });

    }

    class Data {

        String key;
        String inviteName;
        String eventKey;
        String adminEmail;

        Data(String key, String inviteName, String eventKey, String adminEmail) {
            this.key = key;
            this.inviteName = inviteName;
            this.eventKey = eventKey;
            this.adminEmail = adminEmail;
        }

        @Override
        public String toString() {
            return inviteName;
        }

    }
}
