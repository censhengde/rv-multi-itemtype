package com.tencent.lib.multi.core.listener;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author：岑胜德 on 2021/3/14 17:18
 *
 * 说明：
 */
public interface OnLongClickItemViewListener<T> {

    boolean onLongClickItemView(@NonNull View v,int viewType,@NonNull T bean, int position);

}
