package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.move4mobile.apps.bite.objects.ArchiveUserOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by casvd on 28-1-2017.
 */

public class ArchiveBitesAdapter extends FirebaseRecyclerAdapter<ArchiveUserOrder, ArchiveBitesViewHolder> {

    private static final String TAG = "ArchiveBitesAdapter";

    private Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public ArchiveBitesAdapter(Class<ArchiveUserOrder> modelClass, int modelLayout, Class<ArchiveBitesViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(ArchiveBitesViewHolder viewHolder, ArchiveUserOrder model, final int position) {

        if(model.getStore() != null){
            viewHolder.mTextTitle.setText(model.getStore());
        }

        if(model.getProducts() != null) {
            int priceTotal = 0;
            for (Map<String, Object> product : model.getProducts().values()) {
                long amount = (long) product.get("amount");
                long price = (long) product.get("price");
                priceTotal += amount * price;
            }
            viewHolder.mTextPrice.setText(String.valueOf(priceTotal));
        }

        if(model.getOpenTime() != null) {
            Date date = new Date(model.getOpenTime());
            String dateString = new SimpleDateFormat("E d MMM", Locale.getDefault()).format(date);
            viewHolder.mTextDate.setText(dateString);
        }

        if (model.getCategories() != null && model.getCategories().size() > 0) {
            for (Map<String, String> category : model.getCategories().values()) {
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

        viewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClosedActivity.class);
                intent.putExtra("key", getRef(position).getKey());
                v.getContext().startActivity(intent);
            }
        });
    }
}
