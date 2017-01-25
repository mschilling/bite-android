package com.move4mobile.apps.bite;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.move4mobile.apps.bite.objects.Store;

import java.util.HashMap;

/**
 * Created by casvd on 25-1-2017.
 */

public class StoreAdapter extends FirebaseRecyclerAdapter<Store, StoreViewHolder> {

    private static final String TAG = "StoreAdapter";
    private Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public StoreAdapter(Class<Store> modelClass, int modelLayout, Class<StoreViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }

    @Override
    protected void populateViewHolder(final StoreViewHolder viewHolder, Store model, final int position) {
        viewHolder.mTextTitle.setText(model.getName());
        viewHolder.mTextLocation.setText(model.getLocation());

        viewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OpenActivity) {
                    if (((OpenActivity) mContext).notifySelectStore(getRef(position).getKey(), position)) {
                        viewHolder.mRadioButton.setChecked(true);
                    }
                }
            }
        });

        viewHolder.mEmojiList.removeAllViews();
        if(model.getCategories() != null) {
            for (HashMap<String, String> category : model.getCategories().values()) {
                ImageView emoji = new ImageView(mContext);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        (int) mContext.getResources().getDimension(R.dimen.bite_card_emoji_size),
                        (int) mContext.getResources().getDimension(R.dimen.bite_card_emoji_size));
                llp.setMargins((int) mContext.getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                        0,
                        (int) mContext.getResources().getDimension(R.dimen.bite_card_emoji_margin_horizontal),
                        0);
                emoji.setLayoutParams(llp);
                emoji.setImageDrawable(BiteApplication.Emojis.getEmoji(Integer.valueOf(category.get("type"))));
                viewHolder.mEmojiList.addView(emoji);
            }
        }
    }
}
