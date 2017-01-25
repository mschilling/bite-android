package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.move4mobile.apps.bite.objects.Store;

public class OpenActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "OpenActivity";

    private String key;

    private RelativeLayout mOpen;
    private RecyclerView mRecyclerViewStores;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefStores;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOpen = (RelativeLayout) findViewById(R.id.bite_open_button);
        mRecyclerViewStores = (RecyclerView) findViewById(R.id.recycler_view_stores);
        mRecyclerViewStores.setHasFixedSize(false);
        mRecyclerViewStores.setNestedScrollingEnabled(false);
        mRecyclerViewStores.setLayoutManager(new LinearLayoutManager(this));

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mRefStores = mDatabase.getReference("stores");
        if(adapter == null) {
            adapter = new StoreAdapter(Store.class, R.layout.bite_card_open, StoreViewHolder.class, mRefStores, OpenActivity.this);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    Log.e(TAG, "Removed at " + positionStart + "|" + itemCount);
                    View v = mRecyclerViewStores.getChildAt(positionStart);
                    Log.e(TAG, "Is it checked: " + ((RadioButton)v.findViewById(R.id.bite_open_radio)).isChecked());
                    notifySelectStore("", -1);
                    super.onItemRangeRemoved(positionStart, itemCount);
                    adapter.notifyDataSetChanged();
                }
            });
            mRecyclerViewStores.setAdapter(adapter);
        }
    }

    @Override
    protected void onLoggedOut() {
        super.onLoggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected boolean notifySelectStore(String key, int position) {
        this.key = key;
        for (int i = 0; i < mRecyclerViewStores.getChildCount(); i++) {
            if(i != position){
                View v = mRecyclerViewStores.getChildAt(i);
                ((RadioButton)v.findViewById(R.id.bite_open_radio)).setChecked(false);
            }
        }
        updateButton();
        return true;
    }

    private void updateButton() {
        mOpen.setBackground(getDrawable(key == null || key.isEmpty() ? R.drawable.bring_it_button_locked : R.drawable.bring_it_button));
    }
}
