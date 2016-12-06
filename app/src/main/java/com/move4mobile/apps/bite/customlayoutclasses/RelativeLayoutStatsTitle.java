package com.move4mobile.apps.bite.customlayoutclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.move4mobile.apps.bite.R;

/**
 * Created by casvd on 27-10-2016.
 */

public class RelativeLayoutStatsTitle extends RelativeLayout {
    public RelativeLayoutStatsTitle(Context context) {
        super(context);
    }
    public RelativeLayoutStatsTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCircleColor(context, attrs);
    }

    public RelativeLayoutStatsTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCircleColor(context, attrs);
    }

    private void setCircleColor(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutStatsTitle);
        int color = a.getColor(0, getResources().getColor(R.color.colorAccent, ctx.getTheme()));
        setStatsTitleColor(ctx, color);
        a.recycle();
    }

    public void setStatsTitleColor(Context ctx, int color){
        GradientDrawable bgDrawable = (GradientDrawable) getBackground();
        bgDrawable.setColor(color);
    }
}
