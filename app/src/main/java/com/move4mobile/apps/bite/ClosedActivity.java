package com.move4mobile.apps.bite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;
import com.move4mobile.apps.bite.objects.ArchiveProduct;
import com.move4mobile.apps.bite.objects.ArchiveProductTotal;
import com.move4mobile.apps.bite.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClosedActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "ClosedActivity";

    private String key;
    private boolean you;

    private TextViewCustom textViewCustomToolbarText;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewTotal;
    private RelativeLayout mButtonYou;
    private RelativeLayout mButtonTotal;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserOrderData;
    private DatabaseReference mRefOrderTotalData;
    private DatabaseReference mRefStore;
    private ValueEventListener storeListener;
    private ValueEventListener productsListener;
    private ArchiveProductAdapter adapter;
    private ArchiveProductTotalAdapter adapterTotal;

    private HashMap<String, ArchiveProductTotal> totalProducts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if (key == null || key.isEmpty()) finish();

        setContentView(R.layout.activity_closed);
        updateOrderPrice(0);
        updateOrderAmount(0);
        updateTotalOrderPrice(0);
        updateTotalOrderAmount(0, 0);

        textViewCustomToolbarText = (TextViewCustom) findViewById(R.id.toolbar_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_archive_user_order);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewTotal = (RecyclerView) findViewById(R.id.recycler_view_archive_user_order_total);
        mRecyclerViewTotal.setHasFixedSize(false);
        mRecyclerViewTotal.setNestedScrollingEnabled(false);
        mRecyclerViewTotal.setLayoutManager(new LinearLayoutManager(this));
        mButtonYou = (RelativeLayout) findViewById(R.id.bite_order_button_you);
        mButtonYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setYou(true);
            }
        });
        mButtonTotal = (RelativeLayout) findViewById(R.id.bite_order_button_total);
        mButtonTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setYou(false);
            }
        });
        setYou(true);

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
                if (storeName != null && !storeName.isEmpty()) {
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
                    if (product != null) {
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

    private void updateTotalOrderPrice(long price) {
        TextViewCustom totalOrderPrice = (TextViewCustom) findViewById(R.id.total_order_price_total);
        totalOrderPrice.setText(getString(R.string.order_items_price, price));
    }

    private void updateTotalOrderAmount(long orders, long products) {
        TextViewCustom totalOrderCount = (TextViewCustom) findViewById(R.id.total_order_total);
        totalOrderCount.setText(getString(R.string.order_items_total, orders, products));
    }

    public void setYou(boolean you) {
        this.you = you;
        LinearLayout linearLayoutYou = (LinearLayout) findViewById(R.id.bite_closed_layout_you);
        LinearLayout linearLayoutTotal = (LinearLayout) findViewById(R.id.bite_closed_layout_total);
        if (this.you) {
            linearLayoutYou.setVisibility(View.VISIBLE);
            linearLayoutTotal.setVisibility(View.GONE);
            mButtonYou.setSelected(true);
            mButtonTotal.setSelected(false);
        } else {
            linearLayoutYou.setVisibility(View.GONE);
            linearLayoutTotal.setVisibility(View.VISIBLE);
            mButtonYou.setSelected(false);
            mButtonTotal.setSelected(true);
        }
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mRefStore = mDatabase.getReference("archive").child("order_items_per_user").child(getUser().getUid()).child(key).child("store");
        mRefStore.addValueEventListener(storeListener);

        mRefUserOrderData = mDatabase.getReference("archive").child("order_items_per_user").child(getUser().getUid()).child(key).child("products");
        if (adapter == null) {
            adapter = new ArchiveProductAdapter(ArchiveProduct.class, R.layout.card_view_menu_item_closed, ArchiveProductViewHolder.class, mRefUserOrderData, this);
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

        mRefOrderTotalData = mDatabase.getReference("archive").child("order_items_per_order").child(key);
        if (adapterTotal == null) {
            mRefOrderTotalData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalProducts = new HashMap<>();
                    final long totalOrders = dataSnapshot.getChildrenCount();
                    final long[] totalOrderProducts = {0};
                    final long[] totalOrderPrice = {0};
                    for (final DataSnapshot userOrder : dataSnapshot.getChildren()) {
                        final String userKey = userOrder.getKey();
                        mDatabase.getReference("users").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    for (DataSnapshot item : userOrder.getChildren()) {
                                        ArchiveProduct product = item.getValue(ArchiveProduct.class);
                                        if (product != null) {
                                            totalOrderProducts[0] += product.getAmount();
                                            totalOrderPrice[0] += product.getAmount() * product.getPrice();
                                            if (totalProducts.containsKey(item.getKey())) {
                                                //Exist Update stuff
                                                totalProducts.get(item.getKey()).updateProduct(product);
                                                totalProducts.get(item.getKey()).addUser(user);
                                            } else {
                                                //New
                                                totalProducts.put(item.getKey(), new ArchiveProductTotal(product, new HashSet<User>() {{
                                                    add(user);
                                                }}));
                                            }
                                            updateTotalOrderPrice(totalOrderPrice[0]);
                                            updateTotalOrderAmount(totalOrders, totalOrderProducts[0]);
                                            updateTotalList();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, databaseError.toString());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });
        }
    }

    private void updateTotalList() {
        adapterTotal = new ArchiveProductTotalAdapter(new ArrayList<>(totalProducts.values()), this);
        mRecyclerViewTotal.setAdapter(adapterTotal);
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
