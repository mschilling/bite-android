package com.move4mobile.apps.bite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;
import com.move4mobile.apps.bite.objects.Bite;
import com.move4mobile.apps.bite.objects.MenuItem;
import com.move4mobile.apps.bite.objects.Store;
import com.move4mobile.apps.bite.objects.User;
import com.move4mobile.apps.bite.objects.UserOrder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by casvd on 15-11-2016.
 */

public class RestaurantActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "RestaurantActivity";

    private String key;

    private TextViewCustom textViewCustomToolbarText;
    private LinearLayout layoutEmojiList;
    private ImageView imageViewStartedBy;
    private TextViewCustom textViewCustomStartedBy;
    private RecyclerView mRecyclerView;
    private RecyclerView mUserOrderRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager userOrderLinearLayoutManager;
    private SlidingUpPanelLayout mSlidingPanelLayout;
    private LinearLayout mEmptyOrder;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrder;
    private DatabaseReference mRefStore;
    private DatabaseReference mRefProducts;
    private DatabaseReference mRefUserData;
    private DatabaseReference mRefUserOrder;

    private ValueEventListener orderListener;
    private ValueEventListener storeListener;
    private ValueEventListener userListener;

    private MenuAdapter adapter;
    private UserOrderAdapter userOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if(key == null || key.isEmpty()) finish();
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_restaurant);

        textViewCustomToolbarText = (TextViewCustom) findViewById(R.id.toolbar_text);
        layoutEmojiList = (LinearLayout) findViewById(R.id.bite_card_restaurant_emoji_list);
        imageViewStartedBy = (ImageView) findViewById(R.id.bite_card_started_by_image);
        textViewCustomStartedBy = (TextViewCustom) findViewById(R.id.bite_card_started_by);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_menu);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mUserOrderRecyclerView = (RecyclerView) findViewById(R.id.user_order_recyclerview);
        mUserOrderRecyclerView.setHasFixedSize(false);
        mUserOrderRecyclerView.setNestedScrollingEnabled(false);
        userOrderLinearLayoutManager = new LinearLayoutManager(this);
        mUserOrderRecyclerView.setLayoutManager(userOrderLinearLayoutManager);
        mSlidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mEmptyOrder = (LinearLayout) findViewById(R.id.empty_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();

        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mRefStore != null) mRefStore.removeEventListener(storeListener);
                if(mRefUserData != null) mRefUserData.removeEventListener(userListener);

                Bite bite = dataSnapshot.getValue(Bite.class);
                if(bite != null) {
                    mRefStore = mDatabase.getReference("stores").child(bite.getStore());
                    mRefProducts = mDatabase.getReference("products").child(bite.getStore()).child("products");
                    if (adapter == null) {
                        adapter = new MenuAdapter(MenuItem.class, R.layout.card_view_menu_item, MenuItemViewHolder.class, mRefProducts, RestaurantActivity.this, getUser(), key);
                        mRecyclerView.setAdapter(adapter);
                    }
                    mRefUserData = mDatabase.getReference("users").child(bite.getOpenedBy());
                    mRefUserOrder = mDatabase.getReference("user_order").child(dataSnapshot.getKey()).child(getUser().getUid());
                    if (userOrderAdapter == null) {
                        userOrderAdapter = new UserOrderAdapter(UserOrder.class, R.layout.card_view_menu_item, MenuItemViewHolder.class, mRefUserOrder, RestaurantActivity.this, bite.getStore());
                        userOrderAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                super.onItemRangeInserted(positionStart, itemCount);
                                updateOrderView(userOrderAdapter.getItemCount());
                            }

                            @Override
                            public void onItemRangeRemoved(int positionStart, int itemCount) {
                                super.onItemRangeRemoved(positionStart, itemCount);
                                userOrderAdapter.notifyDataSetChanged();
                                updateOrderView(userOrderAdapter.getItemCount());
                            }
                        });
                        mUserOrderRecyclerView.setAdapter(userOrderAdapter);
                    }

                    mRefStore.addValueEventListener(storeListener);
                    mRefUserData.addValueEventListener(userListener);
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
        storeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());
                Store store = dataSnapshot.getValue(Store.class);
                if(store != null) {
                    textViewCustomToolbarText.setText(store.getName());

                    if(store.getCategories() != null && store.getCategories().size() > 0) {
                        layoutEmojiList.removeAllViews();
                        for (HashMap<String, String> category: store.getCategories().values()) {
                            ImageView emo = new ImageView(RestaurantActivity.this);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                    (int)getResources().getDimension(R.dimen.bite_card_restaurant_emoji_size),
                                    (int)getResources().getDimension(R.dimen.bite_card_restaurant_emoji_size));
                            llp.setMargins((int)getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                                    0,
                                    (int)getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                                    0);
                            emo.setLayoutParams(llp);
                            emo.setImageDrawable(BiteApplication.Emojis.getEmoji(Integer.valueOf(category.get("type"))));
                            layoutEmojiList.addView(emo);
                        }
                    } else {
                        layoutEmojiList.removeAllViews();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    textViewCustomStartedBy.setText(String.format("GESTART DOOR %s", user.getDisplayName().toUpperCase(Locale.getDefault())));
                    Glide.with(getApplicationContext()).load(user.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.ic_shipit)
                            .into(new BitmapImageViewTarget(imageViewStartedBy) {

                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imageViewStartedBy.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
    }

    private void updateOrderView(int count) {
        if(count == 0) {
            mUserOrderRecyclerView.setVisibility(View.GONE);
            mEmptyOrder.setVisibility(View.VISIBLE);
        } else {
            mUserOrderRecyclerView.setVisibility(View.VISIBLE);
            mEmptyOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
        mRefOrder = mDatabase.getReference("orders").child(key);
        mRefOrder.addValueEventListener(orderListener);
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
        if(mRefOrder != null) mRefOrder.removeEventListener(orderListener);
        if(mRefStore != null) mRefStore.removeEventListener(storeListener);
        if(mRefUserData != null) mRefUserData.removeEventListener(userListener);
    }
}
