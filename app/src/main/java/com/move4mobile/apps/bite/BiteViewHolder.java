package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 8-11-2016.
 */

public class BiteViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "BiteViewHolder";
    View view;
    ImageView mImageView;
    TextViewCustom mTextTitle;
    TextViewCustom mTextLocation;
    Button mButtonRemove;
    LinearLayout mEmojiList;
    RecyclerView mSocialList;

    public BiteViewHolder(View v) {
        super(v);
        view = v;
        mImageView = (ImageView) v.findViewById(R.id.bite_card_created);
        mTextTitle = (TextViewCustom) v.findViewById(R.id.bite_card_restaurant_title);
        mTextLocation = (TextViewCustom) v.findViewById(R.id.bite_card_location);
        mButtonRemove = (Button) v.findViewById(R.id.bite_card_remove);
        mEmojiList = (LinearLayout) v.findViewById(R.id.bite_card_emoji_list);
        mSocialList = (RecyclerView) v.findViewById(R.id.bite_card_social);
    }
}
