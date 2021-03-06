package com.test.banner.viewitem;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;
import com.test.banner.R;
import com.test.banner.TestBean;

/***
 *   created by android on 2019/4/12
 */
public class TestViewItem3 implements com.github.banner.BannerItem<TestBean> {
    private Activity activity;

    public TestViewItem3(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item3;
    }
    @Override
    public boolean isItemType(TestBean item, int position, int dataCount) {
        /*如果itemType==3 使用item3.xml布局*/
        return item.itemType==3;
    }
    @Override
    public void onItemClick(TestBean item, int position, int dataCount) {
        Toast.makeText(activity,"点击"+position+"下标的"+item.title,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void bindData(BannerHolder holder, TestBean item,final int position, int dataCount) {
        ImageView iv = holder.getView(R.id.iv);
        Glide.with(iv.getContext()).load(item.imageUrl).into(iv);

        TextView title = holder.getView(R.id.title);
        title.setText(item.title);

    }
}
