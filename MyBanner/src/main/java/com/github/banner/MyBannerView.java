package com.github.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.banner.listener.OnPagerListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/***
 *   created by android on 2019/4/11
 */
public class MyBannerView extends FrameLayout {
    /**********************************************************************************/
    private OnPagerListener pagerListener;

    private LayoutManager layoutManager;


    @IntDef({RecyclerView.HORIZONTAL, RecyclerView.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface orientation {
    }

    private int horizontalSlideOffset;
    private int verticalSlideOffset;

    private float horizontalSlideScale=1;
    private float verticalSlideScale=1;
    private int bannerHeight = -1;
    private int direction = RecyclerView.HORIZONTAL;
    /*轮播时间间隔*/
    private int timeInterval = 6000;
    /*滚动时间*/
    private int timeScroll = 600;
    /*是否自动轮播*/
    private boolean autoPlay = true;
    /*true:从左往右(从下往上)，false:从右往左(从上往下)*/
    private boolean reverse = false;
    /*是否可以手动滑动*/
    private boolean useGesture = true;
    private boolean indicatorHidden = false;
    private int indicatorDistance;

    public Drawable indicatorSelectDrawable;
    public Drawable indicatorUnSelectDrawable;
    private int indicatorSelectDrawableColor = -1;
    private int indicatorUnSelectDrawableColor = -1;
    private int indicatorDrawableWidth = -1;
    private int indicatorDrawableHeight = -1;
    private int indicatorDrawableRadius;
    private boolean clipToPadding;

    private Interpolator interpolator;

    private List<ImageView> indicatorList = new ArrayList<>();
    /*indicator预设置View*/
    private ImageView preImageView;

    /*保存不同状态的indicator*/
    private Drawable selectDrawable;
    private Drawable unSelectDrawable;
    private boolean detachReset=true;
    /**********************************************************************************/

    private LinearLayout indicatorParent;
    private BannerRecyclerView recyclerView;
    private MyBannerAdapter adapter;
    private List list = new ArrayList<>();
    private int beforeItemPosition;

    private Handler handler;
    private final int playBanner = 1000;

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
        bannerHeight = (int) typedArray.getDimension(R.styleable.MyBannerView_bannerHeight, -1);
        direction = typedArray.getInt(R.styleable.MyBannerView_direction, RecyclerView.HORIZONTAL);
        timeScroll = typedArray.getInt(R.styleable.MyBannerView_timeScroll, timeScroll);
        timeInterval = typedArray.getInt(R.styleable.MyBannerView_timeInterval, timeInterval);
        autoPlay = typedArray.getBoolean(R.styleable.MyBannerView_autoPlay, true);
        reverse = typedArray.getBoolean(R.styleable.MyBannerView_reverse, false);
        useGesture = typedArray.getBoolean(R.styleable.MyBannerView_useGesture, true);
        indicatorHidden = typedArray.getBoolean(R.styleable.MyBannerView_indicatorHidden, false);
        indicatorDistance = (int) typedArray.getDimension(R.styleable.MyBannerView_indicatorDistance, dp2Px(6));
        indicatorSelectDrawable = typedArray.getDrawable(R.styleable.MyBannerView_indicatorSelectDrawable);
        indicatorUnSelectDrawable = typedArray.getDrawable(R.styleable.MyBannerView_indicatorUnSelectDrawable);
        indicatorSelectDrawableColor = typedArray.getColor(R.styleable.MyBannerView_indicatorSelectDrawableColor, -1);
        indicatorUnSelectDrawableColor = typedArray.getColor(R.styleable.MyBannerView_indicatorUnSelectDrawableColor, -1);

        indicatorDrawableWidth = (int) typedArray.getDimension(R.styleable.MyBannerView_indicatorDrawableWidth, -1);
        indicatorDrawableHeight = (int) typedArray.getDimension(R.styleable.MyBannerView_indicatorDrawableHeight, -1);
        indicatorDrawableRadius = (int) typedArray.getDimension(R.styleable.MyBannerView_indicatorDrawableRadius, 0);

        horizontalSlideOffset = (int) typedArray.getDimension(R.styleable.MyBannerView_horizontalSlideOffset, 0);
        verticalSlideOffset = (int) typedArray.getDimension(R.styleable.MyBannerView_verticalSlideOffset, 0);
        clipToPadding = typedArray.getBoolean(R.styleable.MyBannerView_android_clipToPadding, true);


        typedArray.recycle();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (getList() == null || getList().size() == 0) {
                    return;
                }
                bannerRunnable();
            }
        };
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case playBanner:
                        handler.post(runnable);
                        message = handler.obtainMessage();
                        message.what = playBanner;
                        handler.sendMessageDelayed(message, getTimeInterval() + getTimeScroll());
                        break;
                }
            }
        };

        adapter = new MyBannerAdapter();
        recyclerView = new BannerRecyclerView(getContext());
        recyclerView.setClipToPadding(clipToPadding);
        recyclerView.setUseGesture(useGesture);
        if (bannerHeight > 0) {
            recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bannerHeight));
        } else {
            recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        addView(recyclerView);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (recyclerView != null) {
            /*recyclerView.post(new Runnable() {
                @Override
                public void run() {

                }
            });*/
            int hSlideOffset = getHorizontalSlideOffset();
            int vSlideOffset = getVerticalSlideOffset();
            if (hSlideOffset <= 0) {
                setHorizontalSlideOffset(recyclerView.getMeasuredWidth() - recyclerView.getPaddingLeft() - recyclerView.getPaddingRight());
            }
            if (vSlideOffset <= 0) {
                setVerticalSlideOffset(recyclerView.getMeasuredHeight() - recyclerView.getPaddingTop() - recyclerView.getPaddingBottom());
            }
        }
    }

    public void setBannerPadding(int left, int top, int right, int bottom) {
        if (recyclerView != null) {
            recyclerView.setPadding(left, top, right, bottom);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView != null) {
                        recyclerView.setPadding(left, top, right, bottom);
                    }
                }
            });
        }
    }

    public List getList() {
        return list == null ? new ArrayList() : list;
    }

    public void setList(List list) {
        if (list == null) {
            getList().clear();
            return;
        }
        this.list = list;
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                beforeItemPosition = 0;
                initIndicator();
                startPlay();
            }
        });
    }

    private void initIndicator() {

        if (indicatorParent != null) {
            indicatorParent.removeAllViews();
        }
        indicatorList.clear();
        if (indicatorHidden == true) {
            return;
        }
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childAt = getChildAt(i);
            if (childAt instanceof LinearLayout) {
                if (indicatorParent == null) {
                    indicatorParent = (LinearLayout) childAt;
                    indicatorParent.setTag(R.id.indicatorId, "banner");
                }
                LinearLayout indicatorViewGroup = (LinearLayout) childAt;
                if (indicatorViewGroup.getTag(R.id.indicatorId) != null) {
                    indicatorParent = indicatorViewGroup;
                    break;
                }
            }
        }

        if (indicatorParent == null) {
            indicatorParent = new LinearLayout(getContext());
            indicatorParent.setTag(R.id.indicatorId, "banner");
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = dp2Px(6);
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            indicatorParent.setLayoutParams(layoutParams);
            addView(indicatorParent);
        }
        addIndicatorToView(indicatorParent);


    }

    private void addIndicatorToView(LinearLayout linearLayout) {
        //选择
        if (indicatorSelectDrawable != null) {
            if (indicatorSelectDrawableColor != -1) {
                indicatorSelectDrawable.mutate().setColorFilter(indicatorSelectDrawableColor, PorterDuff.Mode.SRC_ATOP);
            }
            selectDrawable = indicatorSelectDrawable;
        } else {
            if (indicatorSelectDrawableColor == -1) {
                indicatorSelectDrawableColor = Color.GRAY;
            }
            //默认设置一个
            if (indicatorDrawableWidth > 0 && indicatorDrawableHeight > 0) {
                selectDrawable = createDrawable(indicatorDrawableWidth, indicatorDrawableHeight, indicatorDrawableRadius, indicatorSelectDrawableColor);
            } else {
                selectDrawable = createDrawable(dp2Px(6), dp2Px(6), dp2Px(3), indicatorSelectDrawableColor);
            }
        }
        //未选择
        if (indicatorUnSelectDrawable != null) {
            if (indicatorUnSelectDrawableColor != -1) {
                indicatorUnSelectDrawable.mutate().setColorFilter(indicatorUnSelectDrawableColor, PorterDuff.Mode.SRC_ATOP);
            }

            unSelectDrawable = indicatorUnSelectDrawable;
        } else {
            if (indicatorUnSelectDrawableColor == -1) {
                indicatorUnSelectDrawableColor = Color.WHITE;
            }
            //默认设置一个
            if (indicatorDrawableWidth > 0 && indicatorDrawableHeight > 0) {
                unSelectDrawable = createDrawable(indicatorDrawableWidth, indicatorDrawableHeight, indicatorDrawableRadius, indicatorUnSelectDrawableColor);
            } else {
                unSelectDrawable = createDrawable(dp2Px(6), dp2Px(6), dp2Px(3), indicatorUnSelectDrawableColor);
            }

        }

        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageDrawable(selectDrawable);
            } else {
                imageView.setImageDrawable(unSelectDrawable);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (indicatorDrawableWidth > 0 && indicatorDrawableHeight > 0) {
                layoutParams = new LinearLayout.LayoutParams(indicatorDrawableWidth, indicatorDrawableHeight);
            }
            int mg = indicatorDistance / 2;
            layoutParams.setMargins(mg, mg, mg, mg);
            imageView.setLayoutParams(layoutParams);
            indicatorList.add(imageView);
            linearLayout.addView(imageView);
        }
    }

    private RecyclerView.OnScrollListener onScrollListener;
    private BannerPagerSnapHelper bannerPagerSnapHelper = new BannerPagerSnapHelper();

    private void startPlay() {
        if (adapter.hasMultiItem() == false) {
            return;
        }
        adapter.setList(list);
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.getOnFlingListener() == null) {
            bannerPagerSnapHelper.attachToRecyclerView(recyclerView);
//            new PagerSnapHelper().attachToRecyclerView(recyclerView);
        }
        layoutManager = new LayoutManager(getContext(), direction, reverse);
        layoutManager.setCalculateSpeedPerPixel(timeScroll, getWidth());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size() * 1000);
