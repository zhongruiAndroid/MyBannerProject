package com.test.banner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.banner.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btSingle;
    Button btMulti;
    Button btCustomDrawable;
    Button btLocation;
    Button btTest;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSingle = findViewById(R.id.btSingle);
        btMulti = findViewById(R.id.btMulti);
        btCustomDrawable = findViewById(R.id.btCustomDrawable);
        btLocation = findViewById(R.id.btLocation);

        btTest = findViewById(R.id.btTest);
        btTest.setOnClickListener(this);

        btSingle.setOnClickListener(this);
        btMulti.setOnClickListener(this);
        btCustomDrawable.setOnClickListener(this);
        btLocation.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSingle:
                startActivity(new Intent(this, BannerActivity.class));
                break;
            case R.id.btMulti:
                startActivity(new Intent(this, MultiBannerActivity.class));
                break;
            case R.id.btCustomDrawable:
                intent = new Intent(this, MultiBannerActivity.class);
                intent.putExtra(MultiBannerActivity.intent_type,MultiBannerActivity.type_custom_drawable);
                startActivity(intent);
                break;
            case R.id.btLocation:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.btTest:
                startActivity(new Intent(this, TestActivity.class));
                break;
        }
    }
}
