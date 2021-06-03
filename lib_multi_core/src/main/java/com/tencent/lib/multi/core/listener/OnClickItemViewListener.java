package com.tencent.lib.multi.core.listener;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author：岑胜德 on 2021/3/14 17:18
 *
 * 说明：
 */
public interface OnClickItemViewListener<T> {

    /**
     * @param v 点击的那个View
     * @param type ItemType标识符，与ItemType getViewType对应。
     * @param item 当前item实体对象
     * @param position 当前position
     */
    void onClickItem(@NonNull View v,int type, @NonNull T item, int position);

}
