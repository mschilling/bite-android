package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 25-1-2017.
 */

public class StoreViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "StoreViewHolder";
    View v;
    TextViewCustom mTextTitle;
    TextViewCustom mTextLocation;
    LinearLayout mEmojiList;
    RadioButton mRadioButton;

    public StoreViewHolder(View v) {
        super(v);
        this.v = v;
        mTextTitle = (TextViewCustom) v.findViewById(R.id.bite_card_open_title);
        mTextLocation = (TextViewCustom) v.findViewById(R.id.bite_card_open_location);
        mEmojiList = (LinearLayout) v.findViewById(R.id.bite_card_open_emoji_list);
        mRadioButton = (RadioButton) v.findViewById(R.id.bite_open_radio);
    }
}
