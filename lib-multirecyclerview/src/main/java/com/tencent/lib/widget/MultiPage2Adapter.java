package com.tencent.lib.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.DataSource.Factory;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedList.Config;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import com.tencent.lib.widget.paged.DataSourceFactory;
import com.tencent.lib.widget.paged.PagedItemManager;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/12 16:36
 * <p>
 * 说明：
 */
final class MultiPage2Adapter<Key, Value> extends PagedListAdapter<Value, MultiViewHolder> implements
        PagedItemManager<Value> {

    private static final String TAG = "MultiPage2Adapter==>";

    private final SparseArray<ItemType<Value>> position_itemType_map = new SparseArray<>(3);
    private final SparseArray<ItemType<Value>> viewType_itemType_map = new SparseArray<>(3);
    @NonNull
    private List<ItemType<?>> types;
    private DataSourceFactory mDataSourceFactory;

    public void setPagedConfig(Config config) {
        this.mConfig = config;
    }

    PagedList.Config mConfig;


    MultiPage2Adapter(@NotNull DiffUtil.ItemCallback<Value> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemType<Value> type = viewType_itemType_map.get(viewType);
        MultiViewHolder holder = MultiViewHolder.create(parent.getContext(), parent, type.getItemLayoutRes());
        holder.itemView.setOnClickListener((v) -> {
            type.onClickItemView(holder, getItem(holder.getAdapterPosition()), holder.getAdapterPosition());
        });
        type.onInitItemSubViewListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        ItemType<Value> type = position_itemType_map.get(position);
        type.onBindViewHolder(holder, getItem(position), position);
    }


    @Override
    public int getItemViewType(int position) {
        final int typeSize = types.size();
        //单样式
        if (typeSize == 1) {
            return types.get(0).getViewType();
        }
        //多样式
        if (typeSize > 1) {
            //先从缓存获取
            ItemType<Value> itemType = position_itemType_map.get(position);
            if (itemType == null) {//如果缓存没有
                Value data = getItem(position);
                //为当前position的实体对象指定它的ItemType
                for (ItemType type : types) {
                    if (type.matchItemType(data, position)) {
                        itemType = type;
                        position_itemType_map.put(position, itemType);
                        viewType_itemType_map.put(itemType.getViewType(), itemType);
                    }
                }
            }
            return itemType == null ? 0 : itemType.getViewType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void removeItem(int position) {

        try {
            final PagedList<Value> oldPagedList = getCurrentList();
            oldPagedList.snapshot().remove(position);
            final Value currItem = getItem(position);
            final NewPageKeyedDataSource newDataSource =
                    new NewPageKeyedDataSource<>((PageKeyedDataSource) oldPagedList.getDataSource());
            // 过滤掉要移除的Item
            for (Value value : oldPagedList) {
                if (currItem != value) {
                    newDataSource.datas.add(value);
                }
            }
            PagedList<Value> newPagedList = newDataSource.createPagedList(mConfig);
            this.submitList(newPagedList);
            getItemViewType(position);//重新匹配
            notifyItemRemoved(position);
            notifyItemRangeRemoved(position, 1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "removeItem 异常:" + e.getMessage());
        }
    }

    @Override
    public void insertItem(int position, Value data) {

    }

    @Override
    public void addItem(Value data) {

    }

    @Override
    public void updateItem(int position) {

    }

    @Override
    public void updateAll() {

    }


    public void setTypes(@NonNull List<ItemType<?>> types) {
        this.types = types;
    }

    public void setType(ItemType<?> type) {
        if (types == null) {
            types = new ArrayList<>(1);
        }
        types.add(type);
    }

     void setDataSourceFactory(@NonNull DataSourceFactory factory) {
        mDataSourceFactory=factory;

    }

    void completed(){
        LiveData liveData=new LivePagedListBuilder<>(new Factory() {
            @NotNull
            @Override
            public DataSource create() {
                return mDataSourceFactory.create();
            }
        },mConfig).build();
        liveData.observeForever(o -> MultiPage2Adapter.this.submitList((PagedList<Value>) o));
    }

    @Override
    public void refresh() {
        PagedList list=getCurrentList();
        if (list!=null){
        DataSource dataSource=list.getDataSource();
        if (dataSource!=null){
            dataSource.invalidate();
        }
        }
    }

    @Override
    public void loadMore() {
        PagedList list=getCurrentList();
        if (list!=null){
            DataSource dataSource=list.getDataSource();
            final  NewPageKeyedDataSource newDataSource=new NewPageKeyedDataSource((PageKeyedDataSource) dataSource);

        }


    }
}
