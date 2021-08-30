package com.tencent.lib.multi.core;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是代理Adapter 的生命周期，
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
public abstract class MultiHelper<T, VH extends RecyclerView.ViewHolder> {


    /**
     * ItemType集合.
     */
    private final SparseArray<ItemType<T, VH>> mItemTypePool = new SparseArray<>();

    public final int getItemViewType(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return RecyclerView.INVALID_TYPE;
        }
        final T data = getItem(position);
        final ItemType<T, VH> currentType = findCurrentItemType(data, position);
        return currentType == null ? RecyclerView.INVALID_TYPE : currentType.getClass().hashCode();
    }


    /**
     * 遍历查找当前position对应的ItemType。
     *
     * @param data
     * @param position
     * @return
     */
    @Nullable
    private ItemType<T, VH> findCurrentItemType(T data, int position) {
        //为当前position 匹配它的ItemType
        for (int i = 0; i < mItemTypePool.size(); i++) {
            final ItemType<T, VH> type = mItemTypePool.valueAt(i);
            if (type.matchItemType(data, position)) {
                return type;
            }
        }
        return null;
    }

    @NotNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType<T, VH> type = mItemTypePool.get(viewType);
        if (viewType == RecyclerView.INVALID_TYPE || type == null) {//表示无效
            throw new IllegalStateException("ItemType 不合法：viewType==" + viewType + " ItemType==" + type);
        }
        final VH holder = type.onCreateViewHolder(parent);
        type.onViewHolderCreated(holder, this);
        return holder;
    }

    public final void onBindViewHolder(@NonNull VH holder,
            int position,
            @NonNull List<Object> payloads) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        /*统一捕获由position引发的可能异常*/
        try {
            final ItemType<T, VH> currentType = mItemTypePool.get(holder.getItemViewType());
            final T bean = getItem(position);
            if (bean == null || currentType == null) {
                return;
            }
            if (payloads.isEmpty()) {
                currentType.onBindViewHolder(holder, bean, position);
            }
            /*item局部刷新*/
            else {
                currentType.onBindViewHolder(holder, bean, position, payloads);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    public abstract T getItem(int position);


    /**
     * 注册ItemType
     *
     * @param type
     * @return
     */
    public final void addItemType(ItemType<T, VH> type) {
        if (type == null) {
            return;
        }
        //getClass().hashCode():确保一种item类型只有一个对应的ItemType实例。
        mItemTypePool.put(type.getClass().hashCode(), type);
    }


}
