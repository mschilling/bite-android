package com.move4mobile.apps.bite;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by casvd on 8-11-2016.
 */

public class BiteViewHolder extends RecyclerView.ViewHolder {

    static final String TAG = "BiteViewHolder";
    ImageView mImageView;
    TextViewCustom mTextViewCustom;

    public BiteViewHolder(View v) {
        super(v);
        mImageView = (ImageView) v.findViewById(R.id.bite_card_created_by_image);
        mTextViewCustom = (TextViewCustom) v.findViewById(R.id.bite_card_restaurant_title_text);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestaurantActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}
