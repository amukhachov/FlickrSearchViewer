package com.project.flickrsearchclient.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class SquaredImageView extends ImageView {

    public SquaredImageView(Context context) {
        this(context, null);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquaredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels / 3;
        setMeasuredDimension(width, width);
    }
}
