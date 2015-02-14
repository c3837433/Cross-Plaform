package com.example.angessmith.littlesayings;


import android.app.Application;


import com.example.angessmith.littlesayings.ParseClass.SayingObject;
import com.parse.Parse;
import com.parse.ParseObject;


// Created by AngeSSmith on 2/2/15.
// *** Github Repository Link https://github.com/c3837433/Cross-Plaform


public class Launch extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // register the Parse saying subclass
        ParseObject.registerSubclass(SayingObject.class);
        // enable the local data store
        Parse.enableLocalDatastore(getApplicationContext());
        // Initialize this app with Parse
        Parse.initialize(this, "N2wRnfGDdmtURsVA4IZfgXU5UdjREkHbQepbwxBv", "kUzQRnQ1mpbyVJ8rLnjbfUYUnIHYqTotnjstXcxu");
    }


}


