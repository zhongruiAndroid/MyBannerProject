package com.github.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/***
 *   created by android on 2019/4/16
 */
public class BannerRecyclerView extends RecyclerView {
    private boolean useGesture=true;
    public BannerRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isUseGesture() {
        return useGesture;
    }

    public void setUseGesture(boolean useGesture) {
        this.useGesture = useGesture;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.useGesture) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.useGesture) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
