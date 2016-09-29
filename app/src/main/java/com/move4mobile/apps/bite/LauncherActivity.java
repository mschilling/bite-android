package com.move4mobile.apps.bite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme = PreferenceManager.getDefaultSharedPreferences(this).getInt("pref_theme", R.style.AppTheme_Light);
        if(theme == R.style.AppTheme_Light){
            setTheme(R.style.AppTheme_Launcher_Light);
        } else {
            setTheme(R.style.AppTheme_Launcher);
        }


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
