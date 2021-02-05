package com.tencent.lib.widget;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 16:02
 *
 * 说明：
 */
public interface OnCheckedItemListCallback<T> {
    void onChecked(@NonNull List<T> datas);

}
