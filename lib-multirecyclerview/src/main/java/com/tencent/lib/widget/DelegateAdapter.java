package com.tencent.lib.widget;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 * 说明：实现Item多样式的公共逻辑封装。
 */
 abstract class DelegateAdapter<T> {
    protected final SparseArray<ItemType<T>> position_itemType_map = new SparseArray<>();
    protected final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>(8);
    protected List<ItemType<T>> types;

//    public DelegateAdapter(Adapter<?> realdapter) {
//        this.realAdapter = realdapter;
//    }
    public DelegateAdapter() {
    }

    public int getItemViewType(int position) {
        final int typeSize = types.size();
        //单样式
        if (typeSize == 1) {
            viewType_itemType_map.put(types.get(0).getViewType(), types.get(0));
            position_itemType_map.put(position, types.get(0));
            return types.get(0).getViewType();
        }
        //多样式
        if (typeSize > 1) {
            //先从缓存获取
            ItemType<T> itemType = position_itemType_map.get(position);
            if (itemType == null) {//如果缓存没有
                T data = getItem(position);
                if (data == null) {
                    return 0;
                }
                //为当前position的实体对象指定它的ItemType
                for (ItemType<T> type : types) {
                    if (type.matchItemType(data, position)) {
                        itemType = type;
                        position_itemType_map.put(position, itemType);
                        viewType_itemType_map.put(itemType.getViewType(), itemType);
                    }
                }
            }
            return itemType == null ? 0 : itemType.getViewType();
        }
        return 0;
    }

    @Nullable
    public abstract T getItem(int position);

    @NonNull
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       final ItemType<T> type = viewType_itemType_map.get(viewType);
        MultiViewHolder holder = MultiViewHolder.create(parent.getContext(), parent, type.getItemLayoutRes());
        //Item条目点击事件
        holder.itemView.setOnClickListener((v) -> {
           final T data = getItem(holder.getAdapterPosition());
            if (data != null) {
                type.onClickItemView(holder,data , holder.getAdapterPosition());
            }
        });
        //Item子视图点击事件在次回调方法实现
        type.onInitItemSubViewListener(holder);
        return holder;
    }

    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        final T data=getItem(position);
        if (data!=null){
        ItemType<T> type = position_itemType_map.get(position);
        type.onBindViewHolder(holder, data, position);
        }
    }

    public void setItemType(@NonNull ItemType<T> type) {
        types = new ArrayList<>(1);
        types.add(type);
    }

    public void setItemTypes(@NonNull List<ItemType<T>> types) {
        this.types = types;
    }

    public final void removeItem(int position) {
        position_itemType_map.remove(position);//移除该位置的ItemType
        getItemViewType(position);//重新匹配该位置的ItemType
    }

}
