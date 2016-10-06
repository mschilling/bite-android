package com.move4mobile.apps.bite;

import android.app.Application;
import android.graphics.Typeface;

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

        initFonts();
    }

    public static class Fonts{
        public static Typeface COMPASSE, COMPASSE_EXTRA_BOLD_ITALIC;
    }

    private void initFonts(){
        Fonts.COMPASSE = Typeface.createFromAsset(getAssets(), "fonts/Compasse.otf");
        Fonts.COMPASSE_EXTRA_BOLD_ITALIC = Typeface.createFromAsset(getAssets(), "fonts/Compasse-ExtraBoldItalic.otf");
    }

}
