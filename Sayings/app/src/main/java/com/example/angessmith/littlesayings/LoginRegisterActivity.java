package com.example.angessmith.littlesayings;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.angessmith.littlesayings.Fragment.LoginFragment;


public class LoginRegisterActivity extends Activity implements LoginFragment.OnFragmentButtonClickListener {

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


    @Override
    public void verifyEnteredLogInCredentialsForLogIn(String email, String password) {
        Log.d(TAG, "User attempting to log in with " + email + " and password: " + password);
    }

    @Override
    public void switchToRegisterFragment() {
        Log.d(TAG, "User wants to register, switch fragments");

    }
}
