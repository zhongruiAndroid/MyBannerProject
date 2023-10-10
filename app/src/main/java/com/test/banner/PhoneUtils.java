package com.test.banner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhoneUtils {
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }
    public static DisplayMetrics getDisplayMetrics(Context context){
        return context.getResources().getDisplayMetrics();
    }
    public static int getPhoneWidth(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }
    public static int getPhoneHeight(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (pxValue / scale + 0.5f);
    }

    public static float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int)(pxValue / scale + 0.5f);
    }

    public static int dipToPx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int)(dipValue * scale + 0.5f);
    }
}
