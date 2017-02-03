package com.move4mobile.apps.bite;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.move4mobile.apps.bite.objects.ArchiveProduct;

/**
 * Created by casvd on 31-1-2017.
 */

public class ArchiveProductAdapter extends FirebaseRecyclerAdapter<ArchiveProduct, ArchiveProductViewHolder> {

    private Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */

    public ArchiveProductAdapter(Class<ArchiveProduct> modelClass, int modelLayout, Class<ArchiveProductViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(ArchiveProductViewHolder viewHolder, ArchiveProduct model, int position) {
        if(model.getName() != null) {
            viewHolder.title.setText(model.getName());
        }
        if(model.getPrice() != null) {
            viewHolder.price.setText(String.valueOf(model.getPrice()));
        }
        if(model.getAmount() != null) {
            viewHolder.amount.setText(String.valueOf(model.getAmount()));
        }
        if(model.isSauce()) {
            viewHolder.image.setImageDrawable(model.isSauce() ? mContext.getDrawable(R.drawable.ic_sauce) : mContext.getDrawable(R.drawable.ic_snacks));
        }
    }
}
