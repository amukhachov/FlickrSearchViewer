package com.project.flickrsearchclient.ui.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacingDecoration extends RecyclerView.ItemDecoration {

    private final int mSpacing;

    public SpacingDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.top = mSpacing;
        outRect.left = mSpacing;
        outRect.bottom = mSpacing;
        outRect.right = mSpacing;
    }
}
