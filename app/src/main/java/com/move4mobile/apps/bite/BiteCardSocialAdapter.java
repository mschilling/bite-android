package com.move4mobile.apps.bite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.customlayoutclasses.RelativeLayoutCircle;
import com.move4mobile.apps.bite.objects.User;

import java.util.HashMap;

/**
 * Created by casvd on 12-12-2016.
 */

public class BiteCardSocialAdapter extends FirebaseRecyclerAdapter<Object, BiteCardSocialViewHolder> {

    private static final String TAG = "BiteCardSocialAdapter";

    private String key;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserData;
    private DatabaseReference mRefLocked;

    private Context mContext;

    private HashMap<String, ValueEventListener> listenerHashMap;
    private HashMap<String, Drawable> drawableHashMap;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public BiteCardSocialAdapter(Class<Object> modelClass, int modelLayout, Class<BiteCardSocialViewHolder> viewHolderClass, Query ref, Context context, String key) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.key = key;
        mContext = context;
        listenerHashMap = new HashMap<>();
        drawableHashMap = new HashMap<>();
        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final BiteCardSocialViewHolder viewHolder, Object model, final int position) {
        if (drawableHashMap.get(getRef(position).getKey()) != null && !viewHolder.imageView.getDrawable().equals(drawableHashMap.get(getRef(position).getKey()))) {
            //Get saved img
            viewHolder.imageView.setImageDrawable(drawableHashMap.get(getRef(position).getKey()));
        }

        mRefLocked = mDatabase.getReference("user_order_locked").child(key).child(getRef(position).getKey());
        mRefLocked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean locked = dataSnapshot.getValue(Boolean.class);
                if (viewHolder.v instanceof RelativeLayoutCircle) {
                    RelativeLayoutCircle v = (RelativeLayoutCircle) viewHolder.v;
                    if (locked != null && locked) {
                        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);
                        v.setCircleColor(mContext, mContext.getResources().getColor(R.color.colorAccent, mContext.getTheme()), strokeWidth);
                    } else {
                        v.setCircleColor(mContext, mContext.getResources().getColor(android.R.color.transparent, mContext.getTheme()), 0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });

        mRefUserData = mDatabase.getReference("users/" + getRef(position).getKey());
        if (listenerHashMap.get(getRef(position).getKey()) != null) {
            mRefUserData.removeEventListener(listenerHashMap.get(getRef(position).getKey()));
        }
        listenerHashMap.put(getRef(position).getKey(), mRefUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Glide.with(mContext).load(user.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.ic_shipit)
                            .into(new BitmapImageViewTarget(viewHolder.imageView) {

                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    try {
                                        drawableHashMap.put(getRef(position).getKey(), circularBitmapDrawable);
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                    viewHolder.imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        }));
    }
}
