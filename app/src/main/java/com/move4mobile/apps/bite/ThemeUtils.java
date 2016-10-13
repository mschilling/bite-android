package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.preference.PreferenceManager;

/**
 * Created by casvd on 13-10-2016.
 */

public class ThemeUtils extends BaseObservable {

    @Bindable
    public static int accentColor;

    private SharedPreferences pref;

    private static ThemeUtils instance;

    public static ThemeUtils getInstance(Context ctx){
        if(instance == null){
            instance = new ThemeUtils(ctx);
        }
        return instance;
    }

    private ThemeUtils(Context ctx) {
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        accentColor = pref.getInt("pref_color", R.color.colorAccent_Light);
    }

    public void setAccentColor(int i){
        pref.edit().putInt("pref_color", i).apply();
        accentColor = i;
        notifyChange();
    }
}
