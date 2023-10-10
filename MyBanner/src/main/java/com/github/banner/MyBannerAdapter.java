package com.github.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/***
 *   created by android on 2019/4/11
 */
public class MyBannerAdapter<T> extends RecyclerView.Adapter<BannerHolder> {
    private long click_interval = 800; // 阻塞时间间隔
    private long lastClickTime;

    private List<T> list = new ArrayList<>();
    private BannerItemManager<T> bannerItemManager = new BannerItemManager();


    public void setList(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.list = list;
    }

    public void addBannerItem(BannerItem item) {
        bannerItemManager.addBannerItem(item);
    }

    public boolean hasMultiItem() {
        return bannerItemManager.hasMultiItem();
    }

    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final BannerItem bannerItem = bannerItemManager.getBannerItem(viewType);
        View itemLayout = bannerItem.getItemLayout(viewGroup.getContext(),viewGroup);
        if (itemLayout == null) {
            itemLayout = LayoutInflater.from(viewGroup.getContext()).inflate(bannerItem.getItemLayoutId(), viewGroup, false);
        }
        final BannerHolder holder = new BannerHolder(itemLayout);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime >= click_interval) {
                    lastClickTime = currentTime;
                    int adapterPosition = holder.getAbsoluteAdapterPosition();
                    bannerItem.onItemClick(getItem(adapterPosition), getRealDataPosition(adapterPosition), getRealCount());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
        bannerItemManager.bindData(holder, getItem(position), getRealDataPosition(position), getRealCount());
    }

    @Override
    public int getItemViewType(int position) {
        return bannerItemManager.getItemViewType(getItem(position), getRealDataPosition(position), getRealCount());
    }

    @Override
    public int getItemCount() {
        if (getRealCount() == 0) {
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return list == null ? 0 : list.size();
    }

    public int getRealDataPosition(int position) {
        if (getRealCount() == 0) {
            return 0;
        }
        return position % getRealCount();
    }

    public T getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(getRealDataPosition(position));
    }
}
