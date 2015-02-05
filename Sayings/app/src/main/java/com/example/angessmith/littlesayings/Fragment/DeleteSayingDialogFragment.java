package com.example.angessmith.littlesayings.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.angessmith.littlesayings.ParseClass.SayingObject;

// Created by AngeSSmith on 2/3/15.
// *** Github Repository Link https://github.com/c3837433/Cross-Plaform


public class DeleteSayingDialogFragment extends DialogFragment {

    public static final String TAG = "DeleteSayingDialogFragment.TAG";
    SayingObject mSelectedsaying;

    // Define an interface so we know when the user confirms to delete the selected saying
    public interface ConfirmDeleteListener {
        public void DeleteSaying(SayingObject saying);
    }

    private ConfirmDeleteListener mListener;

    // Create new instance of dialog
    public static DeleteSayingDialogFragment newInstance(SayingObject saying) {
        DeleteSayingDialogFragment fragment = new DeleteSayingDialogFragment();

        // save the saying selected to delete
        fragment.mSelectedsaying = saying;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String childName = mSelectedsaying.getChild();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete " + childName + "'s saying?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // delete the item
                        mListener.DeleteSaying(mSelectedsaying);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, nothing to do
                    }
                });
        // start the builder
        return builder.create();
    }

    // Attach the listener to the dialog
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ConfirmDeleteListener) activity;
    }

}
