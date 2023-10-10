package com.test.banner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.test.banner.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btSingle;
    Button btMulti;
    Button btCustomDrawable;
    Button btLocation;
    Button btTest;
    Button btTestList;
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

        btTestList = findViewById(R.id.btTestList);
        btTestList.setOnClickListener(this);

        btSingle.setOnClickListener(this);
        btMulti.setOnClickListener(this);
        btCustomDrawable.setOnClickListener(this);
        btLocation.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btSingle) {
            startActivity(new Intent(this, BannerActivity.class));
        } else if (id == R.id.btMulti) {
            startActivity(new Intent(this, MultiBannerActivity.class));
        } else if (id == R.id.btCustomDrawable) {
            intent = new Intent(this, MultiBannerActivity.class);
            intent.putExtra(MultiBannerActivity.intent_type, MultiBannerActivity.type_custom_drawable);
            startActivity(intent);
        } else if (id == R.id.btLocation) {
            startActivity(new Intent(this, LocationActivity.class));
        } else if (id == R.id.btTest) {
            startActivity(new Intent(this, TestActivity.class));
        }else if (id == R.id.btTestList) {
            startActivity(new Intent(this, ListActivity.class));
        }
    }
}
