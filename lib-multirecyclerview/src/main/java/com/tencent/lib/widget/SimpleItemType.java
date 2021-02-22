package com.tencent.lib.widget;

import androidx.annotation.NonNull;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 * 说明：单样式的 ItemType
 */
public abstract class SimpleItemType<T> implements ItemType<T> {

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean matchItemType(@NonNull T data, int position) {
        return false;
    }



    @Override
    public void onInitItemSubViewListener(@NonNull MultiViewHolder holder) {

    }

    @Override
    public void onClickItemView(@NonNull MultiViewHolder holder, @NonNull T data, int position) {

    }


}
