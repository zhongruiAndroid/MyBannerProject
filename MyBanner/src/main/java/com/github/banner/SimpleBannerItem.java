package com.github.banner;

/***
 *   created by android on 2019/4/17
 */
public abstract class SimpleBannerItem<T> implements BannerItem<T> {
    @Override
    public boolean isItemType(T item, int position, int dataCount) {
        return true;
    }
    @Override
    public void onItemClick(T item, int position, int dataCount) {

    }
}
