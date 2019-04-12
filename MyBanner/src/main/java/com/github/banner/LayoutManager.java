package com.github.banner;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

/***
 *   created by android on 2019/4/12
 */
public class LayoutManager extends LinearLayoutManager {
    private float calculateSpeedPerPixel=0.4f;
    public LayoutManager(Context context) {
        super(context);
    }
    public LayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    public LayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return calculateSpeedPerPixel;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public float getCalculateSpeedPerPixel() {
        return calculateSpeedPerPixel;
    }

    public void setCalculateSpeedPerPixel(float timeScroll,float width) {
        if(width<=0){
            return;
        }
        this.calculateSpeedPerPixel = timeScroll/width;
    }
}
