package com.test.banner.viewitem;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;
import com.github.banner.BannerItem;
import com.test.banner.R;

/***
 *   created by android on 2019/4/12
 */
public class StringUrlViewItem implements BannerItem<String> {
    private Activity activity;

    public StringUrlViewItem(Activity activity) {
        this.activity = activity;
    }
    @Override
    public int getItemLayoutId() {
        return R.layout.item1;
    }
    @Override
    public boolean isItemType(String item, int position, int dataCount) {
        /*如果是单布局banner，此处一定要返回true*/
        return true;//注意！注意！注意！注意！注意！注意！注意！注意！注意！
    }
    @Override
    public void onItemClick(String item, int position, int dataCount) {
        Toast.makeText(activity,"点击"+position+"下标的图片",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void bindData(BannerHolder holder, String item, final int position, int dataCount) {
        ImageView iv = holder.getView(R.id.iv);
        Glide.with(iv.getContext()).load(item).into(iv);
    }
}
