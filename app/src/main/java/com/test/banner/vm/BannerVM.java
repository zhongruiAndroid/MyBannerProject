package com.test.banner.vm;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;
import com.github.banner.BannerItem;
import com.github.banner.MyBannerView;
import com.test.banner.R;

import java.util.List;

/***
 *   created by android on 2019/2/12
 */
public class BannerVM extends BaseVM {
    public BannerVM(Activity context) {
        super(context);
    }
    MyBannerView banner;
    @Override
    public int getContentView() {
        return R.layout.banner_layout;
    }
    @Override
    public void onResume() {
        super.onResume();
        //开始轮播
        banner.startAutoPlay();
    }
    @Override
    public void onPause() {
        super.onPause();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void initView(View view) {
        banner = view.findViewById(R.id.banner);
    }
    public void setBannerHeight(int height) {
        banner.setIndicatorUnSelectDrawable(null);
        banner.setIndicatorSelectDrawable(null);
        setBannerHeight(height, height);
    }

    public void setBannerHeight(int height, final int viewHeight) {
        if (banner != null) {
            /*if (banner.getLayoutParams() != null) {
                banner.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                banner.getLayoutParams().height = height;
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                banner.setLayoutParams(layoutParams);
            }*/
            banner.setViewHeight(height);
            banner.setBannerHeight(viewHeight);

        }
    }

    @Override
    public void onNoDoubleClick(View v) {

    }

    /*商品详情*/
    public void setBannerData(List<String> imageList) {
        banner.setList(imageList);
        banner.addBannerItem(new BannerItem<String>() {
            @Override
            public int getItemLayoutId() {
                return R.layout.banner_item;
            }

            @Override
            public boolean isItemType(String item, int position, int dataCount) {
                return true;
            }

            @Override
            public void onItemClick(String item, int position, int dataCount) {
            }

            @Override
            public void bindData(BannerHolder holder, String item, int position, int dataCount) {
                ImageView ivBannerItem = holder.getView(R.id.ivBannerItem);
                Glide.with(mContext).load(item).error(R.color.colorAccent).into(ivBannerItem);
            }
        });
        banner.start();
    }

    public MyBannerView getBannerView(){
        return banner;
    }


}
