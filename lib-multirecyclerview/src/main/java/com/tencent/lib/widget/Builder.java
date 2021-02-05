package com.tencent.lib.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/29 14:49
 *
 * 说明：
 */
public abstract class Builder<T extends Builder> {

    @NonNull
    protected MultiRecyclerView recyclerView;


    Builder(MultiRecyclerView rv) {
        recyclerView = rv;
    }




    /*默认实现是不分页*/
    public abstract void build();

    static void assertNull(Object o, @NonNull String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
