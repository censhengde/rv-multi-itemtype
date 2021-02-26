package com.tencent.lib.multi.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 17:20
 *
 * 说明：
 */
public class AdapterBuilder<T extends AdapterBuilder> {
    @Nullable
    protected ItemType<?> itemType;
    @Nullable
    protected List<ItemType<?>>itemTypes;

    protected boolean checkable = false;
    protected BaseRecyclerView recyclerView;
    protected OnCheckedItemCallback onCheckedItemCallback;
    protected boolean isSingleSelection = false;
    protected OnCompletedCheckItemCallback onCompletedCheckItemCallback;

    public AdapterBuilder(BaseRecyclerView recyclerView) {
        this.recyclerView=recyclerView;
    }

    public T setOnCompletedCheckItemCallback(OnCompletedCheckItemCallback callback) {
        this.onCompletedCheckItemCallback = callback;
        return (T) this;
    }

    public T setOnCheckedItemCallbak(@Nullable OnCheckedItemCallback<?> callbak) {
        onCheckedItemCallback = callbak;
        return (T) this;
    }

    public T setSingleSelection(boolean single) {
        recyclerView.setSingleSelection(single);
        return (T) this;
    }

    public T checkable(boolean checkable) {
        recyclerView.setCheckable(checkable);
        return (T) this;
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


}
