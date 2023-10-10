package com.test.banner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.banner.MyBannerView;
import com.github.banner.listener.OnPagerListener;
import com.test.banner.ImageURL;
import com.test.banner.R;
import com.test.banner.TestBean;
import com.test.banner.viewitem.TestViewItem1;
import com.test.banner.viewitem.TestViewItem2;
import com.test.banner.viewitem.TestViewItem3;

public class MultiBannerActivity extends AppCompatActivity {
    public static final String intent_type="intent_type";
    public static final int type_custom_drawable=1;
    public static final int type_custom_layout=2;

    Activity activity;
    MyBannerView banner;
    Button btChangeDirection;
    Button btChangeOrder;
    Button btStop;
    CheckBox cbUseGesture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_multi_banner);

        banner=findViewById(R.id.banner);
        btStop=findViewById(R.id.btStop);
        cbUseGesture=findViewById(R.id.cbUseGesture);
        btChangeOrder=findViewById(R.id.btChangeOrder);

        btChangeDirection=findViewById(R.id.btChangeDirection);

        setClickListener();


        int type = getIntent().getIntExtra(intent_type, 0);
        if(type==type_custom_drawable){
            /*自定义drawable-indicator*/

            /*设置indicator间距*/
            banner.setIndicatorDistance(banner.dp2Px(8));
            /*设置indicator drawable*/
            /*如果drawable是设置的shape且两种状态的宽高不一致，建议在shape.xml中指定宽高，banner和布局则不用另外设置(只针对选中indicator和没选中indicator宽高不一致的情况)*/
            banner.setIndicatorSelectDrawable(getResources().getDrawable(R.drawable.indicator_select));
            banner.setIndicatorUnSelectDrawable(getResources().getDrawable(R.drawable.indicator_normal));
        }
        initData();
    }

    private void initData() {
        /*设置对象列表*/
        banner.setList(ImageURL.getTestBeanList());/*对应OnPagerListener里面的数据类型*/
        /*为banner多视图绑定数据*/
        banner.addBannerItem(new TestViewItem1(this));
        banner.addBannerItem(new TestViewItem2(this));
        banner.addBannerItem(new TestViewItem3(this));

        /*监听滑动*/
        banner.setPagerListener(new OnPagerListener<TestBean>() {
            @Override
            public void onPageSelected(TestBean item, int position, int beforePosition) {
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
