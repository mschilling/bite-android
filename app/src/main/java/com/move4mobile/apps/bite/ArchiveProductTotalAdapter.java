package com.move4mobile.apps.bite;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.move4mobile.apps.bite.objects.ArchiveProductTotal;
import com.move4mobile.apps.bite.objects.User;

import java.util.List;

/**
 * Created by casvd on 2-2-2017.
 */

public class ArchiveProductTotalAdapter extends RecyclerView.Adapter<ArchiveProductTotalViewHolder> {

    private static final String TAG = "ArchiveProdTotalAdapter";
    private List<ArchiveProductTotal> totalProducts;
    private Context mContext;

    public ArchiveProductTotalAdapter(List<ArchiveProductTotal> totalProducts, Context context) {
        this.totalProducts = totalProducts;
        this.mContext = context;
    }

    @Override
    public ArchiveProductTotalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_menu_item_closed_total, parent, false);

        return new ArchiveProductTotalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArchiveProductTotalViewHolder holder, final int position) {
        ArchiveProductTotal productTotal = totalProducts.get(position);
        if (productTotal != null) {
            holder.title.setText(productTotal.getProduct().getName());
            holder.price.setText(String.valueOf(productTotal.getProduct().getPrice()));
            holder.amount.setText(String.valueOf(productTotal.getProduct().getAmount()));

            holder.list.removeAllViews();
            boolean first = true;
            int index = productTotal.getUsers().size();
            for (User user : productTotal.getUsers()) {
                Log.e(TAG, user.getName());
                final ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        (int) mContext.getResources().getDimension(R.dimen.bite_card_social_image_size),
                        (int) mContext.getResources().getDimension(R.dimen.bite_card_social_image_size));
                llp.setMargins(first ? 0 : (int) mContext.getResources().getDimension(R.dimen.bite_card_social_image_negative_margin),
                        0,
                        0,
                        0);
                imageView.setLayoutParams(llp);
                imageView.setElevation(index);
                holder.list.addView(imageView);
                Glide.with(mContext).load(user.getPhotoUrl())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.ic_shipit)
                        .into(new BitmapImageViewTarget(imageView) {

                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                first = false;
                index--;
            }
        }
    }

    @Override
    public int getItemCount() {
        return totalProducts.size();
    }
}
