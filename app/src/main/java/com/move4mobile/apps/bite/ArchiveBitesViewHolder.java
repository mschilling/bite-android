package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 28-1-2017.
 */

public class ArchiveBitesViewHolder extends RecyclerView.ViewHolder {

    View v;
    TextViewCustom mTextTitle;
    TextViewCustom mTextPrice;
    TextViewCustom mTextDate;
    LinearLayout mEmojiList;

    public ArchiveBitesViewHolder(View v) {
        super(v);
        this.v = v;
        mTextTitle = (TextViewCustom) v.findViewById(R.id.bite_card_closed_title);
        mTextPrice = (TextViewCustom) v.findViewById(R.id.bite_card_closed_price);
        mTextDate = (TextViewCustom) v.findViewById(R.id.bite_card_closed_date);
        mEmojiList = (LinearLayout) v.findViewById(R.id.bite_card_closed_emoji_list);
    }
}
