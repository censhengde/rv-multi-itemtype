package com.tencent.lib.multi;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.Checkable;
import com.tencent.lib.multi.core.ItemType;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.OnCheckingFinishedCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T> extends RecyclerView.Adapter<MultiViewHolder> {

    private OnCheckingFinishedCallback<T> onCheckingFinishedCallback;
    protected List<T> mData;

    protected MultiHelper<T> mMultiHelper;

    public MultiAdapter() {
        mMultiHelper = new MultiHelper<T>(this) {
        @Nullable
        @Override
        public T getItem(int position) {
           return MultiAdapter.this.getItem(position);
        }

            @Override
            public void finishChecking(OnCheckingFinishedCallback<T> callback) {
                final int count = this.getCheckedItemCount();
                if (callback != null && count > 0) {//性能优化的一个点：当前列表没有被选中的Item就没有必要再遍历数据源
                    final List<T> checked = new ArrayList<>(count);
                    //筛选出被选中的Item
                    if (mData != null && !mData.isEmpty()) {
                        for (T data : mData) {
                            if (((Checkable) data).isChecked()) {
                                checked.add(data);
                            }
                        }
                    }
                    callback.onCheckingFinished(checked);
                }
            }
    };
    }

    public final boolean isInValidPosition(int position) {
        return  position<0|| mData ==null||position>= mData.size();
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
        return mData == null ? 0 : mData.size();
    }

    @Nullable
    public T getItem(int position) {
        return isInValidPosition(position)? null : mData.get(position);
    }

    /*list 引用改变*/
    public MultiAdapter<T> setData(@NonNull List<T> data) {
        mMultiHelper.getItemTypeRecord().clear();//确保ItemType记录重新匹配
        if (data == this.mData) {
            notifyDataSetChanged();
            return this;
        }
        this.mData = data;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 当涉及item的增、删、改时，数据集元素的增删改必须与ItemTypeRecord增删改同步。
     * 一般来说当我们明确知道某position的ItemType的改变时，应主动调用这个方法进行更新，因为由
     * {@getItemViewType}自动匹配的话执行过程相对复杂度较高，消耗略大。例子见MultiAdapter removeItem方法。
     * @return ItemType记录
     */
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
        if (!isInValidPosition(position)){
            /*如果删除的Item是被选中的Item，则数量要减一*/
            final T item = mData.get(position);
            if (item instanceof Checkable) {
                final Checkable checkable = (Checkable) item;
                if (checkable.isChecked()) {
                    mMultiHelper.setCheckedItemCount(mMultiHelper.getCheckedItemCount() - 1);
                }
            }
            mData.remove(position);
            mMultiHelper.getItemTypeRecord().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size()-position);
        }
    }

    /**
     * 取消全选
     */
    public void cancelAll() {
        /*复选模式下才进行全选*/
        if (!mMultiHelper.isSingleChecking()) {
            if (mData != null && !mData.isEmpty()) {
                for (T data : mData) {
                    ((Checkable) data).setChecked(false);
                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 全选
     */
    public void checkAll() {
        /*复选模式下才进行全选*/
        if (!mMultiHelper.isSingleChecking()) {
            if (mData != null && !mData.isEmpty()) {
                for (T data : mData) {
                    ((Checkable) data).setChecked(true);
                }
                notifyDataSetChanged();
            }
        }
    }

    public MultiAdapter<T> setSingleChecking(boolean single) {
        mMultiHelper.setSingleChecking(single);
        return this;
    }

    /**
     * 设置完成选择后的回调监听
     * @param callback
     * @return
     */
    public MultiAdapter<T> setOnCheckingFinishedCallback(OnCheckingFinishedCallback<T> callback) {
        this.onCheckingFinishedCallback = callback;
        return this;
    }


    /**
     * 完成选择。调用这里将会触发OnCompletedCheckCallback回调。
     */
    public void finishChecking() {
        if (onCheckingFinishedCallback != null) {
            mMultiHelper.finishChecking(onCheckingFinishedCallback);
        }
    }

    /**
     * 选中某Item
     * @param position
     */
    public final void checkItem(int position) {
        mMultiHelper.checkItem(position);
    }

}
