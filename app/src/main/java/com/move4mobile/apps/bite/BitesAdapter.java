package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.dialogs.RemoveBiteDialog;
import com.move4mobile.apps.bite.objects.Bite;
import com.move4mobile.apps.bite.objects.Store;
import com.move4mobile.apps.bite.objects.User;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by casvd on 8-11-2016.
 */

public class BitesAdapter extends FirebaseRecyclerAdapter<Bite, BiteViewHolder> {

    private static final String TAG = "BitesAdapter";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefStoreName;
    private DatabaseReference mRefUserData;

    private Context mContext;
    private FirebaseUser user;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public BitesAdapter(Class<Bite> modelClass, int modelLayout, Class<BiteViewHolder> viewHolderClass, Query ref, Context context, FirebaseUser user) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        this.user = user;
        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final BiteViewHolder viewHolder, final Bite model, final int position) {

        if(Objects.equals(user.getUid(), model.getOpenedBy())) {
            viewHolder.mButtonRemove.setVisibility(View.VISIBLE);
            viewHolder.mButtonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RemoveBiteDialog.class);
                    intent.putExtra("key", getRef(position).getKey());
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            viewHolder.mButtonRemove.setVisibility(View.GONE);
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestaurantActivity.class);
                intent.putExtra("key", getRef(position).getKey());
                v.getContext().startActivity(intent);
            }
        });

        //Header extra margin for shadow
        if(position == 0){
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);

            RecyclerView.LayoutParams llp = (RecyclerView.LayoutParams) viewHolder.view.getLayoutParams();
            llp.setMargins(
                    (int) mContext.getResources().getDimension(R.dimen.bite_card_margin_horizontal),
                    (int) mContext.getResources().getDimension(R.dimen.bite_card_margin_top) + px,
                    (int) mContext.getResources().getDimension(R.dimen.bite_card_margin_horizontal),
                    (int) mContext.getResources().getDimension(R.dimen.bite_card_margin_bottom)
            );
            viewHolder.view.setLayoutParams(llp);
        }

        mRefStoreName = mDatabase.getReference("stores/"+model.getStore());
        mRefStoreName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.e(TAG, dataSnapshot.toString());
                    Store store = dataSnapshot.getValue(Store.class);
                    if(store != null) {
                        viewHolder.mTextTitle.setText(store.getName().toUpperCase(Locale.getDefault()));
                        viewHolder.mTextLocation.setText(store.getLocation());

                        if(store.getCategories() != null && store.getCategories().size() > 0) {
                            viewHolder.mEmojiList.removeAllViews();
                            for (HashMap<String, String> category: store.getCategories().values()) {
                                ImageView emoji = new ImageView(mContext);
                                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                        (int)mContext.getResources().getDimension(R.dimen.bite_card_emoji_size),
                                        (int)mContext.getResources().getDimension(R.dimen.bite_card_emoji_size));
                                llp.setMargins((int)mContext.getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                                        0,
                                        (int)mContext.getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                                        0);
                                emoji.setLayoutParams(llp);
                                emoji.setImageDrawable(BiteApplication.Emojis.getEmoji(Integer.valueOf(category.get("type"))));
                                viewHolder.mEmojiList.addView(emoji);
                            }
                        } else {
                            viewHolder.mEmojiList.removeAllViews();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });

        mRefUserData = mDatabase.getReference("users/"+model.getOpenedBy());
        mRefUserData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.e(TAG, dataSnapshot.toString());
                    final User user = dataSnapshot.getValue(User.class);
                    if(user != null) {

                        Glide.with(mContext).load(user.getPhotoUrl())
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
                                Toast toast = Toast.makeText(v.getContext(), user.getDisplayName() + " | " + user.getName(), Toast.LENGTH_SHORT);
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
