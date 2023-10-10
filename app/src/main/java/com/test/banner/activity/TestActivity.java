package com.test.banner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.test.banner.ImageURL;
import com.test.banner.PhoneUtils;
import com.test.banner.R;
import com.test.banner.vm.BannerVM;

/***
 *   created by android on 2019/5/30
 */
public class TestActivity extends AppCompatActivity {

    Activity activity;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_test);
        ll = findViewById(R.id.ll);


        BannerVM bannerVM=new BannerVM(activity);
        bannerVM.setBannerHeight(PhoneUtils.dipToPx(activity,200),PhoneUtils.dipToPx(activity,200));
        bannerVM.setBannerData(ImageURL.getStringURLList());

        ll.addView(bannerVM.getBannerView());


    }
}