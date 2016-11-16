package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by casvd on 8-11-2016.
 */

public class BitesAdapter extends FirebaseRecyclerAdapter<Bite, BiteViewHolder> {

    private static final String TAG = "BitesAdapter";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefStoreName;
    private DatabaseReference mRefUserData;

    private Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public BitesAdapter(Class<Bite> modelClass, int modelLayout, Class<BiteViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final BiteViewHolder viewHolder, final Bite model, final int position) {

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestaurantActivity.class);
                intent.putExtra("key", getRef(position).getKey());
                v.getContext().startActivity(intent);
            }
        });

        mRefStoreName = mDatabase.getReference("stores/"+model.getStore());
        mRefStoreName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.e(TAG, dataSnapshot.toString());
                    Store store = dataSnapshot.getValue(Store.class);
                    viewHolder.mTextTitle.setText(store.getName().toUpperCase());
                    viewHolder.mTextLocation.setText(store.getLocation());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });

        mRefUserData = mDatabase.getReference("users/"+model.getOpened_by());
        mRefUserData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.e(TAG, dataSnapshot.toString());
                    final User user = dataSnapshot.getValue(User.class);
                    if(user != null) {

                        Glide.with(mContext).load(user.getPhoto_url())
                                .asBitmap()
                                .centerCrop()
                                .placeholder(R.drawable.ic_shipit)
                                .into(new BitmapImageViewTarget(viewHolder.mImageView) {

                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        viewHolder.mImageView.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                        viewHolder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Toast toast = Toast.makeText(v.getContext(), user.getDisplay_name() + " | " + user.getName(), Toast.LENGTH_SHORT);
                                int[] loc = new int[2];
                                v.getLocationInWindow(loc);
                                toast.setGravity(Gravity.TOP | Gravity.START, loc[0], loc[1] + v.getHeight() / 2);
                                toast.show();
                                return false;
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
