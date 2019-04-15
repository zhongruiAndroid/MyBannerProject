package com.github.banner;


/***
 *   created by android on 2019/4/11
 */
public interface BannerItem<T> {
    int getItemLayoutId();

    boolean isItemType(T item, int position, int dataCount);

    void onItemClick(T item, int position, int dataCount);

    void bindData(BannerHolder holder, T item, int position, int dataCount);


}
