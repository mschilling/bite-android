package com.move4mobile.apps.bite;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuAdapter extends FirebaseRecyclerAdapter<MenuItem, MenuItemViewHolder> {

    private static final String TAG = "MenuAdapter";
    private Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public MenuAdapter(Class<MenuItem> modelClass, int modelLayout, Class<MenuItemViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }

    @Override
    protected void populateViewHolder(final MenuItemViewHolder viewHolder, MenuItem model, final int position) {
        viewHolder.mTextTitle.setText(model.getName());
        viewHolder.mTextPrice.setText(mContext.getString(R.string.menu_item_price, model.getPrice()));
        viewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(viewHolder.v, getRef(position).getKey(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
