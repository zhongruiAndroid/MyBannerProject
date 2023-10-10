package com.github.banner;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/***
 *   created by android on 2019/4/11
 */
public interface BannerItem<T> {
    int getItemLayoutId();
    default View getItemLayout(Context context, ViewGroup viewGroup){
        return null;
    }

    boolean isItemType(T item, int position, int dataCount);

    void onItemClick(T item, int position, int dataCount);

    void bindData(BannerHolder holder, T item, int position, int dataCount);


}
