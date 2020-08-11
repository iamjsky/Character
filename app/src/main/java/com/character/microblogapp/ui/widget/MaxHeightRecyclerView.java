package com.character.microblogapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.character.microblogapp.R;

/**
 * Created by Crazy on 5/11/2019.
 */

public class MaxHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context, attributeSet);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        initialize(context, attributeSet);
    }

    private void initialize(Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.MaxHeightRecyclerView);
        mMaxHeight = array.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
