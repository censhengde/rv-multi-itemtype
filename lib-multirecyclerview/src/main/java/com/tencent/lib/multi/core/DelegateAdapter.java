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
public abstract class DelegateAdapter<T> {


    private static final int SELECTED_NONE = -1;//表示全列表都没有Item被选中
    private int mSelectedPosition = SELECTED_NONE;
    private OnCheckedItemCallback<T> onCheckedItemCallback;
    private boolean isSingleSelection = false;
    private RecyclerView.Adapter realAdapter;

    protected final SparseArray<ItemType<T>> position_itemType_map = new SparseArray<>();
    protected final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>(8);
    protected List<ItemType<T>> types;

    public DelegateAdapter(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public int getItemViewType(int position) {
        final int typeSize = types.size();
        //单样式
        if (typeSize == 1) {
            viewType_itemType_map.put(types.get(0).getViewType(), types.get(0));
            position_itemType_map.put(position, types.get(0));
            return types.get(0).getViewType();
        }
        //多样式
        if (typeSize > 1) {
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
        return 0;
    }

    @Nullable
    public abstract T getItem(int position);

    @NonNull
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

    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        final T data=getItem(position);
        if (data!=null){
        ItemType<T> type = position_itemType_map.get(position);
        type.onBindViewHolder(holder, data, position);
        }
    }

    public void setItemType(@NonNull ItemType<T> type) {
        types = new ArrayList<>(1);
        types.add(type);
    }

    public void setItemTypes(@NonNull List<ItemType<T>> types) {
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
    public void checkItem(int position) {

        final T data = getItem(position);
        if (data == null) {
            return;
        }
        if (!(data instanceof Checkable)){
            throw new IllegalStateException(" Item 实体类必须是 Checkable 类型");
        }
        final   Checkable checkableData= (Checkable)data;
        //========单选=================
        if (isSingleSelection) {
            //列表中已有被选中Item，且当前被选中的Item==上次被选中的,则将Item重置为未选中状态,此时全列表0个item被选中。
            if (position == mSelectedPosition) {
                checkableData.setChecked(false);
                mSelectedPosition = SELECTED_NONE;
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
                realAdapter.notifyItemChanged(position);
            }
            //将选择的Item信息回调出去
            if (onCheckedItemCallback != null) {
                onCheckedItemCallback.onCkeked(data, position);
            }
            //==========复选===============
        } else {
            //如果当前item已经被选中，则取消被选中。
            if (checkableData.isChecked()) {
                checkableData.setChecked(false);
                realAdapter.notifyItemChanged(position);
            } else {//否则被选中
                checkableData.setChecked(true);
                realAdapter.notifyItemChanged(position);
            }

        }
    }
    /*有待开发*/
    final void addItem(int position) {

    }
    boolean checkable = false;//是否开启列表单选、多选功能

    public void setOnCheckedItemCallback(@NonNull OnCheckedItemCallback<T> callback) {
        onCheckedItemCallback = callback;
    }

    public void setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
    }
    public abstract void complete(OnCompletedCheckItemCallback<T> callback);
}
