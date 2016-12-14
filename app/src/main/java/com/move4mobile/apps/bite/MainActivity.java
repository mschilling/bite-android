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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;
import com.move4mobile.apps.bite.objects.Bite;

import java.util.Locale;

/**
 * Created by casvd on 8-11-2016.
 */

public class MainActivity extends AppCompatActivityFireAuth {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private LinearLayout mEmptyBites;
    private ProgressBar mProgressBar;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrders;
    private DatabaseReference mRefUserData;
    private FirebaseRecyclerAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private ImageView firebaseStatusImage;

    private FloatingActionButton fab;

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
        mEmptyBites = (LinearLayout) findViewById(R.id.empty_bites);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        firebaseStatusImage = (ImageView) findViewById(R.id.connection_state);

        TextViewCustom textView = (TextViewCustom) findViewById(R.id.bites_title);
        textView.setText(getString(R.string.bites, 0));

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mRefOrders = mDatabase.getReference("orders");
        mRefUserData = mDatabase.getReference("users");

        fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bite b = new Bite(getUser().getUid(), "-JhLeOlGIEjaIOFHR0xd", System.currentTimeMillis(), System.currentTimeMillis() + 9000);
                mRefOrders.push().setValue(b);
            }
        });

        firebaseStatusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                Toast.makeText(MainActivity.this, "Subscribed to News", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Subscribed to News");
            }
        });
        firebaseStatusImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, BuildConfig.VERSION_CODE + " | " + BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
                Log.d(TAG, BuildConfig.VERSION_CODE + " | " + BuildConfig.VERSION_NAME);
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase.goOnline();
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
        mRefUserData = mRefUserData.child(getUser().getUid());
        mRefUserData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userChildEventHandler(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                userChildEventHandler(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                userChildEventHandler(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                userChildEventHandler(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(adapter == null) {
            adapter = new BitesAdapter(Bite.class, R.layout.bite_card, BiteViewHolder.class, mRefOrders, getBaseContext(), getUser());
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
                    adapter.notifyDataSetChanged();
                    updateBitesList(adapter.getItemCount());
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    adapter.notifyDataSetChanged();
                    updateBitesList(adapter.getItemCount());
                }
            });
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void updateBitesList(int count) {
        TextViewCustom textView = (TextViewCustom) findViewById(R.id.bites_title);
        textView.setText(getString(R.string.bites, count));
        if(count == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyBites.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyBites.setVisibility(View.GONE);
        }
    }

    private void userChildEventHandler(DataSnapshot data, String s){
        switch (data.getKey()) {
            case "display_name":
                updateToolbarTitle(data);
                break;
            case "admin":
                updateAddBiteFAB(data);
                break;
            default:
                break;
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void userChildEventHandler(DataSnapshot data){
        userChildEventHandler(data, "");
    }

    private void updateAddBiteFAB(DataSnapshot data){
        if(data.getValue() == null) return;
        if(data.getValue(Boolean.class)) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
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
        toolbarTitle.setText(getString(R.string.toolbar_title, username.toUpperCase(Locale.getDefault())));
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
