package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;
import com.move4mobile.apps.bite.dialogs.UsernameDialog;
import com.move4mobile.apps.bite.objects.ArchiveUserOrder;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivityFireAuth{

    private static final String TAG = "ProfileActivity";

    private RecyclerView mRecyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserData;
    private DatabaseReference mRefArchiveUserOrder;
    private ArchiveBitesAdapter adapter;
    private ChildEventListener listener;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UsernameDialog.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_archive);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateToolbarTitle(null);
        updateBitesList(0);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mRefUserData = mDatabase.getReference("users");
        mRefArchiveUserOrder = mDatabase.getReference("archive").child("order_items_per_user");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void userChildEventHandler(DataSnapshot data){
        userChildEventHandler(data, "");
    }

    private void userChildEventHandler(DataSnapshot data, String s){
        switch (data.getKey()) {
            case "display_name":
                updateToolbarTitle(data);
                break;
            default:
                break;
        }
    }

    private void updateToolbarTitle(DataSnapshot data) {
        String username;
        if(data != null) {
            username = (String) data.getValue();
            if(username == null || username.isEmpty()) {
                username = "ANON";
            }
        } else {
            username = "ANON";
        }
        toolbarTitle.setText(getString(R.string.toolbar_profile, username.toUpperCase(Locale.getDefault())));
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mDatabase.goOnline();

        mRefUserData = mRefUserData.child(getUser().getUid());
        listener = mRefUserData.addChildEventListener(new ChildEventListener() {
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
                Log.e(TAG, databaseError.toString());
            }
        });

        mRefArchiveUserOrder = mRefArchiveUserOrder.child(getUser().getUid());
        if(adapter == null) {
            adapter = new ArchiveBitesAdapter(ArchiveUserOrder.class, R.layout.bite_card_closed, ArchiveBitesViewHolder.class, mRefArchiveUserOrder, this);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
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
        TextViewCustom textView = (TextViewCustom) findViewById(R.id.closed_bites_title);
        textView.setText(getString(R.string.closed_bites, count));
        /*if(count == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRefUserData != null) mRefUserData.removeEventListener(listener);
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
