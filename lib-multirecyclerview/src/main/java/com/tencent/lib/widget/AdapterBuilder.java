package com.tencent.lib.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 17:20
 *
 * 说明：
 */
public  class AdapterBuilder<T extends AdapterBuilder> extends Builder<T> {
  private   MultiAdapter adapter;
    @Nullable
    protected ItemType<?> itemType;
    @Nullable
    protected List<ItemType<?>>itemTypes;
    AdapterBuilder(MultiRecyclerView rv) {
        super(rv);
    }
    /*单样式就调用这个*/
    public T setItemType(@NonNull ItemType<?> type) {
        this.itemType = type;
        return (T) this;
    }

    /*多样式就调用这个*/
    public T setItemTypes(@NonNull List<ItemType<?>> types) {
        itemTypes = types;
        return (T) this;
    }
    /*默认实现*/
    public void build() {
//        MultiAdapter adapter=getAdapter();
        adapter=new MultiAdapter<>();
        if (itemType != null) {
            adapter.setItemType(itemType);
        }
        if (itemTypes != null) {
            adapter.setItemTypes(itemTypes);
        }
        recyclerView.setAdapter(adapter);
    }
    protected  MultiAdapter getAdapter(){
        if (adapter==null){
            new MultiAdapter();
        }
        return adapter;
    }
}
