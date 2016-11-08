package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by casvd on 8-11-2016.
 */

public class MainActivity extends AppCompatActivityFireAuth {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrders;
    private FirebaseRecyclerAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_bites);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mRefOrders = mDatabase.getReference("orders");
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase.goOnline();
        if(adapter == null) {
            adapter = new BitesAdapter(Bite.class, R.layout.bite_card, BiteViewHolder.class, mRefOrders);
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

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        String displayName = getUser().getDisplayName();
        String firstName = null;
        if (displayName != null) {
            firstName = displayName.split("\\s+")[0];
        }
        if (firstName != null) {
            toolbarTitle.setText(getString(R.string.toolbar_title, firstName.toUpperCase()));
        } else {
            toolbarTitle.setText(getString(R.string.toolbar_title, "ANON"));
        }
    }

    @Override
    protected void onLoggedOut() {
        mDatabase.goOffline();
        super.onLoggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
