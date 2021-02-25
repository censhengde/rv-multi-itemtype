package com.tencent.lib.multi;

import androidx.annotation.Nullable;

/**
 * Author：岑胜德 on 2021/2/5 16:00
 *
 * 说明：
 */
public interface OnCheckedItemCallback<T> {

    void onCkeked(@Nullable T data, int position);
}
