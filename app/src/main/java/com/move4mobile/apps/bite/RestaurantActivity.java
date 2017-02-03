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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by casvd on 15-11-2016.
 */

public class RestaurantActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "RestaurantActivity";

    private String key;
    private boolean locked = true;
    private boolean hasOrder;

    private TextViewCustom textViewCustomToolbarText;
    private LinearLayout layoutEmojiList;
    private ImageView imageViewStartedBy;
    private TextViewCustom textViewCustomStartedBy;
    private TextViewCustom textViewCustomStartClosetime;
    private RecyclerView mRecyclerView;
    private RecyclerView mUserOrderRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager userOrderLinearLayoutManager;
    private SlidingUpPanelLayout mSlidingPanelLayout;
    private LinearLayout mEmptyOrder;
    private RelativeLayout mBringItOn;
    private TextViewCustom mBringItOnText;

    private RecyclerView.OnItemTouchListener onItemTouchListener;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrder;
    private DatabaseReference mRefStore;
    private DatabaseReference mRefProducts;
    private DatabaseReference mRefUserData;
    private DatabaseReference mRefUserOrder;
    private DatabaseReference mRefUserOrderLocked;

    private ValueEventListener orderListener;
    private ValueEventListener storeListener;
    private ValueEventListener userListener;
    private ValueEventListener userOrderListener;
    private ValueEventListener userOrderLockedListener;

    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerAdapter userOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if (key == null || key.isEmpty()) finish();
        //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_restaurant);

        textViewCustomToolbarText = (TextViewCustom) findViewById(R.id.toolbar_text);
        layoutEmojiList = (LinearLayout) findViewById(R.id.bite_card_restaurant_emoji_list);
        imageViewStartedBy = (ImageView) findViewById(R.id.bite_card_started_by_image);
        textViewCustomStartedBy = (TextViewCustom) findViewById(R.id.bite_card_started_by);
        textViewCustomStartClosetime = (TextViewCustom) findViewById(R.id.bite_card_start_close_time);
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
        mBringItOn = (RelativeLayout) findViewById(R.id.bite_bring_it_on);
        mBringItOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasOrder)
                    mRefUserOrderLocked.setValue(locked ? null : true);
            }
        });
        mBringItOnText = (TextViewCustom) findViewById(R.id.bite_bring_it_on_text);

        onItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return locked && hasOrder;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        mRecyclerView.addOnItemTouchListener(onItemTouchListener);
        mUserOrderRecyclerView.addOnItemTouchListener(onItemTouchListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        final Bite[] bite = new Bite[1];
        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mRefStore != null) mRefStore.removeEventListener(storeListener);
                if (mRefUserData != null) mRefUserData.removeEventListener(userListener);
                if (mRefUserOrder != null) mRefUserOrder.removeEventListener(userOrderListener);
                if (mRefUserOrderLocked != null)
                    mRefUserOrderLocked.removeEventListener(userOrderLockedListener);

                bite[0] = dataSnapshot.getValue(Bite.class);
                if (bite[0] != null) {

                    if(Objects.equals(bite[0].getStatus(), "closed")){
                        Toast.makeText(RestaurantActivity.this, "Deze Bite is gesloten ):", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    Date dfStart = new Date(bite[0].getOpenTime());
                    Date dfClose = new Date(bite[0].getCloseTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    textViewCustomStartClosetime.setText(getString(R.string.bite_card_start_close_time, sdf.format(dfStart), sdf.format(dfClose)));

                    mRefStore = mDatabase.getReference("stores").child(bite[0].getStore());
                    mRefProducts = mDatabase.getReference("products").child(bite[0].getStore()).child("products");
                    if (adapter == null) {
                        adapter = new MenuAdapter(MenuItem.class, R.layout.card_view_menu_item, MenuItemViewHolder.class, mRefProducts, RestaurantActivity.this, getUser(), key);
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
                    mRefUserData = mDatabase.getReference("users").child(bite[0].getOpenedBy());
                    mRefUserOrder = mDatabase.getReference("user_order").child(dataSnapshot.getKey()).child(getUser().getUid());
                    if (userOrderAdapter == null) {
                        userOrderAdapter = new UserOrderAdapter(UserOrder.class, R.layout.card_view_menu_item, MenuItemViewHolder.class, mRefUserOrder, RestaurantActivity.this, bite[0].getStore());
                        userOrderAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                super.onItemRangeInserted(positionStart, itemCount);
                                userOrderAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onItemRangeRemoved(int positionStart, int itemCount) {
                                super.onItemRangeRemoved(positionStart, itemCount);
                                userOrderAdapter.notifyDataSetChanged();
                            }
                        });
                        mUserOrderRecyclerView.setAdapter(userOrderAdapter);
                    }
                    mRefUserOrderLocked = mDatabase.getReference("user_order_locked").child(dataSnapshot.getKey()).child(getUser().getUid());

                    mRefStore.addValueEventListener(storeListener);
                    mRefUserData.addValueEventListener(userListener);
                    mRefUserOrder.addValueEventListener(userOrderListener);
                    mRefUserOrderLocked.addValueEventListener(userOrderLockedListener);
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
                Store store = dataSnapshot.getValue(Store.class);
                if (store != null) {
                    textViewCustomToolbarText.setText(store.getName().toUpperCase(Locale.getDefault()));
                    if (store.getCategories() != null && store.getCategories().size() > 0) {
                        layoutEmojiList.removeAllViews();
                        for (HashMap<String, String> category : store.getCategories().values()) {
                            ImageView emo = new ImageView(RestaurantActivity.this);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                    (int) getResources().getDimension(R.dimen.bite_card_restaurant_emoji_size),
                                    (int) getResources().getDimension(R.dimen.bite_card_restaurant_emoji_size));
                            llp.setMargins((int) getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                                    0,
                                    (int) getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
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
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
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
        userOrderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    int amount = 0;
                    final long[] price = {0};
                    for (final DataSnapshot data : dataSnapshot.getChildren()) {
                        final UserOrder order = data.getValue(UserOrder.class);
                        if (order != null) {
                            amount += order.getAmount();
                            mDatabase.getReference("products").child(bite[0].getStore()).child("products").child(data.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    MenuItem item = dataSnapshot.getValue(MenuItem.class);
                                    if (item != null) {
                                        price[0] += order.getAmount() * item.getPrice();
                                        updateOrderPrice(price[0]);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, databaseError.toString());
                                }
                            });
                        }
                    }
                    updateOrderView(amount);
                    updateOrderCount(amount);
                } else {
                    updateOrderView(0);
                    updateOrderCount(0);
                    updateOrderPrice(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        };
        userOrderLockedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isLocked = dataSnapshot.getValue(Boolean.class);
                if (isLocked != null) {
                    locked = isLocked;
                } else {
                    locked = false;
                }
                updateBringItOn();
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

    private void updateOrderCount(int count) {
        TextViewCustom orderCount = (TextViewCustom) findViewById(R.id.total_order_count);
        orderCount.setText(getString(R.string.order_items_count, count));
    }

    private void updateOrderView(int count) {
        if (count == 0) {
            hasOrder = false;
            mRefUserOrderLocked.setValue(null);
            mUserOrderRecyclerView.setVisibility(View.GONE);
            mEmptyOrder.setVisibility(View.VISIBLE);
        } else {
            hasOrder = true;
            mUserOrderRecyclerView.setVisibility(View.VISIBLE);
            mEmptyOrder.setVisibility(View.GONE);
        }
    }

    private void updateBringItOn() {
        mBringItOn.setBackground(getDrawable(locked && hasOrder ? R.drawable.bring_it_button_locked : R.drawable.bring_it_button));
        mBringItOnText.setText(getString(locked && hasOrder ? R.string.bring_it_on_locked : R.string.bring_it_on));
        mRecyclerView.setForeground(getDrawable(locked && hasOrder ? android.R.drawable.screen_background_light_transparent : android.R.color.transparent));
        mUserOrderRecyclerView.setForeground(getDrawable(locked && hasOrder ? android.R.drawable.screen_background_light_transparent : android.R.color.transparent));
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
        updateBringItOn();
        updateOrderPrice(0);
        updateOrderCount(0);
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
        if (mRefOrder != null) mRefOrder.removeEventListener(orderListener);
        if (mRefStore != null) mRefStore.removeEventListener(storeListener);
        if (mRefUserData != null) mRefUserData.removeEventListener(userListener);
        if (mRefUserOrder != null) mRefUserOrder.removeEventListener(userOrderListener);
        if (mRefUserOrderLocked != null)
            mRefUserOrderLocked.removeEventListener(userOrderLockedListener);
        if(onItemTouchListener != null) {
            mRecyclerView.removeOnItemTouchListener(onItemTouchListener);
            mUserOrderRecyclerView.removeOnItemTouchListener(onItemTouchListener);
        }
    }
}
