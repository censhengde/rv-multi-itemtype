package com.tencent.lib.multi.core.listener;

import android.view.View;
import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.MultiItemType;

/**
 * Author：岑胜德 on 2021/3/14 17:18
 *
 * 说明：
 */
public interface OnLongClickItemViewListener<T> {

    boolean onLongClickItemView(@NonNull View v, MultiItemType<?,?> item, @NonNull T bean, int position);

}
