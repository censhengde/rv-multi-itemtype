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
    @Nullable
    protected MultiAdapter multiAdapter;

    @Nullable
    protected ItemType itemType;
    @Nullable
    protected List<ItemType> itemTypes;
    private boolean isPaged;//是否分页

    Builder(MultiRecyclerView rv) {
        recyclerView = rv;
    }

    public T setPaged(boolean isPaged) {
        this.isPaged = isPaged;
        return (T) this;
    }

    /*单样式就调用这个*/
    public T setItemType(@NonNull ItemType type) {
        this.itemType = type;
        return (T) this;
    }

    /*多样式就调用这个*/
    public T setItemTypes(@NonNull List<ItemType> types) {
        itemTypes = types;
        return (T) this;
    }

    /*默认实现是不分页*/
    public void build() {
        multiAdapter = new MultiAdapter();
        if (itemType != null) {
            multiAdapter.setItemType(itemType);
        }
        if (itemTypes != null) {
            multiAdapter.setItemTypes(itemTypes);
        }
        recyclerView.setAdapter(multiAdapter);
    }

    static void assertNull(Object o, @NonNull String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
