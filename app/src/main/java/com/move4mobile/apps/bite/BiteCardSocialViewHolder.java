package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by casvd on 12-12-2016.
 */

public class BiteCardSocialViewHolder extends RecyclerView.ViewHolder {

    View v;
    ImageView imageView;

    public BiteCardSocialViewHolder(View v) {
        super(v);
        this.v = v;
        imageView = (ImageView) v.findViewById(R.id.bite_card_social_image);
    }
}
