package com.test.banner.viewitem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;
import com.github.banner.BannerItem;
import com.test.banner.R;
import com.test.banner.TestBean;
import com.zr.FrameLayoutAdapt;

/***
 *   created by android on 2019/4/12
 */
public class TestViewItem1 implements com.github.banner.BannerItem<TestBean> {
    private Activity activity;

    public TestViewItem1(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item1;
    }

    @Override
    public View getItemLayout(Context context, ViewGroup viewGroup) {
        FrameLayoutAdapt inflate = (FrameLayoutAdapt) LayoutInflater.from(context).inflate(R.layout.item1, viewGroup,false);
        FrameLayoutAdapt.LayoutParams layoutParams = new FrameLayoutAdapt.LayoutParams(111, 111);
        layoutParams.getLayoutAdaptInfo().widthAdapt= (int) (context.getResources().getDisplayMetrics().density*250);
        inflate.setLayoutParams(layoutParams);
        return null;
    }

    @Override
    public boolean isItemType(TestBean item, int position, int dataCount) {
        /*如果itemType==1 使用item1.xml布局*/
        return item.itemType==1;
    }
    @Override
    public void onItemClick(TestBean item, int position, int dataCount) {
        Toast.makeText(activity,"点击"+position+"下标的"+item.title,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void bindData(BannerHolder holder, TestBean item, final int position, int dataCount) {
        ImageView iv = holder.getView(R.id.iv);
        Glide.with(iv.getContext()).load(item.imageUrl).into(iv);

    }
}
