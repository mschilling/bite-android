package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MenuItemViewHolder";
    View v;
    TextViewCustom mTextTitle;
    TextViewCustom mTextPrice;

    public MenuItemViewHolder(View v) {
        super(v);
        this.v = v;
        mTextTitle = (TextViewCustom) v.findViewById(R.id.menu_item_title);
        mTextTitle.setSelected(true);
        mTextTitle.setHorizontallyScrolling(true);
        mTextPrice = (TextViewCustom) v.findViewById(R.id.menu_item_price);
    }
}
