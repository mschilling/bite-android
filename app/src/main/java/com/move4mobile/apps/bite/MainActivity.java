package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ".MainActivity";
    private FirebaseAnalytics mFirebaseAnalytics;

    private static Toast toast;

    private boolean isDarkThemeUnlocked = false;
    private static int unlockNumber = 10;

    private int nCards = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isDarkThemeUnlocked = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme_enabled", false);

        int theme;
        if(isDarkThemeUnlocked){
            theme = PreferenceManager.getDefaultSharedPreferences(this).getInt("pref_theme", R.style.AppTheme_Light);
        } else {
            theme = R.style.AppTheme_Light;
        }
        setTheme(theme);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            nCards = savedInstanceState.getInt("Cards");

            for (int i = 0; i < nCards; i++){
                LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout ll = (LinearLayout) findViewById(R.id.nested_scroll_view_layout);
                View cardView = vi.inflate(R.layout.bite_card, ll, false);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                        startActivity(intent);
                    }
                });
                ll.addView(cardView);
            }
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout ll = (LinearLayout) findViewById(R.id.nested_scroll_view_layout);
                View cardView = vi.inflate(R.layout.bite_card, ll, false);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                        startActivity(intent);
                    }
                });
                ll.addView(cardView);
                nCards++;
                Snackbar.make(findViewById(R.id.coordinator_layout), "Bite added...", Snackbar.LENGTH_SHORT).show();
            }
        });


        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(isDarkThemeUnlocked){
                    int theme = pref.getInt("pref_theme", R.style.AppTheme_Light);
                    if(theme == R.style.AppTheme){
                        pref.edit().putInt("pref_theme", R.style.AppTheme_Light).apply();
                    } else {
                        pref.edit().putInt("pref_theme", R.style.AppTheme).apply();
                    }
                    recreate();
                } else {
                    unlockNumber--;
                    if(unlockNumber == 0) {
                        pref.edit().putBoolean("pref_dark_theme_enabled", true).apply();

                        if(toast != null) toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Dark Theme unlocked !", Toast.LENGTH_SHORT);
                        toast.show();
                        isDarkThemeUnlocked = true;
                    }else if(unlockNumber <= 5) {
                        if(toast != null) toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Press " + unlockNumber + " more times to unlock Dark Theme", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_account:
                Snackbar.make(findViewById(R.id.coordinator_layout), "Account stuff", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("Cards", nCards);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
