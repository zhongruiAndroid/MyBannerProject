package com.github.banner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/***
 *   created by android on 2019/4/11
 */
public class MyBannerAdapter<T> extends RecyclerView.Adapter<BannerHolder> {
    private List<T> list=new ArrayList<>();
    private BannerItemManager<T> bannerItemManager =new BannerItemManager();

    public void setList(List<T> list) {
        if(list==null||list.size()==0){
            return;
        }
        this.list = list;
    }


    public void addItem(BannerItem item){
        bannerItemManager.addBannerItem(item);
    }
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        BannerItem bannerItem = bannerItemManager.getBannerItem(viewType);
        BannerHolder holder=new BannerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(bannerItem.getItemLayout(),viewGroup,false));
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
        bannerItemManager.bindData(holder,getItem(position),getRealDataPosition(position),getRealCount());
    }
    @Override
    public int getItemViewType(int position) {
        return bannerItemManager.getItemViewType(getItem(position),getRealDataPosition(position),getRealCount());
    }
    @Override
    public int getItemCount() {
        if(getRealCount()==0){
            return 0;
        }
        return Integer.MAX_VALUE;
    }
    public int getRealCount() {
        return list==null?0:list.size();
    }

    public int getRealDataPosition(int position){
        return position%getRealCount();
    }
    public T getItem(int position){
        if(list==null){
            return null;
        }
        return list.get(getRealDataPosition(position));
    }
}
