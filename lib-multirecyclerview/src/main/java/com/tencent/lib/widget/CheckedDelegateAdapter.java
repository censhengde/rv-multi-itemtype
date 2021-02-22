package com.tencent.lib.widget;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Author：岑胜德 on 2021/2/5 16:58
 *
 * 说明：
 */
 abstract class CheckedDelegateAdapter<T extends Checkable> extends DelegateAdapter<T> {

    private static final int SELECTED_NONE = -1;//表示全列表都没有Item被选中
    private int mSelectedPosition = SELECTED_NONE;
    private OnCheckedItemCallback<T> onCheckedItemCallback;
    private boolean isSingleSelection = false;
    private RecyclerView.Adapter realAdapter;

    boolean checkable = false;//是否开启列表单选、多选功能

    public void setOnCheckedItemCallback(@NonNull OnCheckedItemCallback<T> callback) {
        onCheckedItemCallback = callback;
    }

    public CheckedDelegateAdapter(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public void setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType<T> type = viewType_itemType_map.get(viewType);
        final MultiViewHolder holder = MultiViewHolder
                .create(parent.getContext(), parent, type.getItemLayoutRes());
        //条目点击
        holder.itemView.setOnClickListener((v) -> {
            final T data = getItem(holder.getAdapterPosition());
            if (data != null) {
                if (checkable) {
                    checkItem(holder.getAdapterPosition());//选中Item
                }
                type.onClickItemView(holder, data, holder.getAdapterPosition());
            }
        });
        type.onInitItemSubViewListener(holder);
        return holder;
    }

    /**
     * 列表选择算法
     *
     * @param position
     * @return
     */
    public void checkItem(int position) {

        final T data = getItem(position);
        if (data == null) {
            return;
        }
        //========单选=================
        if (isSingleSelection) {
            //列表中已有被选中Item，且当前被选中的Item==上次被选中的,则将Item重置为未选中状态,此时全列表0个item被选中。
            if (position == mSelectedPosition) {
                data.setChecked(false);
                mSelectedPosition = SELECTED_NONE;
                realAdapter.notifyItemChanged(position);
            }
            //列表中已有被选中Item，但当前被选中Item！=上次被选中Item,则将上次的重置为未选中状态,再将当前Item置为被选中状态。
            else if (mSelectedPosition != SELECTED_NONE) {
                T selectedData = getItem(mSelectedPosition);
                if (selectedData == null) {
                    return;
                }
                selectedData.setChecked(false);
                realAdapter.notifyItemChanged(mSelectedPosition);
                data.setChecked(true);
                mSelectedPosition = position;
                realAdapter.notifyItemChanged(mSelectedPosition);
            }
            //列表中尚未有Item被选中,则将当前Item置为被选中状态。
            else if (mSelectedPosition == SELECTED_NONE) {
                data.setChecked(true);
                mSelectedPosition = position;
                realAdapter.notifyItemChanged(position);
            }
            //将选择的Item信息回调出去
            if (onCheckedItemCallback != null) {
                onCheckedItemCallback.onCkeked(data, position);
            }
            //==========复选===============
        } else {
            //如果当前item已经被选中，则取消被选中。
            if (data.isChecked()) {
                data.setChecked(false);
                realAdapter.notifyItemChanged(position);
            } else {//否则被选中
                data.setChecked(true);
                realAdapter.notifyItemChanged(position);
            }

        }
    }

    public abstract void complete(OnCompletedCheckItemCallback<T> callback);
}
