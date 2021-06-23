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
public abstract class MultiHelper<T,VH extends RecyclerView.ViewHolder> {

    public static final int INVALID_VIEW_TYPE = -1;
    private RecyclerView.Adapter realAdapter;
    private final SparseArray<ItemType<T,VH>> viewType_itemType_map = new SparseArray<>();

    /**
     * ItemType在Adapter position上的记录，其索引与Adapter position一一对应,表示某position想要表现的ItemType，
     * 注意，并非一定与数据集的index对应。
     */
    private final List<ItemType<T,VH>> mItemTypeRecord = new ArrayList<>();


    public MultiHelper(Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    public final int getItemViewType(int position) {
        ItemType<T,VH> currentType = null;
        final int typeSize = viewType_itemType_map.size();
        //单样式
        if (typeSize == 1) {
            try {
                currentType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
            } catch (Exception e) {
                currentType = viewType_itemType_map.valueAt(0);
                mItemTypeRecord.add(currentType);
            }
        }
        //多样式
        else if (typeSize > 1) {
            final T data = getItem(position);
            if (data == null) {
                return INVALID_VIEW_TYPE;
            }
            try {
                currentType = mItemTypeRecord.get(position);//第一次进来会越界，说明尚无记录
                //如果当前position 的ItemType不再与当前的data所指定的ItemType匹配，说明当前data已经被更改，
                // 需重新匹配当前data所指定的ItemType
                if (!currentType.matchItemType(data, position)) {
                    for (int i = 0; i < viewType_itemType_map.size(); i++) {
                        final ItemType<T,VH> type = viewType_itemType_map.valueAt(i);
                        if (type.matchItemType(data, position)) {
                            currentType = type;
                            mItemTypeRecord.set(position, type);
                            break;
                        }
                    }

                }
            } catch (Exception e) {
                //为当前position的实体对象指定它的ItemType
                for (int i = 0; i < viewType_itemType_map.size(); i++) {
                    final ItemType<T,VH> type = viewType_itemType_map.valueAt(i);
                    if (type.matchItemType(data, position)) {
                        currentType = type;
                        mItemTypeRecord.add(type);
                        break;
                    }
                }
            }

        }
        return currentType == null ? INVALID_VIEW_TYPE : currentType.getViewType();
    }

    @NonNull
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemType<T,VH> type = viewType_itemType_map.get(viewType);
        if (viewType == INVALID_VIEW_TYPE || type == null) {//表示无效
            return (VH) MultiViewHolder.createInvalid(parent.getContext());
        }
        final VH holder = type.onCreateViewHolder(parent);
        type.onViewHolderCreated(holder, this);
        return holder;
    }

    public final void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {

        try {
            /*可能有越界风险*/
            final ItemType<T,VH> type = mItemTypeRecord.get(position);
            if (payloads.isEmpty()) {
                type.onBindViewHolder(holder, this, position);
            }
            /*局部刷新*/
            else {
                type.onBindViewHolder(holder, this, position, payloads);
                }
        } catch (Exception e) {
                e.printStackTrace();
            }

    }
    @Nullable
    public abstract T getItem(int position);

    public int getItemCount() {
        return realAdapter == null ? 0 : realAdapter.getItemCount();
    }
    /**
     * 当涉及item的增、删、改时，数据集元素的增删改必须与ItemTypeRecord增删改同步。
     * 一般来说当我们明确知道某position的ItemType的改变时，应主动调用这个方法进行更新，因为由
     * {@getItemViewType}自动匹配的话执行过程相对复杂度较高，消耗略大。例子见MultiAdapter removeItem方法。
     * @return ItemType记录
     */
    @NonNull
    public final List<ItemType<T,VH>> getItemTypeRecord() {
        return mItemTypeRecord;
    }


    public final MultiHelper<T,VH> addItemType( ItemType<T,VH> type) {
             if (type==null){
                 return this;
             }
             final  int key=type.getViewType();
             final ItemType<T,VH> old=viewType_itemType_map.get(key);
             //如果存在相同的，则忽略。
             if (type.equals(old)){
                 return this;
             }
             //如果添加多个view type相同的ItemType，则抛异常。
             if (old!=null&&!type.equals(old)){
                 throw new IllegalStateException("不能添加多个view type相同的ItemType对象！");
             }
        viewType_itemType_map.put(type.getViewType(), type);
        return this;
    }


}
