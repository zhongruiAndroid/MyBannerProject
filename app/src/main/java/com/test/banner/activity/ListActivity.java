package com.test.banner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.adapter.CustomAdapter;
import com.github.adapter.CustomViewHolder;
import com.github.adapter.LoadMoreAdapter;
import com.github.banner.MyBannerView;
import com.github.banner.listener.OnPagerListener;
import com.test.banner.ImageURL;
import com.test.banner.R;
import com.test.banner.TestBean;
import com.test.banner.viewitem.TestViewItem1;
import com.test.banner.viewitem.TestViewItem2;
import com.test.banner.viewitem.TestViewItem3;
import com.zr.FrameLayoutAdapt;
import com.zr.LinearLayoutAdapt;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private CustomAdapter adapter;
    private MyBannerView banner;
    private LinearLayoutAdapt rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rootView = findViewById(R.id.rootView);
        rvList = findViewById(R.id.rvList);
        Button btChangeOrder=findViewById(R.id.btChangeOrder);

        Button btChangeDirection=findViewById(R.id.btChangeDirection);
        btChangeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banner.getDirection()== RecyclerView.HORIZONTAL){
                    btChangeOrder.setText("切换滑动方向(从上往下or从下往上)");
                    banner.setDirection(RecyclerView.VERTICAL);
                }else{
                    btChangeOrder.setText("切换滑动方向(从左往右or从左往右)");
                    banner.setDirection(RecyclerView.HORIZONTAL);
                }
            }
        });
        btChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner.setReverse(!banner.isReverse());
            }
        });


        adapter = new CustomAdapter<String>(android.R.layout.test_list_item) {
            @Override
            public void bindData(CustomViewHolder holder, int position, String item) {
                holder.setText(android.R.id.text1,item);
            }
        };

        initData();

        List<String> list=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(""+i);
        }
        adapter.setList(list);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);


    }

    private void initData() {
        initData(true);
    }
    private void initData(boolean play) {
        /*app:autoPlay="true"
        app:direction="horizontal"
        app:reverse="false"
        app:timeInterval="2000"
        app:timeScroll="600"
        app:useGesture="true"*/

       /* rootView.post(new Runnable() {
            @Override
            public void run() {*/
                float sizeRatio = rootView.getSizeRatio();

                banner = new MyBannerView(ListActivity.this);
                banner.setViewHeight(400);
                banner.setAutoPlay(true);
                banner.setDirection(RecyclerView.HORIZONTAL);
                banner.setHorizontalSlideScale(sizeRatio);
                banner.setReverse(false);
                banner.setDetachReset(false);
//                banner.setBannerPadding(20,20,20,20);
                banner.setUseGesture(true);
                banner.setTimeInterval(500);
                banner.setTimeScroll(3000);
                /*设置indicator间距*/
                banner.setIndicatorDistance(banner.dp2Px(8));
                /*设置indicator drawable*/
                /*如果drawable是设置的shape且两种状态的宽高不一致，建议在shape.xml中指定宽高，banner和布局则不用另外设置(只针对选中indicator和没选中indicator宽高不一致的情况)*/
                banner.setIndicatorSelectDrawable(getResources().getDrawable(R.drawable.indicator_select));
                banner.setIndicatorUnSelectDrawable(getResources().getDrawable(R.drawable.indicator_normal));

                /*设置对象列表*/
                banner.setList(ImageURL.getTestBeanList());/*对应OnPagerListener里面的数据类型*/
                /*为banner多视图绑定数据*/
                banner.addBannerItem(new TestViewItem1(ListActivity.this));
                banner.addBannerItem(new TestViewItem2(ListActivity.this));
                banner.addBannerItem(new TestViewItem3(ListActivity.this));

                /*监听滑动*/
                banner.setPagerListener(new OnPagerListener<TestBean>() {
                    @Override
                    public void onPageSelected(TestBean item, int position, int beforePosition) {
                        /*item为当前数据*/
                        Log.i("BannerActivity=====","上个item下标："+beforePosition+"=========当前item下标："+position);
                    }
                });
                if(play){
                    /*开始轮播*/
                    banner.start();
                }

                adapter.addHeaderView(banner);
                adapter.notifyDataSetChanged();
//            }
        /*});*/

    }
}