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
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
public abstract class MultiHelper<T, VH extends RecyclerView.ViewHolder> {


    /**
     * MultiItem 集合.
     */
    private final SparseArray<MultiItem> mItemTypePool = new SparseArray<>();

    public final long getItemId(int position) {
        final MultiItem type = findCurrentItem(getItem(position), position);
        return type == null ? NO_ID : type.getItemId(position);
    }

    public final int getItemViewType(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return RecyclerView.INVALID_TYPE;
        }
        final T data = getItem(position);
        final MultiItem currentItem = findCurrentItem(data, position);
        return currentItem == null ? RecyclerView.INVALID_TYPE : currentItem.getItemType();
    }


    /**
     * 遍历查找当前position对应的ItemType。
     *
     * @param data
     * @param position
     * @return
     */
    @Nullable
    private MultiItem findCurrentItem(T data, int position) {
        //为当前position 匹配它的ItemType
        for (int i = 0; i < mItemTypePool.size(); i++) {
            final MultiItem item = mItemTypePool.valueAt(i);
            if (item.isMatchForMe(data, position)) {
                return item;
            }
        }
        return null;
    }

    @SuppressWarnings("uncheck all")
    @NotNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final MultiItem item = mItemTypePool.get(viewType);
        if (viewType == RecyclerView.INVALID_TYPE || item == null) {//表示无效
            /*一般由于ItemType matchItemTYpe方法实现错误引起的异常*/
            throw new RuntimeException("ItemType 不合法：viewType==" + viewType + " ItemType==" + item
                    + " 请检查 ItemType matchItemType方法实现是否正确。");
        }
        final VH holder = (VH) item.onCreateViewHolder(parent);
        item.onViewHolderCreated(holder, this);
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
        final MultiItem currentItem = mItemTypePool.get(holder.getItemViewType());
        final T bean = getItem(position);
        if (bean == null || currentItem == null) {
            return;
        }
        currentItem.onBindViewHolder(holder, bean, position, payloads);
    }

    @SuppressWarnings("uncheck all")
    public final void onViewRecycled(@NotNull VH holder) {
        final MultiItem item = mItemTypePool.get(holder.getItemViewType());
        if (item != null) {
            item.onViewRecycled(holder);
        }
    }

    public final boolean onFailedToRecycleView(@NonNull VH holder) {
        final MultiItem item = mItemTypePool.get(holder.getItemViewType());
        return item != null && item.onFailedToRecycleView(holder);
    }

    public final void onViewAttachedToWindow(@NonNull VH holder) {
        final MultiItem item = mItemTypePool.get(holder.getItemViewType());
        if (item != null) {
            item.onViewAttachedToWindow(holder);
        }
    }

    public final void onViewDetachedFromWindow(@NonNull VH holder) {
        final MultiItem item = mItemTypePool.get(holder.getItemViewType());
        if (item != null) {
            item.onViewDetachedFromWindow(holder);
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
     *  添加 MultiItem
     * @param item
     */
    public final void addMultiItem(MultiItem item) {
        if (item == null) {
            return;
        }
        mItemTypePool.put(item.getItemType(), item);
    }

}
