package com.example.angessmith.littlesayings.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.angessmith.littlesayings.R;

import java.util.Calendar;

// Created by AngeSSmith on 2/3/15.
// *** Github Repository Link https://github.com/c3837433/Cross-Plaform

public class AddSayingFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddSayingFragment.TAG;
    // get access to all the textviews
    private EditText mNameView;
    private EditText mAgeView;
    private EditText mSayingView;
    private TextView mDateView;

    // Define the listener interface
    private AddSayingButtonListener mListener;

    public static AddSayingFragment newInstance() {
        return new AddSayingFragment();
    }

    public AddSayingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_add_saying, container, false);
        // Get the button
        Button saveButton = (Button) view.findViewById(R.id.speaker_save_saying);
        ImageButton getDateButton = (ImageButton) view.findViewById(R.id.speaker_get_date);
        // set the listeners
        saveButton.setOnClickListener(this);
        getDateButton.setOnClickListener(this);

        // get user entered values
        mNameView = (EditText)view.findViewById(R.id.speaker_name);
        mAgeView = (EditText) view.findViewById(R.id.speaker_age);
        mSayingView = (EditText)view.findViewById(R.id.speaker_saying);
        mDateView = (TextView) view.findViewById(R.id.speaker_date);

        // get today's date
        Calendar mCalendarDate = Calendar.getInstance();
        int year = mCalendarDate.get(Calendar.YEAR);
        int month = mCalendarDate.get(Calendar.MONTH) + 1; // to compensate for return error
        int day = mCalendarDate.get(Calendar.DAY_OF_MONTH);
        String date = month + "/" + day + "/" + year;
        // set the date in the textview
        mDateView.setText(date);

        // return the view
        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddSayingButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddSayingButtonListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        // get what triggered the click
        switch (v.getId()) {
            case R.id.speaker_save_saying:
                // get the number entered in the age edit text view
                Integer age = Integer.parseInt(mAgeView.getText().toString());
                mListener.gatherEnteredData(mNameView.getText().toString(), age, mSayingView.getText().toString(), mDateView.getText().toString());
                break;
            case R.id.speaker_get_date:
                mListener.getDate(mDateView);
        }
    }


    public interface AddSayingButtonListener {
        // button methods
        public void gatherEnteredData(String name, Integer age, String saying, String date);
        public void getDate(TextView textView);
    }

}
