package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 31-1-2017.
 */

public class ArchiveProductViewHolder extends RecyclerView.ViewHolder {

    View v;
    TextViewCustom title;
    TextViewCustom price;
    TextViewCustom amount;

    public ArchiveProductViewHolder(View v) {
        super(v);
        this.v = v;
        title = (TextViewCustom) v.findViewById(R.id.menu_item_title);
        price = (TextViewCustom) v.findViewById(R.id.menu_item_price);
        amount = (TextViewCustom) v.findViewById(R.id.menu_item_amount);
    }
}
