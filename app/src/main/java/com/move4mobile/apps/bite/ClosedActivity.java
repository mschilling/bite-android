package com.move4mobile.apps.bite;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClosedActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "ClosedActivity";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
