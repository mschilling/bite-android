package com.move4mobile.apps.bite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by casvd on 27-10-2016.
 */

public class RelativeLayoutCircle extends RelativeLayout {
    public RelativeLayoutCircle(Context context) {
        super(context);
    }
    public RelativeLayoutCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCircleColor(context, attrs);
    }

    public RelativeLayoutCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCircleColor(context, attrs);
    }

    private void setCircleColor(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutCircle);
        int color = a.getColor(R.styleable.RelativeLayoutCircle_customCircleColor, getResources().getColor(R.color.colorAccent, ctx.getTheme()));
        float width = a.getDimension(R.styleable.RelativeLayoutCircle_customStrokeWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, getResources().getDisplayMetrics()));
        setCircleColor(ctx, color, width);
        a.recycle();
    }

    public void setCircleColor(Context ctx, int color, float width){
        GradientDrawable bgDrawable = (GradientDrawable) getBackground();

        /*DisplayMetrics dm = getResources().getDisplayMetrics() ;
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, dm);*/

        bgDrawable.setStroke((int) width, color);
    }
}