//        recyclerView.clearOnScrollListeners();
        if (onScrollListener != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
        }
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int nowPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (nowPosition == -1) {
                        return;
                    }

                    int realBeforePosition = adapter.getRealDataPosition(beforeItemPosition);
                    int realNowPosition = adapter.getRealDataPosition(nowPosition);

                    //设置对应的indicator
                    if (indicatorParent != null) {
                        //预设置的indicator复原
                        if (preImageView != null) {
                            preImageView.setImageDrawable(unSelectDrawable);
                        }
                        if (indicatorList != null && realBeforePosition < indicatorList.size() && realNowPosition < indicatorList.size()) {
                            indicatorList.get(realBeforePosition).setImageDrawable(unSelectDrawable);
                            indicatorList.get(realNowPosition).setImageDrawable(selectDrawable);
                        }
                    }

                    if (realBeforePosition != realNowPosition) {
                        if (pagerListener != null) {
                            pagerListener.onPageSelected(list.get(realNowPosition), realNowPosition, realBeforePosition);
                        }
                        beforeItemPosition = realNowPosition;
                    }

                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);


        startAutoPlay();
    }


    private void bannerRunnable() {
        if (recyclerView != null && layoutManager != null) {

            int position = layoutManager.findFirstVisibleItemPosition();
//            recyclerView.smoothScrollToPosition(position + 1);

            View childAt = recyclerView.getChildAt(0);
            if(childAt!=null){
                if (layoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
                    recyclerView.smoothScrollBy((isReverse() ? -childAt.getMeasuredWidth(): childAt.getMeasuredWidth()), 0, getInterpolator(), timeScroll);
                } else {
                    recyclerView.smoothScrollBy(0,(isReverse() ? -childAt.getMeasuredHeight() : childAt.getMeasuredHeight()), getInterpolator(), timeScroll);
                }
            }else{
                if (layoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
                    recyclerView.smoothScrollBy((int) (isReverse() ? -getHorizontalSlideOffset()*getHorizontalSlideScale() : getHorizontalSlideOffset()*getHorizontalSlideScale()), 0, getInterpolator(), timeScroll);
                } else {
                    recyclerView.smoothScrollBy(0, (int) (isReverse() ? -getVerticalSlideOffset()*getVerticalSlideScale() : getVerticalSlideOffset()*getVerticalSlideScale()), getInterpolator(), timeScroll);
                }
            }

            int realPosition = adapter.getRealDataPosition(position);
            int realNextPosition= adapter.getRealDataPosition(isReverse() ? position - 1 : position + 1);

            if (realPosition < 0 || realNextPosition < 0) {
                return;
            }
            //自动滑动结束之前设置对应的indicator提升体验
            if (indicatorParent != null && indicatorList != null) {
                ImageView imageView = indicatorList.get(realPosition);
                if (imageView != null) {
                    imageView.setImageDrawable(unSelectDrawable);
                }
                preImageView = indicatorList.get(realNextPosition);
                if (preImageView != null) {
                    preImageView.setImageDrawable(selectDrawable);
                }
            }
        }
    }

    public void startAutoPlay() {
        if (autoPlay == false || layoutManager == null || getList() == null || getList().size() == 0) {
            return;
        }
        handler.removeMessages(playBanner);

        message = handler.obtainMessage();
        message.what = playBanner;
        handler.sendMessageDelayed(message, getTimeInterval() + getTimeScroll());
    }

    public void stopAutoPlay() {
        if (autoPlay == false) {
            return;
        }
        handler.removeMessages(playBanner);
    }

    public <T> void addBannerItem(BannerItem<T> item) {
        adapter.addBannerItem(item);
    }

    public void setPagerListener(OnPagerListener pagerListener) {
        this.pagerListener = pagerListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (useGesture) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                    startAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }

        }

        return super.dispatchTouchEvent(event);
    }


    public int getBannerHeight() {
        return bannerHeight;
    }

    public void setBannerHeight(int bannerHeight) {
        this.bannerHeight = bannerHeight;
        if (recyclerView != null && this.bannerHeight > 0) {
            recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bannerHeight));
        }
    }

    public void setViewHeight(int viewHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = viewHeight;
        } else {
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeight));
        }
    }


    /*************************************bannerView 属性*********************************************/
    public int getHorizontalSlideOffset() {
        return horizontalSlideOffset;
    }

    public void setHorizontalSlideOffset(int horizontalSlideOffset) {
        this.horizontalSlideOffset = horizontalSlideOffset;
    }

    public float getHorizontalSlideScale() {
        return horizontalSlideScale;
    }

    public void setHorizontalSlideScale(float horizontalSlideScale) {
        this.horizontalSlideScale = horizontalSlideScale;
    }

    public float getVerticalSlideScale() {
        return verticalSlideScale;
    }

    public void setVerticalSlideScale(float verticalSlideScale) {
        this.verticalSlideScale = verticalSlideScale;
    }

    public int getVerticalSlideOffset() {
        return verticalSlideOffset;
    }

    public void setVerticalSlideOffset(int verticalSlideOffset) {
        this.verticalSlideOffset = verticalSlideOffset;
    }
    public int getDirection() {
        return direction;
    }

    public void setDirection(@orientation int direction) {
        if (direction == this.direction) {
            return;
        }
        this.direction = direction;
        if (layoutManager != null) {
            layoutManager.setOrientation(direction);
        }
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getTimeScroll() {
        return timeScroll;
    }

    public void setTimeScroll(int timeScroll) {
        this.timeScroll = timeScroll;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        /*if (layoutManager != null) {
            layoutManager.setReverseLayout(reverse);
        }*/
    }

    public boolean isUseGesture() {
        return useGesture;
    }

    public void setUseGesture(boolean useGesture) {
        this.useGesture = useGesture;
        if (recyclerView != null) {
            recyclerView.setUseGesture(useGesture);
        }
    }

    public boolean isIndicatorHidden() {
        return indicatorHidden;
    }

    public void setIndicatorHidden(boolean indicatorHidden) {
        this.indicatorHidden = indicatorHidden;
    }

    public int getIndicatorDistance() {
        return indicatorDistance;
    }

    public void setIndicatorDistance(int indicatorDistance) {
        this.indicatorDistance = indicatorDistance;
    }

    public Drawable getIndicatorSelectDrawable() {
        return indicatorSelectDrawable;
    }

    public void setIndicatorSelectDrawable(Drawable indicatorSelectDrawable) {
        this.indicatorSelectDrawable = indicatorSelectDrawable;
    }

    public Drawable getIndicatorUnSelectDrawable() {
        return indicatorUnSelectDrawable;
    }

    public void setIndicatorUnSelectDrawable(Drawable indicatorUnSelectDrawable) {
        this.indicatorUnSelectDrawable = indicatorUnSelectDrawable;
    }

    public int getIndicatorDrawableWidth() {
        return indicatorDrawableWidth;
    }

    public void setIndicatorDrawableWidth(int indicatorDrawableWidth) {
        this.indicatorDrawableWidth = indicatorDrawableWidth;
    }

    public int getIndicatorDrawableHeight() {
        return indicatorDrawableHeight;
    }

    public void setIndicatorDrawableHeight(int indicatorDrawableHeight) {
        this.indicatorDrawableHeight = indicatorDrawableHeight;
    }

    public int dp2Px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public LinearLayout getIndicatorParent() {
        return indicatorParent;
    }

    private BitmapDrawable createDrawable(int width, int height, int cornerRadius, @ColorInt int filledColor) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        if (filledColor == 0) {
            filledColor = Color.TRANSPARENT;
        }
        if (cornerRadius > 0) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(filledColor);
            canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);
        } else {
            canvas.drawColor(filledColor);
        }
        return new BitmapDrawable(getResources(), output);
    }

    public int getIndicatorSelectDrawableColor() {
        return indicatorSelectDrawableColor;
    }

    public void setIndicatorSelectDrawableColor(int indicatorSelectDrawableColor) {
        this.indicatorSelectDrawableColor = indicatorSelectDrawableColor;
    }

    public int getIndicatorUnSelectDrawableColor() {
        return indicatorUnSelectDrawableColor;
    }

    public void setIndicatorUnSelectDrawableColor(int indicatorUnSelectDrawableColor) {
        this.indicatorUnSelectDrawableColor = indicatorUnSelectDrawableColor;
    }

    public int getIndicatorDrawableRadius() {
        return indicatorDrawableRadius;
    }

    public void setIndicatorDrawableRadius(int indicatorDrawableRadius) {
        this.indicatorDrawableRadius = indicatorDrawableRadius;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public boolean isDetachReset() {
        return detachReset;
    }

    public void setDetachReset(boolean detachReset) {
        this.detachReset = detachReset;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
        if(isDetachReset()){
            resetLocation(0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!isDetachReset()){
            resetLocation();
        }
        startAutoPlay();
    }

    private void resetLocation() {
        resetLocation(-1);
    }

    private void resetLocation(int duration) {
        if (recyclerView != null) {
            View childAt;
            if (isReverse()) {
                childAt = recyclerView.getChildAt(0);
            } else {
                childAt = recyclerView.getChildAt(1);
            }
            if (childAt != null) {
                int hOffset = childAt.getLeft();
                int vOffset = childAt.getTop();
                if (getDirection() == RecyclerView.HORIZONTAL) {
                    if (hOffset != 0) {
                        float totalHOffset = getHorizontalSlideOffset();
                        recyclerView.smoothScrollBy(hOffset, vOffset, getInterpolator(), duration >= 0 ? duration : Math.abs((int) (timeScroll * hOffset / totalHOffset)));
                    }
                } else {
                    if (vOffset != 0) {
                        float totalVOffset = getVerticalSlideOffset();
                        recyclerView.smoothScrollBy(hOffset, vOffset, getInterpolator(), duration >= 0 ? duration : Math.abs((int) (timeScroll * vOffset / totalVOffset)));
                    }
                }
            }
        }
    }
}
