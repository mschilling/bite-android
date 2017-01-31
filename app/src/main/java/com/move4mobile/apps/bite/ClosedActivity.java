package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;
import com.move4mobile.apps.bite.objects.ArchiveProduct;

public class ClosedActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "ClosedActivity";

    private String key;

    private TextViewCustom textViewCustomToolbarText;
    private RecyclerView mRecyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserOrderData;
    private DatabaseReference mRefStore;
    private ValueEventListener storeListener;
    private ValueEventListener productsListener;
    private ArchiveProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if (key == null || key.isEmpty()) finish();

        setContentView(R.layout.activity_closed);
        updateOrderPrice(0);
        updateOrderAmount(0);

        textViewCustomToolbarText = (TextViewCustom) findViewById(R.id.toolbar_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_archive_user_order);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        storeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if(storeName != null && !storeName.isEmpty()) {
                    textViewCustomToolbarText.setText(storeName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
        productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int amount = 0;
                long price = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArchiveProduct product = child.getValue(ArchiveProduct.class);
                    if(product != null) {
                        amount += product.getAmount();
                        price += product.getAmount() * product.getPrice();
                    }
                }
                updateOrderAmount(amount);
                updateOrderPrice(price);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
    }

    private void updateOrderPrice(long price) {
        TextViewCustom orderPrice = (TextViewCustom) findViewById(R.id.total_order_price);
        orderPrice.setText(getString(R.string.order_items_price, price));
    }

    private void updateOrderAmount(int count) {
        TextViewCustom orderCount = (TextViewCustom) findViewById(R.id.total_order_count);
        orderCount.setText(getString(R.string.order_items_count, count));
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mRefStore = mDatabase.getReference("archive").child("order_items_per_user").child(getUser().getUid()).child(key).child("store");
        mRefStore.addValueEventListener(storeListener);

        mRefUserOrderData = mDatabase.getReference("archive").child("order_items_per_user").child(getUser().getUid()).child(key).child("products");
        if(adapter == null) {
            adapter = new ArchiveProductAdapter(ArchiveProduct.class, R.layout.card_view_menu_item_closed, ArchiveProductViewHolder.class, mRefUserOrderData);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    adapter.notifyDataSetChanged();
                }
            });
            mRecyclerView.setAdapter(adapter);
        }
        mRefUserOrderData.addValueEventListener(productsListener);
    }

    @Override
    protected void onLoggedOut() {
        super.onLoggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRefStore != null) mRefStore.removeEventListener(storeListener);
        if (mRefUserOrderData != null) mRefUserOrderData.removeEventListener(productsListener);
    }
}
