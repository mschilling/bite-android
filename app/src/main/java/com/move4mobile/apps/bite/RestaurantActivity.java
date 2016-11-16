package com.move4mobile.apps.bite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 15-11-2016.
 */

public class RestaurantActivity extends AppCompatActivityFireAuth {

    private static final String TAG = "RestaurantActivity";

    private String key;

    private TextViewCustom textViewCustomToolbarText;

    private ImageView imageViewStartedBy;
    private TextViewCustom textViewCustomStartedBy;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefOrder;
    private DatabaseReference mRefStore;
    private DatabaseReference mRefProducts;
    private DatabaseReference mRefUserData;

    private ValueEventListener storeListener;
    private ValueEventListener productsListener;
    private ValueEventListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_restaurant);

        textViewCustomToolbarText = (TextViewCustom) findViewById(R.id.toolbar_text);
        imageViewStartedBy = (ImageView) findViewById(R.id.bite_card_started_by_image);
        textViewCustomStartedBy = (TextViewCustom) findViewById(R.id.bite_card_started_by);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();

        /*if(mRefStore != null) {
            if (mRefOrder != null && storeListener != null) mRefStore.removeEventListener(storeListener);
            if (mRefProducts != null && productsListener != null) mRefProducts.removeEventListener(productsListener);
            if (mRefUserData != null && userListener != null) mRefUserData.removeEventListener(userListener);
        }*/

        mRefOrder = mDatabase.getReference("orders").child(key);

        mRefOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());

                Bite bite = dataSnapshot.getValue(Bite.class);
                mRefStore = mDatabase.getReference("stores").child(bite.getStore());
                mRefProducts = mDatabase.getReference("products").child(bite.getStore());
                mRefUserData = mDatabase.getReference("users").child(bite.getOpened_by());

                mRefStore.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                        Store store = dataSnapshot.getValue(Store.class);
                        if(store != null) {
                            textViewCustomToolbarText.setText(store.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });
                mRefProducts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });
                mRefUserData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                        User user = dataSnapshot.getValue(User.class);
                        if(user != null){
                            textViewCustomStartedBy.setText(String.format("GESTART DOOR %s", user.getDisplay_name().toUpperCase()));
                            Glide.with(getApplicationContext()).load(user.getPhoto_url())
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
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }

    @Override
    protected void onLoggedOut() {
        super.onLoggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
