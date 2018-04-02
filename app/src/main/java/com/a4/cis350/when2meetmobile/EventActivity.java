package com.a4.cis350.when2meetmobile;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static String eventKey;
    private static String adminEmail;
    private static String userEmail;
    public static DataSnapshot eventDataSnapshot;
    public static boolean isOrganiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //get intent
        Intent intent = getIntent();
        eventKey = intent.getStringExtra("EVENT_KEY");

        userEmail = MainActivity.account.getEmail();

        // DataSnapshot is the type for a firebase level / piece of data
        eventDataSnapshot = null;
        adminEmail = null;
        isOrganiser = false;

        // Child refers to next lower level in the firebase hierarchy
        HomeScreenActivity.firebaseDatabase.getReference("events").addValueEventListener(new ValueEventListener() {
            // dataSnapshot refers to the set of all events
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Search through all events, until one is found where the current event's name is
                // equal to the name of eventSnapshot
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    if (eventKey.equals(eventSnapshot.getKey())) {

                        eventDataSnapshot = eventSnapshot;
                        adminEmail = eventSnapshot.child("admin").child("email").getValue(String.class);
                        if (userEmail.equals(adminEmail)) {
                            isOrganiser = true;
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Snackbar
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

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IndividualFragment(), "Individual");
        adapter.addFragment(new GroupFragment(), "Group");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
