package com.example.angessmith.littlesayings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class Launch extends Activity {

    public static final String TAG = "Launch";
    public static final int LOGIN_REGISTER_INTENT = 9212011;
    public static final int LIST_INTENT = 10112001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // register the Parse saying subclass
        ParseObject.registerSubclass(SayingObject.class);

        // Set up font for launch logo
        TextView titleTextView = (TextView) findViewById(R.id.launch_logo_title);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/giddyup.otf");
        titleTextView.setTypeface(face);

        // Initialize this app with Parse
        Parse.initialize(this, "N2wRnfGDdmtURsVA4IZfgXU5UdjREkHbQepbwxBv", "kUzQRnQ1mpbyVJ8rLnjbfUYUnIHYqTotnjstXcxu");

        // Switch to whichever view is needed (Login/List)
        checkLogInAndSwitchViews();


    }

    private void checkLogInAndSwitchViews() {
        // Check network
        NetworkChecker networkChecker = new NetworkChecker(this);
        boolean canAccess = networkChecker.networkAvailable();
        if (canAccess) {
            // See if we are logged in
            final ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // user is logged in, move to saying list
                Log.i(TAG, "User logged in");
                Intent loggedInIntent = new Intent(this, SayingListActivity.class);
                startActivityForResult(loggedInIntent, LIST_INTENT);
            } else {
                // user needs to log in, move to login screen
                Log.i(TAG, "User not logged in");
                Intent loginIntent = new Intent(this, LoginRegisterActivity.class);
                startActivityForResult(loginIntent, LOGIN_REGISTER_INTENT);
            }
        } else {
            // Unable to access internet, inform user
            Toast.makeText(this, "Please check your internet access.", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        // in case user moves back here after resetting internet or pressing the back button
        checkLogInAndSwitchViews();
    }

}
