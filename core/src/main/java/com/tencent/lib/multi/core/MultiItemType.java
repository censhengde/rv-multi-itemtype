package com.tencent.lib.multi.core;

import android.view.ViewGroup;
import androidx.annotation.NonNull;

/**
 * Author：岑胜德 on 2021/6/23 19:14
 *
 * 说明：
 */
public abstract class MultiItemType<T> extends AbstractItemType<T,MultiViewHolder> {

    /**
     * 这里需要重写onCreateViewHolder方法，否则 MultiItemType 的子类无法隔代反射到 VH 参数
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return MultiViewHolder.create(parent.getContext(),parent,getItemLayoutRes());
    }
}
