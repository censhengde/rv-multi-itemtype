package com.tencent.lib.widget;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：岑胜德 on 2021/2/5 16:43
 *
 * 说明：实现列表单选、多选功能的管理类
 */
public class CkeckedManager {

    private RecyclerView recyclerView;
    private OnCheckedItemCallback<?> onCheckedItemCallback;
    private CheckedAdapter<?> adapter;

    public void setOnCheckedItemCallback(OnCheckedItemCallback<?> onCheckedItemCallback) {
        this.onCheckedItemCallback = onCheckedItemCallback;
    }

    /*全部取消*/
    public void cancelAll() {
//        adapter.cancelAll();
    }

    /*全选*/
    public void checkAll() {
//        adapter.checkAll();
    }

}
