package com.move4mobile.apps.bite;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.move4mobile.apps.bite.dialogs.UpdateDialog;

import java.util.Objects;

/**
 * Created by casvd on 28-9-2016.
 */

public class BiteApplication extends Application {

    private static final String TAG = ".BiteApplication";

    private FirebaseDatabase database;
    private DatabaseReference androidVersionRef;

    private static int versionCode;
    private static String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAnalytics.getInstance(this);
        database = FirebaseDatabase.getInstance();
        androidVersionRef = database.getReference("android_app");

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

        initFonts();

        androidVersionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setAppVersion(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setAppVersion(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                setAppVersion(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                setAppVersion(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Failed to read
            }
        });
    }

    public static class Fonts {
        public static Typeface COMPASSE, COMPASSE_BOLD, COMPASSE_EXTRA_BOLD, COMPASSE_EXTRA_BOLD_ITALIC;

        public static Typeface getFont(String asset){
            switch (asset) {
                case "compasse":
                    return COMPASSE;
                case "compasse_bold":
                    return COMPASSE_BOLD;
                case "compasse_extra_bold":
                    return COMPASSE_EXTRA_BOLD;
                case "compasse_extra_bold_italic":
                    return COMPASSE_EXTRA_BOLD_ITALIC;
                default:
                    return COMPASSE;
            }
        }
    }

    private void initFonts() {
        Fonts.COMPASSE = Typeface.createFromAsset(getAssets(), "fonts/Compasse.otf");
        Fonts.COMPASSE_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Compasse-Bold.otf");
        Fonts.COMPASSE_EXTRA_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Compasse-ExtraBold.otf");
        Fonts.COMPASSE_EXTRA_BOLD_ITALIC = Typeface.createFromAsset(getAssets(), "fonts/Compasse-ExtraBoldItalic.otf");
    }

    private void setAppVersion(DataSnapshot dataSnapshot) {
        Log.d(TAG, dataSnapshot.toString());
        if (Objects.equals(dataSnapshot.getKey(), "version_code")) {
            if(versionCode < ((Long)dataSnapshot.getValue()).intValue()){
                versionCode = ((Long)dataSnapshot.getValue()).intValue();
                checkUpdate();
            }
        } else if (Objects.equals(dataSnapshot.getKey(), "version_name")) {
            //myVersionName = (String) dataSnapshot.getValue();
            switch (versionName.compareToIgnoreCase((String)dataSnapshot.getValue())) {
                case 1:
                    //Log.e(TAG, "Smaller");
                    //Can you timetravel?
                    //You have a newer version than that exists in today's world...
                    versionName = (String)dataSnapshot.getValue();
                    break;
                case 0:
                    //Log.e(TAG, "Equals");
                    //Current version
                    break;
                case -1:
                    //Log.e(TAG, "Bigger");
                    //Newer version
                    versionName = (String)dataSnapshot.getValue();
                    checkUpdate();
                    break;
                default:
                    //Log.e(TAG, "WHAT");
                    //Way newer version
                    //Yer way behind
                    versionName = (String)dataSnapshot.getValue();
                    checkUpdate();
                    break;
            }
        }
    }

    private void checkUpdate() {
        if(versionCode > BuildConfig.VERSION_CODE && !versionName.equalsIgnoreCase(BuildConfig.VERSION_NAME)){
            showUpdateMessage();
        }
    }

    private void showUpdateMessage() {
        if(UpdateDialog.isActive()){
            UpdateDialog updateDialog = UpdateDialog.getInstance();
            updateDialog.showUpdateMessage();
        } else {
            Intent intent = new Intent(this, UpdateDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    protected static int getNewVersionCode() {
        return versionCode;
    }
    public static String getNewVersionName() {
        return versionName;
    }

}
