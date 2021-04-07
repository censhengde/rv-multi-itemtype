package com.tencent.lib.multi.core;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

/**
 * Author：岑胜德 on 2021/1/29 16:18
 *
 * 说明：实现风格的Builder
 */
public abstract class LayoutBuilder<T extends LayoutBuilder> {

    protected RecyclerView recyclerView;
    protected LayoutManager layoutManager;
    LayoutBuilder(BaseRecyclerView rv) {
        recyclerView = rv;
    }

    public  void build(){
       Utils.assertNull(layoutManager,"LayoutManager 不允许为 null");
        recyclerView.setLayoutManager(layoutManager);
    }

}
