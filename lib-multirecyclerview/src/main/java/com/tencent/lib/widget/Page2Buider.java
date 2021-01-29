package com.tencent.lib.widget;

import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import com.tencent.lib.widget.paged.DataSourceFactory;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/13 21:28
 * <p>
 * 说明：
 */
public class Page2Buider<B extends Page2Buider> extends MultiRecyclerView.Builder<B> {

    MultiPage2Adapter<?, ?> mAdapter;
    DiffUtil.ItemCallback<?> mDiffCallback;

    Page2Buider(MultiRecyclerView rv) {
        super(rv);

    }

    public B setDiffCallback(DiffUtil.ItemCallback<?> callback) {
        mDiffCallback = callback;
        mAdapter = new MultiPage2Adapter<>(mDiffCallback);
        return (B) this;
    }


    public Page2Buider setPagedConfig(PagedList.Config config) {
        mAdapter.setPagedConfig(config);
        return this;
    }

    public Page2Buider setTypes(List<ItemType<?>> types) {
        mAdapter.setTypes(types);
        return this;
    }

    public Page2Buider setType(ItemType<?> type) {
        mAdapter.setType(type);
        return this;
    }

    public Page2Buider setDataSourceFactory(DataSourceFactory factory) {
        mAdapter.setDataSourceFactory(factory);
        return this;
    }
    @Override
    public void build() {
        mAdapter.completed();
    }


}
