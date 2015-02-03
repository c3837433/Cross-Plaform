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

import com.example.angessmith.littlesayings.R;

 // Created by AngeSSmith on 2/3/15.

public class AddSayingFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddSayingFragment.TAG;
    // get access to all the textviews
    private EditText mNameView;
    private EditText mAgeView;
    private EditText mSayingView;
    private EditText mDateView;

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
        mDateView = (EditText) view.findViewById(R.id.speaker_date);

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
                mListener.gatherEnteredData(mNameView.getText().toString(), mAgeView.getText().toString(), mSayingView.getText().toString(), mDateView.getText().toString());
                break;
            case R.id.speaker_get_date:
                mListener.getDate();
        }
    }


    public interface AddSayingButtonListener {
        // button methods
        public void gatherEnteredData(String name, String age, String saying, String date);
        public void getDate();
    }

}
