package com.test.banner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.banner.MyBannerView;
import com.github.banner.listener.OnPagerListener;
import com.test.banner.ImageURL;
import com.test.banner.PhoneUtils;
import com.test.banner.viewitem.StringUrlViewItem;
import com.test.banner.vm.BannerVM;
import com.test.banner.R;

public class BannerActivity extends AppCompatActivity {
    Activity activity;
    MyBannerView banner;
    Button btChangeDirection;
    Button btChangeOrder;
    Button btStop;
    CheckBox cbUseGesture;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_banner);

        ll=findViewById(R.id.ll);
        banner=findViewById(R.id.banner);
        btStop=findViewById(R.id.btStop);
        cbUseGesture=findViewById(R.id.cbUseGesture);
        btChangeOrder=findViewById(R.id.btChangeOrder);

        btChangeDirection=findViewById(R.id.btChangeDirection);

        setClickListener();

        /*if (banner != null) {
            if (banner.getLayoutParams() != null) {
                banner.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                banner.getLayoutParams().height = PhoneUtils.dipToPx(activity,230);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PhoneUtils.dipToPx(activity,230));
                banner.setLayoutParams(layoutParams);
            }
            banner.setBannerHeight(PhoneUtils.dipToPx(activity,200));
        }*/

        initData();

        BannerVM bannerVM=new BannerVM(activity);
        bannerVM.setBannerHeight(PhoneUtils.dipToPx(activity,200));

        bannerVM.setBannerData(ImageURL.getStringURLList());
        ll.addView(bannerVM.getBannerView());

    }

    private void initData() {
        /*此处直接设置String  Url ,也可以设置 对象列表(MultiBannerActivity可见)*/
        banner.setList(ImageURL.getStringURLList());
        /*为banner视图绑定数据*/
        banner.addBannerItem(new StringUrlViewItem(this));
        /*监听滑动*/
        banner.setPagerListener(new OnPagerListener<String>() {
            @Override
            public void onPageSelected(String item, int position, int beforePosition) {
                /*item为当前数据*/
                Log.i("BannerActivity=====","上个item下标："+beforePosition+"=========当前item下标："+position);
            }
        });
        /*开始轮播*/
        banner.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*此处代码主要是减少cpu的资源消耗,不写对内存影响不大*/
        banner.startAutoPlay();
    }
    @Override
    protected void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    private void setClickListener() {
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
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(activity,TranslateActivity.class));
            }
        });

        cbUseGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                banner.setUseGesture(isChecked);
            }
        });
    }
}
