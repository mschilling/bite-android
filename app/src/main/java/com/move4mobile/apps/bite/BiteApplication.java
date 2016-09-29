package com.move4mobile.apps.bite;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by casvd on 28-9-2016.
 */

public class BiteApplication extends Application {

    private static final String TAG = ".BiteApplication";

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseAnalytics.getInstance(this);
    }

}
