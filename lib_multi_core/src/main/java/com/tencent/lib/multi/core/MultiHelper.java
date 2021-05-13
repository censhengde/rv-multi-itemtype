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

    public static final int DEFAULT_VIEW_TYPE = 0;
    private RecyclerView.Adapter realAdapter;
    private final SparseArray<ItemType<T>> viewType_itemType_map = new SparseArray<>();
    private List<ItemType<T>> mTypes;

    /*index与Adapter position一一对应,表示某position想要表现的ItemType，
    注意，并非一定与数据集的index对应。
    */
    private final List<ItemType<T>> mItemTypeRecord = new ArrayList<>();




    public MultiHelper(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public final int getItemViewType(int position) {
        if (mTypes == null || mTypes.isEmpty()) {
            return DEFAULT_VIEW_TYPE;
        }
        ItemType<T> currentType = null;
        final int typeSize = mTypes.size();
        //单样式
        if (typeSize == 1) {
            try {
                currentType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
            } catch (Exception e) {
                currentType = mTypes.get(0);
                mItemTypeRecord.add(currentType);
            }
        }
        //多样式
        else {
            final T data = getItem(position);
            if (data == null) {
                return DEFAULT_VIEW_TYPE;
            }
            try {
                currentType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
                //如果当前position 的ItemType不再与当前的data所指定的ItemType匹配，说明当前data已经被更改，
                // 需重新匹配当前data所指定的ItemType
                if (!currentType.matchItemType(data, position)) {
                    for (ItemType<T> type : mTypes) {
                        if (type.matchItemType(data, position)) {
                            //更新当前position的ItemType记录
                            currentType = type;
                            mItemTypeRecord.set(position, type);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                //为当前position的实体对象指定它的ItemType
                for (ItemType<T> type : mTypes) {
                    if (type.matchItemType(data, position)) {
                        currentType = type;
                        mItemTypeRecord.add(currentType);
                        break;
                    }
                }
            }
            if (currentType == null) {
                return DEFAULT_VIEW_TYPE;
            }
        }
        //如果已经存在该ItemType，则不需要重新put
        if (viewType_itemType_map.get(currentType.getViewType()) == null) {
            viewType_itemType_map.put(currentType.getViewType(), currentType);
        }
        return currentType.getViewType();
    }

    @Nullable
    protected abstract T getItem(int position);

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
     * 当涉及item的增、删、改时，数据集元素的增删改必须与ItemTypeRecord增删改同步。
     * 一般来说当我们明确知道某position的ItemType的改变时，应主动调用这个方法进行更新，因为由
     * {@getItemViewType}自动匹配的话执行过程相对复杂度较高，消耗略大。例子见MultiAdapter removeItem方法。
     * @return ItemType记录
     */
    @NonNull
    public final List<ItemType<T>> getItemTypeRecord() {
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


}
