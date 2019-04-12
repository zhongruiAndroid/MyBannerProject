package com.github.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.banner.listener.OnPagerListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/***
 *   created by android on 2019/4/11
 */
public class MyBannerView extends RelativeLayout {
    /**********************************************************************************/
    private OnPagerListener pagerListener;

    public static final int horizontal = 0;
    public static final int vertical = 1;
    private LayoutManager layoutManager;
    private int beforeItemPosition;


    @IntDef({horizontal, vertical})
    @Retention(RetentionPolicy.SOURCE)
    public @interface orientation {
    }

    private int direction = horizontal;
    /*轮播时间间隔*/
    private int timeInterval = 6000;
    /*滚动时间*/
    private int timeScroll = 800;
    /*是否自动轮播*/
    private boolean autoPlay = true;
    /*true:从左往右(从下往上)，false:从右往左(从上往下)*/
    private boolean reverse = false;
    /*是否可以手动滑动*/
    private boolean useGesture = true;
    private float indicatorDistance = 20;
    private Drawable indicatorSelectDrawable;
    private Drawable indicatorUnSelectDrawable;

    private int indicatorSelectLayout = -1;
    private int indicatorUnSelectLayout = -1;
    /**********************************************************************************/

    private RecyclerView recyclerView;
    private MyBannerAdapter adapter;
    private List list = new ArrayList<>();

    private WeakReference<Handler> handler;
    private final int playBanner=1000;

    private Runnable runnable;
    private Message message;

    public MyBannerView(Context context) {
        super(context);
        init(null);
    }

    public MyBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyBannerView);
        direction = typedArray.getInt(R.styleable.MyBannerView_direction, horizontal);
        timeScroll = typedArray.getInt(R.styleable.MyBannerView_timeScroll, timeScroll);
        timeInterval = typedArray.getInt(R.styleable.MyBannerView_timeInterval, timeInterval);
        timeInterval=timeInterval+timeScroll;
        autoPlay = typedArray.getBoolean(R.styleable.MyBannerView_autoPlay, true);
        reverse = typedArray.getBoolean(R.styleable.MyBannerView_reverse, false);
        useGesture = typedArray.getBoolean(R.styleable.MyBannerView_useGesture, true);
        indicatorDistance = typedArray.getDimension(R.styleable.MyBannerView_indicatorDistance, indicatorDistance);
        indicatorSelectDrawable = typedArray.getDrawable(R.styleable.MyBannerView_indicatorSelectDrawable);
        indicatorUnSelectDrawable = typedArray.getDrawable(R.styleable.MyBannerView_indicatorUnSelectDrawable);
        indicatorSelectLayout = typedArray.getResourceId(R.styleable.MyBannerView_indicatorSelectLayout, -1);
        indicatorUnSelectLayout = typedArray.getResourceId(R.styleable.MyBannerView_indicatorUnSelectLayout, -1);

        typedArray.recycle();

        runnable=new Runnable() {
            @Override
            public void run() {
                bannerRunnable();
            }
        };
        handler=new WeakReference<>(new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case playBanner:
                        handler.get().post(runnable);
                        message = handler.get().obtainMessage();
                        message.what=playBanner;
                        handler.get().sendMessageDelayed(message,timeInterval);
                    break;
                }
            }
        });

        adapter = new MyBannerAdapter();
        recyclerView=new RecyclerView(getContext());
        recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        addView(recyclerView);

    }



    public List getList() {
        return list;
    }

    public void setList(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.list = list;
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        });
    }
    private void startPlay() {
        if (adapter.hasMultiItem()==false) {
            return;
        }
        adapter.setList(list);
        if(recyclerView==null){
            return;
        }
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        layoutManager = new LayoutManager(getContext(), direction, reverse);
        layoutManager.setCalculateSpeedPerPixel(timeScroll, getWidth());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size() * 1000);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (pagerListener != null) {
                        int nowPosition = layoutManager.findFirstVisibleItemPosition();

                        int realBeforePosition = adapter.getRealDataPosition(beforeItemPosition);
                        int realNowPosition = adapter.getRealDataPosition(nowPosition);
                        if(realBeforePosition!=realNowPosition){
                            pagerListener.onPageSelected(list.get(realNowPosition),realNowPosition, realBeforePosition);
                        }
                    }
                }
            }
        });


        startAutoPlay();
    }
    public void refresh(int direction){
        layoutManager.setOrientation(direction);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void bannerRunnable() {
        if (recyclerView != null && layoutManager != null) {

            beforeItemPosition = layoutManager.findFirstVisibleItemPosition();
            recyclerView.smoothScrollToPosition(beforeItemPosition + 1);

        }
    }

    public void startAutoPlay() {
        handler.get().removeMessages(playBanner);

        message = handler.get().obtainMessage();
        message.what=playBanner;
        handler.get().sendMessageDelayed(message,timeInterval);
    }

    public void stopAutoPlay() {
        handler.get().removeMessages(playBanner);
    }

    public <T> void addBannerItem(BannerItem<T> item) {
        adapter.addBannerItem(item);
    }

    public void setPagerListener(OnPagerListener pagerListener) {
        this.pagerListener = pagerListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("=====","===ACTION_DOWN11111==");
                stopAutoPlay();
                if (layoutManager != null) {
                    beforeItemPosition = layoutManager.findFirstVisibleItemPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
                startAutoPlay();
                break;
        }

        return super.dispatchTouchEvent(event);
    }


}
