package com.test.banner.vm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;


import androidx.annotation.IdRes;

import java.util.Calendar;

/***
 *   created by android on 2019/2/12
 */
public abstract class BaseVM implements View.OnClickListener {
    protected View mView;
    protected Activity mContext;

    protected abstract int getContentView();

    public abstract void initView(View view);

    public void initViewAfter(View view) {
    }

    public abstract void onNoDoubleClick(View v);
    private int viewClickId = -1;
    private static int MIN_CLICK_DELAY_TIME = 800;
    private long lastClickTime = 0;
    @Override
    public void onClick(View v) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        boolean isFastClick = true;
        if (timeInMillis - lastClickTime>MIN_CLICK_DELAY_TIME) {
            lastClickTime=timeInMillis;
            isFastClick=false;
        }
        if (viewClickId == v.getId() && isFastClick) {
            return;
        } else {
            viewClickId = -1;
            onNoDoubleClick(v);
        }
    }

    public void initRxBus() {

    }

    public BaseVM(Activity context) {
        mContext = context;
        int contentViewResId = getContentView();
        if (contentViewResId > 0) {
            this.mView = LayoutInflater.from(context).inflate(contentViewResId, null);
            initView(mView);
            initViewAfter(mView);
            initRxBus();
        }
    }

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
        initView(mView);
        initViewAfter(mView);
        initRxBus();
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        } else {
            throw new IllegalStateException(this.getClass().getName() + "view is null");
        }
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }


    public void onDestroy() {
    }

    /***********************************************************/



}
