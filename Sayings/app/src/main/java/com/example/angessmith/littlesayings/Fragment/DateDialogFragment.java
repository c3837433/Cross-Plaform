package com.example.angessmith.littlesayings.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

// Created by AngeSSmith on 2/2/15.

public class DateDialogFragment extends DialogFragment {

    // Create the tag
    public static final String TAG = "DateDialogFragment.TAG";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // set the current date in the calendar
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // return the instance to the activity so we can access the selected date
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }

}
