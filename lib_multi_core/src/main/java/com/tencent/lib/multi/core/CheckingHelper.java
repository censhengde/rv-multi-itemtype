package com.tencent.lib.multi.core;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/5/12 11:50
 *
 * 说明：列表选择帮助类
 */
public abstract class CheckingHelper<T> {

    private RecyclerView.Adapter mAdapter;
    private int mCheckedItemCount = 0;//当前列表被已被选中的Item数目
    private static final int SELECTED_NONE = -1;//表示全列表都没有Item被选中
    private int mSelectedPosition = SELECTED_NONE;
    private OnCheckingFinishedCallback<T> mOnCheckingFinishedCallback;


    public void setCheckedItemCount(int checkedItemCount) {
        mCheckedItemCount = checkedItemCount;
    }

    public CheckingHelper(Adapter adapter) {
        this.mAdapter = adapter;
    }


    @Nullable
    public abstract T getItem(int position);

    protected abstract int getDataSize();

    public int getCheckedItemCount() {
        return mCheckedItemCount;
    }




    public void setOnCheckingFinishedCallback(OnCheckingFinishedCallback<T> callback) {
        mOnCheckingFinishedCallback = callback;
    }

    public final void finishChecking() {
        final int count = mCheckedItemCount;
        if (count > 0) {//性能优化的一个点：当前列表没有被选中的Item就没有必要再遍历数据源
            final List<T> checked = new ArrayList<>(count);
            final int size = getDataSize();
            //筛选出被选中的Item
            for (int i = 0; i < size; i++) {
                final T item = getItem(i);
                if (item instanceof Checkable) {
                    if (((Checkable) item).isChecked()) {
                        checked.add(item);
                    }
                }
            }
            if (mOnCheckingFinishedCallback != null) {
                mOnCheckingFinishedCallback.onCheckingFinished(checked);
            }
        }
    }


    /**
     * 单选请调用这个方法
     * @param position
     * @param payload
     */
    public final void singleCheckItem(int position,@Nullable Object payload) {
        final T data = getItem(position);
        if (data == null) {
            return;
        }
        if (!(data instanceof Checkable)){
            throw new IllegalStateException(" Item 实体类必须是 Checkable 类型");
        }
        final   Checkable checkableData= (Checkable)data;
        //列表中已有被选中Item，且当前被选中的Item==上次被选中的,则将Item重置为未选中状态,此时全列表0个item被选中。
        if (position == mSelectedPosition) {
            checkableData.setChecked(false);
            mSelectedPosition = SELECTED_NONE;
            mCheckedItemCount--;
            mAdapter.notifyItemChanged(position,payload);
        }
        //列表中已有被选中Item，但当前被选中Item！=上次被选中Item,则将上次的重置为未选中状态,再将当前Item置为被选中状态。
        else if (mSelectedPosition != SELECTED_NONE) {
            Checkable selectedData = (Checkable) getItem(mSelectedPosition);
            if (selectedData == null) {
                return;
            }
            selectedData.setChecked(false);
            mAdapter.notifyItemChanged(mSelectedPosition,payload);
            checkableData.setChecked(true);
            mSelectedPosition = position;
            mAdapter.notifyItemChanged(mSelectedPosition,payload);
        }
        //列表中尚未有Item被选中,则将当前Item置为被选中状态。
        else if (mSelectedPosition == SELECTED_NONE) {
            checkableData.setChecked(true);
            mSelectedPosition = position;
            mCheckedItemCount++;
            mAdapter.notifyItemChanged(position,payload);
        }

    }
    /**
     * 多选请调用这个方法
     * @param position
     * @return
     */
    public final void checkItem(int position,@Nullable Object payload) {
        final T data = getItem(position);
        if (data == null) {
            return;
        }
        if (!(data instanceof Checkable)){
            throw new IllegalStateException(" Item 实体类必须是 Checkable 类型");
        }
        final   Checkable checkableData= (Checkable)data;
        //如果当前item已经被选中，则取消被选中。
        if (checkableData.isChecked()) {
            checkableData.setChecked(false);
            mCheckedItemCount--;
            mAdapter.notifyItemChanged(position,payload);
        } else {//否则被选中
            checkableData.setChecked(true);
            mCheckedItemCount++;
            mAdapter.notifyItemChanged(position,payload);
        }
    }

    /**
     * 当列表存在头布局或脚布局等情况下，需要进行精确区间筛选。
     *
     * @param start
     * @param itemCount
     * @param payload
     */
    public final void checkRange(int start, int itemCount, @Nullable Object payload) {
        handleRangeInternal(start, itemCount, payload, true);
    }

    private void handleRangeInternal(int start, int itemCount, @Nullable Object payload, boolean check) {
        final int size = getDataSize();
        final int end = (start + itemCount - 1);
        if (start < 0 || itemCount <= 0 || end >= size) {
            return;
        }
        for (int i = start; i <= end; i++) {
            final T item = getItem(i);
            if (item instanceof Checkable) {
                final Checkable checkable = (Checkable) item;
                /*如果是选中*/
                if (check) {
                    /**/
                    if (!checkable.isChecked()) {
                        checkable.setChecked(true);
                        mCheckedItemCount++;
                        mAdapter.notifyItemChanged(i, payload);
                    }
                }
                /*取消选中*/
                else {
                    if (checkable.isChecked()) {
                        checkable.setChecked(false);
                        mCheckedItemCount--;
                        mAdapter.notifyItemChanged(i, payload);
                    }
                }
            }
        }
    }
    /**
     * 全选
     *
     * @param payload
     */
    public final void checkAll(@Nullable Object payload) {
        checkRange(0, getDataSize(), payload);
    }

    /**
     * 取消全选
     */
    public final void cancelAll(@Nullable Object payload) {
        cancelRange(0, getDataSize(), payload);

    }

    public final void cancelRange(int start, int itemCount, @Nullable Object payload) {
        handleRangeInternal(start, itemCount, payload, false);
    }
}
