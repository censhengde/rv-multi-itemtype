package com.tencent.lib.multi.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * Author：岑胜德 on 2021/1/6 14:58
 * <p>
 * 说明：
 */
public class MultiViewHolder extends RecyclerView.ViewHolder {

    //由于findViewById频繁调用比较消耗性能，所以要缓存
    private  SparseArray<View> mCacheViews;

    ViewBinding vb;

    public MultiViewHolder(ViewBinding vb) {
        this(vb.getRoot());
        this.vb = vb;
    }

    public MultiViewHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T getView(@IdRes int id) {
        if (mCacheViews==null){
            mCacheViews = new SparseArray<>();
        }
        T v = (T) mCacheViews.get(id);
        if (v == null) {
            v = itemView.findViewById(id);
            if (v == null) {
                throw new IllegalStateException("未找到View");
            }
            mCacheViews.put(id, v);
        }
        return v;
    }

}
