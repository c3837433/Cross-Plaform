package com.example.angessmith.littlesayings.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.angessmith.littlesayings.R;

 // Created by AngeSSmith on 2/2/15.

public class RegisterFragment extends Fragment implements View.OnClickListener {

    // Create the tag
    public static final String TAG = "LoginFragment.TAG";
    // Set up the edit text views to get the entered data when user logs in
    private EditText mRegEmailTextView;
    private EditText mRegPasswordView;
    private EditText mRevPasswordConfirmView;
    // Define the listener interface for clicks
    private OnRegisterFragmentButtonListener mListener;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate this layout
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Get the buttons
        Button registerButton = (Button) view.findViewById(R.id.register_user_button);
        Button cancelButton = (Button) view.findViewById(R.id.register_cancel_button);

        // set the button listeners
        registerButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // get email and password textfields set
        mRegEmailTextView = (EditText)view.findViewById(R.id.register_email);
        mRegPasswordView = (EditText) view.findViewById(R.id.register_password);
        mRevPasswordConfirmView = (EditText) view.findViewById(R.id.register_password_conf);

        // return the view
        return view;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegisterFragmentButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegisterFragmentButtonListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnRegisterFragmentButtonListener {
        // Define the methods to implement with the button listeners int eh activity
        public void verifyRegistrationData(String email, String password, String password2);
        public void registerCanceledReturnToLogInFragment();
    }



    @Override
    public void onClick(View v) {
        // see which button was selected
        switch (v.getId()) {
            case R.id.register_user_button:
                // Get the entered values
                mListener.verifyRegistrationData(mRegEmailTextView.getText().toString(), mRegPasswordView.getText().toString(),  mRevPasswordConfirmView.getText().toString());
                break;
            case R.id.register_cancel_button:
                mListener.registerCanceledReturnToLogInFragment();
                break;

        }
    }

}
