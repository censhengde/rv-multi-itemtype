package com.tencent.lib.multi.core.listener;

import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * Author：岑胜德 on 2021/5/13 10:18
 *
 * 说明：
 */
public interface OnClickItemChildViewListener<T> {

    void onClickItemChildView(@NonNull View v, int type, @NonNull T item, int position);
}
