package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.move4mobile.apps.bite.customlayoutclasses.TextViewCustom;

/**
 * Created by casvd on 2-2-2017.
 */

public class ArchiveProductTotalViewHolder extends RecyclerView.ViewHolder {

    View v;
    TextViewCustom title;
    TextViewCustom price;
    TextViewCustom amount;
    LinearLayout list;

    public ArchiveProductTotalViewHolder(View v) {
        super(v);
        this.v = v;
        title = (TextViewCustom) v.findViewById(R.id.menu_item_title);
        title.setSelected(true);
        title.setHorizontallyScrolling(true);
        price = (TextViewCustom) v.findViewById(R.id.menu_item_price);
        amount = (TextViewCustom) v.findViewById(R.id.menu_item_amount);
        list = (LinearLayout) v.findViewById(R.id.order_product_list);
    }
}
