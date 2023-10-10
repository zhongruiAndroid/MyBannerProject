package com.github.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;

/***
 *   created by android on 2019/4/12
 */
class LayoutManager extends LinearLayoutManager {
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
        BigDecimal bigDecimal=new BigDecimal(timeScroll/width);
        bigDecimal= bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP);
        this.calculateSpeedPerPixel =bigDecimal.floatValue();
    }
}
