package com.test.banner;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.banner.BannerHolder;
import com.github.banner.LayoutManager;
import com.github.banner.MyBannerAdapter;
import com.github.banner.MyBannerView;
import com.github.banner.listener.OnPagerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button change;
    Button pause;
    int a=0;
    ImageView iv;
    boolean flag=true;
    boolean flag2=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv=findViewById(R.id.iv);
        final MyBannerView banner = findViewById(R.id.banner);
        banner.setList(getList());
        banner.addBannerItem(new TestViewItem (this));
        banner.addBannerItem(new TestViewItem2(this));
        banner.addBannerItem(new TestViewItem3(this));

        banner.setPagerListener(new OnPagerListener<TestBean>() {
            @Override
            public void onPageSelected(TestBean item, int position, int beforePosition) {
                Log.i("=====", beforePosition + "===onPageSelected===" + position);
            }
        });
        banner.start();

        change=findViewById(R.id.change);
        pause=findViewById(R.id.pause);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2) {
                    banner.setDirection(RecyclerView.VERTICAL);
                }else{
                    banner.setDirection(RecyclerView.HORIZONTAL);
                }
                flag2=!flag2;
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    banner.stopAutoPlay();
                }else{
                    banner.startAutoPlay();
                }
                flag=!flag;
            }
        });
    }

    private List getList() {
        //也可以直接用String
        //List<String> list = new ArrayList<>();
        List<TestBean> list = new ArrayList<>();

        TestBean testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/15538442465c9dc8169a308.png";
        testBean.title = "banner1号";
        testBean.itemType = 1;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/15547179695cab1d11c8ed9.png";
        testBean.title = "banner3号";
        testBean.itemType = 3;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/%E6%8B%9B%E8%81%98.png";
        testBean.title = "banner2号";
        testBean.itemType = 2;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/%E6%98%A5%E5%AD%A3.png";
        testBean.title = "banner2号";
        testBean.itemType = 2;
        list.add(testBean);
        return list;
    }

}
