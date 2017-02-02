package com.move4mobile.apps.bite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.move4mobile.apps.bite.objects.ArchiveProductTotal;

import java.util.List;

/**
 * Created by casvd on 2-2-2017.
 */

public class ArchiveProductTotalAdapter extends RecyclerView.Adapter<ArchiveProductTotalViewHolder> {

    private List<ArchiveProductTotal> totalProducts;

    public ArchiveProductTotalAdapter(List<ArchiveProductTotal> totalProducts) {
        this.totalProducts = totalProducts;
    }

    @Override
    public ArchiveProductTotalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_menu_item_closed_total, parent, false);

        return new ArchiveProductTotalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArchiveProductTotalViewHolder holder, int position) {
        ArchiveProductTotal productTotal = totalProducts.get(position);
        if (productTotal != null) {
            holder.title.setText(productTotal.getProduct().getName());
            holder.price.setText(String.valueOf(productTotal.getProduct().getPrice()));
            holder.amount.setText(String.valueOf(productTotal.getProduct().getAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return totalProducts.size();
    }
}
