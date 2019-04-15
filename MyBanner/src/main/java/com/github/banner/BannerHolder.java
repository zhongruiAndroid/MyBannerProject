package com.github.banner;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/***
 *   created by android on 2019/4/11
 */
public class BannerHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> viewSparseArray = new SparseArray<>();
    public BannerHolder(@NonNull View itemView) {
        super(itemView);
    }
    public <T extends View> T getView(int resId) {
        View view = viewSparseArray.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            viewSparseArray.put(resId, view);
        }
        return (T) view;
    }
    /******************************TextView************************************/
    public BannerHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BannerHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }
    public BannerHolder setText(int viewId, CharSequence value, TextView.BufferType bufferType) {
        TextView view = getView(viewId);
        view.setText(value,bufferType);
        return this;
    }
    public BannerHolder setText(int viewId, @StringRes int value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }
    public BannerHolder setText(int viewId, @StringRes int value, TextView.BufferType bufferType) {
        TextView view = getView(viewId);
        view.setText(value,bufferType);
        return this;
    }
    public BannerHolder setText(int viewId, char[] value, int start, int len) {
        TextView view = getView(viewId);
        view.setText(value,start,len);
        return this;
    }

    public ImageView getImageView(int viewId){
        return getView(viewId);
    }
}
