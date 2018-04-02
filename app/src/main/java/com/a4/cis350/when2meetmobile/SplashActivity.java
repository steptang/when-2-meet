package com.a4.cis350.when2meetmobile;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * SplashActivity briefly displays our splash page, while simultaneously determining which activity
 * to send the user to next (depending on whether or not a user is logged in). It will then start
 * this new activity.
 */

public class SplashActivity extends AppCompatActivity {

    // splash display time in milliseconds
    final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Creating a Handler and Runnable to handle switching to the next activity (either
        // MainActivity or HomeScreenActivity) after SPLASH_TIME.

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        };

        handler.postDelayed(runnable, SPLASH_TIME);

    }

//    // Returns the intent for the next activity, i.e. either the MainActivity (i.e. login screen) or
//    // the HomeScreenActivity (if already logged in).
//    private Intent getActivityIntent() {
//
//        Intent intent;
//
//        // if not logged in
//        if (loggedIn()) {
//            return new Intent(getBaseContext(), MainActivity.class);
//        }
//
//        // if logged in
//        else {
//            return new Intent(getBaseContext(), HomeScreenActivity.class);
//        }
//
//    }
//
//    // Returns true if a user is already logged into this device.
//    private boolean loggedIn() {
//
//    }

}
