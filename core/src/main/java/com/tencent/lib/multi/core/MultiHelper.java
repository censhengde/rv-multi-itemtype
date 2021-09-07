package com.tencent.lib.multi.core;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是Adapter 生命周期的代理类，
 *      将 Adapter 生命周期分发给了position对应的ItemType。
 */
public abstract class MultiHelper<T, VH extends RecyclerView.ViewHolder>  {


    /**
     * ItemType集合.
     */
    private final SparseArray<ItemType> mItemTypePool = new SparseArray<>();

    public final long getItemId(int position) {
        final ItemType type = findCurrentItemType(getItem(position), position);
        return type == null ? NO_ID : type.getItemId(position);
    }

    public final int getItemViewType(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return RecyclerView.INVALID_TYPE;
        }
        final T data = getItem(position);
        final ItemType currentType = findCurrentItemType(data, position);
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
    private ItemType findCurrentItemType(T data, int position) {
        //为当前position 匹配它的ItemType
        for (int i = 0; i < mItemTypePool.size(); i++) {
            final ItemType<?, ? extends RecyclerView.ViewHolder> type = mItemTypePool.valueAt(i);
            if (type.matchItemType(data, position)) {
                return type;
            }
        }
        return null;
    }

    @SuppressWarnings("uncheck all")
    @NotNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType type = mItemTypePool.get(viewType);
        if (viewType == RecyclerView.INVALID_TYPE || type == null) {//表示无效
            /*一般由于ItemType matchItemTYpe方法实现错误引起的异常*/
            throw new RuntimeException("ItemType 不合法：viewType==" + viewType + " ItemType==" + type
                    + " 请检查 ItemType matchItemType方法实现是否正确。");
        }
        final VH holder = (VH) type.onCreateViewHolder(parent);
        type.onViewHolderCreated(holder, this);
        return holder;
    }

    @SuppressWarnings("uncheck all")
    public final void onBindViewHolder(@NonNull VH holder,
            int position,
            @NonNull List<Object> payloads) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        /*统一捕获由position引发的可能异常*/
        final ItemType currentType = mItemTypePool.get(holder.getItemViewType());
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

    }

    @SuppressWarnings("uncheck all")
    public final void onViewRecycled(@NotNull VH holder) {
        final ItemType type = mItemTypePool.get(holder.getItemViewType());
        if (type != null) {
            type.onViewRecycled(holder);
        }
    }

    public final boolean onFailedToRecycleView(@NonNull VH holder) {
        final ItemType type = mItemTypePool.get(holder.getItemViewType());
        return type != null && type.onFailedToRecycleView(holder);
    }

    public final void onViewAttachedToWindow(@NonNull VH holder) {
        final ItemType type = mItemTypePool.get(holder.getItemViewType());
        if (type != null) {
            type.onViewAttachedToWindow(holder);
        }
    }

    public final void onViewDetachedFromWindow(@NonNull VH holder) {
        final ItemType type = mItemTypePool.get(holder.getItemViewType());
        if (type != null) {
            type.onViewDetachedFromWindow(holder);
        }
    }

    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < mItemTypePool.size(); i++) {
            mItemTypePool.valueAt(i).onAttachedToRecyclerView(recyclerView);
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    public final void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < mItemTypePool.size(); i++) {
            mItemTypePool.valueAt(i).onDetachedFromRecyclerView(recyclerView);
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
    public final void addItemType(ItemType type) {
        if (type == null) {
            return;
        }
        //getClass().hashCode():确保一种item类型只有一个对应的ItemType实例。
        mItemTypePool.put(type.getClass().hashCode(), type);
    }


}
