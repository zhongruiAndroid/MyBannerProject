package com.test.banner;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;

/***
 *   created by android on 2019/4/12
 */
public class TestViewItem  implements com.github.banner.BannerItem<TestBean> {
    private Activity activity;

    public TestViewItem(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item;
    }
    @Override
    public boolean isItemType(TestBean item, int position, int dataCount) {
        return item.itemType==1;
    }
    @Override
    public void onItemClick(TestBean item, int position, int dataCount) {
        Toast.makeText(activity,"点击"+position+"下标的"+item.title,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void bindData(BannerHolder holder, TestBean item, int position, int dataCount) {
        ImageView iv = holder.getView(R.id.iv);
        Glide.with(iv.getContext()).load(item.imageUrl).into(iv);
    }
}
