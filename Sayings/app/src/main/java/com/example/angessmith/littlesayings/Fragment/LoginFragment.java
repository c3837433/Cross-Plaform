package com.example.angessmith.littlesayings.Fragment;

// Created by AngeSSmith on 2/2/15.

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.angessmith.littlesayings.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    // Create the tag
    public static final String TAG = "LoginFragment.TAG";
    // Set up the edit text views to get the entered data when user logs in
    private EditText mEnteredEmailTextView;
    private EditText mEnteredPasswordView;
    // Define the listener interface for clicks
    private OnFragmentButtonClickListener mListener;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Get the login or register buttons
        Button loginButton = (Button) view.findViewById(R.id.login_log_user_button);
        Button registerButton = (Button) view.findViewById(R.id.login_register_signup_button);

        // set the listeners to the buttons
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // get email and password textfields set
        mEnteredEmailTextView = (EditText)view.findViewById(R.id.login_email);
        mEnteredPasswordView = (EditText) view.findViewById(R.id.login_password);

        // return the view
        return view;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        // see which button was selected
        switch (v.getId()) {
            case R.id.login_register_signup_button:
                mListener.switchToRegisterFragment();
                break;
            case R.id.login_log_user_button:
                // Get the entered username and password from the fields for verification
                mListener.verifyEnteredLogInCredentialsForLogIn(mEnteredEmailTextView.getText().toString(), mEnteredPasswordView.getText().toString());
        }
    }


    public interface OnFragmentButtonClickListener {
        public void verifyEnteredLogInCredentialsForLogIn(String email, String password);
        public void switchToRegisterFragment();
    }

}
