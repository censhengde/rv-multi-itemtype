package com.tencent.multirecyclerview.widget;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：
 */
final class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> implements IBuilder {
    private final SparseArray<ItemType<T>> position_itemType_map = new SparseArray<>(3);
    private final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>(3);
    private List<T> datas;
    private List<ItemType<?>> types;

    @Override
    public int getItemViewType(int position) {
        final int typeSize = types.size();
        //单样式
        if (typeSize == 1) {
            return types.get(0).getViewType();
        }
        //多样式
        if (typeSize > 1) {
            //先从缓存获取
            ItemType<T> itemType = position_itemType_map.get(position);
            if (itemType == null) {//如果缓存没有
                T data = datas.get(position);
                //为当前position的实体对象指定它的ItemType
                for (ItemType type : types) {
                    if (type.matchItemType(data, position)) {
                        itemType = type;
                        position_itemType_map.put(position, itemType);
                        viewType_itemType_map.put(itemType.getViewType(), itemType);
                    }
                }
            }
            return itemType == null ? 0 : itemType.getViewType();
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemType<T> type = viewType_itemType_map.get(viewType);
        MultiViewHolder holder = MultiViewHolder.newInstance(parent.getContext(),parent, type.getItemLayoutRes());
        holder.itemView.setOnClickListener((v) -> {
            type.onClickItemView(holder, datas.get(holder.getAdapterPosition()), holder.getAdapterPosition());
        });
        type.onInitItemSubViewListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        ItemType<T> type = position_itemType_map.get(position);
        type.onBindViewHolder(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void setDatas(@NonNull List<?> datas) {
        this.datas = (List<T>) datas;
    }

    @Override
    public void setItemTypes(@NonNull List<ItemType<?>> types) {
        this.types = types;
    }

    @Override
    public void setItemType(@NonNull ItemType<?> type) {
        types = new ArrayList<>(1);
        types.add(type);
    }
}
