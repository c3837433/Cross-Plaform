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
import android.widget.Toast;

import com.example.angessmith.littlesayings.Fragment.AddSayingFragment;
import com.example.angessmith.littlesayings.Fragment.DateDialogFragment;
import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddSayingActivity extends ActionBarActivity implements AddSayingFragment.AddSayingButtonListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = "AddSayingActivity.TAG";
    public TextView mDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saying);

        String objectID = null;
        // if this is an edit of a current item,get the id and change the title bar
        if (getIntent().hasExtra("SayingId")) {
            objectID = getIntent().getExtras().getString("SayingId");
            // update the action bar
            getSupportActionBar().setTitle("Edit Saying");
        }

        // set up the add saying fragment
        AddSayingFragment addSayingFragment = AddSayingFragment.newInstance(objectID);
        getFragmentManager().beginTransaction().replace(R.id.add_saying_container, addSayingFragment, AddSayingFragment.TAG).commit();


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


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        int month = monthOfYear +1;// Error handling with current calendar
        String dateInString = dayOfMonth + "-" + month + "-" + year;
        String dateForView = month + "/" + dayOfMonth + "/" + year;
        Date date = convertToDateObject(sdf, dateInString);
        Log.d(TAG, "Date set: " + date);
        // Update the text view
        mDateTextView.setText(dateForView);
    }

    private Date convertToDateObject(SimpleDateFormat sdf, String dateInString) {
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    // ADD SAYING BUTTON INTERFACE LISTENERS
    @Override
    public void gatherEnteredData(String sayingId, final String name, final Integer age, final String saying, final String date) {
        Log.d(TAG, "User entered name: " + name + " age: " + age + " saying: " + saying + " on date: " + date);

        // Make sure we at least have a saying
        if (saying.length() != 0) {
            if (sayingId != null) {
                // this is old, search for it
                ParseQuery<SayingObject> query = SayingObject.getQuery();
                // get the saying
                query.getInBackground(sayingId, new GetCallback<SayingObject>() {
                    public void done(SayingObject sayingObject, ParseException e) {
                        if (e == null) {
                            Log.d(TAG, "Retrieved object for update");
                            saveSaying(sayingObject, name, saying, age, date);
                        }
                    }
                });
            } else  {
                // Save what we have from the user
                SayingObject newSaying = new SayingObject();
                Log.d(TAG, "Preparing new saying");
                newSaying.put("Parent",ParseUser.getCurrentUser());
                // set the permission
                newSaying.setACL(new ParseACL(ParseUser.getCurrentUser()));
                saveSaying(newSaying, name, saying, age, date);

            }

        } else {
            // No saying
            toastUser("Whoops! We need a saying to save.");
        }

    }

    public void saveSaying(SayingObject thisSaying, String name, String saying, Integer age, String date ) {
        Log.d(TAG, "Saving object");
        thisSaying.setSaying(saying);
        thisSaying.setAge(age);
        thisSaying.setChild(name);
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        Date newDate = convertToDateObject(sdf, date);
        thisSaying.setDate(newDate);
        // Save this saying
        thisSaying.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // return to the list view
                    Log.d(TAG, "Saying saved!");
                    returnToListView();
                } else {
                    Log.d(TAG, "Problem saving: " + e.getCode());
                    toastUser("We had a problem saving this saying. Please try again later.");
                }

            }
        });

    }
    @Override
    public void getDate(TextView textView) {
        // set the text view so we can update the date after the user changes it
        mDateTextView = textView;
        // Open the date dialog for user to change the date
        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.show(getFragmentManager(), DateDialogFragment.TAG);
    }

    public void toastUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void returnToListView() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
