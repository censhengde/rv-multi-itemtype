package com.tencent.lib.multi;

import android.os.Bundle;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.AdapterBuilder;
import com.tencent.lib.multi.core.BaseRecyclerView;
import com.tencent.lib.multi.core.CheckManager;
import com.tencent.lib.multi.core.Checkable;
import com.tencent.lib.multi.core.ItemManager;
import com.tencent.lib.multi.core.ItemType;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.OnCompletedCheckItemCallback;
import com.tencent.lib.multi.core.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> implements ItemManager<T>, CheckManager {

    private OnCompletedCheckItemCallback<T> onCompletedCheckItemCallback;
    protected List<T> datas;

    protected MultiHelper<T> mMultiHelper;

    public MultiAdapter() {
        mMultiHelper = new MultiHelper<T>(this) {
        @Nullable
        @Override
        public T getItem(int position) {
           return MultiAdapter.this.getItem(position);
        }

            @Override
            public void complete(OnCompletedCheckItemCallback<T> callback) {
                final int count = this.getCheckedItemCount();
                if (callback != null && count > 0) {//性能优化的一个点：当前列表没有被选中的Item就没有必要再遍历数据源
                    final List<T> checked = new ArrayList<>(count);
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

    private boolean isInValidPostion(int position) {
        return  position<0||datas==null||position>=datas.size();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mMultiHelper.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position, @NonNull List<Object> payloads) {
        mMultiHelper.onBindViewHolder(holder, position, payloads);
    }



    @Override
    public int getItemViewType(int position) {
        return mMultiHelper.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        return datas == null ? 0 : datas.size();
    }

    @Nullable
    public T getItem(int position) {
        return isInValidPostion(position)? null : datas.get(position);
    }

    /*list 引用改变*/
    public void setDatas(@NonNull List<T> datas) {
        mMultiHelper.clearItemTypes();//确保ItemType记录重新匹配
        if (datas == this.datas) {
            notifyDataSetChanged();
            return;
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    public MultiAdapter<T> addItemType(@NonNull ItemType<T> type) {
        mMultiHelper.addItemType(type);
        return this;
    }

    @Override
    public void removeItem(int position) {
        if (datas!=null){
            /*如果删除的Item是被选中的Item，则数量要减一*/
            final T item = datas.get(position);
            if (item instanceof Checkable) {
                final Checkable checkable = (Checkable) item;
                if (checkable.isChecked()) {
                    mMultiHelper.setCheckedItemCount(mMultiHelper.getCheckedItemCount() - 1);
                }
            }
            datas.remove(position);
            mMultiHelper.removeItem(position);
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
        /*复选模式下才进行全选*/
        if (!mMultiHelper.isSingleSelection()) {
            if (datas != null && !datas.isEmpty()) {
                for (T data : datas) {
                    ((Checkable) data).setChecked(false);
                }
                notifyDataSetChanged();
            }
        }
    }


    @Override
    public void checkAll() {
        /*复选模式下才进行全选*/
        if (!mMultiHelper.isSingleSelection()) {
            if (datas != null && !datas.isEmpty()) {
                for (T data : datas) {
                    ((Checkable) data).setChecked(true);
                }
                notifyDataSetChanged();
            }
        }
    }

    public void updateItem(int position) {

    }
    public void setSingleSelection(boolean isSingleSelection) {
        mMultiHelper.setSingleSelection(isSingleSelection);
    }

    public void setOnCompletedCheckItemCallback(OnCompletedCheckItemCallback<T> callback) {
        this.onCompletedCheckItemCallback = callback;
    }

    public void setCheckable(boolean checkable) {
        mMultiHelper.checkable = checkable;
    }

    public void complete() {
        if (onCompletedCheckItemCallback != null) {
            mMultiHelper.complete(onCompletedCheckItemCallback);
        }
    }


    @Override
    public void saveCheckedItem(Bundle out) {

    }

    @Override
    public void restoreCheckedItem(Bundle in) {

    }

    public void checkItem(int position) {
        mMultiHelper.checkItem(position);
    }

public static class Builder extends AdapterBuilder<Builder> {
    private List<?> datas;
    private MultiAdapter adapter;

    public Builder setDatas(List<?> datas) {
        this.datas = datas;
        return this;
    }

    Builder(BaseRecyclerView rv) {
        super(rv);
    }

    public void build() {
        Utils.assertNull(datas, "datas 不允许 为 null");
        adapter = new MultiAdapter<>();
        adapter.setOnCompletedCheckItemCallback(onCompletedCheckItemCallback);
        adapter.setSingleSelection(recyclerView.getSingleSelection());
        adapter.setCheckable(recyclerView.getCheckable());
        adapter.setDatas(datas);
        if (itemType != null) {
            adapter.addItemType(itemType);
        }

        final RecyclerView rv = recyclerView;
        rv.setAdapter(adapter);
    }

}

    @Override
    public void onViewRecycled(@NonNull MultiViewHolder holder) {
        holder.onViewRecycled();
    }
}
