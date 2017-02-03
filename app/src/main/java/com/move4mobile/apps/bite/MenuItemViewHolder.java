package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 18-11-2016.
 */

public class MenuItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MenuItemViewHolder";
    View v;
    TextViewCustom mTextTitle;
    TextViewCustom mTextPrice;
    TextViewCustom mTextRemove;
    TextViewCustom mTextAdd;
    TextViewCustom mTextAmount;
    ImageView mImage;

    public MenuItemViewHolder(View v) {
        super(v);
        this.v = v;
        mTextTitle = (TextViewCustom) v.findViewById(R.id.menu_item_title);
        mTextTitle.setSelected(true);
        mTextTitle.setHorizontallyScrolling(true);
        mTextPrice = (TextViewCustom) v.findViewById(R.id.menu_item_price);
        mTextRemove = (TextViewCustom) v.findViewById(R.id.menu_item_remove);
        mTextAdd = (TextViewCustom) v.findViewById(R.id.menu_item_add);
        mTextAmount = (TextViewCustom) v.findViewById(R.id.menu_item_amount);
        mImage = (ImageView) v.findViewById(R.id.menu_item_image);
    }
}
