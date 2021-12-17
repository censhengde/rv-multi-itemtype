package com.tencent.lib.multi.core.listener;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.lib.multi.core.MultiItem;

/**
 * Author：岑胜德 on 2021/3/14 17:18
 *
 * 说明：
 */
public interface OnClickItemViewListener<T> {

    /**
     * @param v 点击的那个View
     * @param bean 当前item实体对象
     * @param position 当前position
     */
    void onClickItemView(@NonNull View v, MultiItem<?,?> item, @NonNull T bean, int position);

}
