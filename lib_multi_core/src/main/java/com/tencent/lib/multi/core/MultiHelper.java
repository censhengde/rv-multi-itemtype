package com.tencent.lib.multi.core;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 * 说明：实现Item多样式的公共逻辑封装。
 */
public abstract class MultiHelper<T> {


    private static final int SELECTED_NONE = -1;//表示全列表都没有Item被选中
    private int mSelectedPosition = SELECTED_NONE;
    private boolean mSingleSelection = false;
    private RecyclerView.Adapter realAdapter;
    private final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>();
    private List<ItemType<T>> mTypes;

    /*index与数据集index一一对应*/
    private final List<ItemType<T>> mItemTypeRecord = new ArrayList<>();


    public void setCheckedItemCount(int checkedItemCount) {
        mCheckedItemCount = checkedItemCount;
    }

    private int mCheckedItemCount = 0;//当前列表被已被选中的Item数目

    public MultiHelper(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public int getItemViewType(int position) {
        if (mTypes == null || mTypes.isEmpty()) {
            return 0;
        }
        ItemType<T> itemType = null;
        final int typeSize = mTypes.size();
        //单样式
        if (typeSize == 1) {
            try {
                itemType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
            } catch (Exception e) {
                itemType = mTypes.get(0);
                mItemTypeRecord.add(itemType);
            }
        }
        //多样式
        else {
            final T data = getItem(position);
            if (data == null) {
                return 0;
            }
            try {
                itemType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
            } catch (Exception e) {
                //为当前position的实体对象指定它的ItemType
                for (ItemType<T> type : mTypes) {
                    if (type.matchItemType(data, position)) {
                        itemType = type;
                        mItemTypeRecord.add(itemType);
                        viewType_itemType_map.put(itemType.getViewType(), itemType);
                    }
                }
            }
            if (itemType == null) {
                return 0;
            }
        }
        if (viewType_itemType_map.get(itemType.getViewType())==null) {
            viewType_itemType_map.put(itemType.getViewType(), itemType);
        }
        return itemType.getViewType();
    }

    @Nullable
    public abstract T getItem(int position);

    public int getItemCount() {
        return realAdapter == null ? 0 : realAdapter.getItemCount();
    }
    @NonNull
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType<T> type = viewType_itemType_map.get(viewType);
        if (type==null){//表示无效
             return MultiViewHolder.create(parent.getContext(),parent,0);
        }
        final MultiViewHolder holder = MultiViewHolder
                .create(parent.getContext(), parent, type.getItemLayoutRes());

        type.onViewHolderCreated(holder, this);
        return holder;
    }

    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (holder.isInvalid) {
            return;
        }
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            try {
                /*可能有越界风险*/
                final ItemType<T> type = mItemTypeRecord.get(position);
                if (type != null) {
                    type.onBindViewHolder(holder, this, position, payloads);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当涉及item的增、删、改时，数据集元素的增删改必须与ItemTypeRecord增删改同步
     */
    @NonNull
    public List<ItemType<T>> getItemTypeRecord() {
        return mItemTypeRecord;
    }
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        if (holder.isInvalid){
            return;
        }
        try {
            ItemType<T> type = mItemTypeRecord.get(position);
            if (type != null) {
                type.onBindViewHolder(holder, this, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MultiHelper addItemType(@NonNull ItemType<T> type) {
        if (mTypes == null) {
            mTypes = new ArrayList<>();
        }
        mTypes.add(type);
        return this;
    }

    public void setItemTypes(List<ItemType<T>> types) {
        this.mTypes = types;
    }


    /**
     * 列表选择算法
     *
     * @param position
     * @return
     */
    public final void checkItem(int position) {

        final T data = getItem(position);
        if (data == null) {
            return;
        }
        if (!(data instanceof Checkable)){
            throw new IllegalStateException(" Item 实体类必须是 Checkable 类型");
        }
        final   Checkable checkableData= (Checkable)data;
        //========单选=================
        if (mSingleSelection) {
            //列表中已有被选中Item，且当前被选中的Item==上次被选中的,则将Item重置为未选中状态,此时全列表0个item被选中。
            if (position == mSelectedPosition) {
                checkableData.setChecked(false);
                mSelectedPosition = SELECTED_NONE;
                mCheckedItemCount--;
                realAdapter.notifyItemChanged(position);
            }
            //列表中已有被选中Item，但当前被选中Item！=上次被选中Item,则将上次的重置为未选中状态,再将当前Item置为被选中状态。
            else if (mSelectedPosition != SELECTED_NONE) {
                Checkable selectedData = (Checkable) getItem(mSelectedPosition);
                if (selectedData == null) {
                    return;
                }
                selectedData.setChecked(false);
                realAdapter.notifyItemChanged(mSelectedPosition);
                checkableData.setChecked(true);
                mSelectedPosition = position;
                realAdapter.notifyItemChanged(mSelectedPosition);
            }
            //列表中尚未有Item被选中,则将当前Item置为被选中状态。
            else if (mSelectedPosition == SELECTED_NONE) {
                checkableData.setChecked(true);
                mSelectedPosition = position;
                mCheckedItemCount++;
                realAdapter.notifyItemChanged(position);
            }

            //==========复选===============
        } else {
            //如果当前item已经被选中，则取消被选中。
            if (checkableData.isChecked()) {
                checkableData.setChecked(false);
                mCheckedItemCount--;
                realAdapter.notifyItemChanged(position);
            } else {//否则被选中
                checkableData.setChecked(true);
                mCheckedItemCount++;
                realAdapter.notifyItemChanged(position);
            }

        }
    }

    public int getCheckedItemCount() {
        return mCheckedItemCount;
    }



    public void setSingleSelection(boolean singleSelection) {
        mSingleSelection = singleSelection;
    }

    public boolean isSingleSelection() {
        return mSingleSelection;
    }
    public abstract void complete(OnCompletedCheckCallback<T> callback);


}
