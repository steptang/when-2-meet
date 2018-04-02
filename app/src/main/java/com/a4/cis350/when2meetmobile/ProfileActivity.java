package com.a4.cis350.when2meetmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name = findViewById(R.id.textViewName);
        TextView bio = findViewById(R.id.textViewBio);

//        // how do i get the user's name, where is the User class being used, and how can I access it here?
//        name.setText(user.getName());

//        // likewise for the bio...
//        bio.setText(user.getBio);
    }
}
