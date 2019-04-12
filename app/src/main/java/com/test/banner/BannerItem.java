package com.test.banner;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;

/***
 *   created by android on 2019/4/12
 */
public class BannerItem implements com.github.banner.BannerItem<String> {
    @Override
    public int indicatorSelectView() {
        return 0;
    }

    @Override
    public int indicatorNormalView() {
        return 0;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item;
    }

    @Override
    public boolean isItemType(String item, int position, int dataCount) {
        return true;
    }

    @Override
    public void bindData(BannerHolder holder, String item, int position, int dataCount) {
        ImageView iv = holder.getView(R.id.iv);

        Glide.with(iv.getContext()).load(item).into(iv);
    }
}
