package com.github.banner.listener;

/***
 *   created by android on 2019/4/12
 */
public interface OnPagerListener<T> {
    void onPageSelected(T item,int position,int beforePosition);
}
