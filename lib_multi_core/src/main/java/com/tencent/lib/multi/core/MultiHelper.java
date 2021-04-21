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
    protected final SparseArray<ItemType<T>> position_itemType_map = new SparseArray<>();
    protected final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>(8);
    protected  List<ItemType<T>> types;


    public void setCheckedItemCount(int checkedItemCount) {
        mCheckedItemCount = checkedItemCount;
    }

    private int mCheckedItemCount = 0;//当前列表被已被选中的Item数目

    public MultiHelper(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public int getItemViewType(int position) {
        if (types==null||types.isEmpty()){
            return 0;
        }
        final int typeSize = types.size();
        //单样式
        if (typeSize == 1) {
            viewType_itemType_map.put(types.get(0).getViewType(), types.get(0));
            position_itemType_map.put(position, types.get(0));
            return types.get(0).getViewType();
        }
        //多样式
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

    public void clearItemTypes() {
        position_itemType_map.clear();
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
            final ItemType<T> type = position_itemType_map.get(position);
            if (type != null) {
                type.onBindViewHolder(holder, this, position, payloads);
            }
        }
    }

    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        if (holder.isInvalid){
            return;
        }
        ItemType<T> type = position_itemType_map.get(position);
        if (type!=null){
            type.onBindViewHolder(holder, this, position);
        }
    }

    public MultiHelper addItemType(@NonNull ItemType<T> type) {
        if (types==null){
            types=new ArrayList<>();
        }
        types.add(type);
        return this;
    }

    public void setItemTypes(List<ItemType<T>> types) {
        this.types = types;
    }
    public final void removeItem(int position) {
            position_itemType_map.remove(position);//移除该位置的ItemType
            getItemViewType(position);//重新匹配该位置的ItemType
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
    /*有待开发*/
    final void addItem(int position) {

    }


    public void setSingleSelection(boolean singleSelection) {
        mSingleSelection = singleSelection;
    }

    public boolean isSingleSelection() {
        return mSingleSelection;
    }
    public abstract void complete(OnCompletedCheckItemCallback<T> callback);


}
