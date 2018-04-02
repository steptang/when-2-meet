
package com.a4.cis350.when2meetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class HomeScreenActivity extends AppCompatActivity {

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference database;
    public static DatabaseReference eventDatabase;
    public static DatabaseReference userDatabase;
    public static ArrayList<User> userList;
    public static HashMap<String, String> userEmailKeyMap;  // maps a user's email to their key
    public static User currentUser;
    public static FirebaseUser currentUserFB;
    public ArrayList<KeyNamePair> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //setup realtime database
        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference();
        eventDatabase = firebaseDatabase.getReference("events");
        userDatabase = firebaseDatabase.getReference("users");

        userList = new ArrayList<>();
        userEmailKeyMap = new HashMap<>();

        currentUser = null;
        currentUserFB = FirebaseAuth.getInstance().getCurrentUser();

        // Get list of users from the database
        // Note, this is run asynchronously to the rest of this class
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear(); // remove previous entries to avoid duplicates

                // getting children of "users", i.e. each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    String key = userSnapshot.getKey();
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String bio =  userSnapshot.child("bio").getValue(String.class);
                    LinkedList<Schedule> schedules = null;       // TODO

                    // handling the return from getValue() - either a list or map
                    List<Invite> invites = null;
                    Object value = userSnapshot.child("invites").getValue();
                    if (value == null) {
                        invites = new ArrayList<>();
                    } else if (value instanceof List) {
                        invites = (ArrayList<Invite>) value;
                    } else {
                        Map<String, Invite> invitesMap = (Map<String, Invite>) value;
                        invites = new ArrayList<Invite>(invitesMap.values());
                    }

                    List<String> joinedEvents = null;
                    Object value2 = userSnapshot.child("joinedEvents").getValue();
                    if (value2 == null) {
                        invites = new ArrayList<>();
                    } else if (value2 instanceof List) {
                        joinedEvents = (ArrayList<String>) value2;
                    } else {
                        Map<String, String> joinedEventsMap = (Map<String, String>) value2;
                        joinedEvents = new ArrayList<String>(joinedEventsMap.values());
                    }

//                    List<String> joinedEvents = (ArrayList<String>) userSnapshot.child("joinedEvents").getValue();

                    userList.add(new User(name, bio, email, schedules, invites, joinedEvents));
                    userEmailKeyMap.put(email, key);
                }

                // If the current user is not currently stored, add them to the database and the list
                // - should happen on only their first time logging in
                if (!currentUserIsStored()) {
                    addCurrentUser();
                }

                // storing the user class for current user
                if (currentUser == null) {
                    currentUser = getCurrentUser();
                }

                // now that user data has been loaded, set up events
                setupEvents();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
            }
        });

        // Setup button Views:
        // Add Event
        Button addEventBtn = findViewById(R.id.add_event_btn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEventIntent = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(addEventIntent);
            }
        });

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
        Button myInvitesBtn = findViewById(R.id.my_invites_btn);
        myInvitesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInvitesIntent = new Intent(getApplicationContext(), InvitesScreenActivity.class);
                startActivity(myInvitesIntent);
            }
        });
    }

    // loads event data from firebase, and displays user-relevant events
    private void setupEvents() {
        // Get the list of events from the database
        ListView listview = findViewById(R.id.event_list);
        list = new ArrayList<>();
        final ArrayAdapter<KeyNamePair> adapter = new ArrayAdapter<KeyNamePair>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {
                String eventKey = list.get(position).key; //this is what was clicked
                Intent EventClicked = new Intent(getApplicationContext(), EventActivity.class);
                EventClicked.putExtra("EVENT_KEY", eventKey);
                EventClicked.putExtra("EVENT_NAME", list.get(position).toString());
                startActivity(EventClicked);
            }
        });

        // storing events in list check if there are any data changes and flush the old data
        firebaseDatabase.getReference("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear(); //remove previous entries to avoid duplicates

                if (currentUser == null) {
                    return;
                }

                // for every event - in which the user has JOINED - add it to the event list
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    if (currentUser.hasJoined(eventSnapshot.getKey())) {
                        String key = eventSnapshot.getKey();
                        String name = eventSnapshot.child("name").getValue(String.class);
                        list.add(new KeyNamePair(key, name));
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


    // checks whether or not the current user is stored in the firebase db
    private boolean currentUserIsStored() {

        // getting the current firebase user's email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        for (User userObject : userList) {
            if (email.equals(userObject.getEmail())) {
                return true;
            }
        }

        // otherwise
        return false;
    }

    // adds a new user to the database and userList
    private void addCurrentUser() {

        // getting the current firebase user
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String name = fbUser.getDisplayName();
        String bio = "Empty Bio";      // not setting this
        String email = fbUser.getEmail();
        LinkedList<Schedule> schedules = null;      // TODO - implement schedule
        List<Invite> invites = new ArrayList<>();
        List<String> joinedEvents = new ArrayList<>();

//        // mock data
//        invites.add(new Invite("event-key", "invite-name", "admin-email"));
//        invites.add(new Invite("event-key2", "invite-name2", "admin-email2"));

        User user = new User(name, bio, email, schedules, invites, joinedEvents);

        // TODO - what does push() do
        //String userKey =  userDatabase.push().getKey();
        String userKey = userDatabase.push().getKey();

        // saving the (current) user to the database
        userDatabase.child(userKey).setValue(user);

        // shouldn't be necessary due to the onDataChange method??
//        // adding them to userList
//        userList.add(user);
    }

    // Finding the current user
    private User getCurrentUser() {

        User cUser = null;

        for (User user : userList) {
            if (user.email.equals(currentUserFB.getEmail())) {
                cUser = user;
            }
        }

        return cUser;
    }



    class KeyNamePair {

        String key;
        String name;

        KeyNamePair(String key, String name) {
            this.key = key;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }
}
