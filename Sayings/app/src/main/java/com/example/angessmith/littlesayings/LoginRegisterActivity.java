package com.example.angessmith.littlesayings;

// Created by AngeSSmith on 2/2/15.

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angessmith.littlesayings.Fragment.LoginFragment;
import com.example.angessmith.littlesayings.Fragment.RegisterFragment;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginRegisterActivity extends Activity implements LoginFragment.OnFragmentButtonClickListener, RegisterFragment.OnRegisterFragmentButtonListener  {

    public static final String TAG = "LoginRegisterActivity.TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        // Set up logo font
        TextView titleTextView = (TextView) findViewById(R.id.login_logo_title);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/giddyup.otf");
        titleTextView.setTypeface(face);

        //set up the fragment to display the log in option to the user
        LoginFragment loginFragment = LoginFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.login_register_container, loginFragment, LoginFragment.TAG).commit();
    }


    // LOG IN FRAGMENT METHODS
    @Override
    public void verifyEnteredLogInCredentialsForLogIn(String email, String password) {
        Log.d(TAG, "User attempting to log in with " + email + " and password: " + password);
        // make sure fields are not empty
        if ((email.length() != 0) && (password.length() != 0)) {
            // make sure we can still access parse
            NetworkChecker networkChecker = new NetworkChecker(this);
            boolean canStillAccess = networkChecker.networkAvailable();
            if (canStillAccess) {
                // attempt to log user in with credentials
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            // We are logged in, return to the launch activity
                            finish();
                        } else {
                            // If there is an error, alert the user
                            if (e != null) {
                                switch (e.getCode()) {
                                    case 101:
                                        // code 101 = email and password do not match
                                        toastUser("Sorry, your email or password are incorrect.");
                                        break;
                                    default:
                                        toastUser("Sorry, we are unable to log in right now, try again later.");
                                        Log.d(TAG, " Code: " + e.getCode() + "Message: " + e.getMessage());
                                        break;
                                }
                            }
                        }
                    }
                });

            } else { // No longer have a network connection
                toastUser("Please check your internet connection and try again.");
            }
        } else {
            // one of the fields are blank
            toastUser("Please enter both your email and password.");
        }
    }

    @Override
    public void switchToRegisterFragment() {
        Log.d(TAG, "User wants to register, switch fragments");
        // Get the registration fragment
        RegisterFragment registerFragment = RegisterFragment.newInstance();
        // Replace the login fragment with the register one
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.login_register_container, registerFragment, RegisterFragment.TAG);
        ft.commit();

    }

    // REGISTER FRAGMENT METHODS
    @Override
    public void verifyRegistrationData(String email, String password, String password2) {
        Log.d(TAG, "User attempting to log in with " + email + " and password 1: " + password + " and 2: " + password2);
        // Verify the data
        // Make sure the passwords match and are not empty
        if ((password.equals(password2)) && password.length() != 0) {
            // make sure the email address is valid
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            Matcher matcher = pattern.matcher(email);
            boolean validEmail = matcher.matches();
            if (validEmail) {
                // begin signing up the user
                registerNewUser(email, password);
            } else {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter two matching passwords.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void registerCanceledReturnToLogInFragment() {
        Log.d(TAG, "User wants to cancel registering, switch back to login");
        // Get the Log in one again
        LoginFragment loginFragment = LoginFragment.newInstance();
        // Return to the Log In Fragment
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.login_register_container, loginFragment, LoginFragment.TAG);
        ft.commit();
    }

    public void registerNewUser(String email, String password) {
        // Make sure we still have a valid network connection
        NetworkChecker networkChecker = new NetworkChecker(this);
        boolean canStillAccess = networkChecker.networkAvailable();
        if (canStillAccess) {
            // Create the new user
            final ParseUser parseUser = new ParseUser();
            // set their email and password
            parseUser.setUsername(email);
            parseUser.setPassword(password);
            // Restrict this user's information to this user only
            parseUser.setACL(new ParseACL(parseUser));
            // sign them up
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    // make sure we have no errors
                    if (e == null) {
                        // return to the launch screen
                        finish();
                    } else {
                        // see if they are already registered
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                toastUser("That email has already been registered.");
                                break;
                            default:
                                toastUser("Oh, sorry! We are unable to register you right now. Please try again later.");
                                Log.d(TAG, "Message: " + e.getMessage());
                                break;
                        }
                    }
                }
            });
        } else {
            toastUser("Please check your network connection and try again.");
        }
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
