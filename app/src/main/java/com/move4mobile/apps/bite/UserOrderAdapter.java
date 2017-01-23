package com.move4mobile.apps.bite;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.objects.MenuItem;
import com.move4mobile.apps.bite.objects.UserOrder;

/**
 * Created by casvd on 13-12-2016.
 */

public class UserOrderAdapter extends FirebaseRecyclerAdapter<UserOrder, MenuItemViewHolder> {
    private static final String TAG = "UserOrderAdapter";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefProduct;

    private Context mContext;
    private String store;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public UserOrderAdapter(Class<UserOrder> modelClass, int modelLayout, Class<MenuItemViewHolder> viewHolderClass, Query ref, Context context, String store) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
        this.store = store;
        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final MenuItemViewHolder viewHolder, final UserOrder model, final int position) {
        mRefProduct = mDatabase.getReference("products").child(store).child("products").child(getRef(position).getKey());
        mRefProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MenuItem item = dataSnapshot.getValue(MenuItem.class);
                if (item != null) {
                    viewHolder.mTextTitle.setText(item.getName());
                    viewHolder.mTextPrice.setText(mContext.getString(R.string.menu_item_price, item.getPrice()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });

        if(model.getAmount() > 0) {
            viewHolder.mTextAmount.setText(mContext.getString(R.string.number, model.getAmount()));
            viewHolder.mTextAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.Add();
                    getRef(position).setValue(model);
                }
            });
            viewHolder.mTextRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.Remove();
                    if (model.getAmount() == 0) {
                        getRef(position).setValue(null);
                    } else {
                        getRef(position).setValue(model);
                    }
                }
            });
            viewHolder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.Add();
                    getRef(position).setValue(model);
                }
            });
            setVisible(viewHolder);
        } else {
            setInvisible(viewHolder);
        }
    }

    private void setVisible(MenuItemViewHolder viewHolder) {
        viewHolder.mTextRemove.setVisibility(View.VISIBLE);
        viewHolder.mTextAdd.setVisibility(View.VISIBLE);
        viewHolder.mTextAmount.setVisibility(View.VISIBLE);
    }

    private void setInvisible(MenuItemViewHolder viewHolder) {
        viewHolder.mTextRemove.setVisibility(View.GONE);
        viewHolder.mTextAdd.setVisibility(View.GONE);
        viewHolder.mTextAmount.setVisibility(View.GONE);
    }
}
