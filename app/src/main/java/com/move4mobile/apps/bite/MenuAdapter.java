package com.move4mobile.apps.bite;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.move4mobile.apps.bite.objects.MenuItem;
import com.move4mobile.apps.bite.objects.UserOrder;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuAdapter extends FirebaseRecyclerAdapter<MenuItem, MenuItemViewHolder> {

    private static final String TAG = "MenuAdapter";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefUserOrder;

    private Context mContext;
    private FirebaseUser user;
    private String biteKey;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public MenuAdapter(Class<MenuItem> modelClass, int modelLayout, Class<MenuItemViewHolder> viewHolderClass, Query ref, Context context, FirebaseUser user, String biteKey) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        this.user = user;
        this.biteKey = biteKey;
        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void populateViewHolder(final MenuItemViewHolder viewHolder, MenuItem model, final int position) {
        viewHolder.mTextTitle.setText(model.getName());
        viewHolder.mTextPrice.setText(mContext.getString(R.string.menu_item_price, model.getPrice()));

        mRefUserOrder = mDatabase.getReference("user_order").child(biteKey).child(user.getUid()).child(getRef(position).getKey());
        mRefUserOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final UserOrder order = dataSnapshot.getValue(UserOrder.class);
                if(order != null) {
                    if(order.getAmount() <= 0) {
                        setInvisible(viewHolder);
                        viewHolder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(viewHolder.v, getRef(position).getKey(), Snackbar.LENGTH_SHORT).show();
                                order.Add();
                                dataSnapshot.getRef().setValue(order);
                            }
                        });
                    } else {
                        viewHolder.mTextAmount.setText(mContext.getString(R.string.number, order.getAmount()));
                        viewHolder.mTextAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                order.Add();
                                dataSnapshot.getRef().setValue(order);
                            }
                        });
                        viewHolder.mTextRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                order.Remove();
                                if(order.getAmount() == 0) {
                                    dataSnapshot.getRef().setValue(null);
                                } else {
                                    dataSnapshot.getRef().setValue(order);
                                }
                            }
                        });
                        viewHolder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(viewHolder.v, getRef(position).getKey(), Snackbar.LENGTH_SHORT).show();
                                order.Add();
                                dataSnapshot.getRef().setValue(order);
                            }
                        });
                        setVisible(viewHolder);
                    }
                } else {
                    setInvisible(viewHolder);
                    viewHolder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(viewHolder.v, getRef(position).getKey(), Snackbar.LENGTH_SHORT).show();
                            UserOrder order = new UserOrder();
                            order.Add();
                            dataSnapshot.getRef().setValue(order);
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
