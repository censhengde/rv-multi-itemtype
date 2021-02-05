package com.tencent.lib.widget;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/**
 * Author：岑胜德 on 2021/2/5 16:58
 *
 * 说明：
 */
public abstract class CheckedDelegateAdapter<T extends CheckableItem> extends DelegateAdapter<T> {

    private static final int SELECTED_NONE = -1;//表示全列表都没有Item被选中
    private int mSelectedPosition = SELECTED_NONE;
    private OnCheckedItemCallback<T> onCheckedItemCallback;
    private boolean isSingleSelection = false;

    public void setOnCheckedItemCallback(@NonNull OnCheckedItemCallback<T> callback) {
        onCheckedItemCallback = callback;
    }

    public CheckedDelegateAdapter(Adapter<?> realdapter) {
        super(realdapter);
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
                checkItem(holder.getAdapterPosition());//选中Item
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
        //========单选=================
        if (isSingleSelection) {
            //列表中已有被选中Item，且当前被选中的Item==上次被选中的,则将Item重置为未选中状态,此时全列表0个item被选中。
            if (position == mSelectedPosition) {
                T data = getItem(position);
                if (data == null) {
                    return;
                }
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
                T data = getItem(position);
                if (data == null) {
                    return;
                }
                data.setChecked(true);
                mSelectedPosition = position;
                realAdapter.notifyItemChanged(mSelectedPosition);
            }
            //列表中尚未有Item被选中,则将当前Item置为被选中状态。
            else if (mSelectedPosition == SELECTED_NONE) {
                T data = getItem(position);
                if (data == null) {
                    return;
                }
                data.setChecked(true);
                mSelectedPosition = position;
                realAdapter.notifyItemChanged(position);
            }
            //将选择的Item信息回调出去
            if (onCheckedItemCallback != null) {
                onCheckedItemCallback.onCkeked(getItem(position), position);
            }
            //==========复选===============
        } else {
            final T data = getItem(position);
            //如果当前item没有被选中
            if (data != null && !data.isChecked()) {
                data.setChecked(true);
                realAdapter.notifyItemChanged(position);
            }
        }
    }
}
