package com.github.banner;

import android.util.SparseArray;

/***
 *   created by android on 2019/4/11
 */
public class BannerItemManager<T>  {
    private SparseArray<BannerItem> helperSparseArray=new SparseArray<>();

    public void addBannerItem(BannerItem helper){
        helperSparseArray.put(helperSparseArray.size(),helper);
    }
    public boolean hasMultiItem(){
        return helperSparseArray!=null&&helperSparseArray.size()>0;
    }
    public BannerItem getBannerItem(int viewType){
        return helperSparseArray.get(viewType);
    }
    public int getItemViewType(T item,int position,int dataCount){
        for (int i = 0; i < helperSparseArray.size(); i++) {
            BannerItem bannerItem = helperSparseArray.valueAt(i);
            if(bannerItem.isItemType(item,position,dataCount)){
                int viewType = helperSparseArray.keyAt(i);
                return viewType;
            }
        }
        throw new IllegalStateException("getItemViewType没有找到item布局,BannerItem.isItemType()请按需求返回true");
    }

    public void bindData(BannerHolder holder, T item, int position, int dataCount){
        for (int i = 0; i < helperSparseArray.size(); i++) {
            BannerItem bannerItem = helperSparseArray.valueAt(i);
            if(bannerItem.isItemType(item,position,dataCount)){
                bannerItem.bindData(holder,item,position,dataCount);
                return;
            }
        }
        throw new IllegalStateException("bindData没有找到item布局,BannerItem.isItemType()请按需求返回true");
    };
}
