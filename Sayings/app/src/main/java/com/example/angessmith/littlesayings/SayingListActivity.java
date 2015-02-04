package com.example.angessmith.littlesayings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angessmith.littlesayings.Fragment.DeleteSayingDialogFragment;
import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

// Created by AngeSSmith on 2/3/15.

public class SayingListActivity extends ActionBarActivity implements DeleteSayingDialogFragment.ConfirmDeleteListener {

    public static final String TAG = SayingListActivity.TAG;
    public static final int ADD_SAYING_INTENT = 62719;

    // Define the Custom Adapter for the list
    CustomSayingAdapter mAdapter;
    // define the action mode for the long click to delete the item

    private ActionMode mActionMode;
    private SayingObject mSelectedSaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saying_list);

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<SayingObject> adapter = new ParseQueryAdapter.QueryFactory<SayingObject>() {
            public ParseQuery<SayingObject> create() {
                // Search for the SayingObject class
                ParseQuery<SayingObject> query = SayingObject.getQuery();
                // put the newest ones first
                query.orderByDescending("createdAt");
                return query;
            }
        };
        // Set up the adapter
        mAdapter = new CustomSayingAdapter(this, adapter);

        // Get the list view and set the adapter to it
        ListView sayingList = (ListView) findViewById(R.id.saying_list_view);
        sayingList.setAdapter(mAdapter);

        // Prepare for an empty list
        LinearLayout emptyListLayout = (LinearLayout) findViewById(R.id.empty_list_view);
        sayingList.setEmptyView(emptyListLayout);

        // set up the list view clicks
       // Open in detail/edit view
        sayingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SayingObject editSaying = mAdapter.getItem(position);
                Log.d(TAG, "User selected to view/edit: " + editSaying.getChild());

            }
        });

        // open action mode to give the user the ability to delete this item
        sayingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(mActionMode != null){
                    // if it does, the user can interact with the one previously created
                    return false;
                }
                mSelectedSaying = mAdapter.getItem(position);
                Log.d(TAG, "User selected to delete: " + mSelectedSaying.getChild());
                // start the action mode
                mActionMode = startActionMode(DeleteActionModeCallback);

                return true;
            }
        });

        // load  any objects available for this user
        mAdapter.loadObjects();
        Integer numberOfitems = mAdapter.getCount();
        Log.d(TAG, "There should be " + numberOfitems + " items listed");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saying_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add_saying:
                // User selected plus button
                Log.i(TAG, "User wants to add a saying");
                Intent intent = new Intent(this, AddSayingActivity.class);
                // Add a request code to see if a saying was added
                startActivityForResult(intent, ADD_SAYING_INTENT);
                break;

            case R.id.action_logout_user:
                Log.i(TAG, "Log out user and return to launch");
                // log the user out
                ParseUser.logOut();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void DeleteSaying(SayingObject saying) {
        // Delete the item
        saying.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Object deleted");
                    // reload the list view
                    mAdapter.loadObjects();
                } else {
                    Log.d(TAG, "Problem deleting: " + e.getMessage());
                }
            }
        });
    }

    private class CustomSayingAdapter extends ParseQueryAdapter<SayingObject> {

        public CustomSayingAdapter(Context context, ParseQueryAdapter.QueryFactory<SayingObject> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(SayingObject saying, View view, ViewGroup viewGroup) {

            if (view == null) {
                //make one, otherwise reuse what we have
                view = View.inflate(getContext(), R.layout.saying_item, null);
            }

            // Get each saying for this user
            super.getItemView(saying, view, viewGroup);

            // Get Child's Name
            if (saying.getChild() != null) {
                //Get and set the title text
                TextView childNameView = (TextView) view.findViewById(R.id.saying_name);
                childNameView.setText(saying.getChild());
            }
            // Get Age
            if (saying.getAge() != null) {
                // Get the timestamp view
                TextView childAgeView = (TextView) view.findViewById(R.id.saying_child_age);
                // set the reformatted timestamp into the timestamp view
                Number age = saying.getAge();
                childAgeView.setText("Age " + age);
            }

            // Get saying
            if (saying.getSaying() != null) {
                TextView childSayingView = (TextView) view.findViewById(R.id.saying_saying);
                childSayingView.setText(saying.getSaying());
            }

            // Get date
            if (saying.getSayingDate() != null) {
                TextView childSayingDateView = (TextView) view.findViewById(R.id.saying_time);
                childSayingDateView.setText(saying.getSayingDate().toString());
            }

            // finally, set the object id to the view tag for when the user clicks on the cell
            view.setTag(saying.getObjectId());
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "User returned from add saying");
        // See if we need to update the list view
        if ((requestCode == ADD_SAYING_INTENT) && (resultCode == RESULT_OK)) {
            Toast.makeText(this, "Saying saved!", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Update List");
            // reload list
            mAdapter.loadObjects();
            Integer numberOfitems = mAdapter.getCount();
            Log.d(TAG, "There should be " + numberOfitems + " items listed");
        }
    }


    private ActionMode.Callback DeleteActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate the delete saying action menu
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.delete_saying, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // check which button was clicked
            if (item.getItemId() == R.id._menu_delete_saying) {
                // Confirm the user wants to delete the saying
                DeleteSayingDialogFragment deleteFragment = DeleteSayingDialogFragment.newInstance(mSelectedSaying);
                deleteFragment.show(getFragmentManager(), DeleteSayingDialogFragment.TAG);
                // close the action bar mode
                mode.finish();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // delete the action mode for reuse
            mActionMode = null;
        }

   };



}
