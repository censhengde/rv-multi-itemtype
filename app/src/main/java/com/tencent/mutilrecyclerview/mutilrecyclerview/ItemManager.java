package com.tencent.mutilrecyclerview.mutilrecyclerview;

/**
 * Author：岑胜德 on 2021/1/6 16:15
 * <p>
 * 说明：
 */
public interface ItemManager<T> {
    void removeItem(int position);
    void insertItem(int position,T data);
    void updateItem(int position);

}
