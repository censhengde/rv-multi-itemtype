package com.tencent.lib.multi;

/**
 * Author：岑胜德 on 2021/1/6 16:15
 * <p>
 * 说明：条目管理
 */
public interface ItemManager<T> {
    void removeItem(int position);
    void addItem(int position,T data);
    void addItem(T data);

}
