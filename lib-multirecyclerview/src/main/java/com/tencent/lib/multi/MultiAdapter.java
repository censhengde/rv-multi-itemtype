package com.tencent.lib.multi;

import android.os.Bundle;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> implements ItemManager<T>,CheckManager {

    private OnCompletedCheckItemCallback<T> onCompletedCheckItemCallback;
    protected List<T> datas;

    protected  DelegateAdapter<T> delegateAdapter;

    public MultiAdapter() {
        delegateAdapter = new DelegateAdapter<T>(this) {
        @Nullable
        @Override
        public T getItem(int position) {
            return datas.get(position);
        }

            @Override
            public void complete(OnCompletedCheckItemCallback<T> callback) {
                if (callback != null) {
                    final List<T> checked = new ArrayList<>();
                    //筛选出被选中的Item
                    if (datas != null && !datas.isEmpty()) {
                        for (T data : datas) {
                            if (((Checkable) data).isChecked()) {
                                checked.add(data);
                            }
                        }
                    }
                    callback.onCompletedChecked(checked);
                }
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


    public void setDatas(@NonNull List<T> datas) {
        this.datas =  datas;
    }

    public void setItemTypes(@NonNull List<ItemType<T>> types) {
        this.delegateAdapter.setItemTypes(types);
    }

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
    public void cancelAll() {
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                ((Checkable)data).setChecked(false);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void checkAll() {
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                ((Checkable)data).setChecked(true);
            }
            notifyDataSetChanged();
        }
    }


    public void setSingleSelection(boolean isSingleSelection) {
        delegateAdapter.setSingleSelection(isSingleSelection);
    }

    public void setOnCompletedCheckItemCallback(OnCompletedCheckItemCallback<T> callback) {
        this.onCompletedCheckItemCallback = callback;
    }

    public void checkable(boolean checkable) {
         delegateAdapter.checkable = checkable;
    }

    public void complete() {
        if (onCompletedCheckItemCallback != null) {
            delegateAdapter.complete(onCompletedCheckItemCallback);
        }
    }


    @Override
    public void saveCheckedItem(Bundle out) {

    }

    @Override
    public void restoreCheckedItem(Bundle in) {

    }


}
