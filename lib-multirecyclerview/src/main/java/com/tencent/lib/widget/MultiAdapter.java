package com.tencent.lib.widget;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
 class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> implements IBuilder<T>,ItemManager<T> {

    protected List<T> datas;

    protected  DelegateAdapter<T> delegateAdapter;

    MultiAdapter() {
    delegateAdapter=new DelegateAdapter<T>() {
        @Nullable
        @Override
        public T getItem(int position) {
            return datas.get(position);
        }
    };
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegateAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        delegateAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return delegateAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void setDatas(@NonNull List<T> datas) {
        this.datas =  datas;
    }

    @Override
    public void setItemTypes(@NonNull List<ItemType<T>> types) {
        this.delegateAdapter.setItemTypes(types);
    }

    @Override
    public void setItemType(@NonNull ItemType<T> type) {
        delegateAdapter.setItemType(type);
    }

    @Override
    public void removeItem(int position) {
        if (datas!=null){
            datas.remove(position);
            delegateAdapter.removeItem(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,datas.size()-position);
        }
    }

    @Override
    public void addItem(int position, T data) {
        if (datas != null) {
            datas.add(position, data);
            notifyItemChanged(position);
        }
    }

    @Override
    public void addItem(T data) {
        addItem(datas.size(), data);
    }

    @Override
    public void updateItem(int position) {

    }

    @Override
    public void updateAll() {

    }



}
