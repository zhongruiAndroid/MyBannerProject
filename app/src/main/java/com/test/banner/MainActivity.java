package com.test.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.github.banner.MyBannerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    MyBannerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv=findViewById(R.id.rv);
        List<String> list=new ArrayList<>();
        list.add("https://js.isheji5.com/album/15538442465c9dc8169a308.png");
        list.add("https://js.isheji5.com/album/15547179695cab1d11c8ed9.png");
        list.add("https://js.isheji5.com/album/%E6%8B%9B%E8%81%98.png");
        list.add("https://js.isheji5.com/album/%E6%98%A5%E5%AD%A3.png");

        adapter=new MyBannerAdapter();
        adapter.addItem(new BannerItem());
        adapter.setList(list);

//        new PagerSnapHelper().attachToRecyclerView(rv);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(adapter);

//        rv.scrollToPosition(Integer.MAX_VALUE/2);

    }
}
