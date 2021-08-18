package com.tencent.lib.multi.core;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是代理Adapter 的生命周期，
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
public abstract class MultiHelper<T, VH extends RecyclerView.ViewHolder> {

    public static final int INVALID_VIEW_TYPE = -1;
    private final RecyclerView.Adapter realAdapter;

    /**
     * ItemType集合，以其id为key，ItemType为value。
     */
    private final SparseArray<ItemType<T, VH>> mItemTypes = new SparseArray<>();


    public MultiHelper(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }


    public final int getItemViewType(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return INVALID_VIEW_TYPE;
        }

        final T bean = getItem(position);
        final ItemType<T, VH> currentType = findCurrentType(bean, position);
        return currentType == null ? INVALID_VIEW_TYPE : currentType.getClass().hashCode();
    }

    /**
     * 遍历查找当前position对应的ItemType。
     *
     * @param data
     * @param position
     * @return
     */
    @Nullable
    private ItemType<T, VH> findCurrentType(T data, int position) {
        //为当前position 匹配它的ItemType(一般一个页面item类型不会很多，遍历代价不大）
        for (int i = 0; i < mItemTypes.size(); i++) {
            final ItemType<T, VH> type = mItemTypes.valueAt(i);
            if (type.matchItemType(data, position)) {
                return type;
            }
        }
        return null;
    }

    @NotNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType<T, VH> type = mItemTypes.get(viewType);
        if (viewType == INVALID_VIEW_TYPE || type == null) {//表示无效
            throw new IllegalStateException("ItemType 不合法：viewType==" + viewType + " ItemType==" + type);
        }
        final VH holder = type.onCreateViewHolder(parent);
        type.onViewHolderCreated(holder, this);
        return holder;
    }

    public final void onBindViewHolder(@NonNull VH holder,
            int position,
            @NonNull List<Object> payloads) {
        /*统一捕获由position引发的可能异常*/
        try {
            final T bean = getItem(position);
            if (bean == null) {
                return;
            }
            final ItemType<T, VH> currentType = findCurrentType(bean, position);
            if (currentType == null) {
                return;
            }
            if (payloads.isEmpty()) {
                currentType.onBindViewHolder(holder, bean, position);
            }
            /*局部刷新*/
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
    public final MultiHelper<T, VH> addItemType(ItemType<T, VH> type) {
        if (type == null) {
            return this;
        }
        //getClass().hashCode():确保一种item类型只有一个对应的ItemType实例。
        mItemTypes.put(type.getClass().hashCode(), type);
        return this;
    }


}
