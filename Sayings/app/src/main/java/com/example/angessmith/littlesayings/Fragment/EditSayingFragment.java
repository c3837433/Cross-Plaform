package com.example.angessmith.littlesayings.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.example.angessmith.littlesayings.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

 // Created by Angela Smith on 2/12/15.

public class EditSayingFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = EditSayingFragment.TAG;
    // get access to all the textviews
    private EditText mEditNameView;
    private EditText mEditAgeView;
    private EditText mEditSayingView;
    private TextView mEditDateView;
    private String mSayingId;

    // Define the listener interface
    private EditSayingButtonListener mListener;

    public static EditSayingFragment newInstance(String id) {
        EditSayingFragment fragment = new EditSayingFragment();
            Log.d(TAG, "Saying id retrieved to edit");
            fragment.mSayingId = id;
        return fragment;
    }

    public EditSayingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_edit_saying, container, false);

        // Get the button
        Button updateButton = (Button) view.findViewById(R.id.update_saying_button);
        ImageButton getDateButton = (ImageButton) view.findViewById(R.id.edit_get_date_button);

        // set the listeners
        updateButton.setOnClickListener(this);
        getDateButton.setOnClickListener(this);

        // get user entered values
        mEditNameView = (EditText)view.findViewById(R.id.edit_name);
        mEditAgeView = (EditText) view.findViewById(R.id.edit_age);
        mEditSayingView = (EditText)view.findViewById(R.id.edit_saying);
        mEditDateView = (TextView) view.findViewById(R.id.edit_date_view);

        // get today's date
        Calendar mCalendarDate = Calendar.getInstance();
        int year = mCalendarDate.get(Calendar.YEAR);
        int month = mCalendarDate.get(Calendar.MONTH) + 1; // to compensate for return error
        int day = mCalendarDate.get(Calendar.DAY_OF_MONTH);
        String date = month + "/" + day + "/" + year;
        // set the date in the textview
        mEditDateView.setText(date);

        // make sure we have an object id
        if (mSayingId !=  null) {
            // and get the object
            ParseQuery<SayingObject> query = SayingObject.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo("objectId", mSayingId);
            query.getFirstInBackground(new GetCallback<SayingObject>() {

                @Override
                public void done(SayingObject saying, ParseException e) {
                    if (e == null) {
                        // set this objects properties in the fields
                        mEditNameView.setText(saying.getChild());
                        Number age = saying.getAge();
                        mEditAgeView.setText(age.toString());
                        mEditSayingView.setText(saying.getSaying());
                        // convert the date to a string
                        Date sayingDate = saying.getSayingDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String formattedDate = dateFormat.format(sayingDate);
                        // set in view
                        mEditDateView.setText(formattedDate);
                    }
                }
            });
        }
        // return the view
        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EditSayingButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditSayingButtonListener");
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
            case R.id.update_saying_button:
                // make sure fields verify
                Boolean fieldsVerify = verifyEnteredData();
                if (fieldsVerify) {
                    // get the number entered in the age edit text view
                    Integer age = Integer.parseInt(mEditAgeView.getText().toString());
                    mListener.gatherUpdatedData(mSayingId, mEditNameView.getText().toString(), age, mEditSayingView.getText().toString(), mEditDateView.getText().toString());
                }
                break;
            case R.id.edit_get_date_button:
                mListener.getDate(mEditDateView);
        }
    }


    public interface EditSayingButtonListener {
        // button methods
        public void gatherUpdatedData(String sayingID, final String name,final Integer age,final String saying,final String date);
        public void getDate(TextView textView);
    }

    public Boolean verifyEnteredData() {
        Boolean fieldsVerified = true;
        // Check the name
        if(TextUtils.isEmpty(mEditNameView.getText().toString())) {
            mEditNameView.setError("Please enter a name.");
            return false;
        }
        if (TextUtils.isEmpty(mEditAgeView.getText().toString())) {
            mEditAgeView.setError("Please enter an age.");
            return  false;
        } else {
            // get the age
            Integer age = Integer.parseInt(mEditAgeView.getText().toString());
            if ((age < 1) || (age > 120)) {
                mEditAgeView.setError("Please enter a valid age.");
                return  false;
            }
        }

        if (TextUtils.isEmpty(mEditSayingView.getText().toString())) {
            mEditSayingView.setError("Please enter a saying.");
            fieldsVerified = false;
        }
        return fieldsVerified;

    }
}
