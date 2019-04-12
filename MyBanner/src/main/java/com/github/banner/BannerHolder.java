package com.github.banner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

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
}
