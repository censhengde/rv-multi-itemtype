package com.tencent.lib.widget;

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

    protected OnCheckedItemCallback onCheckedItemCallback;
    protected boolean isSingleSelection = false;
    protected OnCompletedCheckItemCallback onCompletedCheckItemCallback;

    public T setOnCompletedCheckItemCallback(OnCompletedCheckItemCallback callback) {
        this.onCompletedCheckItemCallback = callback;
        return (T) this;
    }

    public T setOnCheckedItemCallbak(@Nullable OnCheckedItemCallback<?> callbak) {
        onCheckedItemCallback = callbak;
        return (T) this;
    }

    public T setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
        return (T) this;
    }

    public T checkable(boolean checkable) {
        this.checkable = checkable;
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
