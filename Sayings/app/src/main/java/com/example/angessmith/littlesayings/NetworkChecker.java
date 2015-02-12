package com.example.angessmith.littlesayings;

// Created by AngeSSmith on 2/2/15.
// *** Github Repository Link https://github.com/c3837433/Cross-Plaform

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {

    Context mContext;

    // Create the checker class
    public NetworkChecker (Context _context) {
        this.mContext = _context;
    }

    // See if the user has a working internet connection for Parse
    public  boolean networkAvailable ( ) {
        ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Access the current network state
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // return if have access and are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean networkAvailability (Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Access the current network state
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // return if have access and are connected
        return networkInfo != null && networkInfo.isConnected();
    }

}
