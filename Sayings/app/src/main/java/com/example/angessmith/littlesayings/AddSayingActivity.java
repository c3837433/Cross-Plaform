package com.example.angessmith.littlesayings;

// Created by Angela Smith for Cross Platform Mobile Development term 1502
// *** Github Repository Link https://github.com/c3837433/Cross-Plaform

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.angessmith.littlesayings.Fragment.AddSayingFragment;
import com.example.angessmith.littlesayings.Fragment.DateDialogFragment;
import com.example.angessmith.littlesayings.Fragment.EditSayingFragment;
import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddSayingActivity extends ActionBarActivity implements AddSayingFragment.AddSayingButtonListener, DatePickerDialog.OnDateSetListener, EditSayingFragment.EditSayingButtonListener {

    public static final String TAG = "AddSayingActivity.TAG";
    public TextView mDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saying);


        // if this is an edit of a current item, get the object id
        if (getIntent().hasExtra("SayingId")) {
            String objectID = getIntent().getExtras().getString("SayingId");
            // and open the edit fragment
            EditSayingFragment editSayingFragment = EditSayingFragment.newInstance(objectID);
            getFragmentManager().beginTransaction().replace(R.id.add_saying_container, editSayingFragment, EditSayingFragment.TAG).commit();
        } else {
            // this is a new instance, set up the add saying fragment
            AddSayingFragment addSayingFragment = AddSayingFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.add_saying_container, addSayingFragment, AddSayingFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_saying, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // ADD SAYING BUTTON INTERFACE LISTENERS
    // PREPARE FOR NEW SAYING OBJECT
    @Override
    public void gatherEnteredData(String name, Integer age, String saying, String date) {
        // Create the new object
        // Save what we have from the user
        SayingObject newSaying = new SayingObject();
        Log.d(TAG, "Preparing new saying");
        newSaying.put("Parent",ParseUser.getCurrentUser());
        // set the permission
        newSaying.setACL(new ParseACL(ParseUser.getCurrentUser()));
        // save this saying
        saveSaying(newSaying, name, saying, age, date);
    }

    // SAVE UPDATED SAYING
    @Override
    public void gatherUpdatedData(String sayingID, final String name, final Integer age, final String saying, final String date) {
        // Data was already verified, get this object from the data store to update it
        ParseQuery<SayingObject> query = SayingObject.getQuery();
        query.fromLocalDatastore();
        // get the saying
        query.getInBackground(sayingID, new GetCallback<SayingObject>() {
            public void done(SayingObject sayingObject, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Retrieved object for update");
                    // save the saying
                    saveSaying(sayingObject, name, saying, age, date);
                }
            }
        });

    }

    // SAVE SAYING OBJECT
    public void saveSaying(final SayingObject thisSaying, String name, String saying, Integer age, String date ) {
        Log.d(TAG, "Saving object");
        // Set the main values for this object
        thisSaying.setSaying(saying);
        thisSaying.setAge(age);
        thisSaying.setChild(name);
        // Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = convertToDateObject(sdf, date);
        thisSaying.setDate(newDate);
        // pin it to the local data store
        thisSaying.pinInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (isFinishing()) {
                            return;
                        }
                        if (e == null) {
                            // save the object
                            savePinnedSaying(thisSaying);
                        } else {
                            Log.d(TAG, "Error pinning: " + e.getMessage());
                        }
                    }
                });
    }
    public void savePinnedSaying(SayingObject saying) {
        // check our connection
        NetworkChecker networkChecker = new NetworkChecker(this);
        boolean connected = networkChecker.networkAvailable();
        if (connected) {
            // save now
            saying.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Saved in background");
                        // return to the list
                        returnToListView(true);
                    } else {
                        Log.d(TAG, "Error saving in background");
                    }
                }
            });
        } else {
            // save when we can
            saying.saveEventually();
            returnToListView(false);
        }

    }

    public void returnToListView(Boolean saved) {
        Intent returnIntent = new Intent();
        if (saved) {
            setResult(RESULT_OK, returnIntent);
        } else {
            // use first user key for no save
            setResult(RESULT_FIRST_USER, returnIntent);
        }
        finish();
    }


    // DATE METHODS
    @Override
    public void getDate(TextView textView) {
        // set the text view so we can update the date after the user changes it
        mDateTextView = textView;
        // Open the date dialog for user to change the date
        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.show(getFragmentManager(), DateDialogFragment.TAG);
    }

    // SET THE DATE IN THE VIEW WHEN RETURNING FROM DATE PICKER
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Adjust the month
        int month = monthOfYear +1;// Error handling with current calendar
        // set it as a string
        String dateForView = month + "/" + dayOfMonth + "/" + year;
        // Update the text view
        mDateTextView.setText(dateForView);
    }

    private Date convertToDateObject(SimpleDateFormat sdf, String dateInString) {
        Date date = null;
        try {
            // try to parse the date string and return it
            date = sdf.parse(dateInString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
