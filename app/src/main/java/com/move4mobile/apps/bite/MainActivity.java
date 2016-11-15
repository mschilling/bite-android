package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 8-11-2016.
 */

public class MainActivity extends AppCompatActivityFireAuth {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrders;
    private DatabaseReference mRefUserData;
    private FirebaseRecyclerAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private ImageView firebaseStatusImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updateToolbarTitle(null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_bites);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        firebaseStatusImage = (ImageView) findViewById(R.id.connection_state);

        TextViewCustom textView = (TextViewCustom) findViewById(R.id.bites_title);
        textView.setText(getString(R.string.bites, 0));

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mRefOrders = mDatabase.getReference("orders");
        mRefUserData = mDatabase.getReference("users");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bite b = new Bite(getUser().getUid(), "-JhLeOlGIEjaIOFHR0xd", System.currentTimeMillis(), System.currentTimeMillis() + 9000);
                mRefOrders.push().setValue(b);*/
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase.goOnline();
        if(adapter == null) {
            adapter = new BitesAdapter(Bite.class, R.layout.bite_card, BiteViewHolder.class, mRefOrders, getBaseContext());
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int friendlyMessageCount = adapter.getItemCount();
                    int lastVisiblePosition =
                            linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.
                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        mRecyclerView.scrollToPosition(positionStart);
                    }

                    TextViewCustom textView = (TextViewCustom) findViewById(R.id.bites_title);
                    textView.setText(getString(R.string.bites, adapter.getItemCount()));
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);

                    TextViewCustom textView = (TextViewCustom) findViewById(R.id.bites_title);
                    textView.setText(getString(R.string.bites, adapter.getItemCount()));
                }
            });
            mRecyclerView.setAdapter(adapter);
        }
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
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();

        mDatabase.goOnline();
        mRefUserData.child(getUser().getUid()).child("data").child("display_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateToolbarTitle(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }

    private void updateToolbarTitle(DataSnapshot data) {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        String username;
        if(data != null) {
            username = (String) data.getValue();
            if(username == null || username.isEmpty()) {
                username = "ANON";
            }
        } else {
            username = "ANON";
        }
        toolbarTitle.setText(getString(R.string.toolbar_title, username.toUpperCase()));
    }

    @Override
    protected void onLoggedOut() {
        mDatabase.goOffline();
        super.onLoggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onConnect() {
        super.onConnect();
        firebaseStatusImage.setImageDrawable(getDrawable(R.drawable.ic_cloud_queue_black));
    }
    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        firebaseStatusImage.setImageDrawable(getDrawable(R.drawable.ic_cloud_off_black));
    }
}
