package com.example.angessmith.littlesayings;


import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.example.angessmith.littlesayings.Fragment.AddSayingFragment;
import com.example.angessmith.littlesayings.Fragment.DateDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddSayingActivity extends ActionBarActivity implements AddSayingFragment.AddSayingButtonListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = "AddSayingActivity.TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saying);

        // set up the add saying fragment
        AddSayingFragment addSayingFragment = AddSayingFragment.newInstance();
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
        String dateInString = dayOfMonth + "-" + monthOfYear + "-" + year;
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Date set: " + date);

    }


    // ADD SAYING BUTTON INTERFACE LISTENERS
    @Override
    public void gatherEnteredData(String name, String age, String saying, String date) {
        Log.d(TAG, "User entered name: " + name + " age: " + age + " saying: " + saying + " on date: " + date);

    }

    @Override
    public void getDate() {
        // Open the date dialog for user to change the date
        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.show(getFragmentManager(), DateDialogFragment.TAG);
    }
}
