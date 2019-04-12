package com.github.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/***
 *   created by android on 2019/4/11
 */
public class MyBannerView extends RecyclerView {
    public MyBannerView(@NonNull Context context) {
        super(context);
    }
    public MyBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public MyBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
