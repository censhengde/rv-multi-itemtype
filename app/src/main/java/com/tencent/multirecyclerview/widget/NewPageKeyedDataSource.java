package com.tencent.multirecyclerview.widget;

import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/18 10:26
 *
 * 说明：
 */
 final class NewPageKeyedDataSource<Key, Value> extends PageKeyedDataSource<Key, Value> {


    final List<Value> datas;
    final PageKeyedDataSource<Key, Value> oldDataSource;

    public NewPageKeyedDataSource(PageKeyedDataSource<Key, Value> oldDataSource) {
        this.oldDataSource = oldDataSource;
        datas = new ArrayList<>();
    }

    public PagedList<Value> createPagedList(PagedList.Config config) {
        return new PagedList.Builder<>(this, config).build();
    }

    @Override
    public void loadAfter(@NotNull LoadParams<Key> loadParams, @NotNull LoadCallback<Key, Value> loadCallback) {
        oldDataSource.loadAfter(loadParams, loadCallback);
    }

    @Override
    public void loadBefore(@NotNull LoadParams<Key> loadParams, @NotNull LoadCallback<Key, Value> loadCallback) {
        loadCallback.onResult(Collections.emptyList(), null);
    }

    @Override
    public void loadInitial(@NotNull LoadInitialParams<Key> loadInitialParams,
            @NotNull LoadInitialCallback<Key, Value> loadInitialCallback) {
        loadInitialCallback.onResult(datas, null, null);
    }
}
