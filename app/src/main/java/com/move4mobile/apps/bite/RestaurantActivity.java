package com.move4mobile.apps.bite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        /*int theme = PreferenceManager.getDefaultSharedPreferences(this).getInt("pref_theme", R.style.AppTheme_Light);
        if(theme == R.style.AppTheme_Light){
            setTheme(R.style.AppTheme_Bite_Light);
        } else {
            setTheme(R.style.AppTheme);
        }*/

        setContentView(R.layout.activity_restaurant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);

        final ArrayList<String> mItems = new ArrayList<>();

        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_menu_item, parent, false);
                return new CViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }
        };

        rv.setAdapter(adapter);

        for(int i = 0; i < 20; i++){
            mItems.add(""+i);
            adapter.notifyItemInserted(mItems.size() - 1);
        }

        final ImageView imageView = (ImageView) findViewById(R.id.image_besteller);
        Glide.with(this).load("http://assets.nydailynews.com/polopoly_fs/1.2654880.1464639106!/img/httpImage/image.jpg_gen/derivatives/article_750/552448316.jpg").asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });


    }
}

class CViewHolder extends RecyclerView.ViewHolder {

    public CViewHolder(View itemView) {
        super(itemView);
    }
}

