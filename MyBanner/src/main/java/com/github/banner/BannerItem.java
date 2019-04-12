package com.github.banner;

import android.view.View;

/***
 *   created by android on 2019/4/11
 */
public interface BannerItem<T> {
    int indicatorSelectView();
    int indicatorNormalView();
    int getItemLayout();
    boolean isItemType(T item,int position,int dataCount);
    void bindData(BannerHolder holder,T item,int position,int dataCount);
}
