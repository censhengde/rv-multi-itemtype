package com.tencent.lib.widget;

import androidx.annotation.NonNull;

/**
 * Author：岑胜德 on 2021/1/6 16:15
 * <p>
 * 说明：条目管理
 */
public interface ItemManager<T> {
    void removeItem(int position);
    void insertItem(int position,T data);
    void addItem(T data);
    void updateItem(int position);
    void updateAll();
    void refreshAll(@NonNull OnRefreshListener listener);
    void loadMore(@NonNull OnLoadMoreListener listener);
}
