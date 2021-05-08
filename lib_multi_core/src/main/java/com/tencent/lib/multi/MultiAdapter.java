package com.tencent.lib.multi;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.Checkable;
import com.tencent.lib.multi.core.ItemType;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.OnCompletedCheckCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> {

    private OnCompletedCheckCallback<T> onCompletedCheckCallback;
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
            public void complete(OnCompletedCheckCallback<T> callback) {
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
    public MultiAdapter<T> setDatas(@NonNull List<T> datas) {
        mMultiHelper.getItemTypeRecord().clear();//确保ItemType记录重新匹配
        if (datas == this.datas) {
            notifyDataSetChanged();
            return this;
        }
        this.datas = datas;
        notifyDataSetChanged();
        return this;
    }

    @NonNull
    public List<ItemType<T>> getItemTypeRecord() {
        return mMultiHelper.getItemTypeRecord();
    }
    public MultiAdapter<T> addItemType(@NonNull ItemType<T> type) {
        mMultiHelper.addItemType(type);
        return this;
    }

    public void setItemTypes(List<ItemType<T>> types) {
        mMultiHelper.setItemTypes(types);
    }
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
            mMultiHelper.getItemTypeRecord().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,datas.size()-position);
        }
    }


    public void addItem(T data) {
        if (datas != null) {
            datas.add(data);
            notifyItemChanged(datas.size()-1);
        }
    }


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

    public MultiAdapter<T> setSingleSelection(boolean single) {
        mMultiHelper.setSingleSelection(single);
        return this;
    }

    public MultiAdapter<T> setOnCompletedCheckCallback(OnCompletedCheckCallback<T> callback) {
        this.onCompletedCheckCallback = callback;
        return this;
    }


    public void complete() {
        if (onCompletedCheckCallback != null) {
            mMultiHelper.complete(onCompletedCheckCallback);
        }
    }

    public final void checkItem(int position) {
        mMultiHelper.checkItem(position);
    }

}
